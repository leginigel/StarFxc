package com.stars.tv.youtube.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import com.stars.tv.youtube.api.PlaylistItemsResponse;
import com.stars.tv.youtube.api.SearchResponse;
import com.stars.tv.youtube.api.VideoResponse;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.network.NetworkDataModel;
import com.stars.tv.youtube.ui.youtube.YoutubeFragment;
import retrofit2.Response;

/**
 *  ViewModel for {@link YoutubeFragment}
 */
public class YoutubeViewModel extends ViewModel {
    public static final String [] recommend_playlistId_url = {
            "PLzjFbaFzsmMS-b4t5Eh3LJcf3HYlmVWYe",
            "PLDcnymzs18LWLKtkNrKYzPpHLbnXRu4nN",
            "PLS3UB7jaIERzy5Ua5i0nlEYY6_xh2PhUf",
            "PL8fVUTBmJhHJmpP7sLb9JfLtdwCmYX9xC",
            "PLAdMV6KkPvD4igiNzQm5Zk3f3vcqN8xFW",
//                "PLiBi9LVIrC-eGpUAUkxkjpw4QIayQJMpD",
//                "PLCmd_pMCXoQLqg3gKa0_c0URlbIEetgXM",
//                "PLkLNgKNlFzZ35-B8UdoLWnMCAf3GsPbr9",
//                "PL57quI9usf_vDPXuhqIjyrPIjkw3C1oPe",
    };

    private static final String music_url = "UC-9-kyTW8ZkZNDHQJ6FgpwQ";
    private static final String gaming_url = "UCOpNcN46UbXVtpKMrmU4Abg";
    private static final String entertain_url = "UCi-g4cjqGV7jvU8aeSuj0jQ";

    private final static String TAG = YoutubeViewModel.class.getSimpleName();

    private CompositeDisposable disposable;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    private MutableLiveData<Map<String, List<YouTubeVideo>>> recommendedChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> musicChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> latestChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> entertainChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> gamingChannelList;

    public LiveData<Map<String, List<YouTubeVideo>>> getLatestChannelList() {
        if(latestChannelList == null) {
            Log.v(TAG, "getLatestChannelList NULL");
            latestChannelList = new MutableLiveData<>();
            popular();
        }
        return latestChannelList;
    }

    public LiveData<Map<String, List<YouTubeVideo>>> getEntertainChannelList() {
        if(entertainChannelList == null) {
            Log.v(TAG, "getEntertainChannelList NULL");
            entertainChannelList = new MutableLiveData<>();
            searchChannel(entertain_url, YoutubeFragment.TabCategory.Entertainment);
        }
        return entertainChannelList;
    }

    public LiveData<Map<String, List<YouTubeVideo>>> getGamingChannelList() {
        if(gamingChannelList == null) {
            Log.v(TAG, "getGamingChannelList NULL");
            gamingChannelList = new MutableLiveData<>();
            searchChannel(gaming_url, YoutubeFragment.TabCategory.Gaming);
        }
        return gamingChannelList;
    }

    public LiveData<Map<String, List<YouTubeVideo>>> getMusicChannelList(){
        if(musicChannelList == null) {
            Log.v(TAG, "getMusicChannelList NULL");
            musicChannelList = new MutableLiveData<>();
            searchChannel(music_url, YoutubeFragment.TabCategory.Music);
        }
        return musicChannelList;
    }

    public LiveData<Map<String, List<YouTubeVideo>>> getRecommendedChannelList(){
        if(recommendedChannelList == null) {
            Log.v(TAG, "getRecommendedChannelList NULL");
            recommendedChannelList = new MutableLiveData<>();
            playlist(recommend_playlistId_url);
        }
        return recommendedChannelList;
    }

