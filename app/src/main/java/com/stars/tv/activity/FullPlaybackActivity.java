package com.stars.tv.activity;

import android.accounts.NetworkErrorException;
import android.content.Context;import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.alibaba.fastjson.util.JavaBeanInfo;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.github.ybq.android.spinkit.style.Circle;
import com.stars.tv.R;
import com.stars.tv.adapter.ChildrenAdapter;
import com.stars.tv.adapter.EpisodeListView;
import com.stars.tv.adapter.EpisodeListViewAdapter;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.bean.IQiYiM3U8Bean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.presenter.IQiYiParseEpisodeListPresenter;
import com.stars.tv.presenter.IQiYiParseM3U8Presenter;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.widget.media.AndroidMediaController;
import com.stars.tv.widget.media.IjkVideoView;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.stars.tv.utils.Constants.EXT_VIDEO_COUNT;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;

public class FullPlaybackActivity extends BaseActivity {
//    IjkMediaPlayer ijkMediaPlayer;
//    private SurfaceView surfaceView;
//    private SurfaceHolder surfaceHolder;

    private IjkVideoView mVideoView;
    //    private AndroidMediaController mMediaController;
    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
    private EpisodeListView mEpisodeListView;
    private View episodeView;
    private PopupWindow popupWindow;
    private TextView textLoading;
    private Circle mCircleDrawable;

    private EpisodeListViewAdapter<String> adapter;
    private List<IQiYiMovieBean> mEplisodeList = new ArrayList<>();
    private String tvId, mVideoPath, name, albumId, latestOrder, malbumImagUrl;
    private int currentPosition, mEpisode, mVideoCount, mVideoType;

