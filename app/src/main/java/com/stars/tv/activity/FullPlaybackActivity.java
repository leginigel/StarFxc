package com.stars.tv.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

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
import com.stars.tv.fragment.MediaControllersFragment;
import com.stars.tv.presenter.IQiYiParseEpisodeListPresenter;
import com.stars.tv.presenter.IQiYiParseM3U8Presenter;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.utils.Utils;
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

    private Context mContext;
    private IjkVideoView mVideoView;
    private TableLayout mHudView;
    private EpisodeListView mEpisodeListView;
    private View episodeView;
    private PopupWindow episodePopupWindow;
    private TextView textLoading;
    private Circle mCircleDrawable;

    private MediaControllersFragment mediaControllersFragment;

    private EpisodeListViewAdapter<String> adapter;
    private List<IQiYiMovieBean> mEplisodeList = new ArrayList<>();
    private String tvId, mVideoPath, name, albumId, latestOrder, malbumImagUrl;
    private int currentPosition = 0;
    private int mEpisode, mVideoCount, mVideoType;
    private ChildrenAdapter mChildrenAdapter;

    final int REFRESH_MOVIE_HQ = 100;
    int currentHQ = 0;
    Boolean isLoading = true;

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
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_MOVIE_HQ:
                    showHQList();
                    break;
                default:
                    currentHQ = msg.what;
                    currentPosition = mVideoView.getCurrentPosition();
                    parseIQiYiRealM3U8WithTvId(tvId);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_video_fullplayback);
        mContext = this;
        Intent intent = getIntent();
        tvId = intent.getStringExtra("tvId");
        name = intent.getStringExtra("name");
        albumId = intent.getStringExtra("albumId");
        currentPosition = intent.getIntExtra("currentPosition", 0);
        latestOrder = intent.getStringExtra("latestOrder");
        mEpisode = intent.getIntExtra("mEpisode", 0);

        // for history usage
        mVideoType = intent.getIntExtra(EXT_VIDEO_TYPE, 0);
        mVideoCount = intent.getIntExtra(EXT_VIDEO_COUNT, 1);
        malbumImagUrl = intent.getStringExtra(EXT_VIDEO_IMAGE_URL);
        // -----------------
        densityRatio = getResources().getDisplayMetrics().density; // 表示获取真正的密度
        initLoading();
        initVideoView();
        parseIQiYiRealM3U8WithTvId(tvId);
        if (null != latestOrder) {
            parseIQiYiEpisodeList(albumId, Integer.valueOf(latestOrder), 1);
        }

        Utils.setSharedValue(mContext, "mVideoType", mVideoType);
        mediaControllersFragment = MediaControllersFragment.newInstance();
        mediaControllersFragment.setVideo(mVideoView);
        mediaControllersFragment.setHandler(mHandler);
    }

    public void initVideoView() {
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.mvideoView);
        mVideoView.setMediaController(null);
        mHudView = (TableLayout) findViewById(R.id.mhud_view);
        mVideoView.setHudView(mHudView);

        mVideoView.setOnPreparedListener(iMediaPlayer -> hideLoading());
        mVideoView.setOnInfoListener((iMediaPlayer, info, error) -> {
            if (info == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                showLoading();
            } else if (info == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                hideLoading();
            }
            return false;
        });
        if (null != latestOrder) {
            mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer mp) {
                    showLoading();
                    mEpisode = mEpisode + 1;
                    currentPosition = 0;
                    parseIQiYiRealM3U8WithTvId(mEplisodeList.get(mEpisode).getTvId());
                    mChildrenAdapter.setSelectedPositions(mEpisode);
                }
            });
        }
        mVideoView.setOnErrorListener((iMediaPlayer, framework_err, impl_err) -> {
            showLoadingError(framework_err + "," + impl_err);
            return true;
        });
    }

    private void startPlay() {
        if (mVideoPath == "") {
            Toast.makeText(this, "No Video Found! Press Back Button To Exit", Toast.LENGTH_LONG).show();
        } else {
            Log.v("FullPlaybackmVideoPath", mVideoPath);
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.seekTo(currentPosition);
            mVideoView.start();
        }
    }

    private void initLoading() {
        if (NetUtil.isConnected()) {
            mCircleDrawable = new Circle();
            mCircleDrawable.setBounds(0, 0, 100, 100);
            mCircleDrawable.setColor(getResources().getColor(R.color.color_focus));
            textLoading = (TextView) findViewById(R.id.mloading);
            textLoading.setCompoundDrawables(null, null, mCircleDrawable, null);
        } else {
            showLoadingError("0");
        }
    }

    private void hideLoading() {
        if (textLoading != null && textLoading.getVisibility() == View.VISIBLE) {
            textLoading.setVisibility(View.GONE);
        }
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
        isLoading = false;
    }

    private void showLoading() {
        if (NetUtil.isConnected()) {
            if (textLoading != null) {
                textLoading.setVisibility(View.VISIBLE);
                textLoading.setText("");
            }
            mCircleDrawable.start();
        } else {
            showLoadingError("0");
        }
    }

    /**
     * @param errorCode 0 -> 网络错误  else ->error code
     */
    private void showLoadingError(String errorCode) {
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        }
        if (textLoading != null) {
            textLoading.setVisibility(View.VISIBLE);
            textLoading.setTextColor(getResources().getColor(R.color.color_all_white));
            if (errorCode.equals("0")) {
                textLoading.setText(getString(R.string.str_network_error));
            } else {
                textLoading.setText(getString(R.string.str_code_error, errorCode));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (null != latestOrder) {
                        showOrHideEpisode();
                        if (!mEpisodeListView.isShown()) {
                            mEpisodeListView.setVisibility(View.VISIBLE);
                            mChildrenAdapter.setCurrentPosition(mEpisode);
                            mChildrenAdapter.setSelectedPositions(mEpisode);
                            return true;
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("controllers");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    mediaControllersFragment.show(ft, "controllers");
                    break;
                case KeyEvent.KEYCODE_BACK:
                    handler.removeCallbacks(r);
                    if (null != latestOrder)
                        episodePopupWindow.dismiss();
                    returnHistoryUpdate();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void returnHistoryUpdate() {
        if (mVideoCount > 0 && malbumImagUrl != null) {
            ExtVideoBean bean = new ExtVideoBean();
            bean.setVideoType(mVideoType);
            bean.setAlbumId(albumId);
            //Modified for non-Series video
            if (null != latestOrder) {
                bean.setVideoId(mEplisodeList.get(mEpisode).getTvId());
                bean.setVideoName(mEplisodeList.get(mEpisode).getName());
                bean.setVideoCurrentViewOrder(mEpisode);
                bean.setVideoLatestOrder(Integer.valueOf(latestOrder));
            }
            bean.setVideoPlayUrl(mVideoPath);
            bean.setAlbumImageUrl(malbumImagUrl);
            bean.setVideoCount(mVideoCount);
            bean.setVideoPlayPosition(mVideoView.getCurrentPosition());
            try {
                LeanCloudStorage.updateIQiyHistory(bean, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        returnPlayData();
                    }
                });
            } catch (Exception e) {
                returnPlayData();
            }
        } else
            returnPlayData();
    }

    private void returnPlayData() {
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        if (isLoading)
            intent.putExtra("currentPosition", currentPosition);
        else
            intent.putExtra("currentPosition", mVideoView.getCurrentPosition());
        intent.putExtra("currentPath", mVideoPath);
        if (null != latestOrder) {
            intent.putExtra("currentEpisode", mEpisode);
        }
        intent.putExtra("exit", true);

        //设置返回数据
        FullPlaybackActivity.this.setResult(RESULT_OK, intent);
        //关闭Activity
        FullPlaybackActivity.this.finish();
    }

    public void initEpisodeList() {
        episodeView = (View) getLayoutInflater().inflate(R.layout.episodelist, null);
        episodePopupWindow = new PopupWindow(episodeView, LayoutParams.MATCH_PARENT, 260, true);
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
        mChildrenAdapter = adapter.getEpisodesAdapter();
        adapter.setSelectedPositions(mEpisode);
        mEpisodeListView.setAdapter(adapter);
        mEpisodeListView.setChildrenItemClickListener(new ChildrenAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {
                showLoading();
                mEpisode = position;
                adapter.setSelectedPositions(mEpisode);
                currentPosition = 0;
                tvId = mEplisodeList.get(position).getTvId();
                parseIQiYiRealM3U8WithTvId(tvId);
                Utils.setSharedValue(mContext, "mEpisodeName", mEplisodeList.get(mEpisode).getName());
                handler.removeCallbacks(r);
                episodePopupWindow.dismiss();
            }
        });
        if (null != latestOrder) {
            Utils.setSharedValue(mContext, "mEpisodeName", mEplisodeList.get(mEpisode).getName());
        }
    }

    private void showOrHideEpisode() {
        if (episodePopupWindow.isShowing()) {
            episodePopupWindow.dismiss();
        } else {
            // 将dp转换为px
            int episodeHeightPixel = (int) (densityRatio * 50);
            episodePopupWindow.showAsDropDown(mVideoView, 0, -episodeHeightPixel);
            mChildrenAdapter.setSelectedPositions(mEpisode);
            // 延时执行
            handler.postDelayed(r, HIDDEN_TIME);
        }
    }

    public IjkVideoView getIjkVideoView() {
        return mVideoView;
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
                if (null != list && list.size() > 0) {
                    showLoading();
                    switch (currentHQ) {
                        case 0:    //默认 取第一个
                            mVideoPath = list.get(0).getM3u();
                            break;
                        case 1:    //省流  96 LD 210p
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getVd() == 96) {
                                    mVideoPath = list.get(i).getM3u();
                                }
                            }
                            break;
                        case 2:   //标清    1  SD 360p
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getVd() == 1) {
                                    mVideoPath = list.get(i).getM3u();
                                }
                            }
                            break;
                        case 3:   //高清    2 HD 540p, 21 HD 540p
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getVd() == 2) {
                                    mVideoPath = list.get(i).getM3u();
                                } else if (list.get(i).getVd() == 21) {
                                    mVideoPath = list.get(i).getM3u();
                                }
                            }
                            break;
                        case 4:   //超清    4 TD 720p, 14 TD 720p, 17 TD 720p
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getVd() == 4) {
                                    mVideoPath = list.get(i).getM3u();
                                } else if (list.get(i).getVd() == 14) {
                                    mVideoPath = list.get(i).getM3u();
                                } else if (list.get(i).getVd() == 17) {
                                    mVideoPath = list.get(i).getM3u();
                                }
                            }
                            break;
                        case 5:   //蓝光    5 BD 1080p，18 BD 1080p
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getVd() == 5) {
                                    mVideoPath = list.get(i).getM3u();
                                } else if (list.get(i).getVd() == 18) {
                                    mVideoPath = list.get(i).getM3u();
                                }
                            }
                            break;
                    }
                    if ("".equals(mVideoPath)) {
                        mVideoPath = list.get(0).getM3u();
                    }
                    startPlay();
                } else {
                    mVideoPath = "";
                    mVideoView.stopPlayback();
                    mVideoView.release(true);
                    showLoadingError("1");

                }
                for (IQiYiM3U8Bean bean : list) {
                    Log.v("FullPlaybackM3U8", bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                mVideoPath = "";
                Log.v("FullPlayback", "获取失败parseIQiYiRealM3U8WithTvId");
                showLoadingError("0");
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
                    Log.v("FullPlaybackEpisodeList", bean.toString());
                }
                initEpisodeList();
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 10 4k,
     * 19 4k,
     * 5 BD 1080p,
     * 18 BD 1080p,
     * 21 HD 540p,
     * 2 HD 540p,
     * 14 TD 720p,
     * 4 TD 720p,
     * 17 TD 720p,
     * 96 LD 210p,
     * 1  SD 360p,
     */
    public void showHQList() {
        final String[] items = {"默认", "省流", "标清", "高清", "超清", "蓝光"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        alertBuilder.setTitle("请选择画质");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(FullPlaybackActivity.this, "选择的画质为：" + items[i], Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(i);
            }
        });
        alertBuilder.show();
    }

}