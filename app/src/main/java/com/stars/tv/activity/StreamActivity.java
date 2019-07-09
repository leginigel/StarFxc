package com.stars.tv.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Circle;
import com.stars.tv.R;
import com.stars.tv.bean.YTM3U8Bean;
import com.stars.tv.presenter.StreamPresenter;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.widget.media.AndroidMediaController;
import com.stars.tv.widget.media.IjkVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class StreamActivity extends BaseActivity {

    Unbinder unbinder;
    @BindView(R.id.name_address)
    TextView addressText;
    @BindView(R.id.name_stream)
    TextView streamText;
    @BindView(R.id.constraint)
    ConstraintLayout constraintLayout;
    @BindView(R.id.services)
    LinearLayout linearLayout;
    @BindView(R.id.bilibili_frame)
    FrameLayout frameLayout;
    @BindView(R.id.image_server)
    ImageView serverImg;
    @BindView(R.id.editText_address)
    EditText address;
    @BindView(R.id.editText_stream)
    EditText stream;
    @BindView(R.id.btn_cast)
    Button startStreamBtn;
    @BindView(R.id.mloading)
    TextView textLoading;
    private StreamPresenter streamPresenter;
    private IjkVideoView mVideoView;
    //    private AndroidMediaController mMediaController;
    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
    private View episodeView;
    private PopupWindow popupWindow;
    private Circle mCircleDrawable;

    private String tvId, mVideoPath, name, albumId, latestOrder, malbumImagUrl;
    private int currentPosition, mEpisode, mVideoCount, mVideoType;
    private int mItemIdx;

    private float densityRatio = 1.0f; // 密度比值系数（密度比值：一英寸中像素点除以160）
    // 自动隐藏Episode的时间
    private static final int HIDDEN_TIME = 5000;
    private Handler handler = new Handler();

    private StreamCate mCate;
    enum StreamCate{
        YT, Twitch, FB, Douyu, Huya, BiliBili, Address
    }

    @Override
    protected void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_stream);

        unbinder = ButterKnife.bind(this);
        // -----------------
        densityRatio = getResources().getDisplayMetrics().density; // 表示获取真正的密度

        mCircleDrawable = new Circle();
        streamPresenter = new StreamPresenter();
        mCate = StreamCate.YT;
        // Setup Top Nav Focus Scheme
        for (int i = 0;i < linearLayout.getChildCount();i++){
            int drawable = 0;
            StreamCate cate = null;
            // Define server image
            switch (i) {
                case 0:
                    drawable = R.drawable.ic_youtube_red_square;
                    cate = StreamCate.YT;
                    break;
                case 1:
                    drawable = R.drawable.ic_glitch_white;
                    cate = StreamCate.Twitch;
                    break;
                case 2:
                    drawable = R.drawable.ic_icon_facebook;
                    cate = StreamCate.FB;
                    break;
                case 3:
                    drawable = R.drawable.douyu_ico;
                    cate = StreamCate.Douyu;
                    break;
                case 4:
                    drawable = R.drawable.huya_ico;
                    cate = StreamCate.Huya;
                    break;
                case 5:
                    drawable = R.drawable.bilibili;
                    cate = StreamCate.BiliBili;
                    break;
                case 6:
                    drawable = R.drawable.temp_tv_icon;
                    cate = StreamCate.Address;
                    break;
            }
            int finalDrawable = drawable;
            int finalI = i;
            StreamCate finalCate = cate;
            linearLayout.getChildAt(i).setOnFocusChangeListener((v, hasFocus)->{
                if(hasFocus) {
                    ((TextView) v).setTextColor(getResources().getColor(R.color.color_focus));
                    serverImg.setImageDrawable(getResources().getDrawable(finalDrawable));
                    mCate = finalCate;
                    switch (finalI){
                        case 0:
                        case 1:
                            streamText.setText("直播网址");
                            break;
                        case 2:
                        case 3:
                            streamText.setText("服务不支援");
                            break;
                        case 6:
                            streamText.setText("串流名称 / 串流金钥");
                            break;
                        case 4:
                        case 5:
                            streamText.setText("直播房间号码");
                            break;
                    }
                    stream.setVisibility((finalI == 2 || finalI == 3) ? View.GONE : View.VISIBLE);
                    addressText.setVisibility((finalI == 6) ? View.VISIBLE : View.GONE);
                    address.setVisibility((finalI == 6) ? View.VISIBLE : View.GONE);
                    startStreamBtn.setVisibility((finalI == 2 || finalI == 3) ? View.GONE : View.VISIBLE);
                }
                else{
                    ((TextView) v).setTextColor(getResources().getColor(R.color.text_white));
                    if(address.isFocused() || stream.isFocused()){
                        ((TextView) v).setTextColor(getResources().getColor(R.color.color_checked));
                    }
                    if(!stream.isFocused() && !address.isFocused())
                        stream.setText("");
                }
                ViewUtils.scaleAnimator(v, hasFocus, 1.2f, 150);
            });
        }
        linearLayout.getChildAt(0).requestFocus();

        // Setup EditText Up Key Navigation
        address.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP){
                linearLayout.getChildAt(6).requestFocus();
                return true;
            }
            else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                return true;
            }
            return false;
        });
        stream.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP){
                switch (mCate) {
                    case YT:
                        linearLayout.getChildAt(0).requestFocus();
                        break;
                    case Twitch:
                        linearLayout.getChildAt(1).requestFocus();
                        break;
                    case FB:
                        linearLayout.getChildAt(2).requestFocus();
                        break;
                    case Douyu:
                        linearLayout.getChildAt(3).requestFocus();
                        break;
                    case Huya:
                        linearLayout.getChildAt(4).requestFocus();
                        break;
                    case BiliBili:
                        linearLayout.getChildAt(5).requestFocus();
                        break;
                    case Address:
                        address.requestFocus();
                        break;
                }
                return true;
            }
            else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                return true;
            }
            return false;
        });

        startStreamBtn.setOnClickListener(v -> {
            if(address.getText() != null || stream.getText() != null) {
                switch (mCate) {
                    case YT:
                        streamYTHLS(stream.getText().toString());
                        break;
                    case Twitch:
                        streamTwitchHLS(stream.getText().toString()/*"tfblade"*/);
                        break;
                    case FB:
                        break;
                    case Douyu:
//                        streamDouyuHLS("160504");
                        break;
                    case Huya:
                        streamHuyaHLS(stream.getText().toString());
                        break;
                    case BiliBili:
                        streamBilibiliHLS(stream.getText().toString());
                        break;
                    case Address: {
                        stream(address.getText().toString() + stream.getText().toString());
                        break;
                    }
                }
            }
        });
        startStreamBtn.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                // change button color if focused
                switch (mCate) {
                    case YT:
                        startStreamBtn.setTextColor(Color.RED);
                        break;
                    case Twitch:
                        startStreamBtn.setTextColor(Color.parseColor("#6441A4"));
                        break;
                    case Huya:
                        startStreamBtn.setTextColor(Color.parseColor("#934908"));
                        break;
                    case BiliBili:
                        startStreamBtn.setTextColor(Color.parseColor("#CC4A72"));
                        break;
                    case Address:
                        startStreamBtn.setTextColor(Color.parseColor("#001442"));
                        break;
                }
                startStreamBtn.setBackgroundColor(getResources().getColor(R.color.color_focus));
            }
            else {
                startStreamBtn.setTextColor(Color.WHITE);
                startStreamBtn.setBackgroundColor(Color.parseColor("#005594"));
            }
        });
        startStreamBtn.setNextFocusLeftId(startStreamBtn.getId());
        startStreamBtn.setNextFocusRightId(startStreamBtn.getId());

        initVideoView();
    }

    private void streamFBDash(String channel){
        // IjkPlayer not support DASH video format
    }

    private void streamTwitchHLS(String channel){
        streamPresenter.getTwitchRealPlayUrl(channel, new CallBack<String>() {
            @Override
            public void success(String s) {
                Log.d("Twitch URL ", s);
                stream(s);
            }

            @Override
            public void error(String msg) {
                showError(msg);
            }
        });
    }

    private void streamDouyuHLS(String channel){
        streamPresenter.getDouyuRealPlayUrl(channel, new CallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void error(String msg) {

            }
        });
    }

    private void streamHuyaHLS(String channel){
        streamPresenter.getHuyaRealPlayUrl(channel, new CallBack<String>() {
            @Override
            public void success(String s) {
                Log.d("Huya URL ", s);
                stream(s);
            }

            @Override
            public void error(String msg) {
                showError(msg);
            }
        });

    }

    private void streamBilibiliHLS(String room_id){
        streamPresenter.getBilibiliRealPlayUrl(room_id, new CallBack<String>() {
            @Override
            public void success(String s) {
                Log.d("Bilibili URL ", s);
                stream(s);
            }

            @Override
            public void error(String msg) {
                showError(msg);
            }
        });
    }

    private void streamYTHLS(String video_id){
        streamPresenter.getYTVideoInfo(video_id, new CallBack<YTM3U8Bean>() {
            @Override
            public void success(YTM3U8Bean ytM3U8Bean) {
                if(ytM3U8Bean.getStreamingData().getHlsManifestUrl() != null) {
                    Log.d("YouTube URL ", ytM3U8Bean.getStreamingData().getHlsManifestUrl());
                    stream(ytM3U8Bean.getStreamingData().getHlsManifestUrl());
                }
                else showError("Cannot Play YouTube Video, Please Enter Live Stream URL");
            }

            @Override
            public void error(String msg) {
                showError(msg);
            }
        });
    }

    private void stream(String url){
        mVideoPath = url;
        loading(View.VISIBLE);
        startPlay();
    }

    private void showError(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        stream.setText("");
        mVideoPath = "";
        Log.v("error", "获取失败");
        textLoading.setText("加载失败！");
        textLoading.setBackgroundColor(Color.BLACK);
        textLoading.setTextColor(Color.WHITE);
    }

    public void initVideoView() {
        // init UI
        mMediaController = new AndroidMediaController(this, false);
//        mMediaController.setSupportActionBar(actionBar);
        mMediaController.clearFocus();
        mMediaController.hide();
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.bili_bili);
        mVideoView.setMediaController(mMediaController);
        mHudView = (TableLayout) findViewById(R.id.mhud_view);
        mVideoView.setHudView(mHudView);
        mVideoView.setOnPreparedListener(iMediaPlayer -> loading(View.INVISIBLE));
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                loading(View.VISIBLE);
            }
        });
    }

    private void startPlay() {
        if (mVideoPath == "") {
            Toast.makeText(this, "No Video Found!", Toast.LENGTH_LONG).show();
        } else {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.seekTo(0);
            mVideoView.start();
        }
    }

    private void loading(Integer visibility) {
        linearLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);
        startStreamBtn.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        textLoading.setCompoundDrawables(null, null, mCircleDrawable, null);
        textLoading.setVisibility(visibility);

        // show the circle when stream loading
        mCircleDrawable.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(frameLayout.getVisibility() == View.VISIBLE){
            linearLayout.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            startStreamBtn.setVisibility(View.VISIBLE);
            startStreamBtn.requestFocus();
            frameLayout.setVisibility(View.GONE);
            mCircleDrawable.stop();
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
            IjkMediaPlayer.native_profileEnd();
        }
        else
            super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCircleDrawable.stop();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }
}