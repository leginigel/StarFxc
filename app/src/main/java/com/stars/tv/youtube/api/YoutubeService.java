package com.stars.tv.youtube.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {
    String key = "AIzaSyDXOC5ojIydszcxB2tOuZRX8o6iqemxRSg";

    @GET("search?part=snippet&maxResults=15&key=" + key)
    Call<SearchResponse> searchVideo(@Query("q") String query);

    //RxJava2
    @GET("search?part=snippet&maxResults=10&type=video&key=" + key)
    Observable<Response<SearchResponse>> searchVideoRx(@Query("q") String query,
                                                       @Query("relatedToVideoId") String relatedVideoId);

    @GET("search?part=snippet" +
            "&fields=items(id,snippet(title,channelTitle))" +
            "&maxResults=5&order=viewCount&type=playlist&key=" + key)
    Observable<Response<SearchResponse>> searchChannelPlaylist(@Query("channelId") String channelId);

    @GET("videos?part=snippet%2CcontentDetails%2Cstatistics&key=" + key)
    Observable<Response<VideoResponse>>  videoDetail(@Query("id") String id);

    @GET("playlistItems?part=snippet%2Cid&maxResults=10&key=" + key)
    Observable<Response<PlaylistItemsResponse>>  playlistItems(@Query("playlistId") String playlistId);

    @GET("videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&maxResults=10&key=" + key)
    Observable<Response<VideoResponse>>  videoPopular();

    @GET("search?part=snippet" +
            "&fields=items(id)" +
            "&maxResults=10&order=viewCount&type=video&key=" + key)
    Observable<Response<SearchResponse>> searchLatestWeek(@Query("publishedAfter") String publishedAfter,
                                                          @Query("publishedBefore") String publishedBefore);
}
