package com.stars.tv.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.R;
import com.stars.tv.adapter.ChildrenAdapter;
import com.stars.tv.adapter.EpisodeListView;
import com.stars.tv.adapter.EpisodeListViewAdapter;
import com.stars.tv.bean.IQiYiM3U8Bean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.fragment.MediaInfoListFragment;
import com.stars.tv.presenter.IQiYiParseEpisodeListPresenter;
import com.stars.tv.presenter.IQiYiParseM3U8Presenter;
import com.stars.tv.presenter.IQiYiParseStarRecommendPresenter;
import com.stars.tv.presenter.IQiYiParseVideoBaseInfoPresenter;
import com.stars.tv.presenter.PreVideoItemPresenter;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.SpaceItemDecoration;
import com.stars.tv.widget.media.AndroidMediaController;
import com.stars.tv.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import com.github.ybq.android.spinkit.style.Circle;

import static com.stars.tv.utils.Constants.VIDEO_TYPE_TVSERIES;

public class VideoPreviewActivity extends BaseActivity {

    private ScrollView scrollView;
    private IjkVideoView mVideoView;
    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
    private String mVideoPath = "xx";
    private EpisodeListView mEpisodeListView;
    private TextView textLoading;
    private Circle mCircleDrawable;
    private IQiYiMovieBean videoBean;
    private String tvId, ctvid;
    private String albumId;
    private String score;
    private String description;
    private String videoCount;
    private String latestOrder;
    private String playUrl;
    private String image_url;
    private String video_type;

  //    private List<IQiYiMovieBean.Role> director;
    private List<IQiYiVideoBaseInfoBean.Director> director;
    private List<IQiYiVideoBaseInfoBean.Director> host;
    private List<IQiYiVideoBaseInfoBean.Director> guest;
    private String directorname = "";
    //    private List<IQiYiMovieBean.Role> main_charactor;
    private List main_charactor;

    private String main_charactorname = "";
    //    private List<IQiYiMovieBean.Role> host;
    private String hostname = "";
    private String guestname = "";
    private String name;

    final static int CHARACTORLIST = 1;
    final int REFRESH_InfoList = 0;
    final int REFRESH_RecommendList = 1;
    private Integer[] selectedPositions = {0};

    private List<IQiYiMovieBean> mEplisodeList = new ArrayList<>();
    private IQiYiVideoBaseInfoBean mVideoBase = new IQiYiVideoBaseInfoBean();
    private List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter rearrayObjectAdapter;
    private ItemBridgeAdapter reItemBridgeAdapter;

    private HorizontalGridView charactor_gv,recommend_gv;
    private int charactorposition;
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;

    private static final int ITEM_TOP_PADDING_PX = 15;
    private static final int ITEM_RIGHT_PADDING_PX = 25;

//    public void setHandler(Handler handler) {
//        this.mHandler = handler;
//        Log.v("tttonmHandlerinput1", mHandler.toString());
//    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v("ttt msg", "" + msg.what);
            switch (msg.what) {
                case REFRESH_InfoList:
                    refreshInfoList();
                    initCharactorGV();
                    initRecommendGV();
                    break;
                case REFRESH_RecommendList:
                    refreshRecommendGV();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        loading(2);
        initGV();
        videoBean = (IQiYiMovieBean) getIntent().getSerializableExtra("videoBean");
        initVideoView();
        getVideoInfo();

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
        IjkMediaPlayer.native_profileEnd();
    }

