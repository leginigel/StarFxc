package com.stars.tv.youtube.ui.player;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.stars.tv.R;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.youtube.YoutubeActivity;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerControlsFragment extends DialogFragment {

    public static PlayerControlsFragment newInstance() {
        return new PlayerControlsFragment();
    }

    private final static String TAG = PlayerControlsFragment.class.getSimpleName();
    private int CountDown;
    private ViewGroup mConstraint, mBackGround;
    private ImageButton playButton, favButton, hqButton, moreButton;
    private TextView timeText, playText, favText, hqText, moreText;
    private TextView countDownText;
    private ImageView replayIcon, replayImg;
    private SeekBar seekBar;
    private Timer timer;
    private YouTubeVideo mVideo;
    private YouTubePlayer mPlayer;
    private ControlRowFragment controlRowFragment;
    private FrameLayout mControlRow, mReplay;
    private MyPlaybackEventListener playbackEventListener = new MyPlaybackEventListener();
    private MyPlayerStateChangeListener playerStateChangeListener = new MyPlayerStateChangeListener();
    private boolean mIsFavorite;
    public enum PlaybackState{
        PLAYING, NOT_PLAYING, STOPPED, PAUSED, BUFFERING
    }
    public enum PlayerState{
        ERROR, VIDEO_ENDED, VIDEO_STARTED, AD_STARTED, LOADED, LOADING, UNINITIALIZED
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);
        controlRowFragment = ControlRowFragment.newInstance(mVideo.getId());
        mControlRow = view.findViewById(R.id.player_control_row);
        mReplay = view.findViewById(R.id.layout_replay);
        timer = new Timer();
        CountDown = 5;
        mPlayer = ((YoutubeActivity) getActivity()).getYouTubePlayer();
        mConstraint = view.findViewById(R.id.view_group);
        mBackGround = view.findViewById(R.id.player_control);
        mIsFavorite = false;
        if(savedInstanceState == null)
            this.getChildFragmentManager().beginTransaction()
                    .add(R.id.player_control_row, controlRowFragment).commit();

        setInformationText(view);

        constructSeekBar(view);
        updateSeekBar();

        hqButton = view.findViewById(R.id.quality_button);
        hqText = view.findViewById(R.id.quality_text);
        hqButton.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                CountDown = 5;
                hqText.setVisibility(View.VISIBLE);
            }
            else hqText.setVisibility(View.INVISIBLE);
        });
        setFavButton(view);
        setMoreButton(view);
        setPlayButton(view);

        if(playerStateChangeListener.getPlayerState() != PlayerState.VIDEO_ENDED) {
            playButton.requestFocus();
        }
        // Open after video ended then show the suggestion and replayIcon
        else {
            // Suggestion row is close in default, open it
            mBackGround.setBackgroundColor(getResources().getColor(R.color.background));
            countDownText.setVisibility(View.VISIBLE);
            replayIcon.setVisibility(View.VISIBLE);
            replayIcon.requestFocus();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mControlRow.getLayoutParams();
            float factor = getResources().getDisplayMetrics().density;
            layoutParams.height = (int) (300 * factor);
            mControlRow.setLayoutParams(layoutParams);
            mConstraint.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void switchMore(){
        playButton.setVisibility(View.GONE);
        favButton.setVisibility(View.VISIBLE);
        hqButton.setVisibility(View.VISIBLE);
    }

    private void switchLess(){
        playButton.setVisibility(View.VISIBLE);
        favButton.setVisibility(View.GONE);
        hqButton.setVisibility(View.GONE);
    }

    public void openRow(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mControlRow.getLayoutParams();
        float factor = getResources().getDisplayMetrics().density;

        ValueAnimator anim = ValueAnimator.ofInt((int) (75 * factor), (int) (300 * factor));
        anim.addUpdateListener(animation -> {
            layoutParams.height = (int) animation.getAnimatedValue();
            mControlRow.setLayoutParams(layoutParams);
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                controlRowFragment.setExpand(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(playerStateChangeListener.getPlayerState() != PlayerState.VIDEO_ENDED) {
                    controlRowFragment.getVerticalGridView().requestFocus();
                }
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(150).start();

        mConstraint.animate().alpha(0.0f).setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mConstraint.setVisibility(View.INVISIBLE);
                    }
                });

        CountDown = 10;
    }

    public void closeRow(){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mControlRow.getLayoutParams();
        float factor = getResources().getDisplayMetrics().density;

        ValueAnimator anim = ValueAnimator.ofInt((int) (300 * factor), (int) (75 * factor));
        anim.addUpdateListener(animation -> {
            layoutParams.height = (int) animation.getAnimatedValue();
            mControlRow.setLayoutParams(layoutParams);
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                controlRowFragment.setExpand(false);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(150).start();

        mConstraint.animate().alpha(1.0f).setDuration(300).setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mConstraint.setVisibility(View.VISIBLE);
                    }
                });

        CountDown = 5;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Dialog dialog = getDialog();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//          dialog.setOnKeyListener
//        Window window = getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        layoutParams.setDimAmount(0f);
//        layoutParams.setFlags(layoutParams.getFlags() | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.setAttributes(layoutParams);
    }

    public void close(){
        Fragment fragment = getFragmentManager().findFragmentByTag("dialog");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment).commit();

        if(playerStateChangeListener.getPlayerState() != PlayerState.VIDEO_ENDED)
            ((YoutubeActivity) getActivity()).getPlayerBox().requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }

    private void updateSeekBar() {
        YoutubeActivity activity = (YoutubeActivity) getActivity();
        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                int currentPosition = mPlayer.getCurrentTimeMillis();
                if(mPlayer.isPlaying()) {
                    seekBar.setProgress(currentPosition);
                    getActivity().runOnUiThread(() -> {
                        timeText.setText(formatTime(mPlayer.getCurrentTimeMillis()));
                    });
                    CountDown--;
                    if(CountDown < 0) close();
                }
                else if(playerStateChangeListener.getPlayerState() == PlayerState.VIDEO_ENDED){
                    // Count down text start on 5 but 4
                    getActivity().runOnUiThread(() -> {
                        countDownText.setText("RETURN IN " + (CountDown + 1)  + " ...");
                    });
                    CountDown--;
                    // After video ended will return to prev page
                    if(CountDown < 0){
                        close();
                        getActivity().runOnUiThread(() -> {
                            activity.onBackPressed();
                        });
                    }
                }
                else if(mConstraint.getVisibility() == View.INVISIBLE){
                    CountDown--;
                    // Count down and close control frag
                    if(CountDown < 0) close();
                }
            }
        };
        timer.schedule(task, 100, 1000);
    }

    private void constructSeekBar(View view) {
        seekBar = view.findViewById(R.id.seekBar);
//        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.button_selecting), PorterDuff.Mode.SRC_IN);
//        seekBar.getProgressDrawable()
//                .setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_IN);
        seekBar.setMax(mPlayer.getDurationMillis());
        seekBar.setProgress(mPlayer.getCurrentTimeMillis());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d("SeekBar", "onProgressChanged" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        seekBar.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) seekBar.setBackgroundColor(Color.parseColor("#22ebebeb"));
            else seekBar.setBackgroundColor(Color.TRANSPARENT);
        });
        seekBar.setOnClickListener(v -> {
//            Log.d("SeekBar", "onClick" + seekBar.getProgress());
            mPlayer.seekToMillis(seekBar.getProgress());
            mPlayer.play();
            close();
        });
        AtomicBoolean isChanged = new AtomicBoolean(false);
        seekBar.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                CountDown = 5;
                if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if(isChanged.get()){
                        mPlayer.play();
                        isChanged.set(false);
                    }
                    close();
