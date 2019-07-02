package com.stars.tv.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.activity.FullPlaybackActivity;
import com.stars.tv.widget.media.IjkVideoView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class MediaControllersFragment extends DialogFragment {

    private ViewGroup bottom_layout;
    private ImageButton playButton, favButton, hqButton;
    private TextView timeText, playText, favText, hqText, durationText, titleText;
    private SeekBar seekBar;

    public enum PlaybackState {
        PLAYING, NOT_PLAYING, STOPPED, PAUSED, BUFFERING
    }

    private Timer timer;
    private IjkVideoView mVideoView;

    public static MediaControllersFragment newInstance() {
        return new MediaControllersFragment();
    }

    final int REFRESH_MOVIE_HQ = 100;
    public Handler mHandler = new Handler();

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.windowAnimations = R.style.bottomSheet_animation;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        View view = (ViewGroup) inflater.inflate(R.layout.media_controllers, container, false);
        timer = new Timer();
        mVideoView = ((FullPlaybackActivity) getActivity()).getIjkVideoView();

        initView(view);
        return view;
    }

    public void initView(View view) {
        bottom_layout = view.findViewById(R.id.bottom_layout);
        setText(view);
        seekBar = view.findViewById(R.id.seekBar);
        constructSeekBar();
        updateSeekBar();

        playText = view.findViewById(R.id.play_text);
        playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_pause));
                playText.setText("Pause");
            } else {
                mVideoView.start();
                playButton.setImageDrawable(getResources().getDrawable(R.drawable.lb_ic_play));
                playText.setText("Play");
            }
        });
        playButton.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                playText.setVisibility(View.VISIBLE);
            } else playText.setVisibility(View.INVISIBLE);
        });

        playButton.requestFocus();

        favButton = view.findViewById(R.id.favorite_button);
        favText = view.findViewById(R.id.favorite_text);
        favButton.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                favText.setVisibility(View.VISIBLE);
            } else favText.setVisibility(View.INVISIBLE);
        });

        hqButton = view.findViewById(R.id.quality_button);
        hqText = view.findViewById(R.id.quality_text);
        hqButton.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                hqText.setVisibility(View.VISIBLE);
            } else hqText.setVisibility(View.INVISIBLE);
        });
        hqButton.setOnClickListener(v -> {
            mHandler.sendEmptyMessage(REFRESH_MOVIE_HQ);
//            showList();
        });


    }

    private void setText(View view) {
        durationText = view.findViewById(R.id.play_duration);
        timeText = view.findViewById(R.id.play_time);
        titleText = view.findViewById(R.id.play_title);
        durationText.setText(" / " + formatTime(mVideoView.getDuration()));
        timeText.setText(formatTime(mVideoView.getCurrentPosition()));
        SharedPreferences videoinfoshare = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String medName = videoinfoshare.getString("name", "");
        titleText.setText(medName);
    }

    public void setVideo(IjkVideoView video) {
        this.mVideoView = video;
    }

    private void constructSeekBar() {
        seekBar.setMax(mVideoView.getDuration());
        seekBar.setProgress(mVideoView.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) seekBar.setBackgroundColor(Color.parseColor("#22ebebeb"));
            else seekBar.setBackgroundColor(Color.TRANSPARENT);
        });
        seekBar.setOnClickListener(v -> {
            mVideoView.seekTo(seekBar.getProgress());
            mVideoView.start();
            close();
        });
        AtomicBoolean isChanged = new AtomicBoolean(false);
        seekBar.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (isChanged.get()) {
                        mVideoView.start();
                        isChanged.set(false);
                    }
                    close();
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    mVideoView.pause();
                    isChanged.set(true);
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (isChanged.get()) {
                        mVideoView.start();
                        isChanged.set(false);
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ((FullPlaybackActivity) getActivity()).getIjkVideoView().requestFocus();
                }
            }
            return false;
        });
    }

    private void updateSeekBar() {
        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                int currentPosition = mVideoView.getCurrentPosition();
                if (mVideoView.isPlaying()) {
                    seekBar.setProgress(currentPosition);
                    getActivity().runOnUiThread(() -> {
                        timeText.setText(formatTime(mVideoView.getCurrentPosition()));

                    });
                }
            }
        };
        timer.schedule(task, 100, 1000);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "" : hours + ":")
                + String.format("%2d:%02d", minutes % 60, seconds % 60);
    }

    private void close() {
        Fragment fragment = getFragmentManager().findFragmentByTag("controllers");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment).commit();

        ((FullPlaybackActivity) getActivity()).getIjkVideoView().requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }

}