    public void initVideoView() {
        Log.v("vvvVideoPreview1", mVideoPath);
        // init UI
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.clearFocus();
        mMediaController.hide();
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) findViewById(R.id.videoView);
        mVideoView.setMediaController(mMediaController);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mVideoView.setHudView(mHudView);

    }

    private void startPlay(){
        if (mVideoPath == "") {
            Toast.makeText(this, "No Video Found! Press Back Button To Exit", Toast.LENGTH_LONG).show();
        } else {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.start();
        }
    }

    private void refreshInfoList() {
        //tvid
        if (null != tvId) {
            Log.v("vvvtvId", tvId);
            parseIQiYiRealM3U8WithTvId(tvId);

        } else {
            tvId = ctvid;
            Log.v("vvvtestctvid", ctvid);
            parseIQiYiRealM3U8WithTvId(ctvid);
        }

        //clear value
        directorname=hostname=main_charactorname=guestname="";

        //director / host
        if (null != mVideoBase.getPeople().getDirector()) {
            director = mVideoBase.getPeople().getDirector();
            for (int i = 0; i < director.size(); i++) {
                directorname += director.get(i).getName() + " ";
            }
        } else if (null != mVideoBase.getPeople().getHost()) {
            host = mVideoBase.getPeople().getHost();
            for (int i = 0; i < host.size(); i++) {
                hostname += host.get(i).getName() + " ";
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

        description = videoBean.getDescription();
        //description
        if (null != videoBean.getDescription()) {
            description = videoBean.getDescription();
        } else if (null != mVideoBase.getDescription()) {
            description = mVideoBase.getDescription();
        }

        shareInfoForFragment();
//        initMediaInfo();
//        initEpisodeList();
        if (mVideoPath != "") {
            initMediaInfo();
            initEpisodeList();

        } else {
            Log.v("VideoPreviewActivity", "获取失败");
            textLoading.setText("加载失败，请确认网络！");
            textLoading.setBackgroundColor(Color.BLACK);
            textLoading.setTextColor(Color.WHITE);
        }
    }

    public void initMediaInfo() {
        Fragment newFragment = MediaInfoListFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.mediainfo, newFragment);
        transaction.commit();
    }

    public void initEpisodeList() {
        mEpisodeListView = (EpisodeListView) findViewById(R.id.episodelistview);

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

        adapter.setSelectedPositions(Arrays.asList(selectedPositions));
        mEpisodeListView.setAdapter(adapter);
        mEpisodeListView.setChildrenItemClickListener(new ChildrenAdapter.OnItemClickListener() {
            @Override
            public void onEpisodesItemClick(View view, int position) {
                tvId = mEplisodeList.get(position).getTvId();
                parseIQiYiRealM3U8WithTvId(tvId);
//                selectedPositions =  Integer.valueOf());
                adapter.setSelectedPositions(Arrays.asList(selectedPositions));
                LeanCloudStorage.updateIQiyHistory(videoBean,
                  mEplisodeList.get(position), position + 1, new SaveCallback() {
                @Override
                public void done(AVException e) {
                }
              });
              Log.v("Clicktest", mEplisodeList.get(position).getTvId());
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEpisodeListView.requestFocus();
            }
        }, 300);
    }

    private void getVideoInfo() {
        name = videoBean.getName();
        score = videoBean.getScore();
        tvId = videoBean.getTvId();
        albumId = videoBean.getAlbumId();
        videoCount = videoBean.getVideoCount();
        latestOrder = videoBean.getLatestOrder();

      /* for history/favorite usage */
      albumId = videoBean.getAlbumId();
      playUrl = videoBean.getPlayUrl();
      image_url = videoBean.getImageUrl();
      video_type = VIDEO_TYPE_TVSERIES;
      /* -------------------------- */

      parseIQiYiEpisodeList(albumId, Integer.valueOf(latestOrder), 1);
        parseIQiYiVideoBaseInfo(albumId);
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

        /* for History/Favorite usage */
        editor.putString("albumId", albumId);
        editor.putString("playurl", playUrl);
        editor.putString("imageurl", image_url);
        editor.putString("videotype", video_type);
        /* -------------------------- */

      editor.commit();
    }

    private void initGV(){
        charactor_gv=(HorizontalGridView)findViewById(R.id.charactor_gv);
        charactor_gv.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING_PX);
        int right = ViewUtils.getPercentWidthSize(ITEM_RIGHT_PADDING_PX);
        charactor_gv.addItemDecoration(new SpaceItemDecoration(right, top));

        recommend_gv=(HorizontalGridView)findViewById(R.id.recommend_gv);
        recommend_gv.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        recommend_gv.addItemDecoration(new SpaceItemDecoration(right, top));
    }

    private void initCharactorGV(){

        ArrayObjectAdapter charrayObjectAdapter = new ArrayObjectAdapter(new PreVideoItemPresenter());
        ItemBridgeAdapter chitemBridgeAdapter = new ItemBridgeAdapter(charrayObjectAdapter);
        List<IQiYiMovieBean> chvideoBeanList = new ArrayList<>();

        for (int i = 0; i < main_charactor.size(); i++) {
            IQiYiMovieBean chvideoBean= new IQiYiMovieBean();
            chvideoBean.setImageUrl(mVideoBase.getPeople().getMain_charactor().get(i).getImage_url());
            chvideoBean.setName(mVideoBase.getPeople().getMain_charactor().get(i).getName());
            chvideoBean.setAlbumId(mVideoBase.getPeople().getMain_charactor().get(i).getId()+"");
            chvideoBeanList.add(chvideoBean);
        }
        charrayObjectAdapter.clear();
        charrayObjectAdapter.addAll(0,chvideoBeanList);
        charactor_gv.setAdapter(chitemBridgeAdapter);

        charactor_gv.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                    ViewUtils.scaleAnimator(view, hasFocus,1.2f,150);

                });
                charactorposition=position;
                parseIQiYiStarRecommendList(chvideoBeanList.get(charactorposition).getAlbumId(), "10", tvId, true);
                Log.v("charactorposition",charactorposition+"");
            }

        });

    }
    private void initRecommendGV(){
        rearrayObjectAdapter = new ArrayObjectAdapter(new PreVideoItemPresenter());
        reItemBridgeAdapter = new ItemBridgeAdapter(rearrayObjectAdapter);
        rearrayObjectAdapter.clear();
        rearrayObjectAdapter.addAll(0,mVideoList);
        recommend_gv.setAdapter(reItemBridgeAdapter);

        recommend_gv.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                    ViewUtils.scaleAnimator(view, hasFocus,1.2f,150);
                });
                child.itemView.setOnClickListener(view -> {
                    // TODO Item点击事件
//                    mVideoView.pause();
                    scrollView.scrollTo(0,0);
                    loading(2);
                    videoBean = mVideoList.get(position);
                    Log.v("vvvRevideoBean",videoBean.toString());
                    getVideoInfo();
                });
            }

        });
    }
    private void refreshRecommendGV(){
        rearrayObjectAdapter.clear();
        rearrayObjectAdapter.addAll(0,mVideoList);
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
                mVideoPath = list.get(0).getM3u();
                Log.v("vvvVideoPreview3",mVideoPath);
                for (IQiYiM3U8Bean bean : list) {
                    Log.v("vvvVideoPreviewM3U8", bean.toString());
                }
//                initVideoView();       //Marked for Test
                startPlay();
                loading(1);

            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                mVideoPath = "";
                Log.v("VideoPreviewActivity", "获取失败");
                textLoading.setText("加载失败，请确认网络！");
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
                ctvid = mEplisodeList.get(0).getTvId();
                Log.v("vvvctvid", ctvid);
                //TODO 获取电视剧剧集列表
                for (IQiYiMovieBean bean : list) {
                    Log.v("VideoPreviewEpisodeList", bean.toString());

                }
                mHandler.sendEmptyMessage(REFRESH_InfoList);
//                if(null==tvId){
//                    parseIQiYiRealM3U8WithTvId(ctvid);
//                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
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
                Log.v("parseIQiYiVideoBaseInfo", bean.toString());
//                mHandler.sendEmptyMessage(REFRESH_InfoList);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
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
    private void parseIQiYiStarRecommendList(String starId, String size, String tvId, boolean withCookie) {
        IQiYiParseStarRecommendPresenter ps = new IQiYiParseStarRecommendPresenter();
        ps.requestIQiYiStarRecommendList(starId, size, tvId, withCookie, new CallBack<List<IQiYiMovieBean>>() {
            @Override
            public void success(List<IQiYiMovieBean> list) {
                mVideoList.clear();
                mVideoList.addAll(list);
                for (IQiYiMovieBean bean : list) {
                    Log.v("PreVideoRowFragment", bean.toString());
                }
                mHandler.sendEmptyMessage(REFRESH_RecommendList);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });

    }

    private void loading(Integer visibility) {
        textLoading = (TextView) findViewById(R.id.loading);
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        textLoading.setCompoundDrawables(null, null, mCircleDrawable, null);
        textLoading.setVisibility(visibility);
    }


}
