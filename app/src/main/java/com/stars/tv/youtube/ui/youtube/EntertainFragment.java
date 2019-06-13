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
public class EntertainFragment extends YoutubeRowFragment {
    public static EntertainFragment newInstance() {
        return new EntertainFragment();
    }

    private static final String TAG = EntertainFragment.class.getSimpleName();
    private YoutubeViewModel mViewModel;
    private Map<String, List<YouTubeVideo>> mEntertainChannel;
    private final YoutubeFragment.TabCategory mTabCategory = YoutubeFragment.TabCategory.Entertainment;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG + " ViewModel", "onActivityCreated");
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        mViewModel.getEntertainChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "EntertainFragment Observe ");
            mEntertainChannel = channels;
            setRows(mEntertainChannel);
        });

    }

    @Override
    public YoutubeFragment.TabCategory getTabCategory() {
        return this.mTabCategory;
    }
}
