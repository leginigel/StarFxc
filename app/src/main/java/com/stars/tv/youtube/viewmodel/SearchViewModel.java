package com.stars.tv.youtube.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import com.stars.tv.youtube.api.SearchResponse;
import com.stars.tv.youtube.api.VideoResponse;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.network.NetworkDataModel;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private CompositeDisposable disposable;

    private MutableLiveData<List<YouTubeVideo>> videos;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    public LiveData<List<YouTubeVideo>> getVideoList(){
        if(videos == null){
            videos = new MutableLiveData<>();
            List<YouTubeVideo> list = new ArrayList<>();
            for (int i = 0;i < 3;i++)
                list.add(new YouTubeVideo("id","test", "test", 0, "now", null));
            videos.setValue(list);
        }
        return videos;
    }

    public void searchRx(String query){
        List<SearchResponse.Items> temp = new ArrayList<>();
        List<YouTubeVideo> ytv = new ArrayList<>();
//        disposable.add(
        networkDataModel.searchVideoRx(query)
                .flatMap(new Function<Response<SearchResponse>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        temp.addAll(items);
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
                .filter(r-> r.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                            Log.d("YoutubeViewModel", "videoResponse : "
                                    + videoResponse.body().getItems().get(0).getSnippet().getTitle());
                            for (int i = 0;i < temp.size();i++){
                                if(temp.get(i).getId().getVideoId() != null &&
                                        temp.get(i).getId().getVideoId().equals(videoResponse.body().getItems().get(i).getId())) {
                                    Log.d("test", "onActivityCreated:"
                                            + videoResponse.body().getItems().get(i).getSnippet().getTitle());
                                    ytv.add(
                                            new YouTubeVideo(
                                                    temp.get(i).getId().getVideoId(),
                                                    temp.get(i).getSnippet().getTitle(),
                                                    temp.get(i).getSnippet().getChannelTitle(),
                                                    videoResponse.body().getItems().get(i).getStatistics().getViewCount(),
                                                    videoResponse.body().getItems().get(i).getSnippet().getPublishedAt(),
                                                    videoResponse.body().getItems().get(i).getContentDetails().getDuration()
                                            ));
                                }
                            }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        videos.setValue(ytv);
                    }
                })
        ;
//    );
    }

    public Observable<Response<SearchResponse>> searchVideo(String query, boolean oldpass){
        return networkDataModel.searchVideoRx(query);
    }

    public Observable<Response<VideoResponse>> videoDetail(String id, boolean oldpass){
        return networkDataModel.videoDetail(id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        disposable.clear();
    }
}
