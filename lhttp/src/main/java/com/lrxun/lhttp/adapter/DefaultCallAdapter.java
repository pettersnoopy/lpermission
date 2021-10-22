package com.lrxun.lhttp.adapter;

/**
 * Created by luopeng on 2019-11-30.
 * from Qidianyun company
 */
public class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {
    @Override
    public Call<T> adapt(Call<T> call, AdapterParam param) {
        return call;
    }
}
