package com.lrxun.lhttp.convert;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public class StringConverter implements Converter<String> {
    @Override
    public String convertResponse(Response response) throws Throwable {
        final ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        return body.string();
    }
}
