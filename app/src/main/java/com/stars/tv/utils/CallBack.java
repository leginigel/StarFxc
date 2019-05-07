package com.stars.tv.utils;

import android.support.annotation.NonNull;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CallBack<T> implements Callback<T> {

    public abstract void success(T t);

    public abstract void error(String msg);

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        success(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {//超时
            error(t.getMessage());
        } else if (t instanceof ConnectException) {//连接错误
            error(t.getMessage());
        } else if (t instanceof UnknownError) { //未找到主机
            error(t.getMessage());
        } else {//其他错误
            error(t.getMessage());
        }
    }


}