    private float densityRatio = 1.0f; // 密度比值系数（密度比值：一英寸中像素点除以160）
    // 自动隐藏Episode的时间
    private static final int HIDDEN_TIME = 5000;
    private Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // 又回到了主线程
            showOrHideEpisode();
        }
    };

    @Override
    protected void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_video_fullplayback);
        name = getIntent().getStringExtra("name");
        mVideoPath = getIntent().getStringExtra("mVideoPath");
        albumId = getIntent().getStringExtra("albumId");
        latestOrder = getIntent().getStringExtra("latestOrder");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);
        mEpisode = getIntent().getIntExtra("mEpisode", 0);
        Log.v("FFFmEpisode",mEpisode+"");

        // for history usage
        mVideoType = getIntent().getIntExtra(EXT_VIDEO_TYPE, 0);
        mVideoCount = getIntent().getIntExtra(EXT_VIDEO_COUNT, 1);
        malbumImagUrl = getIntent().getStringExtra(EXT_VIDEO_IMAGE_URL);
        // -----------------
        densityRatio = getResources().getDisplayMetrics().density; // 表示获取真正的密度

        loading(View.VISIBLE);
        initVideoView();
        startPlay();
        parseIQiYiEpisodeList(albumId, Integer.valueOf(latestOrder), 1);

    }

    public void initVideoView() {
        Log.v("vvvVideoPreview1", mVideoPath);
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

        mVideoView = (IjkVideoView) findViewById(R.id.mvideoView);
        mVideoView.setMediaController(mMediaController);
        mHudView = (TableLayout) findViewById(R.id.mhud_view);
        mVideoView.setHudView(mHudView);
        mVideoView.setOnPreparedListener(iMediaPlayer -> loading(View.INVISIBLE));
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                loading(View.VISIBLE);
                mEpisode=mEpisode+1;
                parseIQiYiRealM3U8WithTvId(mEplisodeList.get(mEpisode).getTvId());
                adapter.setSelectedPositions(Arrays.asList(mEpisode));
    }
        });
    }

    private void startPlay() {
        if (mVideoPath == "") {
            Toast.makeText(this, "No Video Found! Press Back Button To Exit", Toast.LENGTH_LONG).show();
        } else {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.seekTo(currentPosition);
            mVideoView.start();
        }
    }

    private void loading(Integer visibility) {
        textLoading = (TextView) findViewById(R.id.mloading);
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        textLoading.setCompoundDrawables(null, null, mCircleDrawable, null);
        textLoading.setVisibility(visibility);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCircleDrawable.start();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("key", event.toString());
        Log.v("key", keyCode + "");
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    showOrHideEpisode();
                    if (!mEpisodeListView.isShown()) {
                        mEpisodeListView.setVisibility(View.VISIBLE);
//                        mEpisodeListView.requestFocus();
                        adapter.setSelectedPositions(Arrays.asList(mEpisode));
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    showOrHideEpisode();
                    if (!mEpisodeListView.isShown()) {
                        mEpisodeListView.setVisibility(View.VISIBLE);
//                        mEpisodeListView.requestFocus();
                        adapter.setSelectedPositions(Arrays.asList(mEpisode));
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (!mMediaController.isShowing()) {
                        mMediaController.show();
                        return true;
                    } else {
                        mVideoView.seekTo(mVideoView.getCurrentPosition() + 5);
                        return true;
                    }
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (!mMediaController.isShowing()) {
                        mMediaController.show();
                        return true;
                    } else {
                        mVideoView.seekTo(mVideoView.getCurrentPosition() - 5);
                        return true;
                    }
                case KeyEvent.KEYCODE_BACK:
                    returnHistoryUpdate();
                    return true;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void returnHistoryUpdate(){
        if ( mVideoCount > 0 ) {
            ExtVideoBean bean = new ExtVideoBean();
            bean.setVideoType(mVideoType);
            bean.setAlbumId(albumId);
            bean.setVideoId(mEplisodeList.get(mEpisode).getTvId());
            bean.setVideoName(mEplisodeList.get(mEpisode).getName());
            bean.setVideoCurrentViewOrder(mEpisode+1);
            bean.setVideoPlayUrl(mEplisodeList.get(mEpisode).getPlayUrl());
            bean.setAlbumImageUrl(malbumImagUrl);
            bean.setVideoCount(mVideoCount);
            bean.setVideoLatestOrder(Integer.valueOf(latestOrder));
            bean.setVideoPlayPosition(mVideoView.getCurrentPosition());
            try {
                LeanCloudStorage.updateIQiyHistory(bean, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        returnPlayData();
                    }
                });
            }catch ( Exception e ){
                returnPlayData();
            }
        }
        else
            returnPlayData();
    }

    private void returnPlayData() {
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("currentPosition", mVideoView.getCurrentPosition());
        intent.putExtra("currentEpisode", mEpisode);
        intent.putExtra("currentPath", mVideoPath);

        //设置返回数据
        FullPlaybackActivity.this.setResult(RESULT_OK, intent);
        //关闭Activity
        FullPlaybackActivity.this.finish();
    }

    public void initEpisodeList() {
        episodeView = (View) getLayoutInflater().inflate(R.layout.episodelist, null);
        popupWindow = new PopupWindow(episodeView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

        mEpisodeListView = (EpisodeListView) episodeView.findViewById(R.id.mepisodelistview);

        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (int i = 0; i < Integer.valueOf(latestOrder); i++) {
            stringArrayList.add(String.valueOf(i + 1));
        }
        final String[] episodes = stringArrayList.toArray(new String[stringArrayList.size()]);
        stringArrayList.clear();
        if (Integer.valueOf(latestOrder) > 10) {
            for (int i = 0; i < Integer.valueOf(latestOrder) / 10; i++) {
                int min = Integer.valueOf(i + "1");
                stringArrayList.add(min + "-" + (i + 1) + "0");
            }
            if (Integer.valueOf(latestOrder) % 10 != 0) {
                if (Integer.valueOf(latestOrder) % 10 == 1) {
                    stringArrayList.add(latestOrder);
                } else {
                    stringArrayList.add((Integer.valueOf(latestOrder) / 10) + "1-" + latestOrder);
                }
            }
        } else {
            stringArrayList.add("1-" + latestOrder);
        }

        final String[] groups = stringArrayList.toArray(new String[stringArrayList.size()]);
        Log.v("VideoPreviewgroups", stringArrayList.toString());

        adapter = new EpisodeListViewAdapter<String>() {
            @Override
            public List<String> getChildrenList() {
                return Arrays.asList(episodes);
            }

            @Override
            public List<String> getParentList() {
                return Arrays.asList(groups);
            }

            @Override
            public int getChildrenPosition(int childPosition) {
                return childPosition * 10;
            }

            @Override
            public int getParentPosition(int parentPosition) {
                return parentPosition / 10;
            }
        };

//        adapter.setSelectedPositions(Arrays.asList(3));
        mEpisodeListView.setAdapter(adapter);
        mEpisodeListView.setChildrenItemClickListener(new ChildrenAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {
                loading(View.VISIBLE);
                tvId = mEplisodeList.get(position).getTvId();
                parseIQiYiRealM3U8WithTvId(tvId);
                mEpisode = position;
                adapter.setSelectedPositions(Arrays.asList(mEpisode));
            }
        });

    }

    private void showOrHideEpisode() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            // 将dp转换为px
            int episodeHeightPixel = (int) (densityRatio * 50);
            popupWindow.showAsDropDown(mVideoView, 0, -episodeHeightPixel);
            // 延时执行
            handler.postDelayed(r, HIDDEN_TIME);
        }
    }

    /**
     * 获取视频真是播放M3U8 （知否第1集）
     *
     * @param tvId 视频tvId  1745487500
     */
    private void parseIQiYiRealM3U8WithTvId(String tvId) {
        IQiYiParseM3U8Presenter ps = new IQiYiParseM3U8Presenter();
        ps.requestIQiYiRealPlayUrlWithTvId(tvId, new CallBack<List<IQiYiM3U8Bean>>() {
            @Override
            public void success(List<IQiYiM3U8Bean> list) {
                //TODO 获取成功在此得到真实播放地址的List，可能会有HD,SD,1080P
                mVideoPath = list.get(0).getM3u();
                for (IQiYiM3U8Bean bean : list) {
                    Log.v("VideoPreviewM3U8", bean.toString());
                }
                startPlay();

            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                mVideoPath = "";
                Log.v("VideoPreviewActivity", "获取失败parseIQiYiRealM3U8WithTvId");
                textLoading.setText("加载失败！");
                textLoading.setBackgroundColor(Color.BLACK);
                textLoading.setTextColor(Color.WHITE);
            }
        });
    }

    /**
     * 获取电视剧剧集列表
     *
     * @param albumId 专辑id， 216266201 //知否知否
     * @param size    获取集数
     * @param pageNum 页码
     */
    private void parseIQiYiEpisodeList(String albumId, int size, int pageNum) {
        IQiYiParseEpisodeListPresenter ps = new IQiYiParseEpisodeListPresenter();
        ps.requestIQiYiEpisodeList(albumId, size, pageNum, new CallBack<List<IQiYiMovieBean>>() {
            @Override
            public void success(List<IQiYiMovieBean> list) {
                mEplisodeList.clear();
                mEplisodeList.addAll(list);
                //TODO 获取电视剧剧集列表
                for (IQiYiMovieBean bean : list) {
                    Log.v("VideoPreviewEpisodeList", bean.toString());
                }
                initEpisodeList();
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

}
