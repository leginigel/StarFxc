package com.stars.tv.utils;

import android.support.annotation.NonNull;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

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
        if (t instanceof SocketTimeoutException) {
            error("连接超时");
        } else if (t instanceof ConnectException) {
            error("连接错误");
        } else if (t instanceof MalformedURLException || t instanceof URISyntaxException) {
            error("URL错误");
        } else if (t instanceof NoRouteToHostException || t instanceof UnknownHostException) {
            error("连接远程主机失败");
        } else {//其他错误
            error("未知错误");
        }
    }


}
