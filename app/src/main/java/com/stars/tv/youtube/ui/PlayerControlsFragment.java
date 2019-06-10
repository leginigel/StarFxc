package com.stars.tv.youtube.ui;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import com.stars.tv.R;
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

    private int CountDown;
    private ImageButton playButton;
    private SeekBar seekBar;
    TextView titleText, channelText, countText, publishText, timeText, durationText, playText;
    private Timer timer;
    private YouTubeVideo mVideo;
    private YouTubePlayer mPlayer;
    private MyPlaybackEventListener playbackEventListener = new MyPlaybackEventListener();
    public enum PlaybackState{
        PLAYING, NOT_PLAYING, STOPPED, PAUSED, BUFFERING
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);
        timer = new Timer();
        CountDown = 5;
        mPlayer = ((YoutubeActivity) getActivity()).getYouTubePlayer();
        setText(view);

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
        updateSeekBar();

        playText = view.findViewById(R.id.play_text);
        playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> {
            if(mPlayer.isPlaying()){
                mPlayer.pause();
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_pause));
                playText.setText("Pause");
            }
            else{
                CountDown = 5;
                mPlayer.play();
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_play));
                playText.setText("Play");
            }
        });
        playButton.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) playText.setVisibility(View.VISIBLE);
            else playText.setVisibility(View.INVISIBLE);
        });
        playButton.requestFocus();
        return view;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void close(){
        Fragment fragment = getFragmentManager().findFragmentByTag("dialog");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment).commit();

        ((YoutubeActivity) getActivity()).getPlayerBox().requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }

    private void updateSeekBar() {
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
            }
        };
        timer.schedule(task, 100, 1000);
    }

    private void setText(View view){
        durationText = view.findViewById(R.id.play_duration);
        timeText = view.findViewById(R.id.play_time);
        titleText = view.findViewById(R.id.play_title);
        channelText = view.findViewById(R.id.play_channel);
        countText = view.findViewById(R.id.play_count);
        publishText = view.findViewById(R.id.play_publish);

        durationText.setText(String.format(" / %s", Utils.DurationConverter(mVideo.getDuration())));
        timeText.setText(formatTime(mPlayer.getCurrentTimeMillis()));
        titleText.setText(Html.fromHtml(mVideo.getTitle()));
        channelText.setText(mVideo.getChannel() + " ‧ ");
        countText.setText(Utils.CountConverter(mVideo.getNumber_views()) + " views ‧ ");
        publishText.setText(Utils.TimeConverter(mVideo.getTime()) + " ago");
    }

    public void setVideo(YouTubeVideo video){
        this.mVideo = video;
//        titleText.setText(Html.fromHtml(video.getTitle()));
//        timeText.setText(Utils.DurationConverter(video.getDuration()));
//        channelText.setText(video.getChannel() + " ‧ ");
//        countText.setText(Utils.CountConverter(video.getNumber_views()) + " views ‧ ");
//        publishText.setText(Utils.TimeConverter(video.getTime()) + " ago");
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
    }
}
