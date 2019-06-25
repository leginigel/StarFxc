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
import com.stars.tv.widget.media.AndroidMediaController;
import com.stars.tv.widget.media.IjkVideoView;

import org.json.JSONArray;

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
    Button castStream;
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
        YT, Twitch, FB, BiliBili, Douyu, Address
    }
//    private Runnable r = new Runnable() {
//        @Override
//        public void run() {
//            // 又回到了主线程
//            showOrHideEpisode();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_stream);

        unbinder = ButterKnife.bind(this);
        // -----------------
        densityRatio = getResources().getDisplayMetrics().density; // 表示获取真正的密度

        streamPresenter = new StreamPresenter();
        mCate = StreamCate.YT;
        for (int i = 0;i < linearLayout.getChildCount();i++){
            int drawable = 0;
            StreamCate cate = null;
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
                    drawable = R.drawable.bilibili;
                    cate = StreamCate.BiliBili;
                    break;
                case 5:
                    drawable = R.drawable.temp_tv_icon;
                    cate = StreamCate.Address;
                    break;
            }
            int finalDrawable = drawable;
            int finalI = i;
            StreamCate finalCate = cate;
            linearLayout.getChildAt(i).setOnFocusChangeListener((v, hasFocus)->{
                if(hasFocus) {
                    serverImg.setImageDrawable(getResources().getDrawable(finalDrawable));
                    mCate = finalCate;
                    if(finalI >= 3){
                        address.setVisibility(View.VISIBLE);
                        addressText.setVisibility(View.VISIBLE);
                    }
                    else {
                        address.setVisibility(View.GONE);
                        addressText.setVisibility(View.GONE);
                    }
                }
            });
        }
        linearLayout.getChildAt(0).requestFocus();

//        castYTStream("ibz7k3DjIeA");
//        String test1 = null;
//        test1 = parseYTM3U8(test1);
//        YTM3U8Bean ytM3U8Bean = new Gson().fromJson(test1, YTM3U8Bean.class);
//
//        address.setText(ytM3U8Bean.getStreamingData().getHlsManifestUrl());
//        stream.setText("");
        castHuyaStream("377885");
//        castDouyuStream("1282190");

        castStream.setOnClickListener(v -> {
            if(address.getText() != null || stream.getText() != null) {
                switch (mCate) {
                    case YT:
                        castYTStream(stream.getText().toString());
                        break;
                    case Twitch:
                        break;
                    case FB:
                        break;
                    case Douyu:
                        break;
                    case BiliBili:
                        castBilibiliStream(stream.getText().toString());
                        break;
                    case Address: {
                        mVideoPath = address.getText().toString() + stream.getText().toString();
                        loading(View.VISIBLE);
                        initVideoView();
                        startPlay();
                        break;
                    }
                }
            }
        });
    }

    private void castDouyuStream(String channel){
        streamPresenter.getDouyuRealPlayUrl(channel, new CallBack<String>() {
            @Override
            public void success(String s) {

            }

            @Override
            public void error(String msg) {

            }
        });
    }

    private void castHuyaStream(String channel){
        streamPresenter.getHuyaRealPlayUrl(channel);

    }

    private void castBilibiliStream(String room_id){
        streamPresenter.getBilibiliRealPlayUrl(room_id, new CallBack<String>() {
            @Override
            public void success(String s) {
                Log.d("Bilibili URL ", s);
                cast(s);
            }

            @Override
            public void error(String msg) {
                showError();
            }
        });
    }

    private void castYTStream(String video_id){
        streamPresenter.getYTVideoInfo(video_id, new CallBack<YTM3U8Bean>() {
            @Override
            public void success(YTM3U8Bean ytM3U8Bean) {
                Log.d("YouTube URL ", ytM3U8Bean.getStreamingData().getHlsManifestUrl());
                cast(ytM3U8Bean.getStreamingData().getHlsManifestUrl());
            }

            @Override
            public void error(String msg) {
                showError();
            }
        });
    }

    private void cast(String url){
        mVideoPath = url;
        loading(View.VISIBLE);
        initVideoView();
        startPlay();
    }

    private void showError(){
        mVideoPath = "";
        Log.v("error", "获取失败");
        textLoading.setText("加载失败！");
        textLoading.setBackgroundColor(Color.BLACK);
        textLoading.setTextColor(Color.WHITE);
    }

    public void initVideoView() {
        // init UI
        mMediaController = new AndroidMediaController(this, false);
//        mMediaController = new AndroidMediaController(this, false);
//        mMediaController.setSupportActionBar(actionBar);
        mMediaController.clearFocus();
//        mMediaController.setVisibility(View.INVISIBLE);
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
            Toast.makeText(this, "No Video Found! Press Back Button To Exit", Toast.LENGTH_LONG).show();
        } else {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.seekTo(0);
            mVideoView.start();
        }
    }

    private void loading(Integer visibility) {
        linearLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.GONE);
        castStream.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        textLoading = (TextView) findViewById(R.id.mloading);
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        textLoading.setCompoundDrawables(null, null, mCircleDrawable, null);
        textLoading.setVisibility(visibility);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mCircleDrawable.start();
//    }

    @Override
    public void onStop() {
        super.onStop();
//        mCircleDrawable.stop();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
    }
}