//                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    mPlayer.pause();
                    isChanged.set(true);
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if(isChanged.get()){
                        mPlayer.play();
                        isChanged.set(false);
                    }
                }
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    ((YoutubeActivity) getActivity()).getPlayerBox().requestFocus();
                }
            }
            return false;
        });
    }

    private void setFavButton(View view){
        favButton = view.findViewById(R.id.favorite_button);
        favText = view.findViewById(R.id.favorite_text);
        try {
            LeanCloudStorage.getYoutubeFavoriteListener(mVideo.getId(),
                    new LeanCloudStorage.VideoSeeker() {
                        @Override
                        public void succeed(ExtVideoBean bean) {
                            favButton.setImageResource(R.drawable.ic_favorite_24dp);
                            mIsFavorite = true;
                        }

                        @Override
                        public void failed() {
                            favButton.setImageResource(R.drawable.ic_favorite_border_24dp);
                            mIsFavorite = false;
                        }
                    });
        }catch(Exception e){ }
        favButton.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                CountDown = 5;
                favText.setVisibility(View.VISIBLE);
            }
            else favText.setVisibility(View.INVISIBLE);
        });
        favButton.setOnClickListener(v -> {
            if ( mIsFavorite ){
                try {
                    LeanCloudStorage.removeYoutubeFavorite(mVideo.getId(), new DeleteCallback() {
                        @Override
                        public void done(AVException e) {
                            if ( e == null ) {
                                favButton.setImageResource(R.drawable.ic_favorite_border_24dp);
                                mIsFavorite = false;
                            }
                        }
                    });
                }catch(Exception e){

                }
            }
            else{
                try {
                    LeanCloudStorage.updateYoutubeFavorite(mVideo, new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if ( e == null ) {
                                favButton.setImageResource(R.drawable.ic_favorite_24dp);
                                mIsFavorite = true;
                            }
                        }
                    });
                }catch(Exception e){

                }
            }
            //favButton.getDrawable()
            //      .setColorFilter(Color.parseColor("#FF1E88E5"), PorterDuff.Mode.SRC_IN);
        });
    }

    private void setMoreButton(View view){
        moreButton = view.findViewById(R.id.more_button);
        moreText = view.findViewById(R.id.more_text);
        moreButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_more_vert_24dp));
        moreButton.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                CountDown = 5;
                moreText.setVisibility(View.VISIBLE);
            }
            else moreText.setVisibility(View.INVISIBLE);
        });
        moreButton.setOnClickListener(v -> {
            if(playButton.getVisibility() != View.GONE){
                switchMore();
                moreButton.setImageDrawable(getActivity().getDrawable(R.drawable.lb_ic_more));
            }
            else{
                switchLess();
                moreButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_more_vert_24dp));
            }
        });
    }

    private void setPlayButton(View view){
        playText = view.findViewById(R.id.play_text);
        playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> {
            if(mPlayer.isPlaying()){
                mPlayer.pause();
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_play));
                playText.setText("Play");
            }
            else{
                CountDown = 5;
                mPlayer.play();
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_pause));
                playText.setText("Pause");
            }
        });
        playButton.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                CountDown = 5;
                playText.setVisibility(View.VISIBLE);
            }
            else playText.setVisibility(View.INVISIBLE);
        });
        playButton.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                openRow();
            }
            return false;
        });
    }

    private void setInformationText(View view){
        // Playback Information
        TextView durationText = view.findViewById(R.id.play_duration);
        timeText = view.findViewById(R.id.play_time);
        TextView titleText = view.findViewById(R.id.play_title);
        TextView channelText = view.findViewById(R.id.play_channel);
        TextView countText = view.findViewById(R.id.play_count);
        TextView publishText = view.findViewById(R.id.play_publish);

        durationText.setText(String.format(" / %s", Utils.DurationConverter(mVideo.getDuration())));
        timeText.setText(formatTime(mPlayer.getCurrentTimeMillis()));
        titleText.setText(Html.fromHtml(mVideo.getTitle()));
        channelText.setText(mVideo.getChannel() + " ‧ ");
        countText.setText(Utils.CountConverter(mVideo.getNumber_views()) + " views ‧ ");
        publishText.setText(Utils.TimeConverter(mVideo.getTime()) + " ago");

        // PostPlay Information
        countDownText = view.findViewById(R.id.count_down);
        replayIcon = view.findViewById(R.id.icon_replay);
        replayImg = view.findViewById(R.id.img_replay);
        Glide.with(this)
                .asBitmap()
                .centerCrop()
                .load(mVideo.getId() != null ? "https://i.ytimg.com/vi/" + mVideo.getId() + "/0.jpg" : null)
                .into(replayImg);

        replayIcon.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    countDownText.setVisibility(View.INVISIBLE);
                    controlRowFragment.getVerticalGridView().requestFocus();
                    timer.cancel();
                    return true;
                }
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    close();
                    getActivity().onBackPressed();
                    return true;
                }
            }
            return false;
        });
        replayIcon.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus && countDownText.getVisibility() == View.INVISIBLE){
                Log.d(TAG, "Replay Icon Focus");
                mReplay.setVisibility(View.VISIBLE);
            }
            else {
                mReplay.setVisibility(View.INVISIBLE);
            }
        });
        replayIcon.setOnClickListener(v -> {
            Log.d(TAG, "Replay Icon Click");
            mPlayer.seekToMillis(0);
            mPlayer.play();
            close();
        });
    }

    public void setCountDown(int countDown) {
        CountDown = countDown;
    }

    public void setVideo(YouTubeVideo video){
        this.mVideo = video;
    }

    public YouTubeVideo getVideo(){
        return this.mVideo;
    }

    public ImageView getReplayIcon() {
        return replayIcon;
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "" : hours + ":")
                + String.format("%2d:%02d", minutes % 60, seconds % 60);
    }

    public MyPlaybackEventListener getPlaybackEventListener() {
        return playbackEventListener;
    }

    public MyPlayerStateChangeListener getPlayerStateChangeListener() {
        return playerStateChangeListener;
    }

    public final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
        PlaybackState playbackState = PlaybackState.NOT_PLAYING;
        String bufferingState = "";
        @Override
        public void onPlaying() {
            playbackState = PlaybackState.PLAYING;
            Log.d(this.getClass().getSimpleName(), "" + playbackState);
        }

        @Override
        public void onBuffering(boolean isBuffering) {
            bufferingState = isBuffering ? "(BUFFERING)" : "";
            Log.d(this.getClass().getSimpleName(), bufferingState + playbackState);
        }

        @Override
        public void onStopped() {
            playbackState = PlaybackState.STOPPED;
            Log.d(this.getClass().getSimpleName(), "" + playbackState);
        }

        @Override
        public void onPaused() {
            playbackState = PlaybackState.PAUSED;
            Log.d(this.getClass().getSimpleName(), "" + playbackState);
        }

        @Override
        public void onSeekTo(int endPositionMillis) {
            Log.d(this.getClass().getSimpleName(),
                    formatTime(endPositionMillis) + formatTime(mPlayer.getDurationMillis()) + playbackState);
        }

        public PlaybackState getPlaybackState() {
            return playbackState;
        }
    }

    public final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
        PlayerState playerState = PlayerState.UNINITIALIZED;
        private PlayerControlsFragment playerControlsFragment;
        private FragmentActivity activity;

        @Override
        public void onLoading() {
            playerState = PlayerState.LOADING;
            Log.d(this.getClass().getSimpleName(), "" + playerState);
        }

        @Override
        public void onLoaded(String videoId) {
            playerState = PlayerState.LOADED;
            Log.d(this.getClass().getSimpleName(), videoId + " " + playerState);
        }

        @Override
        public void onAdStarted() {
            playerState = PlayerState.AD_STARTED;
            Log.d(this.getClass().getSimpleName(), "" + playerState);
        }

        @Override
        public void onVideoStarted() {
            playerState = PlayerState.VIDEO_STARTED;
            Log.d(this.getClass().getSimpleName(), "" + playerState);
        }

        @Override
        public void onVideoEnded() {
            playerState = PlayerState.VIDEO_ENDED;
            Log.d(this.getClass().getSimpleName(), "" + playerState);

            // Show Post Play from PlayerControls
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("dialog");
            // PlayerControls Exist or not
            if(prev != null) {
                mBackGround.setBackgroundColor(getResources().getColor(R.color.background));
                countDownText.setVisibility(View.VISIBLE);
                replayIcon.setVisibility(View.VISIBLE);
                playerControlsFragment.openRow();
                replayIcon.requestFocus();
                setCountDown(5);
            }
            else {
                playerControlsFragment.show(ft, "dialog");
            }
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason reason) {
            playerState = PlayerState.ERROR;
            if (reason == YouTubePlayer.ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                // When this error occurs the player is released and can no longer be used.
                mPlayer = null;
//        setControlsEnabled(false);
            }
            Log.d(this.getClass().getSimpleName(), playerState + " Reason: " + reason);
        }

        public PlayerState getPlayerState() {
            return playerState;
        }

        public void setPlayerControlsFragment(PlayerControlsFragment playerControlsFragment) {
            this.playerControlsFragment = playerControlsFragment;
        }

        public void setActivity(FragmentActivity activity) {
            this.activity = activity;
        }
    }
}
