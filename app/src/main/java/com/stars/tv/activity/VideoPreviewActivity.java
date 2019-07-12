package com.stars.tv.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stars.tv.R;
import com.stars.tv.adapter.ChildrenAdapter;
import com.stars.tv.adapter.EpisodeListView;
import com.stars.tv.adapter.EpisodeListViewAdapter;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.bean.IQiYiBaseBean;
import com.stars.tv.bean.IQiYiM3U8Bean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.fragment.MediaInfoListFragment;
import com.stars.tv.presenter.IQiYiParseEpisodeListPresenter;
import com.stars.tv.presenter.IQiYiParseM3U8Presenter;
import com.stars.tv.presenter.IQiYiParseStarRecommendPresenter;
import com.stars.tv.presenter.IQiYiParseVideoBaseInfoPresenter;
import com.stars.tv.presenter.PreVideoItemPresenter;
import com.stars.tv.sample.PortraitVideoItemPresenter;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.SpaceItemDecoration;
import com.stars.tv.widget.media.AndroidMediaController;
import com.stars.tv.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.github.ybq.android.spinkit.style.Circle;

import static com.stars.tv.utils.Constants.EXT_VIDEO_ALBUM;
import static com.stars.tv.utils.Constants.EXT_VIDEO_COUNT;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYURL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;

public class VideoPreviewActivity extends BaseActivity {

    private ScrollView scrollView;
    private IjkVideoView mVideoView;
    private TableLayout mHudView;
    private FrameLayout pre_videoLayout;
    private String mVideoPath = "xx";
    private EpisodeListView mEpisodeListView;
    private TextView textLoading, title;
    private Circle mCircleDrawable;
    private IQiYiBaseBean videoBean;
    private String tvId, ctvid;
    private String albumId;
    private String score;
    private String description;
    private String videoCount;
    private String latestOrder;
    private int mVideoCount;
    private String mPlayUrl;
    private String mAlbumImageUrl;
    private int mVideoType;

    private List<IQiYiVideoBaseInfoBean.Director> director;
    private List<IQiYiVideoBaseInfoBean.Director> host;
    private List<IQiYiVideoBaseInfoBean.Director> guest;
    private String directorname = "";
    private List main_charactor;

    private String main_charactorname = "";
    private String hostname = "";
    private String guestname = "";
    private String name;

    final static int CHARACTORLIST = 1;
    final int REFRESH_InfoList = 0;
    final int REFRESH_RecommendList = 1;
    final int TURNTOFULLSCREEN = 2;
    final int REFRESH_VideoBaseInfo = 3;
    final int REFRESH_HistoryInfo = 4;
    private int mPosition = 0;
    private int mEpisode = 0;

    private List<IQiYiMovieBean> mEplisodeList = new ArrayList<>();
    private IQiYiVideoBaseInfoBean mVideoBase = new IQiYiVideoBaseInfoBean();
    private List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter rearrayObjectAdapter;
    private ItemBridgeAdapter reItemBridgeAdapter;

    private HorizontalGridView charactor_gv, recommend_gv;
    private int charactorposition;
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;

    private static final int ITEM_TOP_PADDING_PX = 15;
    private static final int ITEM_RIGHT_PADDING_PX = 25;
    private String htvId, hmVideoPath;
    private int hmPosition, hmEpisode;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_VideoBaseInfo:
                    getVideoInfo();
                case REFRESH_HistoryInfo:
                    checkHistoryInfo(albumId);
                case REFRESH_InfoList:
                    refreshInfoList();
                    if (null != mVideoBase.getPeople()) {
                        if (null != mVideoBase.getPeople().getMain_charactor() || null != mVideoBase.getPeople().getHost() || null != mVideoBase.getPeople().getGuest()) {
                            initCharactorGV();
                        }
                    } else {
                        charactor_gv.setVisibility(View.GONE);
                    }
                    initRecommendGV();
                    break;
                case REFRESH_RecommendList:
                    refreshRecommendGV();
                    break;
                case TURNTOFULLSCREEN:
                    turntoFullPlayback();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        initLoading();
        initView();
        videoBean = (IQiYiBaseBean) getIntent().getSerializableExtra("videoBean");
        Log.v("videoBean", videoBean.toString());
        if (null != videoBean.getId()) {
            parseIQiYiVideoBaseInfo(videoBean.getId());
        } else if (null != videoBean.getUrl()) {
            parseIQiYiVideoBaseInfoByURL(videoBean.getUrl());
        } else {
            Toast.makeText(this, "No Video Found! Press Back Button To Exit", Toast.LENGTH_LONG).show();
        }
        initVideoView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Intent intent = new Intent();
                    intent.putExtra("exit", true);
                    this.setResult(RESULT_OK, intent);
                    this.finish();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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

