package com.stars.tv.youtube.ui.youtube;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;
import java.util.Map;

import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.viewmodel.YoutubeViewModel;

/**
 * {@link YoutubeRowFragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class RecommendedFragment extends YoutubeRowFragment {

    public static RecommendedFragment newInstance() {
        return new RecommendedFragment();
    }

    private static final String TAG = RecommendedFragment.class.getSimpleName();
    private YoutubeViewModel mViewModel;
    private Map<String, List<YouTubeVideo>> mRecommendedChannel;
    private final YoutubeFragment.TabCategory mTabCategory = YoutubeFragment.TabCategory.Recommended;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG + " ViewModel", "onActivityCreated");
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        mViewModel.getRecommendedChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "Recommended Observe ");
            mRecommendedChannel = channels;
            setRows(mRecommendedChannel);
        });

//        mViewModel.playlist(recommend_playlist_url);
//        mRecommendedChannel = mViewModel.getRecommendedChannelList().getValue();
    }

    @Override
    public YoutubeFragment.TabCategory getTabCategory() {
        return this.mTabCategory;
    }
}
