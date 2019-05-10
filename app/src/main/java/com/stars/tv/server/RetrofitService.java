package com.stars.tv.server;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 网络请求API
 * @Author: Dicks.yang
 * @Date: 2019.04.24
 */
public interface  RetrofitService {

    @GET
    Observable<ResponseBody> getIQiYiMovieList(@Url String url);

    @GET
    Observable<ResponseBody> getIQiYiRealPlayUrl(@Url  String url);

    @GET
    Observable<ResponseBody> getIQiYiVideoBaseInfoWithUrl(@Url  String url);

    @GET("albums/album/avlistinfo")
    Observable<ResponseBody> getIQiYiEpisodeList(@Query("aid") String albumId, @Query("size")int size, @Query("page")int pageNum);

    @GET("album/source/svlistinfo")
    Observable<ResponseBody> getIQiYiVarietyAlbumList(@Query("sourceid") String albumId, @Query("cid")int cid, @Query("timelist")String timelist);

    @GET("video/video/hotplaytimes/{tvId}")
    Observable<ResponseBody> getIQiYiHotPlayTimes(@Path("tvId") String tvId);

    @GET("video/video/baseinfo/{tvId}")
    Observable<ResponseBody> getIQiYiVideoBaseInfo(@Path("tvId") String tvId);

    @GET("star/{starId}/recommend")
    Observable<ResponseBody> getIQiYiStarRecommend(@Path("starId") String starId, @Query("size")String size, @Query("tvid")String tvid, @Query("withcookie")boolean withCookie);

    @GET
    Observable<ResponseBody> getIQiYiStarsInfo(@Url  String url);

    @GET
    Observable<ResponseBody> getIQiYiHotList(@Url  String url);

    @GET("album/album/fytoplist")
    Observable<ResponseBody> getIQiYiTopList(@Query("cid")String cid, @Query("type")String type, @Query("size")int size, @Query("page")int page);

    @GET("star/star/basicstarinfo")
    Observable<ResponseBody> getIQiYiBasicStarsInfo(@Query("starId")String starId, @Query(value="channleIds",encoded = true) String channleIds, @Query("limit")int size);

    @GET("{channel}")
    Observable<ResponseBody> getIQiYiBannerInfo(@Path("channel") String channel);
}
