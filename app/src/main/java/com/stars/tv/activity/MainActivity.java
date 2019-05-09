package com.stars.tv.activity;

import com.stars.tv.R;
import com.stars.tv.bean.IQiYiBannerInfoBean;
import com.stars.tv.bean.IQiYiBasicStarInfoBean;
import com.stars.tv.bean.IQiYiEpisodeBean;
import com.stars.tv.bean.IQiYiHotSearchItemBean;
import com.stars.tv.bean.IQiYiM3U8Bean;
import com.stars.tv.bean.IQiYiRecommendVideoBean;
import com.stars.tv.bean.IQiYiStarInfoBean;
import com.stars.tv.bean.IQiYiTopListBean;
import com.stars.tv.bean.IQiYiVarietyBean;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.bean.TvTitle;
import com.stars.tv.fragment.VideoRowSampleFragment;
import com.stars.tv.model.TvTitleModel;
import com.stars.tv.presenter.IQiYiParseBannerInfoPresenter;
import com.stars.tv.presenter.IQiYiParseBasicStarInfoPresenter;
import com.stars.tv.presenter.IQiYiParseHotSearchListPresenter;
import com.stars.tv.presenter.IQiYiParseStarRecommendPresenter;
import com.stars.tv.presenter.IQiYiParseStarInfoPresenter;
import com.stars.tv.presenter.IQiYiParseEpisodeListPresenter;
import com.stars.tv.presenter.IQiYiParseM3U8Presenter;
import com.stars.tv.presenter.IQiYiParseTopListPresenter;
import com.stars.tv.presenter.IQiYiParseVarietyAlbumListPresenter;
import com.stars.tv.presenter.IQiYiParseVideoBaseInfoPresenter;
import com.stars.tv.presenter.TvTitlePresenter;
import com.stars.tv.fragment.VideoVGridSampleMVPFragment;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.SpaceItemDecoration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BaseGridView;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private static final int TITLE_PADDING_LEFT_PX = 60;
    private static final int TITLE_TOP_PADDING_PC = 0;
    private static final int TITLE_RIGHT_PADDING_PC = 20;

    private String TAG = "MainActivity";

    @BindView(R.id.hg_title) HorizontalGridView hgTitle;
    @BindView(R.id.page_vp) ViewPager pageVp;
    @BindView(R.id.search_btn) Button searchBtn;

    FragAdapter mFragAdapter;
//    List<VideoRowSampleFragment> mFragmentList = new ArrayList<>();
    List<Fragment> mFragmentList = new ArrayList<>();

    Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
