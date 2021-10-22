package com.lrxun.lhttp.adapter;

/**
 * Created by luopeng on 2019-11-30.
 * from Qidianyun company
 */
public interface CallAdapter<T, R> {
    /** call执行的代理方法 */
    R adapt(Call<T> call, AdapterParam param);
}
