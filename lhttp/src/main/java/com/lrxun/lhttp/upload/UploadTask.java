package com.lrxun.lhttp.upload;

import android.content.ContentValues;

import com.lrxun.lhttp.LUpload;
import com.lrxun.lhttp.db.UploadManager;
import com.lrxun.lhttp.model.Progress;
import com.lrxun.lhttp.model.Response;
import com.lrxun.lhttp.request.base.ProgressRequestBody;
import com.lrxun.lhttp.request.base.Request;
import com.lrxun.lhttp.task.PriorityRunnable;
import com.lrxun.lhttp.utils.BulletLogger;
import com.lrxun.lhttp.utils.HttpUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Call;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public class UploadTask<T> implements Runnable {

    public Progress progress;
    public Map<Object, UploadListener<T>> listeners;
    private ThreadPoolExecutor executor;
    private PriorityRunnable priorityRunnable;

    public UploadTask(String tag, Request<T, ? extends Request> request) {
        HttpUtils.checkNotNull(tag, "tag == null");
        progress = new Progress();
        progress.tag = tag;
        progress.url = request.getBaseUrl();
        progress.status = Progress.NONE;
        progress.totalSize = -1;
        progress.request = request;

        executor = LUpload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
    }

    public UploadTask(Progress progress) {
        HttpUtils.checkNotNull(progress, "progress == null");
        this.progress = progress;
        executor = LUpload.getInstance().getThreadPool().getExecutor();
        listeners = new HashMap<>();
    }

    public UploadTask<T> priority(int priority) {
        progress.priority = priority;
        return this;
    }

    public UploadTask<T> extra1(Serializable extra1) {
        progress.extra1 = extra1;
        return this;
    }

    public UploadTask<T> extra2(Serializable extra2) {
        progress.extra2 = extra2;
        return this;
    }

    public UploadTask<T> extra3(Serializable extra3) {
        progress.extra3 = extra3;
        return this;
    }

    public UploadTask<T> save() {
        UploadManager.getInstance().replace(progress);
        return this;
    }

    public UploadTask<T> register(UploadListener<T> listener) {
        if (listener != null) {
            listeners.put(listener.tag, listener);
        }
        return this;
    }

    public void unRegister(UploadListener<T> listener) {
        HttpUtils.checkNotNull(listener, "listener == null");
        listeners.remove(listener.tag);
    }

    public void unRegister(String tag) {
        HttpUtils.checkNotNull(tag, "tag == null");
        listeners.remove(tag);
    }

    public UploadTask<T> start() {
        if (LUpload.getInstance().getTask(progress.tag) == null || UploadManager.getInstance().get(progress.tag) == null) {
            throw new IllegalStateException("you must call UploadTask#save() before UploadTask#start()???");
        }
        if (progress.status != Progress.WAITING && progress.status != Progress.LOADING) {
            postOnStart(progress);
            postWaiting(progress);
            priorityRunnable = new PriorityRunnable(progress.priority, this);
            executor.execute(priorityRunnable);
        } else {
            BulletLogger.w("the task with tag " + progress.tag + " is already in the upload queue, current task status is " + progress.status);
        }
        return this;
    }

    public void restart() {
        pause();
        progress.status = Progress.NONE;
        progress.currentSize = 0;
        progress.fraction = 0;
        progress.speed = 0;
        UploadManager.getInstance().replace(progress);
        start();
    }

    /** ??????????????? */
    public void pause() {
        executor.remove(priorityRunnable);
        if (progress.status == Progress.WAITING) {
            postPause(progress);
        } else if (progress.status == Progress.LOADING) {
            progress.speed = 0;
            progress.status = Progress.PAUSE;
        } else {
            BulletLogger.w("only the task with status WAITING(1) or LOADING(2) can pause, current status is " + progress.status);
        }
    }

    /** ??????????????????,????????????????????? */
    public UploadTask<T> remove() {
        pause();
        UploadManager.getInstance().delete(progress.tag);
        //noinspection unchecked
        UploadTask<T> task = (UploadTask<T>) LUpload.getInstance().removeTask(progress.tag);
        postOnRemove(progress);
        return task;
    }

    @Override
    public void run() {
        progress.status = Progress.LOADING;
        postLoading(progress);
        final Response<T> response;
        try {
            //noinspection unchecked
            Request<T, ? extends Request> request = (Request<T, ? extends Request>) progress.request;
            final Call rawCall = request.getRawCall();
            request.uploadInterceptor(new ProgressRequestBody.UploadInterceptor() {
                @Override
                public void uploadProgress(Progress innerProgress) {
                    if (rawCall.isCanceled()) {
                        return;
                    }
                    if (progress.status != Progress.LOADING) {
                        rawCall.cancel();
                        return;
                    }
                    progress.from(innerProgress);
                    postLoading(progress);
                }
            });
            response = request.adapt().execute();
        } catch (Exception e) {
            postOnError(progress, e);
            return;
        }

        if (response.isSuccessful()) {
            postOnFinish(progress, response.body());
        } else {
            postOnError(progress, response.getException());
        }
    }

    private void postOnStart(final Progress progress) {
        progress.speed = 0;
        progress.status = Progress.NONE;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onStart(progress);
                }
            }
        });
    }

    private void postWaiting(final Progress progress) {
        progress.speed = 0;
        progress.status = Progress.WAITING;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postPause(final Progress progress) {
        progress.speed = 0;
        progress.status = Progress.PAUSE;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postLoading(final Progress progress) {
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                }
            }
        });
    }

    private void postOnError(final Progress progress, final Throwable throwable) {
        progress.speed = 0;
        progress.status = Progress.ERROR;
        progress.exception = throwable;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onError(progress);
                }
            }
        });
    }

    private void postOnFinish(final Progress progress, final T t) {
        progress.speed = 0;
        progress.fraction = 1.0f;
        progress.status = Progress.FINISH;
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onProgress(progress);
                    listener.onFinish(t, progress);
                }
            }
        });
    }

    private void postOnRemove(final Progress progress) {
        updateDatabase(progress);
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (UploadListener<T> listener : listeners.values()) {
                    listener.onRemove(progress);
                }
                listeners.clear();
            }
        });
    }

    private void updateDatabase(Progress progress) {
        ContentValues contentValues = Progress.buildUpdateContentValues(progress);
        UploadManager.getInstance().update(contentValues, progress.tag);
    }
}
