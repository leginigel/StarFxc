package com.stars.tv.server;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 网络请求API
 * @Author: Dicks.yang
 * @Date: 2019.04.24
 */
public interface  RetrofitService {

    @GET("{url}")
    Observable<ResponseBody> getIQiYiMovieList(@Path("url") String url);

}
