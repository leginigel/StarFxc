package com.stars.tv.youtube;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import com.stars.tv.youtube.api.YoutubeService;
import com.stars.tv.youtube.ui.PlayerControlsFragment;
import com.stars.tv.R;
import com.stars.tv.youtube.ui.search.SearchFragment;
import com.stars.tv.youtube.ui.youtube.YoutubeFragment;

@RequiresApi(api = Build.VERSION_CODES.N)
public class YoutubeActivity extends FragmentActivity implements YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static String TAG = YoutubeActivity.class.getSimpleName();
    private PageCategory mPageCategory;

    private SearchFragment searchFragment;
    private YoutubeFragment youtubeFragment;
    private PlayerControlsFragment playerControlsFragment;
    private YouTubePlayer youTubePlayer;

    private ImageView searchIcon, homeIcon, subIcon, folderIcon, settingIcon;
    private View playerBox, playerControls;

    public enum PageCategory {
        Search, Home, Subscription, Library, Account, Setting
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        searchFragment = SearchFragment.newInstance();
        youtubeFragment = YoutubeFragment.newInstance();
        playerControlsFragment = playerControlsFragment.newInstance();
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
//                    .addToBackStack(tag)
                    .replace(R.id.container_home, youtubeFragment)
                    .commitNow();
            getSupportFragmentManager().beginTransaction()
//                    .addToBackStack(tag)
                    .replace(R.id.fragment_youtube_player, youTubePlayerFragment)
                    .commit();
            getSupportFragmentManager().beginTransaction()
//                    .addToBackStack(tag)
                    .replace(R.id.fragment_player_controls, playerControlsFragment)
                    .commit();
        }

        youTubePlayerFragment.initialize(YoutubeService.key, this);
        playerControls = findViewById(R.id.fragment_player_controls);
        playerControls.setVisibility(View.INVISIBLE);
        playerBox = findViewById(R.id.fragment_youtube_player);
        playerBox.setVisibility(View.INVISIBLE);
        playerBox.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                Log.d("onKey","KEYCODE_DPAD_DOWN");
                playerControls.setVisibility(View.VISIBLE);
                playerControls.requestFocus();
                playerControlsFragment.showTime();
                return true;
            }
            return false;
        });

        mPageCategory = PageCategory.Home;

        ViewGroup leftNav = findViewById(R.id.left_nav);
        leftNav.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        searchIcon = findViewById(R.id.search_btn);
        homeIcon = findViewById(R.id.home_btn);
        subIcon = findViewById(R.id.subscribe_btn);
        folderIcon = findViewById(R.id.folder_btn);
        settingIcon = findViewById(R.id.setting_btn);

        homeIcon.setSelected(true);
        homeIcon.getDrawable().setColorFilter(getResources().getColor(R.color.button_selecting), PorterDuff.Mode.SRC_IN);

        setIconFocusListener();
        setIconOnKeyListener();

        checkYouTubeApi();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private void setIconFocus(ImageView image, PageCategory category){
        image.setOnFocusChangeListener((v, hasFocus)->{
            Drawable drawable = ((ImageView) v).getDrawable();
            if(hasFocus) {
                Log.d(TAG, "Icon on Focus " + category);
                v.setSelected(false);
                this.mPageCategory = category;
                drawable.setColorFilter(getResources().getColor(R.color.left_nav), PorterDuff.Mode.MULTIPLY);
                ((ImageView) v).setImageDrawable(drawable);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_home, getPageFragment(category)).commitNow();
            }
            else{
                if(category == this.mPageCategory && v.isSelected())
                    drawable.setColorFilter(getResources().getColor(R.color.button_selecting), PorterDuff.Mode.SRC_IN);
                else
                    drawable.setColorFilter(getResources().getColor(R.color.button), PorterDuff.Mode.SRC_IN);
                ((ImageView) v).setImageDrawable(drawable);
            }
        });
    }

    private void setIconFocusListener(){
        setIconFocus(searchIcon, PageCategory.Search);
        setIconFocus(homeIcon, PageCategory.Home);
        setIconFocus(subIcon, PageCategory.Search);
        setIconFocus(folderIcon, PageCategory.Search);
        setIconFocus(settingIcon, PageCategory.Search);
    }

    private void setIconOnKey(ImageView image){
        image.setOnKeyListener((view, keyCode, event)->{
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                if(KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                    view.setSelected(true);
                }
            }
            return false;
        });
    }

    private void setIconOnKeyListener(){
        setIconOnKey(searchIcon);
        setIconOnKey(homeIcon);
        setIconOnKey(subIcon);
        setIconOnKey(folderIcon);
        setIconOnKey(settingIcon);
    }

    public Fragment getPageFragment(PageCategory category){
        switch (category){
            case Home:
                return youtubeFragment;
            case Search:
                return searchFragment;
        }
        return searchFragment;
    }

    public View getPlayerBox() {
        return playerBox;
    }

    public YouTubePlayer getYouTubePlayer() {
        return youTubePlayer;
    }

    public YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_youtube_player);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
//        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//            youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        this.youTubePlayer = youTubePlayer;
//            youTubePlayer.setPlaylistEventListener(playlistEventListener);
//            youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//            youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            Log.d("CheckPoint", "CheckPoint !wasRestored");
//                youTubePlayer.cueVideo(video.getId());
//                youTubePlayer.play();
//            youTubePlayer.loadVideo(video.getId());
        }
        else{
            Log.d("CheckPoint", "CheckPoint Restored");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        Log.d("onInitializationFailure", "Failed to initialize.");
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = errorReason.toString();
            Log.d("onInitializationFailure", errorMessage);
        }

    }

    @Override
    public void onBackPressed() {
        if (playerBox.getVisibility() == View.VISIBLE){
            playerBox.setVisibility(View.INVISIBLE);
            playerControls.setVisibility(View.INVISIBLE);
        }
        else
        super.onBackPressed();
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = errorReason.toString();
            Log.d("checkYouTubeApi", errorMessage);
        }
    }
}
