package com.stars.tv.youtube.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.stars.tv.R;
import com.stars.tv.youtube.api.SearchResponse;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

//        mDisposable.add(
//                mViewModel.searchVideo("End Game")
//                        .subscribeOn(Schedulers.io())
//                        .flatMap(new Function<Response<SearchResponse>, ObservableSource<Response<VideoResponse>>>() {
//                            @Override
//                            public ObservableSource<Response<VideoResponse>> apply(Response<SearchResponse> searchResponseResponse) throws Exception {
//                                String videoId = searchResponseResponse.body().getItems().get(0).getId().getVideoId();
//                                return mViewModel.videoDetail(videoId);
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableObserver<Response<VideoResponse>>() {
//                            @Override
//                            public void onNext(Response<VideoResponse> searchResponseResponse) {
////                                Log.d(SearchFragment.class.getSimpleName(), );
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        })
//
//        NetworkDataModel n = new NetworkDataModel();
//        n.searchVideoRx("End Game")
//                        .subscribeOn(Schedulers.io())
//                        .map(searchResponseResponse -> searchResponseResponse.body().getItems())
//                        .flatMap(s -> Observable.fromIterable(s))
//                        .map(item -> item.getId())
//                        .flatMap(new Function<SearchResponse.Items.ID, ObservableSource<Response<VideoResponse>>>() {
//                            @Override
//                            public ObservableSource<Response<VideoResponse>> apply(SearchResponse.Items.ID id) throws Exception {
//                                return mViewModel.videoDetail(id.getVideoId());
//                            }
//                        })
//                        .doOnNext(response -> Log.d("test", "onActivityCreated: : " + response.body().getItems().get(0).getSnippet().getTitle()))
//                        .subscribe();

        List<SearchResponse.Items> temp = new ArrayList<>();
        mViewModel.getVideoList().observe(getActivity(), (videos) ->{
            mVideoList = videos;
            mCardsAdapter.clear();
            for (YouTubeVideo v : mVideoList){
                mCardsAdapter.add(v);
            }
            mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
            Log.d("Fragment ViewModel", "notify");
        });
//        mViewModel.searchVideo("suzy");
        mVideoList = mViewModel.getVideoList().getValue();
//        mViewModel.searchVideo("End Game")
//                .flatMap(new Function<Response<SearchResponse>, ObservableSource<SearchResponse.Items>>() {
//                    @Override
//                    public ObservableSource<SearchResponse.Items> apply(Response<SearchResponse> searchResponse) throws Exception {
//                        List<SearchResponse.Items> items = searchResponse.body().getItems();
//                        temp.addAll(items);
//                        return Observable.fromIterable(items);
//                    }
//                })
//                .flatMap(new Function<SearchResponse.Items, ObservableSource<Response<VideoResponse>>>() {
//                    @Override
//                    public ObservableSource<Response<VideoResponse>> apply(SearchResponse.Items items) throws Exception {
//                        return mViewModel.videoDetail(items.getId().getVideoId());
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onNext(Response<VideoResponse> videoResponse) {
//                        if(videoResponse.code() == 200) {
//                            Log.d("test", "onActivityCreated:"
//                                    + videoResponse.body().getItems().get(0).getSnippet().getTitle());
//                            temp.forEach(i -> {
//                                if(i.getId().getVideoId() != null &&
//                                        i.getId().getVideoId().equals(videoResponse.body().getItems().get(0).getId())) {
//                                    Log.d("test", "onActivityCreated:"
//                                            + videoResponse.body().getItems().get(0).getSnippet().getTitle());
//                                    new YouTubeVideo(
//                                            i.getId().getVideoId(),
//                                            i.getSnippet().getTitle(),
//                                            i.getSnippet().getChannelTitle(),
//                                            videoResponse.body().getItems().get(0).getStatistics().getViewCount(),
//                                            videoResponse.body().getItems().get(0).getContentDetails().getDuration()
//                                    );
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

}
