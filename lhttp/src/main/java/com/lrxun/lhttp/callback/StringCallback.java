package com.lrxun.lhttp.callback;

import com.lrxun.lhttp.convert.StringConverter;

import okhttp3.Response;

/**
 * Created by
 * @author luopeng
 * @date 2019-11-30.
 * from Qidianyun company
 */
public abstract class StringCallback extends AbsCallback<String> {

    private StringConverter convert;

    public StringCallback() {
        convert = new StringConverter();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }
}