    public void initVideoView() {
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.videoView);
        mVideoView.setMediaController(null);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
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
                    parseIQiYiRealM3U8WithTvId(mEplisodeList.get(mEpisode).getTvId());
                }
            });
        }
        mVideoView.setOnErrorListener((iMediaPlayer, framework_err, impl_err) -> {
            showLoadingError(framework_err + "," + impl_err);
            return true;
        });

        pre_videoLayout = (FrameLayout) findViewById(R.id.pre_video);
        pre_videoLayout.setOnFocusChangeListener((view1, hasFocus) -> {
            if (pre_videoLayout != null) {
                pre_videoLayout.findViewById(R.id.videoView_board).setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
            }
        });
        pre_videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turntoFullPlayback();
            }
        });
    }

    private void startPlay() {
        if (mVideoPath == "") {
            showLoadingError("0");
        } else {
            //电视剧 2  动漫 4
            if (mVideoBase.getChannelId() == 2 || mVideoBase.getChannelId() == 4) {
                if (mEplisodeList.toString().equals("[]"))
                    title.setText(name);
                else {
                    title.setText(mEplisodeList.get(mEpisode).getName());
                }
            } else title.setText(name);
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.seekTo(mPosition);
            mVideoView.start();
        }
    }

    private void refreshInfoList() {
        //tvid
        if (tvId.equals("")) {
            tvId = ctvid;
        }
        parseIQiYiRealM3U8WithTvId(tvId);

        //clear value
        directorname = hostname = main_charactorname = guestname = "";

        //director / host
        if (null != mVideoBase.getPeople()) {
            if (null != mVideoBase.getPeople().getDirector()) {
                director = mVideoBase.getPeople().getDirector();
                for (int i = 0; i < director.size(); i++) {
                    directorname += director.get(i).getName() + " ";
                }
            } else if (null != mVideoBase.getPeople().getHost()) {
                host = mVideoBase.getPeople().getHost();
                for (int i = 0; i < host.size(); i++) {
                    if (!host.get(i).getName().isEmpty()) {
                        hostname += host.get(i).getName() + " ";
                    } else {
                        hostname = "";
                    }
                }
            }
            //main_charactor, main_charactorname / guest
            if (null != mVideoBase.getPeople().getMain_charactor()) {
                if (mVideoBase.getPeople().getMain_charactor().size() < 5) {
                    for (int i = 0; i < mVideoBase.getPeople().getMain_charactor().size(); i++) {
                        main_charactorname += mVideoBase.getPeople().getMain_charactor().get(i).getName() + " ";
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        main_charactorname += mVideoBase.getPeople().getMain_charactor().get(i).getName() + " ";
                    }
                }
                main_charactor = mVideoBase.getPeople().getMain_charactor();
            } else if (null != mVideoBase.getPeople().getGuest()) {
                if (mVideoBase.getPeople().getGuest().size() < 5) {
                    for (int i = 0; i < mVideoBase.getPeople().getGuest().size(); i++) {
                        guestname += mVideoBase.getPeople().getGuest().get(i).getName() + " ";
                    }
                } else {
                    for (int i = 0; i < 5; i++) {
                        guestname += mVideoBase.getPeople().getGuest().get(i).getName() + " ";
                    }
                }
                guest = mVideoBase.getPeople().getGuest();
            }
        }

        description = mVideoBase.getDescription();
        //description
        if (null != mVideoBase.getDescription()) {
            description = mVideoBase.getDescription();
        } else if (null != mVideoBase.getDescription()) {
            description = mVideoBase.getDescription();
        }

        shareInfoForFragment();

        initMediaInfo();
        if (Integer.valueOf(videoCount) > 1) {
            initEpisodeList();
        } else {
            mEpisodeListView.setVisibility(View.GONE);
        }
    }

    private void getVideoInfo() {
        //电影  1
        if (mVideoBase.getChannelId() == 1) {
            name = mVideoBase.getName();
        } else {
            if (mVideoBase.getAlbumName() != null && (!mVideoBase.getAlbumName().equals("")) && (!mVideoBase.getAlbumName().isEmpty())) {
                name = mVideoBase.getAlbumName();
            } else {
                name = mVideoBase.getName();
            }
        }
        score = mVideoBase.getScore();
        tvId = mVideoBase.getTvId();
        albumId = mVideoBase.getAlbumId();
        videoCount = mVideoBase.getVideoCount();
        latestOrder = mVideoBase.getLatestOrder();

        if (Integer.valueOf(videoCount) > 1) {
            parseIQiYiEpisodeList(albumId, Integer.valueOf(latestOrder), 1);
        } else {
            mHandler.sendEmptyMessage(REFRESH_HistoryInfo);
        }

//        checkHistoryInfo(albumId);

        // For Favorite Usage
        mVideoType = mVideoBase.getChannelId();
        mVideoCount = Integer.valueOf(mVideoBase.getVideoCount());
        mPlayUrl = mVideoBase.getPlayUrl();
        mAlbumImageUrl = mVideoBase.getAlbumImageUrl();
        // ------------------

    }

    private void shareInfoForFragment() {
        SharedPreferences videoinfoshare = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = videoinfoshare.edit();
        editor.putString("tvId", tvId);
        editor.putString("name", name);
        editor.putString("score", score);
        editor.putString("videoCount", videoCount);
        editor.putString("latestOrder", latestOrder);
        editor.putString("directorname", directorname);
        editor.putString("main_charactorname", main_charactorname);
        editor.putString("hostname", hostname);
        editor.putString("guestname", guestname);
        editor.putString("description", description);

        // For Favorite Usage
        editor.putInt(EXT_VIDEO_TYPE, mVideoType);
        editor.putString(EXT_VIDEO_ALBUM, albumId);
        editor.putString(EXT_VIDEO_PLAYURL, mPlayUrl);
        editor.putString(EXT_VIDEO_IMAGE_URL, mAlbumImageUrl);
        // ------------------
        editor.apply();
    }

    public void checkHistoryInfo(String albumId) {
        Intent intent = new Intent();
        intent.setClassName("com.stars.tv.activity", "VideoPreviewActivity");
        try {
            LeanCloudStorage.getIQiyHistoryListener(albumId, new LeanCloudStorage.VideoSeeker() {
                @Override
                public void succeed(ExtVideoBean bean) {
                    if (bean != null) {
                        htvId = bean.getVideoId();
                        hmVideoPath = bean.getVideoPlayUrl();
                        hmPosition = bean.getVideoPlayPosition();
                        hmEpisode = bean.getVideoCurrentViewOrder();
                        if (htvId != null && !htvId.isEmpty() && !htvId.equals("")) {
                            tvId = htvId;
                            mVideoPath = hmVideoPath;
                            mPosition = hmPosition;
                            mEpisode = hmEpisode;
                        }
                        Log.v("HistoryInfo", "tvId" + tvId + "hmVideoPath" + hmVideoPath + ";mPosition" + mPosition + ";mEpisode" + mEpisode);
                    } else {
                        mEpisode = 0;
                        mPosition = 0;
                    }
                    if (getPackageManager().resolveActivity(intent, 0) != null) {
                        mHandler.sendEmptyMessage(REFRESH_InfoList);
                    }
                }

                @Override
                public void failed() {
                    if (getPackageManager().resolveActivity(intent, 0) != null) {
                        mHandler.sendEmptyMessage(REFRESH_InfoList);
                    }
                }
            });
        } catch (Exception e) {
//            mHandler.sendEmptyMessage(REFRESH_InfoList);
        }
    }

    public void initMediaInfo() {
        Fragment mediaInfoListFragment = MediaInfoListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mediainfo, mediaInfoListFragment)
                .commit();

        ((MediaInfoListFragment) mediaInfoListFragment).setHandler(mHandler);
    }

    public void initEpisodeList() {
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

        final EpisodeListViewAdapter<String> adapter = new EpisodeListViewAdapter<String>() {
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

        adapter.setSelectedPositions(mEpisode);
        mEpisodeListView.setAdapter(adapter);
        mEpisodeListView.setChildrenItemClickListener(new ChildrenAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {
                showLoading();
                tvId = mEplisodeList.get(position).getTvId();
                mPosition = 0;
                parseIQiYiRealM3U8WithTvId(tvId);
                mEpisode = position;
                initEpisodeList();
            }
        });
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        mEpisodeListView = (EpisodeListView) findViewById(R.id.episodelistview);

        charactor_gv = (HorizontalGridView) findViewById(R.id.charactor_gv);
        charactor_gv.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING_PX);
        int right = ViewUtils.getPercentWidthSize(ITEM_RIGHT_PADDING_PX);
        charactor_gv.addItemDecoration(new SpaceItemDecoration(right, top));

        recommend_gv = (HorizontalGridView) findViewById(R.id.recommend_gv);
        recommend_gv.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        recommend_gv.addItemDecoration(new SpaceItemDecoration(right, top));
    }

    private void initCharactorGV() {
        ArrayObjectAdapter charrayObjectAdapter = new ArrayObjectAdapter(new PreVideoItemPresenter());
        ItemBridgeAdapter chitemBridgeAdapter = new ItemBridgeAdapter(charrayObjectAdapter);
        List<IQiYiMovieBean> chvideoBeanList = new ArrayList<>();

        if (null != mVideoBase.getPeople().getMain_charactor() && mVideoBase.getPeople().getMain_charactor().size() > 0) {
            for (int i = 0; i < mVideoBase.getPeople().getMain_charactor().size(); i++) {
                IQiYiMovieBean chvideoBean = new IQiYiMovieBean();
                chvideoBean.setImageUrl(mVideoBase.getPeople().getMain_charactor().get(i).getImage_url());
                chvideoBean.setName(mVideoBase.getPeople().getMain_charactor().get(i).getName());
                chvideoBean.setAlbumId(mVideoBase.getPeople().getMain_charactor().get(i).getId() + "");
                chvideoBeanList.add(chvideoBean);
            }
        } else if (null != host) {
            for (int i = 0; i < host.size(); i++) {
                IQiYiMovieBean chvideoBean = new IQiYiMovieBean();
                chvideoBean.setImageUrl(mVideoBase.getPeople().getHost().get(i).getImage_url());
                chvideoBean.setName(mVideoBase.getPeople().getHost().get(i).getName());
                chvideoBean.setAlbumId(mVideoBase.getPeople().getHost().get(i).getId() + "");
                chvideoBeanList.add(chvideoBean);
            }
        } else if (null != guest) {
            for (int i = 0; i < guest.size(); i++) {
                IQiYiMovieBean chvideoBean = new IQiYiMovieBean();
                chvideoBean.setImageUrl(mVideoBase.getPeople().getGuest().get(i).getImage_url());
                chvideoBean.setName(mVideoBase.getPeople().getGuest().get(i).getName());
                chvideoBean.setAlbumId(mVideoBase.getPeople().getGuest().get(i).getId() + "");
                chvideoBeanList.add(chvideoBean);
            }
        }

        charrayObjectAdapter.clear();
        charrayObjectAdapter.addAll(0, chvideoBeanList);
        charactor_gv.setAdapter(chitemBridgeAdapter);

        charactor_gv.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                    ViewUtils.scaleAnimator(view, hasFocus, 1.2f, 150);

                });
                charactorposition = position;
                parseIQiYiStarRecommendList(chvideoBeanList.get(charactorposition).getAlbumId(), "10", tvId, true);
                Log.v("charactorposition", charactorposition + "");
            }

        });

    }

    private void initRecommendGV() {
        rearrayObjectAdapter = new ArrayObjectAdapter(new PortraitVideoItemPresenter());
        reItemBridgeAdapter = new ItemBridgeAdapter(rearrayObjectAdapter);
        rearrayObjectAdapter.clear();
        rearrayObjectAdapter.addAll(0, mVideoList);
        recommend_gv.setAdapter(reItemBridgeAdapter);

        recommend_gv.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                    ViewUtils.scaleAnimator(view, hasFocus, 1.2f, 150);
                });
                child.itemView.setOnClickListener(view -> {
                    // TODO Item点击事件
//                    mVideoView.pause();
                    scrollView.scrollTo(0, 0);
                    showLoading();
                    if (null != mVideoList.get(position).getUrl()) {
                        parseIQiYiVideoBaseInfoByURL(mVideoList.get(position).getUrl());
                    } else {
                        showLoadingError("0");
                    }
                });
            }
        });
    }

    private void refreshRecommendGV() {
        rearrayObjectAdapter.clear();
        rearrayObjectAdapter.addAll(0, mVideoList);
        reItemBridgeAdapter.notifyDataSetChanged();
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
                for (IQiYiM3U8Bean bean : list) {
                    Log.v("VideoPreviewM3U8", bean.toString());
                }
                if (list.size() > 0 && (null != list.get(0).getM3u())) {
                    mVideoPath = list.get(0).getM3u();
                    startPlay();
                } else {
                    mVideoPath = "";
                    mVideoView.stopPlayback();
                    mVideoView.release(true);
                    showLoadingError("1");
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                mVideoPath = "";
                Log.v("VideoPreviewActivity", "获取失败parseIQiYiRealM3U8WithTvId");
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
                if (null != list) {
                    mEplisodeList.clear();
                    showLoading();
                    mEplisodeList.addAll(list);
                    ctvid = mEplisodeList.get(0).getTvId();
                    //TODO 获取电视剧剧集列表
                    for (IQiYiMovieBean bean : list) {
                        Log.v("VideoPreviewEpisodeList", bean.toString());
                    }
                } else showLoadingError("1");
                mHandler.sendEmptyMessage(REFRESH_HistoryInfo);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                Log.v("VideoPreviewActivity", "获取失败parseIQiYiEpisodeList");
                showLoadingError("0");
            }
        });
    }

    /**
     * 获取Video Basic info
     *
     * @param tvId tvId   1745487500
     */
    private void parseIQiYiVideoBaseInfo(String tvId) {
        IQiYiParseVideoBaseInfoPresenter ps = new IQiYiParseVideoBaseInfoPresenter();
        ps.requestIQiYiVideoBaseInfo(tvId, new CallBack<IQiYiVideoBaseInfoBean>() {
            @Override
            public void success(IQiYiVideoBaseInfoBean bean) {
                mVideoBase = bean;
                showLoading();
                Log.v("parseIQiYiVideoBaseInfo", bean.toString());
                mHandler.sendEmptyMessage(REFRESH_VideoBaseInfo);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                Log.v("VideoPreviewActivity", "获取失败parseIQiYiVideoBaseInfo");
                showLoadingError("0");
            }
        });
    }


    /**
     * 获取Video Basic info
     *
     * @param playUrl playUrl   xxxx
     */
    private void parseIQiYiVideoBaseInfoByURL(String playUrl) {
        IQiYiParseVideoBaseInfoPresenter ps = new IQiYiParseVideoBaseInfoPresenter();
        ps.requestIQiYiVideoBaseInfoWithUrl(playUrl, new CallBack<IQiYiVideoBaseInfoBean>() {
            @Override
            public void success(IQiYiVideoBaseInfoBean bean) {
                mVideoBase = bean;
                showLoading();
                Log.v("parseIQiYiVideoBaseInfo", bean.toString());
                mHandler.sendEmptyMessage(REFRESH_VideoBaseInfo);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                Log.v("VideoPreviewActivity", "获取失败parseIQiYiVideoBaseInfoByURL");
                showLoadingError("1");
            }
        });
    }

    /**
     * 获取明星推荐视频
     *
     * @param starId     明星id 205633105
     * @param size       需要推荐的个数  20
     * @param tvId       此处无意义，但是必须要有，可以填入当前视频的tvId   875112600
     * @param withCookie 此处根据需要填入，true/false
     */
    private void parseIQiYiStarRecommendList(String starId, String size, String tvId,
                                             boolean withCookie) {
        IQiYiParseStarRecommendPresenter ps = new IQiYiParseStarRecommendPresenter();
        ps.requestIQiYiStarRecommendList(starId, size, tvId, withCookie, new CallBack<List<IQiYiMovieBean>>() {
            @Override
            public void success(List<IQiYiMovieBean> list) {
                mVideoList.clear();
                showLoading();
                mVideoList.addAll(list);
                for (IQiYiMovieBean bean : list) {
                    Log.v("VideoPreviewActivity", bean.toString());
                }
                mHandler.sendEmptyMessage(REFRESH_RecommendList);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                Log.v("VideoPreviewActivity", "获取失败parseIQiYiStarRecommendList");
                showLoadingError("0");
            }
        });
    }

    private void initLoading() {
        if (NetUtil.isConnected()) {
            mCircleDrawable = new Circle();
            mCircleDrawable.setBounds(0, 0, 100, 100);
            mCircleDrawable.setColor(getResources().getColor(R.color.color_focus));
            textLoading = (TextView) findViewById(R.id.loading);
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
            textLoading.setVisibility(View.INVISIBLE);
            textLoading.setTextColor(getResources().getColor(R.color.color_all_white));
            if (errorCode.equals("0")) {
                textLoading.setText(getString(R.string.str_network_error));
            } else if (errorCode.equals("1")) {
                textLoading.setText(getString(R.string.str_needpay_error));
            } else {
                textLoading.setText(getString(R.string.str_code_error, errorCode));
            }
            textLoading.setVisibility(View.VISIBLE);
        }
    }

    public void turntoFullPlayback() {
        Intent intent = new Intent(getBaseContext(), FullPlaybackActivity.class);
        intent.putExtra("tvId", tvId);
        intent.putExtra("name", name);
        intent.putExtra("albumId", albumId);
        intent.putExtra("currentPosition", mVideoView.getCurrentPosition());
        if (Integer.valueOf(videoCount) > 1) {
            intent.putExtra("latestOrder", latestOrder);
            intent.putExtra("mEpisode", mEpisode);
        }
        // for history usage
        intent.putExtra(EXT_VIDEO_TYPE, mVideoBase.getChannelId());
        intent.putExtra(EXT_VIDEO_COUNT, mVideoCount);
        intent.putExtra(EXT_VIDEO_IMAGE_URL, mAlbumImageUrl);
        // -----------------

        mCircleDrawable.stop();
        mVideoView.stopPlayback();
        mVideoView.release(true);
        mVideoView.stopBackgroundPlay();
        IjkMediaPlayer.native_profileEnd();
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //得到新Activity 关闭后返回的数据
        mPosition = data.getIntExtra("currentPosition", 0);
        mEpisode = data.getIntExtra("currentEpisode", 0);
        mVideoPath = data.getStringExtra("currentPath");
        initEpisodeList();
        if (null != mVideoPath) {
            startPlay();
        }
    }
}