package com.lrxun.lhttp.cache;

import android.content.ContentValues;
import android.database.Cursor;

import com.lrxun.lhttp.model.HttpHeaders;
import com.lrxun.lhttp.utils.IOUtils;

import java.io.Serializable;


/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public class CacheEntity<T> implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;

    /**
     * 缓存永不过期
     */
    public static final long CACHE_NEVER_EXPIRE = -1;

    public static final String KEY = "key";
    public static final String LOCAL_EXPIRE = "localExpire";
    public static final String HEAD = "head";
    public static final String DATA = "data";

    /**
     * 缓存key
     */
    private String key;

    /**
     * 缓存过期时间
     */
    private long localExpire;

    /**
     * 缓存的响应头
     */
    private HttpHeaders responseHeaders;

    /**
     * 缓存的实体数据
     */
    private T data;

    /**
     * 缓存是否过期该变量不必保存到数据库，程序运行起来后会动态计算
     */
    private boolean isExpire;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(HttpHeaders responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getLocalExpire() {
        return localExpire;
    }

    public void setLocalExpire(long localExpire) {
        this.localExpire = localExpire;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }

    /**
     * @param cacheTime 允许的缓存时间
     * @param baseTime  基准时间,小于当前时间视为过期
     * @return 是否过期
     */
    public boolean checkExpire(CacheMode cacheMode, long cacheTime, long baseTime) {
        //304的默认缓存模式,设置缓存时间无效,需要依靠服务端的响应头控制
        if (cacheMode == CacheMode.DEFAULT) {
            return getLocalExpire() < baseTime;
        }
        if (cacheTime == CACHE_NEVER_EXPIRE) {
            return false;
        }
        return getLocalExpire() + cacheTime < baseTime;
    }

    public static <T> ContentValues getContentValues(CacheEntity<T> cacheEntity) {
        ContentValues values = new ContentValues();
        values.put(KEY, cacheEntity.getKey());
        values.put(LOCAL_EXPIRE, cacheEntity.getLocalExpire());
        values.put(HEAD, IOUtils.toByteArray(cacheEntity.getResponseHeaders()));
        values.put(DATA, IOUtils.toByteArray(cacheEntity.getData()));
        return values;
    }

    public static <T> CacheEntity<T> parseCursorToBean(Cursor cursor) {
        CacheEntity<T> cacheEntity = new CacheEntity<>();
        cacheEntity.setKey(cursor.getString(cursor.getColumnIndex(KEY)));
        cacheEntity.setLocalExpire(cursor.getLong(cursor.getColumnIndex(LOCAL_EXPIRE)));
        cacheEntity.setResponseHeaders((HttpHeaders) IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(HEAD))));
        //noinspection unchecked
        cacheEntity.setData((T) IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex(DATA))));
        return cacheEntity;
    }

    @Override
    public String toString() {
        return "CacheEntity{key='" + key + '\'' +
                ", responseHeaders=" + responseHeaders +
                ", data=" + data +
                ", localExpire=" + localExpire +
                '}';
    }
}
