package com.stars.tv.youtube.ui.youtube;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stars.tv.R;
import com.stars.tv.youtube.viewmodel.YoutubeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * ViewModel {@link YoutubeViewModel}
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class YoutubeFragment extends Fragment {

    private final static String TAG = YoutubeFragment.class.getSimpleName();

    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    FragmentManager fm;

    RecommendedFragment recommendedFragment;
    MusicFragment musicFragment;

    private TabCategory mTab = TabCategory.Recommended;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recommendedFragment = RecommendedFragment.newInstance();
        musicFragment = MusicFragment.newInstance();

        View view = inflater.inflate(R.layout.youtube_fragment, container, false);
        if(savedInstanceState == null) {
            fm = getFragmentManager();
            FragmentTransaction fmts = fm.beginTransaction();
            fmts.replace(R.id.container, recommendedFragment).commit();
        }
        Button recommend, latest, music, entertain, gaming;
        recommend = view.findViewById(R.id.recommend_btn);
        latest = view.findViewById(R.id.latest_btn);
        music = view.findViewById(R.id.music_btn);
        entertain = view.findViewById(R.id.entertainment_btn);
        gaming = view.findViewById(R.id.gaming_btn);
        setButtonFocusListener(recommend, recommendedFragment, TabCategory.Recommended);
        setButtonFocusListener(latest, recommendedFragment, TabCategory.Recommended);
        setButtonFocusListener(music, musicFragment, TabCategory.Music);
        setButtonFocusListener(entertain, recommendedFragment, TabCategory.Recommended);
        setButtonFocusListener(gaming, recommendedFragment, TabCategory.Recommended);

        return view;
    }

        @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
        YoutubeViewModel mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
    }

    void setButtonFocusListener(Button button, Fragment fragment, TabCategory category){
        button.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                button.setTextColor(Color.BLACK);
                if(getTabCategory() != category)
                    setTabCategory(category);
                fm.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
            else{
                button.setTextColor(getActivity().getResources().getColor(R.color.btn_text));
            }
        });
    }

    public enum TabCategory {
        Recommended, Latest, Music, Entertainment, Gaming
    }

    public void setTabCategory(TabCategory category){
        this.mTab = category;
        switch (category){
            case Recommended:
                Log.d(TAG, "Fragment setTabCategory Recommended");
//                setRows(mRecommendedChannel);
                break;
            case Latest:
//                setRows(mLatestChannel);
                break;
            case Music:
                Log.d(TAG, "Fragment setTabCategory Music");
//                setRows(mMusicChannel);
                break;
            case Entertainment:break;
            case Gaming:break;
        }
    }

    public TabCategory getTabCategory() {
        return mTab;
    }
}
