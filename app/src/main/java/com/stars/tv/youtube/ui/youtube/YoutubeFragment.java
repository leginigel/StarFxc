package com.stars.tv.youtube.ui.youtube;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
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

    private FragmentManager fm;

    private RecommendedFragment recommendedFragment;
    private MusicFragment musicFragment;
    private GamingFragment gamingFragment;
    private EntertainFragment entertainFragment;
    private LatestFragment latestFragment;

    private TabCategory mTab;
    private ViewGroup mContainerRow, mLeftNav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendedFragment = RecommendedFragment.newInstance();
        musicFragment = MusicFragment.newInstance();
        gamingFragment = GamingFragment.newInstance();
        entertainFragment = EntertainFragment.newInstance();
        latestFragment = LatestFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.youtube_fragment, container, false);
        if(savedInstanceState == null) {
            fm = getFragmentManager();
            FragmentTransaction fmts = fm.beginTransaction();
            fmts.replace(R.id.container_row, recommendedFragment).commit();
        }
        Button recommend, latest, music, entertain, gaming;
        recommend = view.findViewById(R.id.recommend_btn);
        latest = view.findViewById(R.id.latest_btn);
        music = view.findViewById(R.id.music_btn);
        entertain = view.findViewById(R.id.entertainment_btn);
        gaming = view.findViewById(R.id.gaming_btn);

        mContainerRow = view.findViewById(R.id.container_row);
        mLeftNav = getActivity().findViewById(R.id.left_nav);

        mTab = TabCategory.Recommended;
        recommend.setSelected(true);
        recommend.setTextColor(getActivity().getResources().getColor(R.color.button_selecting));

        setButtonFocusListener(recommend, recommendedFragment, TabCategory.Recommended);
        setButtonFocusListener(latest, latestFragment, TabCategory.Latest);
        setButtonFocusListener(music, musicFragment, TabCategory.Music);
        setButtonFocusListener(entertain, entertainFragment, TabCategory.Entertainment);
        setButtonFocusListener(gaming, gamingFragment, TabCategory.Gaming);

        setButtonKeyListener(recommend, TabCategory.Recommended);
        setButtonKeyListener(latest, TabCategory.Latest);
        setButtonKeyListener(music, TabCategory.Music);
        setButtonKeyListener(entertain, TabCategory.Entertainment);
        setButtonKeyListener(gaming, TabCategory.Gaming);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
        YoutubeViewModel mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
    }

    void setButtonKeyListener(Button button, TabCategory category){
        button.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    mContainerRow.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    if(getTabCategory() == category) {
                        button.setSelected(true);
                    }

                    YoutubeRowFragment frag = (YoutubeRowFragment) fm.findFragmentById(R.id.container_row);
                    YoutubeRowFragment.highlightRowFocus(getActivity(), frag);
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && category == TabCategory.Recommended) {
                    button.setSelected(true);
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
            }
            return false;
        });
    }

    void setButtonFocusListener(Button button, Fragment fragment, TabCategory category){
        button.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                button.setSelected(false);
                button.setTextColor(Color.BLACK);
                if(getTabCategory() != category)
                    setTabCategory(category);
                fm.beginTransaction()
                        .replace(R.id.container_row, fragment).commit();
                mContainerRow.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            }
            else{
                button.setTextColor(getActivity().getResources().getColor(R.color.btn_text));
                if(button.isSelected())
                    button.setTextColor(getActivity().getResources().getColor(R.color.button_selecting));
            }
        });
    }

    public enum TabCategory {
        Recommended, Latest, Music, Entertainment, Gaming
    }

    public void setTabCategory(TabCategory category){
        this.mTab = category;
//        switch (category){
//            case Recommended:
//                Log.d(TAG, "Fragment setTabCategory Recommended");
//                setRows(mRecommendedChannel);
//                break;
//            case Latest:
//                setRows(mLatestChannel);
//                break;
//            case Music:
//                Log.d(TAG, "Fragment setTabCategory Music");
//                setRows(mMusicChannel);
//                break;
//            case Entertainment:break;
//            case Gaming:break;
//        }
    }

    public TabCategory getTabCategory() {
        return mTab;
    }
}
