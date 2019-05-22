package com.stars.tv.youtube.network;

import com.stars.tv.youtube.api.YoutubeService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private YoutubeService mService;
    private static RetrofitManager instance = new RetrofitManager();

    String url = "https://www.googleapis.com/youtube/v3/";
    private RetrofitManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mService = retrofit.create(YoutubeService.class);
    }

    public static RetrofitManager getInstance(){
        return instance;
    }

    public YoutubeService getAPI(){
        return instance.mService;
    }
}
