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
public class LatestFragment extends YoutubeRowFragment {
    public static LatestFragment newInstance() {
        return new LatestFragment();
    }

    private static final String TAG = LatestFragment.class.getSimpleName();
    private YoutubeViewModel mViewModel;
    private Map<String, List<YouTubeVideo>> mLatestChannel;
    private final YoutubeFragment.TabCategory mTabCategory = YoutubeFragment.TabCategory.Latest;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG + " ViewModel", "onActivityCreated");
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        mViewModel.getLatestChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "mLatest Observe ");
            mLatestChannel = channels;
            setRows(mLatestChannel);
        });
    }

    @Override
    public YoutubeFragment.TabCategory getTabCategory() {
        return this.mTabCategory;
    }
}
