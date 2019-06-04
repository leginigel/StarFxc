package com.stars.tv.youtube.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {
    @GET("search?output=toolbar&ds=yt")
    Observable<Response<GoogleResponse>> searchGoogle(@Query("q") String query);
}
