package com.stars.tv.server;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    @GET("search/video/videolists")
    Observable<ResponseBody> getIQiYiMovieSimplifiedList(@Query("channel_id") int channel,
                                                         @Query(value="three_category_id",encoded = true) String orderList,
                                                         @Query("is_purchase") String payStatus,
                                                         @Query("market_release_date_level") String myYear,
                                                         @Query("mode") int sortType,
                                                         @Query("pageNum") int pageNum,
                                                         @Query("data_type") int dataType,
                                                         @Query("site") String siteType,
                                                         @Query("source_type") int sourceType,
                                                         @Query("is_album_finished") String comicsStatus,
                                                         @Query("pageSize") int pageSize);

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

    @GET("m")
    Observable<ResponseBody> getIQiYiSearchHotQueryWord(@Query("if")String quercyType);

    @GET("/")
    Observable<ResponseBody> getIQiYiSearchSuggestWord(@Query("key")String keyWord,@Query("rltnum")int resultNum);

    @GET("so/q_{keyWord}_ctg_{channel}_t_{duration}_page_{pageNum}_p_1_qc_0_rd_{publishTime}_site_iqiyi_m_{sort}_bitrate_{pictureQuality}")
    Observable<ResponseBody> getIQiYiSearchResult(@Path("keyWord") String keyWord,@Path("channel") String channel,@Path("duration") int duration,
                                                  @Path("pageNum") int pageNum,@Path("publishTime") String publishTime,@Path("sort") int sort,@Path("pictureQuality") String pictureQuality);

    @GET("o")
    Observable<ResponseBody> getIQiYiSearchSimplified(@Query("key")String keyWord,@Query("if")String ifWord,@Query("pageNum")int pageNum,
                                                @Query("pageSize")int pageSize);

    @Headers("Cache-Control: public ,max-age= 300")
    @GET
    Observable<ResponseBody> getTvMaoEpg(@Url String url);

    @GET("get_video_info?eurl=https%3A%2F%2Fy")
    Observable<ResponseBody> getYTVideoInfo(@Query("video_id") String video_id);

    @GET("room_init?")
    Observable<ResponseBody> getBilibiliRoom(@Query("id") String id);

    @GET("playUrl?quality=0&platform=web")
    Observable<ResponseBody> getBilibiliRealPlayUrl(@Query("cid") String cid);

    @Headers("User-Agent: Mozilla/5.0 (iPad; CPU OS 12_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1")
    @GET("{channel}")
    Observable<ResponseBody> getHuyaChannelIpadPage(@Path("channel") String channel);

    @Headers("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 920)")
    @GET("{suffix}")
    Observable<ResponseBody> getDouyuRealPlayUrl(@Path("suffix") String suffix, @Query("auth") String auth);
}
