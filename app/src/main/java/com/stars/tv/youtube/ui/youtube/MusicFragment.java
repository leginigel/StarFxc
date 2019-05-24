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
public class MusicFragment extends YoutubeRowFragment {
    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    private static final String TAG = MusicFragment.class.getSimpleName();
    private YoutubeViewModel mViewModel;
    private Map<String, List<YouTubeVideo>> mMusicChannel;
    private final YoutubeFragment.TabCategory mTabCategory = YoutubeFragment.TabCategory.Music;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG + " ViewModel", "onActivityCreated");
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        mViewModel.getMusicChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "MusicFragment Observe ");
            mMusicChannel = channels;
            setRows(mMusicChannel);
        });

    }

    @Override
    public YoutubeFragment.TabCategory getTabCategory() {
        return this.mTabCategory;
    }
}
