package com.stars.tv.youtube.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
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
import com.stars.tv.youtube.api.PlaylistItems;
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
//                "PLS3UB7jaIERzy5Ua5i0nlEYY6_xh2PhUf",
//                "PL8fVUTBmJhHJmpP7sLb9JfLtdwCmYX9xC",
//                "PLAdMV6KkPvD4igiNzQm5Zk3f3vcqN8xFW",
//                "PLiBi9LVIrC-eGpUAUkxkjpw4QIayQJMpD",
//                "PLCmd_pMCXoQLqg3gKa0_c0URlbIEetgXM",
//                "PLkLNgKNlFzZ35-B8UdoLWnMCAf3GsPbr9",
//                "PL57quI9usf_vDPXuhqIjyrPIjkw3C1oPe",
    };

    private String music_url = "UC-9-kyTW8ZkZNDHQJ6FgpwQ";

    private final static String TAG = YoutubeViewModel.class.getSimpleName();

    private CompositeDisposable disposable;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    private MutableLiveData<Map<String, List<YouTubeVideo>>> recommendedChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> musicChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> latestChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> entertainChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> gamingChannelList;

    public LiveData<Map<String, List<YouTubeVideo>>> getMusicChannelList(){
        if(musicChannelList == null) {
            Log.v(TAG, "getMusicChannelList NULL");
            musicChannelList = new MutableLiveData<>();
//            searchChannel(music_url, YoutubeFragment.TabCategory.Music);
        }
        return musicChannelList;
    }

    public LiveData<Map<String, List<YouTubeVideo>>> getRecommendedChannelList(){
        if(recommendedChannelList == null) {
            Log.v(TAG, "getRecommendedChannelList NULL");
            recommendedChannelList = new MutableLiveData<>();
//            playlist(recommend_playlistId_url);
        }
        return recommendedChannelList;
    }

    public void searchChannel(String channelId, YoutubeFragment.TabCategory category){
        final String[] channelTitle = new String[1];
        Map<String, List<YouTubeVideo>> channel =
                musicChannelList.getValue() == null ? new HashMap<>() : musicChannelList.getValue();

        networkDataModel.searchChannelPlaylist(channelId)
                .flatMap(new Function<Response<SearchResponse>, Observable<SearchResponse.Items>>() {
                    @Override
                    public Observable<SearchResponse.Items> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        return Observable.fromIterable(items);
                    }
                })
                .flatMap(new Function<SearchResponse.Items, ObservableSource<Response<PlaylistItems>>>() {
                    @Override
                    public ObservableSource<Response<PlaylistItems>> apply(SearchResponse.Items items) throws Exception {
                        channelTitle[0] = items.getSnippet().getTitle();
                        String playlistId = items.getId().getPlaylistId();
                        Log.d("searchChannel searchRes", channelTitle[0]);
                        Log.d("searchChannel searchRes", playlistId);
                        return networkDataModel.playlistItems(playlistId);
                    }
                })
                .flatMap(new Function<Response<PlaylistItems>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Response<PlaylistItems> playlistItemsResponse) throws Exception {

                        List<PlaylistItems.Items> items = playlistItemsResponse.body().getItems();
                        String multi_id = "";
                        for (PlaylistItems.Items i : items){
                            Log.d("searchChannel Items", i.getSnippet().getTitle());
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
                        for (int i = 0;i < videoResponse.body().getItems().size();i++){
                            yt.add(
                                    new YouTubeVideo(
                                            videoResponse.body().getItems().get(i).getId(),
                                            videoResponse.body().getItems().get(i).getSnippet().getTitle(),
                                            videoResponse.body().getItems().get(i).getSnippet().getChannelTitle(),
                                            videoResponse.body().getItems().get(i).getStatistics().getViewCount(),
                                            videoResponse.body().getItems().get(i).getSnippet().getPublishedAt(),
                                            videoResponse.body().getItems().get(i).getContentDetails().getDuration()
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
    public void playlist(String[] playlistId){
        List<PlaylistItems.Items> temp = new ArrayList<>();
        final String[] channelTitle = new String[1];
        Map<String, List<YouTubeVideo>> channel =
                getRecommendedChannelList().getValue() == null ? new HashMap<>() : getRecommendedChannelList().getValue();

//        networkDataModel.playlistItems(playlistId[0])
//                .mergeWith(networkDataModel.playlistItems(playlistId[1]))
        Observable.fromArray(playlistId)
                .flatMap(new Function<String, ObservableSource<Response<PlaylistItems>>>() {
                    @Override
                    public ObservableSource<Response<PlaylistItems>> apply(String playlistId) throws Exception {
                        return networkDataModel.playlistItems(playlistId);
                    }
                })
                .flatMap(new Function<Response<PlaylistItems>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<PlaylistItems> playlistItemsResponse) throws Exception {
                        channelTitle[0] = playlistItemsResponse.body().getItems().get(0).getSnippet().getChannelTitle();

                        List<PlaylistItems.Items> items = playlistItemsResponse.body().getItems();
                        temp.addAll(items);
                        String multi_id = "";
                        for (PlaylistItems.Items i : items){
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
                        for (int i = 0;i < videoResponse.body().getItems().size();i++){
                            yt.add(
                                    new YouTubeVideo(
                                            videoResponse.body().getItems().get(i).getId(),
                                            videoResponse.body().getItems().get(i).getSnippet().getTitle(),
                                            videoResponse.body().getItems().get(i).getSnippet().getChannelTitle(),
                                            videoResponse.body().getItems().get(i).getStatistics().getViewCount(),
                                            videoResponse.body().getItems().get(i).getSnippet().getPublishedAt(),
                                            videoResponse.body().getItems().get(i).getContentDetails().getDuration()
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