//        parseIQiYiRealM3U8("http://www.iqiyi.com/v_19rsfhgzfw.html");
//        parseIQiYiRealM3U8("1745487500","785c82b2b8949a49c6be38876fede723");
//        parseIQiYiRealM3U8WithTvId("1745487500");
//        parseIQiYiEpisodeList("202861101",50,1);
//        parseIQiYiStarsInfo("http://www.iqiyi.com/lib/s_213640105.html");
//        parseIQiYiHotSearchList("index/dianying/dongzuo");
//        parseIQiYiVarietyAlbumList("233071001","2019");
//        parseIQiYiStarRecommendList("205633105","20","875112600",true);
//        parseIQiYiVideoBaseInfo("1745487500");
//        parseIQiYiTopList("1","realTime",50,1);
//        parseIQiYiBasicStarInfo("213640105","1,2,6",3);
//        parseIQiYiBasicStarInfo("dianying");
        initTitle();
        initContentViews();
        refreshRequest();
    }

    private void initTitle() {
        hgTitle.setPadding(TITLE_PADDING_LEFT_PX, 0, 0, 0);
        int top = ViewUtils.getPercentHeightSize(TITLE_TOP_PADDING_PC);
        int right = ViewUtils.getPercentWidthSize(TITLE_RIGHT_PADDING_PC);
        hgTitle.addItemDecoration(new SpaceItemDecoration(right, top));
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new TvTitlePresenter());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(arrayObjectAdapter);
        hgTitle.setAdapter(itemBridgeAdapter);
        arrayObjectAdapter.addAll(0, TvTitleModel.getTitleList());

        hgTitle.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    // 避免焦点跑到搜索框去.
                    searchBtn.setFocusableInTouchMode(true);
                    searchBtn.setFocusable(true);
                    searchBtn.requestFocusFromTouch();
                }
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    // hide search button
                    searchBtn.setVisibility(View.GONE);
                }
                return false;
            }
        });


        hgTitle.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                    // show search button
                    if(hasFocus) {
                        searchBtn.setVisibility(View.VISIBLE);
                    }
                    ViewUtils.scaleAnimator(view, hasFocus,1.2f,150);
                    TextView tv = view.findViewById(R.id.tv_title);
                    View lineView = view.findViewById(R.id.title_under_line);
                    lineView.setBackgroundColor(getResources().getColor(hasFocus ? R.color.color_focus : R.color.color_transparent));
                    tv.setTextColor(getResources().getColor(hasFocus ? R.color.color_focus : R.color.color_all_white));
                    if ((int) view.getTag() == pageVp.getCurrentItem())
                    {
                        ViewUtils.scaleAnimator(view, true,1.2f,150);
                    }
                });
                pageVp.setCurrentItem(position);
            }
        });
    }

    private void initContentViews() {
        mFragAdapter = new FragAdapter(getSupportFragmentManager());
        searchBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchBtn.setBackgroundResource(b ? R.drawable.ic_search_btn_focus : R.drawable.ic_search_btn_normal);
            }
        });
        searchBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN ) {
                    if ( keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        searchBtn.setFocusable(false);
                        hgTitle.requestFocusFromTouch();
                        return true;
                    }
                    else if ( keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP ){
                      Intent intent = new Intent(MainActivity.this, DragMainActivity.class);
                      startActivity(intent);
                      return true;
                    }
                }
                return false;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        pageVp.setOffscreenPageLimit(1); // 缓存2个页面
        pageVp.setAdapter(mFragAdapter);
    }

    /**
     * 获取视频真是播放M3U8
     * @param url 播放url，不支持电视剧的列表简介页面 test url "http://www.iqiyi.com/v_19rsfhgzfw.html"
     */
    private void parseIQiYiRealM3U8(String url){
        IQiYiParseM3U8Presenter ps = new IQiYiParseM3U8Presenter();
        ps.requestIQiYiRealPlayUrl(url, new CallBack<List<IQiYiM3U8Bean>>() {
            @Override
            public void success(List<IQiYiM3U8Bean> list) {
                //TODO 获取成功在此得到真实播放地址的List，可能会有HD,SD,1080P
                for(IQiYiM3U8Bean bean:list)
                {
                    Log.v(TAG,bean.toString());
                }

            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取视频真是播放M3U8 （知否第1集）
     * @param tvId  视频tvId  1745487500
     */
    private void parseIQiYiRealM3U8WithTvId(String tvId){
        IQiYiParseM3U8Presenter ps = new IQiYiParseM3U8Presenter();
        ps.requestIQiYiRealPlayUrlWithTvId(tvId, new CallBack<List<IQiYiM3U8Bean>>() {
            @Override
            public void success(List<IQiYiM3U8Bean> list) {
                //TODO 获取成功在此得到真实播放地址的List，可能会有HD,SD,1080P
                for(IQiYiM3U8Bean bean:list)
                {
                    Log.v(TAG,bean.toString());
                }

            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取视频真是播放M3U8 （知否第1集）
     * @param tvId  视频tvId  1745487500
     * @param vid  视频vid 785c82b2b8949a49c6be38876fede723
     */
    private void parseIQiYiRealM3U8(String tvId,String vid){
        IQiYiParseM3U8Presenter ps = new IQiYiParseM3U8Presenter();
        ps.requestIQiYiRealPlayUrl(tvId,vid, new CallBack<List<IQiYiM3U8Bean>>() {
            @Override
            public void success(List<IQiYiM3U8Bean> list) {
                //TODO 获取成功在此得到真实播放地址的List，可能会有HD,SD,1080P
                for(IQiYiM3U8Bean bean:list)
                {
                    Log.v(TAG,bean.toString());
                }
            }
            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     *  获取电视剧剧集列表
     * @param albumId 专辑id， 216266201 //知否知否
     * @param size  获取集数
     * @param pageNum 页码
     */
    private void parseIQiYiEpisodeList(String albumId, int size, int pageNum){
        IQiYiParseEpisodeListPresenter ps = new IQiYiParseEpisodeListPresenter();
        ps.requestIQiYiEpisodeList(albumId,size,pageNum, new CallBack<List<IQiYiEpisodeBean>>() {
            @Override
            public void success(List<IQiYiEpisodeBean> list) {
                //TODO 获取电视剧剧集列表
                for(IQiYiEpisodeBean bean:list)
                {
                    Log.v(TAG,bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     *  获取综艺剧集列表
     * @param albumId 专辑id， 233071001 //王牌对王牌第四季
     * @param timeList 时间list 例如2019/ 201904/ 201903/
     */
    private void parseIQiYiVarietyAlbumList(String albumId, String timeList){
        IQiYiParseVarietyAlbumListPresenter ps = new IQiYiParseVarietyAlbumListPresenter();
        ps.requestIQiYiVarietyAlbumList(albumId,timeList, new CallBack<List<IQiYiVarietyBean>>() {
            @Override
            public void success(List<IQiYiVarietyBean> list) {
                //TODO 获取综艺剧集列表
                for(IQiYiVarietyBean bean:list)
                {
                    Log.v(TAG,bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });

    }

    /**
     *
     * @param url 明星url， test url "http://www.iqiyi.com/lib/s_213640105.html" 沈腾
     */
    private void parseIQiYiStarsInfo(String url){
        IQiYiParseStarInfoPresenter ps = new IQiYiParseStarInfoPresenter();
        ps.requestIQiYiStarsInfo(url, new CallBack<IQiYiStarInfoBean>() {
            @Override
            public void success(IQiYiStarInfoBean list) {
                //TODO 获取明星信息
                    Log.v(TAG,list.toString());
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     *
     * @param url 热搜榜， test url "http://v.iqiyi.com/index/dianying/dongzuo/index.html
     */
    private void parseIQiYiHotSearchList(String url){
        IQiYiParseHotSearchListPresenter ps = new IQiYiParseHotSearchListPresenter();
        ps.requestIQiYiHotList(url, new CallBack<List<IQiYiHotSearchItemBean>>() {
            @Override
            public void success(List<IQiYiHotSearchItemBean> list) {
                //TODO 获取榜单
                for(IQiYiHotSearchItemBean bean:list) {
                    Log.v(TAG, bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取明星推荐视频
     * @param starId  明星id 205633105
     * @param size 需要推荐的个数  20
     * @param tvId 此处无意义，但是必须要有，可以填入当前视频的tvId   875112600
     * @param withCookie 此处根据需要填入，true/false
     */
    private void parseIQiYiStarRecommendList(String starId, String size,String tvId, boolean withCookie){
        IQiYiParseStarRecommendPresenter ps = new IQiYiParseStarRecommendPresenter();
        ps.requestIQiYiStarRecommendList(starId, size, tvId, withCookie, new CallBack<List<IQiYiRecommendVideoBean>>() {
            @Override
            public void success(List<IQiYiRecommendVideoBean> list) {
                for(IQiYiRecommendVideoBean bean:list) {
                    Log.v(TAG, bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }


    /**
     * 获取Video Basic info
     * @param tvId tvId   1745487500
     */
    private void parseIQiYiVideoBaseInfo(String tvId){
        IQiYiParseVideoBaseInfoPresenter ps = new IQiYiParseVideoBaseInfoPresenter();
        ps.requestIQiYiVideoBaseInfo( tvId, new CallBack<IQiYiVideoBaseInfoBean>() {
            @Override
            public void success(IQiYiVideoBaseInfoBean bean) {
                    Log.v(TAG, bean.toString());

            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取Top list
     * @param cid channel id , 热播榜为-1，其余根据list定义， 电视剧为2，电影为1
     * @param type 播放指数榜：playindex   飙升榜：rise  热度榜：realTime
     * @param size 获取个数
     * @param page 获取页面page number
     */
    private void parseIQiYiTopList(String cid, String type,int size, int page){
        IQiYiParseTopListPresenter ps = new IQiYiParseTopListPresenter();
        ps.requestIQiYiTopList( cid,type,size,page, new CallBack<List<IQiYiTopListBean>>() {
            @Override
            public void success(List<IQiYiTopListBean> list) {
                for(IQiYiTopListBean bean:list) {
                    Log.v(TAG, bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取明星基本信息
     * @param starId 明星id , 沈腾 213640105
     * @param channleIds channel id list  1,2,6 电影/电视剧/综艺
     * @param size 获取作品最大个数
     */

    private void parseIQiYiBasicStarInfo(String starId, String channleIds, int size){
        IQiYiParseBasicStarInfoPresenter ps = new IQiYiParseBasicStarInfoPresenter();
        ps.requestIQiYiStarsInfo( starId,channleIds,size, new CallBack<IQiYiBasicStarInfoBean>() {
            @Override
            public void success(IQiYiBasicStarInfoBean bean) {
                    Log.v(TAG, bean.toString());
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取推荐栏位基本信息
     * @param channel  电视剧：dianshiju    电影：dianying  综艺：zongyi   动漫：dongman
     */

    private void parseIQiYiBasicStarInfo(String channel){
        IQiYiParseBannerInfoPresenter ps = new IQiYiParseBannerInfoPresenter();
        ps.requestIQiYiBannerInfo( channel, new CallBack<List<IQiYiBannerInfoBean>>() {
            @Override
            public void success(List<IQiYiBannerInfoBean> list) {
                for(IQiYiBannerInfoBean bean:list) {
                    Log.v(TAG, bean.toString());
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    private void refreshRequest() {
            mFragmentList.clear();
            AtomicInteger i= new AtomicInteger();
            for (TvTitle titleMode : TvTitleModel.getTitleList()) {
                if(i.get() %2==0)
                {
                    mFragmentList.add(VideoRowSampleFragment.getInstance(titleMode.getName()));
                }else
                {
                    mFragmentList.add(VideoVGridSampleMVPFragment.getInstance(titleMode.getName()));
                }
                i.getAndIncrement();
            }
            mFragAdapter.notifyDataSetChanged();
        //TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    /**
     * 内容 framgnt Adapter.
     */
    public class FragAdapter extends FragmentPagerAdapter {

        FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return null != mFragmentList ? mFragmentList.size() : 0;
        }

    }

}

