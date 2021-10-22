package com.lrxun.lhttp.convert;

import okhttp3.Response;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */

public interface Converter<T> {
    /**
     * 解析数据
     * @param response okhttp返回
     * @return 返回泛型
     * @throws Throwable 抛出异常
     */
    T convertResponse(Response response) throws Throwable;
}
