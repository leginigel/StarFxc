package com.stars.tv.youtube.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.stars.tv.R;
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

//        mViewModel.getVideoList().observe(getActivity(), (videos) ->{
//            mVideoList = videos;
//            mCardsAdapter.clear();
//            for (YouTubeVideo v : mVideoList){
//                mCardsAdapter.add(v);
//            }
//            mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
//            Log.d("Fragment ViewModel", "notify");
//        });
//        mViewModel.searchVideo("suzy");
        mVideoList = mViewModel.getVideoList().getValue();
    }

}
