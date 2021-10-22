package com.lrxun.lhttp.callback;

import com.lrxun.lhttp.model.Progress;
import com.lrxun.lhttp.model.Response;
import com.lrxun.lhttp.request.base.Request;
import com.lrxun.lhttp.utils.BulletLogger;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public abstract class AbsCallback<T> implements Callback<T> {

    @Override
    public void onStart(Request<T, ? extends Request> request) {
    }

    @Override
    public void onError(Response<T> response) {
        BulletLogger.printStackTrace(response.getException());
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void uploadProgress(Progress progress) {
    }

    @Override
    public void downloadProgress(Progress progress) {
    }
}
