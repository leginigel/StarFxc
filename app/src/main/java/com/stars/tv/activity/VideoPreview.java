package com.stars.tv.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stars.tv.R;
import com.stars.tv.adapter.ChildrenAdapter;
import com.stars.tv.adapter.EpisodeListView;
import com.stars.tv.adapter.EpisodeListViewAdapter;
import com.stars.tv.bean.IQiYiM3U8Bean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.fragment.MediaInfoListFragment;
//import com.stars.tv.fragment.PreVideoRecommendationListFragment;
import com.stars.tv.fragment.PreVideoRowFragment;
import com.stars.tv.presenter.IQiYiParseEpisodeListPresenter;
import com.stars.tv.presenter.IQiYiParseM3U8Presenter;
import com.stars.tv.utils.CallBack;
import com.stars.tv.widget.media.AndroidMediaController;
import com.stars.tv.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.github.ybq.android.spinkit.style.Circle;

public class VideoPreview extends BaseActivity {

  private IjkVideoView mVideoView;
  private AndroidMediaController mMediaController;
  private TableLayout mHudView;
  private String mVideoPath = "xx";
  private EpisodeListView mEpisodeListView;
//    private Handler mHandler = new Handler(Looper.getMainLooper());

  private TextView textLoading;
  private ViewPager bottompage;
  private Circle mCircleDrawable;
  private IQiYiMovieBean videoBean;
  private String tvId;
  private String vid;
  private String albumId;
  private String playUrl;
  private String score;
  private String duration;
  private String description;
  private String videoCount;
  private String latestOrder;
  private List<IQiYiMovieBean.Role> director;
  private String directorname = "";
  private String secondInfo;
  private List<IQiYiMovieBean.Role> main_charactor;
  private String main_charactorname = "";
  private List<IQiYiMovieBean.Role> host;
  private String hostname = "";
  private String image_url;
  private String name;
  private long id;

  private ArrayList charactorlist;

  final static int CHARACTORLIST = 1;
  private Integer[] selectedPositions = {0};

  List<IQiYiMovieBean> mVideoList = new ArrayList<>();

  FragAdapter mFragAdapter;
  List<Fragment> mFragmentList = new ArrayList<>();

  public void setHandler(Handler handler) {
    this.mHandler = handler;
    Log.v("tttonmHandlerinput1", mHandler.toString());
  }


  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_preview);

    loading(2);
    getVideoInfo();
    initBottomRow();

    if (mVideoPath != "") {
      initMediaInfo();
      initEpisodeList();

    } else {
      Log.v("VideoPreview", "获取失败");
      textLoading.setText("加载失败，请确认网络！");
      textLoading.setBackgroundColor(Color.BLACK);
      textLoading.setTextColor(Color.WHITE);
    }

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
    //Log.v("VideoPreview1", mVideoPath);
    //Log.d("Test","Jack66, initVideoVidePath="+mVideoPath);
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

    if (mVideoPath == "") {
      Toast.makeText(this, "No Video Found! Press Back Button To Exit", Toast.LENGTH_LONG).show();
    } else {
//            mVideoView.setVideoURI(Uri.parse(mVideoPath));
      mVideoView.setVideoURI(Uri.parse(mVideoPath));
      mVideoView.start();
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
    //Log.v("VideoPreviewgroups", stringArrayList.toString());

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
        tvId = mVideoList.get(position).getTvId();
        parseIQiYiRealM3U8WithTvId(tvId);
//                selectedPositions =  Integer.valueOf());
        adapter.setSelectedPositions(Arrays.asList(selectedPositions));
        //Log.v("Clicktest", mVideoList.get(position).getTvId());
        //Log.d("Test", "Jack66, itemclick.TVID="+mVideoList.get(position).toString());
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
    videoBean = (IQiYiMovieBean) getIntent().getSerializableExtra("videoBean");
    //Log.v("videoBean", videoBean.toString());
    //Log.d("Test", "Jack66, videoBean.contents="+videoBean.toString());
    name = videoBean.getName();
    score = videoBean.getScore();
    tvId = videoBean.getTvId();
    albumId = videoBean.getAlbumId();
    videoCount = videoBean.getVideoCount();
    latestOrder = videoBean.getLatestOrder();

    director = videoBean.getCast().getDirector();
    if (null != director) {
      for (int i = 0; i < director.size(); i++) {
        directorname += director.get(i).getName() + " ";
      }
    }
    secondInfo = videoBean.getSecondInfo().substring(3);
    main_charactor = videoBean.getCast().getMain_charactor();

    Log.v("secondInfo", secondInfo);
    host = videoBean.getCast().getHost();
//        for(int i=0; i<host.size();i++){
//            hostname+=host.get(i).getName()+" ";
//        }

    description = videoBean.getDescription();

    SharedPreferences videoinfoshare = getSharedPreferences("data", MODE_PRIVATE);
    SharedPreferences.Editor editor = videoinfoshare.edit();
    editor.putString("tvId", tvId);
    editor.putString("name", name);
    editor.putString("score", score);
    editor.putString("videoCount", videoCount);
    editor.putString("latestOrder", latestOrder);
    editor.putString("directorname", directorname);
    editor.putString("secondInfo", secondInfo);
    editor.putString("main_charactorname", main_charactorname);
    editor.putString("hostname", hostname);
    editor.putString("description", description);
    editor.commit();

    //Log.d("Test", "Jack66, editor.contents="+editor.toString());

    parseIQiYiRealM3U8WithTvId(tvId);
    parseIQiYiEpisodeList(albumId, Integer.valueOf(latestOrder), 1);
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
 //                Log.v("VideoPreview3",mVideoPath);
        for (IQiYiM3U8Bean bean : list) {
          //Log.v("VideoPreviewM3U8", bean.toString());
          //Log.d("Test", "Jack66, VideoPreviewM3U8="+bean.toString());
        }
        initVideoView();       //Marked for Test
        loading(1);
      }

      @Override
      public void error(String msg) {
        //TODO 获取失败
        mVideoPath = "";
        Log.v("VideoPreview", "获取失败");
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
        mVideoList.addAll(list);
        //TODO 获取电视剧剧集列表
        for (IQiYiMovieBean bean : list) {
          //Log.v("VideoPreviewEpisodeList",bean.toString());
          //Log.d("Test","Jack66, EpisodeList="+bean.toString());
        }
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

  private void initBottomRow() {
    bottompage = (ViewPager) findViewById(R.id.bottompage);

    mFragmentList.clear();
    mFragmentList.add(PreVideoRowFragment.newInstance(main_charactor.toString()));
    mFragAdapter = new FragAdapter(getSupportFragmentManager());
//        bottompage.setOffscreenPageLimit(1); // 缓存2个页面
    bottompage.setAdapter(mFragAdapter);
    bottompage.setCurrentItem(0);
//        mFragAdapter.notifyDataSetChanged();


  }

  public class FragAdapter extends FragmentPagerAdapter {

    FragAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//            Log.v("ttt","mFragmentList.get(position)"+mFragmentList.get(position));
      return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
      return null != mFragmentList ? mFragmentList.size() : 0;
    }

  }

}