    private void popular(){
        Map<String, List<YouTubeVideo>> channel =
                getLatestChannelList().getValue() == null ? new HashMap<>() : getLatestChannelList().getValue();

        networkDataModel.videoPopular()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(response -> response.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        List<YouTubeVideo> yt = new ArrayList<>();
                        for (VideoResponse.Items item : videoResponse.body().getItems()){
                            yt.add(
                                    new YouTubeVideo(
                                            item.getId(),
                                            item.getSnippet().getTitle(),
                                            item.getSnippet().getChannelTitle(),
                                            item.getStatistics().getViewCount(),
                                            item.getSnippet().getPublishedAt(),
                                            item.getContentDetails().getDuration()
                                    ));
                        }
                        Log.d("YoutubeViewModel", "onNext :" + yt.size());
                        channel.put("Trending today", yt);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : Latest Popular");
                        searchLatestWeek(channel);
                    }
                });
    }

    private void searchLatestWeek(Map<String, List<YouTubeVideo>> channel){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date();
        String publishAfter = format.format(new Date(date.getTime() - 2592000000L));
        String publishBefore = format.format(new Date(date.getTime() - 604800000L));

        networkDataModel.searchLatestWeek(publishAfter, publishBefore)
                .flatMap(new Function<Response<SearchResponse>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        String multi_id = "";
                        for (SearchResponse.Items i : items){
                            multi_id = multi_id + i.getId().getVideoId() + ",";
                        }
                        return Observable.just(multi_id);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Response<VideoResponse>>>() {
                    @Override
                    public ObservableSource<Response<VideoResponse>> apply(String s) throws Exception {
                        return networkDataModel.videoDetail(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(response -> response.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        List<YouTubeVideo> yt = new ArrayList<>();
                        for (VideoResponse.Items item : videoResponse.body().getItems()){
                            yt.add(
                                    new YouTubeVideo(
                                            item.getId(),
                                            item.getSnippet().getTitle(),
                                            item.getSnippet().getChannelTitle(),
                                            item.getStatistics().getViewCount(),
                                            item.getSnippet().getPublishedAt(),
                                            item.getContentDetails().getDuration()
                                    ));
                        }
                        Log.d("YoutubeViewModel", "onNext :" + yt.size());
                        channel.put("Trending this week", yt);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : Latest Week " + channel.size());
                        latestChannelList.setValue(channel);
                    }
                });
    }

    private void searchChannel(String channelId, YoutubeFragment.TabCategory category){
        final String[] channelTitle = new String[1];
        Map<String, List<YouTubeVideo>> channel;
        switch (category){
            case Music:
            default:
                channel = musicChannelList.getValue() == null ? new HashMap<>() : musicChannelList.getValue();
                break;
            case Gaming:
                channel = gamingChannelList.getValue() == null ? new HashMap<>() : gamingChannelList.getValue();
                break;
            case Entertainment:
                channel = entertainChannelList.getValue() == null ? new HashMap<>() : entertainChannelList.getValue();
                break;
        }

        networkDataModel.searchChannelPlaylist(channelId)
                .flatMap(new Function<Response<SearchResponse>, Observable<SearchResponse.Items>>() {
                    @Override
                    public Observable<SearchResponse.Items> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        return Observable.fromIterable(items);
                    }
                })
                .flatMap(new Function<SearchResponse.Items, ObservableSource<Response<PlaylistItemsResponse>>>() {
                    @Override
                    public ObservableSource<Response<PlaylistItemsResponse>> apply(SearchResponse.Items items) throws Exception {
                        channelTitle[0] = items.getSnippet().getTitle();
                        String playlistId = items.getId().getPlaylistId();
                        Log.d("searchChannel searchRes", channelTitle[0] + playlistId);
                        return networkDataModel.playlistItems(playlistId);
                    }
                })
                .flatMap(new Function<Response<PlaylistItemsResponse>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Response<PlaylistItemsResponse> playlistItemsResponse) throws Exception {

                        List<PlaylistItemsResponse.Items> items = playlistItemsResponse.body().getItems();
                        String multi_id = "";
                        for (PlaylistItemsResponse.Items i : items){
                            Log.v("searchChannel Items", i.getSnippet().getTitle());
                            multi_id = multi_id + i.getSnippet().getResourceId().getVideoId() + ",";
                        }
                        return Observable.just(multi_id);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Response<VideoResponse>>>() {
                    @Override
                    public ObservableSource<Response<VideoResponse>> apply(String videoId) throws Exception {
                        return networkDataModel.videoDetail(videoId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(response -> response.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        List<YouTubeVideo> yt = new ArrayList<>();
                        for (VideoResponse.Items item : videoResponse.body().getItems()){
                            yt.add(
                                    new YouTubeVideo(
                                            item.getId(),
                                            item.getSnippet().getTitle(),
                                            item.getSnippet().getChannelTitle(),
                                            item.getStatistics().getViewCount(),
                                            item.getSnippet().getPublishedAt(),
                                            item.getContentDetails().getDuration()
                                    ));
                        }
                        Log.d(TAG + "SearchChannel", "onNext :" + channelTitle[0] + yt.size());
                        channel.put(channelTitle[0], yt);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        switch (category){
                            case Music:
                                musicChannelList.postValue(channel);
                                break;
                            case Entertainment:
                                entertainChannelList.postValue(channel);
                                break;
                            case Gaming:
                                gamingChannelList.postValue(channel);
                                break;
                        }
                    }
                });
    }

    /**
     * PlaylistId as channel playlists ID
     * @param playlistId
     *
     * Request with playlistId and Return Video Items
     * And Transform Items to Video Information
     * Subscribe These Information to Class Video
     */
    private void playlist(String[] playlistId){
        List<PlaylistItemsResponse.Items> temp = new ArrayList<>();
        final String[] channelTitle = new String[1];
        Map<String, List<YouTubeVideo>> channel =
                getRecommendedChannelList().getValue() == null ? new HashMap<>() : getRecommendedChannelList().getValue();

//        networkDataModel.playlistItems(playlistId[0])
//                .mergeWith(networkDataModel.playlistItems(playlistId[1]))
        Observable.fromArray(playlistId)
                .flatMap(new Function<String, ObservableSource<Response<PlaylistItemsResponse>>>() {
                    @Override
                    public ObservableSource<Response<PlaylistItemsResponse>> apply(String playlistId) throws Exception {
                        return networkDataModel.playlistItems(playlistId);
                    }
                })
                .flatMap(new Function<Response<PlaylistItemsResponse>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<PlaylistItemsResponse> playlistItemsResponse) throws Exception {
                        channelTitle[0] = playlistItemsResponse.body().getItems().get(0).getSnippet().getChannelTitle();

                        List<PlaylistItemsResponse.Items> items = playlistItemsResponse.body().getItems();
                        temp.addAll(items);
                        String multi_id = "";
                        for (PlaylistItemsResponse.Items i : items){
                            multi_id = multi_id + i.getSnippet().getResourceId().getVideoId() + ",";
                        }
                        return Observable.just(multi_id);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Response<VideoResponse>>>() {
                    @Override
                    public ObservableSource<Response<VideoResponse>> apply(String s) throws Exception {
                        return networkDataModel.videoDetail(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(response -> response.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        List<YouTubeVideo> yt = new ArrayList<>();
                        for (VideoResponse.Items item : videoResponse.body().getItems()){
                            yt.add(
                                    new YouTubeVideo(
                                            item.getId(),
                                            item.getSnippet().getTitle(),
                                            item.getSnippet().getChannelTitle(),
                                            item.getStatistics().getViewCount(),
                                            item.getSnippet().getPublishedAt(),
                                            item.getContentDetails().getDuration()
                                    ));
                        }
                        Log.d("YoutubeViewModel", "onNext :" + channelTitle[0] + yt.size());
                        channel.put(channelTitle[0], yt);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : Size " + channel.size());
                        recommendedChannelList.setValue(channel);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
