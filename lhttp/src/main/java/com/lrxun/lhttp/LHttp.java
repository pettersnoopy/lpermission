package com.lrxun.lhttp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.lrxun.lhttp.cache.CacheEntity;
import com.lrxun.lhttp.cache.CacheMode;
import com.lrxun.lhttp.cookie.CookieJarImpl;
import com.lrxun.lhttp.https.HttpsUtils;
import com.lrxun.lhttp.interceptor.HttpLoggingInterceptor;
import com.lrxun.lhttp.model.HttpHeaders;
import com.lrxun.lhttp.model.HttpParams;
import com.lrxun.lhttp.request.DeleteRequest;
import com.lrxun.lhttp.request.GetRequest;
import com.lrxun.lhttp.request.HeadRequest;
import com.lrxun.lhttp.request.OptionsRequest;
import com.lrxun.lhttp.request.PatchRequest;
import com.lrxun.lhttp.request.PostRequest;
import com.lrxun.lhttp.request.PutRequest;
import com.lrxun.lhttp.request.TraceRequest;
import com.lrxun.lhttp.utils.HttpUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public class LHttp {
    public static final class Builder {
        long mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        long mReadTimeout = LHttp.DEFAULT_MILLISECONDS;
        long mWriteTimeout = LHttp.DEFAULT_MILLISECONDS;
        long mConnectTimeout = LHttp.DEFAULT_MILLISECONDS;
        int mRetryCount = 3;
        CacheMode mCacheMode = CacheMode.NO_CACHE;
        HttpHeaders mHttpHeaders = new HttpHeaders();
        HttpParams mHttpParams = new HttpParams();

        public Builder retry(int count) {
            mRetryCount = count;
            return this;
        }

        public Builder cacheMode(CacheMode cacheMode) {
            mCacheMode = cacheMode;
            return this;
        }

        public Builder cacheTime(long cacheTime) {
            mCacheTime = cacheTime;
            return this;
        }

        public Builder readTimeout(long readTimeout) {
            mReadTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(long writeTimeout) {
            mWriteTimeout = writeTimeout;
            return this;
        }

        public Builder connectTimeout(long connectTimeout) {
            mConnectTimeout = connectTimeout;
            return this;
        }

        public Builder commonHeaders(HttpHeaders headers) {
            mHttpHeaders = headers;
            return this;
        }

        public Builder commonParams(HttpParams params) {
            mHttpParams = params;
            return this;
        }

        public void build(Application application) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("BulletHttp");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.WARNING);
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();

            LHttp.getInstance()
                    .init(application)
                    .setCacheMode(mCacheMode)
                    .setCacheTime(mCacheTime)
                    .setRetryCount(mRetryCount)
                    .setOkHttpClient(new okhttp3.OkHttpClient.Builder()
                            .readTimeout(mReadTimeout, TimeUnit.MILLISECONDS)
                            .writeTimeout(mWriteTimeout, TimeUnit.MILLISECONDS)
                            .connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS)
                            .addInterceptor(loggingInterceptor)
                            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                            .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)
                            .build()
                    )
                    .addCommonHeaders(mHttpHeaders)
                    .addCommonParams(mHttpParams)
                    .addCommonQueryParams(mHttpParams)
            ;
        }
    }


    /**
     * ?????????????????????
     */
    public static final long DEFAULT_MILLISECONDS = 60000;
    /**
     * ???????????????????????????ms???
     */
    public static long REFRESH_TIME = 300;

    private Application context;
    /**
     * ????????????????????????????????????
     */
    private Handler mDelivery;
    /**
     * ok??????????????????
     */
    private OkHttpClient okHttpClient;
    /**
     * ????????????????????????
     */
    private HttpParams mCommonParams;

    /**
     * ????????????query??????????????????post???????????????????????????url???
     */
    private HttpParams mCommonQueryParams;

    /**
     * ?????????????????????
     */
    private HttpHeaders mCommonHeaders;
    /**
     * ????????????????????????
     */
    private int mRetryCount;
    /**
     * ??????????????????
     */
    private CacheMode mCacheMode;

    /**
     * ????????????????????????,??????????????????
     */
    private long mCacheTime;

    private LHttp() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.NO_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("BulletHttp");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(LHttp.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(LHttp.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(LHttp.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
    }

    public static LHttp getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        private static LHttp holder = new LHttp();
    }

    /** get?????? */
    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    /** post?????? */
    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }

    /** put?????? */
    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    /** head?????? */
    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    /** delete?????? */
    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    /** options?????? */
    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    /** patch?????? */
    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    /** trace?????? */
    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }

    /** ???????????????Application??????????????????context???????????????????????????????????? */
    public LHttp init(Application app) {
        context = app;
        return this;
    }

    /** ????????????????????? */
    public Context getContext() {
        HttpUtils.checkNotNull(context, "please call BulletHttp.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient, "please call BulletHttp.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /** ???????????? */
    public LHttp setOkHttpClient(OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /** ???????????????cookie?????? */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }

    /** ?????????????????? */
    public LHttp setRetryCount(int retryCount) {
        if (retryCount < 0) {
            throw new IllegalArgumentException("retryCount must > 0");
        }
        mRetryCount = retryCount;
        return this;
    }

    /** ?????????????????? */
    public int getRetryCount() {
        return mRetryCount;
    }

    /** ????????????????????? */
    public LHttp setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** ??????????????????????????? */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** ??????????????????????????? */
    public LHttp setCacheTime(long cacheTime) {
        if (cacheTime <= -1) {
            cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        }
        mCacheTime = cacheTime;
        return this;
    }

    /** ????????????????????????????????? */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** ?????????????????????????????? */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** ?????????????????????????????? */
    public LHttp addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) {
            mCommonParams = new HttpParams();
        }
        mCommonParams.put(commonParams);
        return this;
    }

    public HttpParams getCommonQueryParams() {
        return mCommonQueryParams;
    }

    /** ????????????query?????? */
    public LHttp addCommonQueryParams(HttpParams commonQueryParams) {
        if (mCommonQueryParams == null) {
            mCommonQueryParams = new HttpParams();
        }
        mCommonQueryParams.put(commonQueryParams);
        return this;
    }

    /** ??????????????????????????? */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** ?????????????????????????????? */
    public LHttp addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) {
            mCommonHeaders = new HttpHeaders();
        }
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** ??????Tag???????????? */
    public void cancelTag(Object tag) {
        if (tag == null) {
            return;
        }
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** ??????Tag???????????? */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) {
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** ???????????????????????? */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** ???????????????????????? */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) {
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
