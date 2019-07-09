package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.Circle;
import com.stars.tv.R;
import com.stars.tv.activity.TotalMediaListActivity;
import com.stars.tv.activity.VideoPreviewActivity;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.contract.IQiYiMovieContract;
import com.stars.tv.presenter.IQiYiMoviePresenter;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.MyVerticalGridView;
import com.stars.tv.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class MediaListMVPFragment
        extends IQiYiBaseFragment<IQiYiMovieContract.IQiYiMoviePresenter>
        implements IQiYiMovieContract.IQiYiMovieView {

    final int REFRESH_MOVIE_CONTENT = 100;

    private static final int ITEM_NUM_ROW = 6; // 一行多少个row item.
    private static final int GRID_VIEW_LEFT_PX = 80;   //80->60
    private static final int GRID_VIEW_RIGHT_PX = 50;    //50->40
    private static final int GRID_VIEW_TOP_PX = 30;     //30->20
    private static final int GRID_VIEW_BOTTOM_PX = 50;   //50->40

    private static final int ITEM_TOP_PADDING_PX = 25;       //15->25
    private static final int ITEM_RIGHT_PADDING_PX = 25;

    private Circle mCircleDrawable;

    List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    String mTvTitle;
    int postition = 0;
    private int mTotal = 0;

    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;
    @BindView(R.id.typename_txt)
    TextView typename_txt;
    @BindView(R.id.typecount_txt)
    TextView typecount_txt;
    @BindView(R.id.loading_txt)
    TextView loading_txt;

    MediaListMVPFragment.VideoSampleAdapter mVideoSampleAdapter;


    int mItemWidth = 0;
    int mItemHeight = 0;
    int mPageNum = 1;

    public static MediaListMVPFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static MediaListMVPFragment newInstance(String titleName) {
        MediaListMVPFragment myFragment = new MediaListMVPFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_MOVIE_CONTENT:
                    typename_txt.setVisibility(View.VISIBLE);
                    typecount_txt.setVisibility(View.VISIBLE);
                    mVideoSampleAdapter.notifyDataSetChanged();
                    if (mVideoList.size() > 900) {
                        videoGrid.endRefreshingWithNoMoreData();
                    }
                    break;
                default:
                    postition = msg.what;
                    mVideoList.clear();
                    refreshRequest(postition);
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    private void refreshRequest(int postition) {
        typename_txt.setVisibility(View.INVISIBLE);
        typecount_txt.setVisibility(View.INVISIBLE);
        if (mTvTitle.contains("Series")) {
            refreshSeries(postition);
        } else if (mTvTitle.contains("Film")) {
            refreshFilm(postition);
        } else if (mTvTitle.contains("Cartoon")) {
            refreshCartoon(postition);
        } else if (mTvTitle.contains("Variety")) {
            refreshVariety(postition);
        }
    }

    //刷新电视剧清单
    private void refreshSeries(int postition) {
        if (mTvTitle.contains("全部")) {
            switch (postition) {
                case 0:    //最近更新
                    typename_txt.setText("分类 - " + "最近更新(");
                    mPresenter.requestIQiYiMovie(2, "15", "", "", 4, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 1:    //本周热播
                    typename_txt.setText("分类 - " + "本周热播(");
                    mPresenter.requestIQiYiMovie(2, "15", "", "", 11, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 2:    //古装传奇
                    typename_txt.setText("分类 - " + "古装传奇(");
                    mPresenter.requestIQiYiMovie(2, "15,24", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 3:    //时尚都市
                    typename_txt.setText("分类 - " + "时尚都市(");
                    mPresenter.requestIQiYiMovie(2, "15,24064", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 4:    //悬疑惊悚
                    typename_txt.setText("分类 - " + "悬疑惊悚(");
                    mPresenter.requestIQiYiMovie(2, "15,32", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 5:    //热血抗战
                    typename_txt.setText("分类 - " + "热血抗战(");
                    mPresenter.requestIQiYiMovie(2, "15,27916", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 6:    //偶像经典
                    typename_txt.setText("分类 - " + "偶像经典(");
                    mPresenter.requestIQiYiMovie(2, "15,30", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 7:    //精彩网剧
                    typename_txt.setText("分类 - " + "精彩网剧(");
                    mPresenter.requestIQiYiMovie(2, "15,24065", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 8:    //年代传奇
                    typename_txt.setText("分类 - " + "年代传奇(");
                    mPresenter.requestIQiYiMovie(2, "15,27", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 9:    //罪案追击
                    typename_txt.setText("分类 - " + "罪案追击(");
                    mPresenter.requestIQiYiMovie(2, "15,149", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 10:    //口碑神剧
                    typename_txt.setText("分类 - " + "口碑神剧(");
                    mPresenter.requestIQiYiMovie(2, "15,145", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 11:    //家庭生活
                    typename_txt.setText("分类 - " + "家庭生活(");
                    mPresenter.requestIQiYiMovie(2, "15,1654", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 12:    //武侠传奇
                    typename_txt.setText("分类 - " + "武侠传奇(");
                    mPresenter.requestIQiYiMovie(2, "15,23", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
            }
        } else if (mTvTitle.contains("古装大剧")) {
            typename_txt.setText("分类 - " + "古装大剧(");
            mPresenter.requestIQiYiMovie(2, "15,24", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("家庭")) {
            typename_txt.setText("分类 - " + "家庭生活(");
            mPresenter.requestIQiYiMovie(2, "15,24,1654", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("言情")) {
            typename_txt.setText("分类 - " + "甜虐言情(");
            mPresenter.requestIQiYiMovie(2, "15,24,20", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("军旅")) {
            typename_txt.setText("分类 - " + "热血军旅(");
            mPresenter.requestIQiYiMovie(2, "15,1655,27916", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("青春偶像")) {
            typename_txt.setText("分类 - " + "青春偶像(");
            mPresenter.requestIQiYiMovie(2, "15,24,30,1653", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        }
    }

    //刷新电影清单
    private void refreshFilm(int postition) {
        if (mTvTitle.contains("全部")) {
            switch (postition) {
                case 0:    //最近更新
                    typename_txt.setText("分类 - " + "最近更新(");
                    mPresenter.requestIQiYiMovie(1, "", "", "", 4, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 1:    //本周热播
                    typename_txt.setText("分类 - " + "本周热播(");
                    mPresenter.requestIQiYiMovie(1, "", "", "", 11, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 2:    //口碑高品质
                    typename_txt.setText("分类 - " + "口碑高品质(");
                    mPresenter.requestIQiYiMovie(1, "", "", "", 8, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 3:    //最燃动作片
                    typename_txt.setText("分类 - " + "最燃动作片(");
                    mPresenter.requestIQiYiMovie(1, "11", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 4:    //动画大放送
                    typename_txt.setText("分类 - " + "动画大放送(");
                    mPresenter.requestIQiYiMovie(1, "12", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 5:    //科幻巨制
                    typename_txt.setText("分类 - " + "科幻巨制(");
                    mPresenter.requestIQiYiMovie(1, "9", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 6:    //悬疑惊悚
                    typename_txt.setText("分类 - " + "悬疑惊悚(");
                    mPresenter.requestIQiYiMovie(1, "289,128", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 7:    //硬汉枪战
                    typename_txt.setText("分类 - " + "硬汉枪战(");
                    mPresenter.requestIQiYiMovie(1, "131,", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 8:    //家庭生活
                    typename_txt.setText("分类 - " + "家庭生活(");
                    mPresenter.requestIQiYiMovie(1, "27356", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 9:    //偶像经典
                    typename_txt.setText("分类 - " + "偶像经典(");
                    mPresenter.requestIQiYiMovie(1, "130", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
            }
        } else if (mTvTitle.contains("华语")) {
            typename_txt.setText("分类 - " + "华语(");
            mPresenter.requestIQiYiMovie(1, "1", "", "", 24, 1, mPageNum, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("香港")) {
            typename_txt.setText("分类 - " + "香港(");
            mPresenter.requestIQiYiMovie(1, "28997", "", "", 24, 1, mPageNum, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("美国")) {
            typename_txt.setText("分类 - " + "美国(");
            mPresenter.requestIQiYiMovie(1, "2", "", "", 24, 1, mPageNum, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("欧洲")) {
            typename_txt.setText("分类 - " + "欧洲(");
            mPresenter.requestIQiYiMovie(1, "3", "", "", 24, 1, mPageNum, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("日本")) {
            typename_txt.setText("分类 - " + "日本(");
            mPresenter.requestIQiYiMovie(1, "308", "", "", 24, 1, mPageNum, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("韩国")) {
            typename_txt.setText("分类 - " + "韩国(");
            mPresenter.requestIQiYiMovie(1, "4", "", "", 24, 1, mPageNum, "iqiyi", 1, "", 48);
        }
    }

    //刷新动漫清单
    private void refreshCartoon(int postition) {
        if (mTvTitle.contains("全部")) {
            switch (postition) {
                case 0:    //最近更新
                    typename_txt.setText("分类 - " + "最近更新(");
                    mPresenter.requestIQiYiMovie(4, "", "", "", 4, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 1:    //本周热播
                    typename_txt.setText("分类 - " + "本周热播(");
                    mPresenter.requestIQiYiMovie(4, "", "", "", 11, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 2:    //热血战斗
                    typename_txt.setText("分类 - " + "热血战斗(");
                    mPresenter.requestIQiYiMovie(4, "30232,30239", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 3:    //搞笑日常
                    typename_txt.setText("分类 - " + "搞笑日常(");
                    mPresenter.requestIQiYiMovie(4, "30230,30252", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 4:    //优秀国漫
                    typename_txt.setText("分类 - " + "优秀国漫(");
                    mPresenter.requestIQiYiMovie(4, "37", "", "", 8, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 5:    //暖心治愈
                    typename_txt.setText("分类 - " + "暖心治愈(");
                    mPresenter.requestIQiYiMovie(4, "30234", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 6:    //甜蜜爱恋
                    typename_txt.setText("分类 - " + "甜蜜爱恋(");
                    mPresenter.requestIQiYiMovie(4, "30243", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 7:    //不朽经典
                    typename_txt.setText("分类 - " + "不朽经典(");
                    mPresenter.requestIQiYiMovie(4, "30231", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 8:    //奇幻魔法
                    typename_txt.setText("分类 - " + "奇幻魔法(");
                    mPresenter.requestIQiYiMovie(4, "30247,30253", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 9:    //青春校园
                    typename_txt.setText("分类 - " + "青春校园(");
                    mPresenter.requestIQiYiMovie(4, "30266,30249", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 10:    //科幻机战
                    typename_txt.setText("分类 - " + "科幻机战(");
                    mPresenter.requestIQiYiMovie(4, "30245,30241", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 11:    //音乐偶像
                    typename_txt.setText("分类 - " + "音乐偶像(");
                    mPresenter.requestIQiYiMovie(4, "30269,30258", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 12:    //运动竞技
                    typename_txt.setText("分类 - " + "运动竞技(");
                    mPresenter.requestIQiYiMovie(4, "30250,30268", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 13:    //合家欢
                    typename_txt.setText("分类 - " + "合家欢(");
                    mPresenter.requestIQiYiMovie(4, "30270", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
            }
        } else if (mTvTitle.contains("科幻")) {
            typename_txt.setText("分类 - " + "科幻(");
            mPresenter.requestIQiYiMovie(4, "30245", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("搞笑")) {
            typename_txt.setText("分类 - " + "搞笑(");
            mPresenter.requestIQiYiMovie(4, "30230", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("战斗")) {
            typename_txt.setText("分类 - " + "战斗(");
            mPresenter.requestIQiYiMovie(4, "30239", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("热血")) {
            typename_txt.setText("分类 - " + "热血(");
            mPresenter.requestIQiYiMovie(4, "30232", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("剧场")) {
            typename_txt.setText("分类 - " + "剧场(");
            mPresenter.requestIQiYiMovie(4, "67", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        }
    }

    //刷新综艺清单
    private void refreshVariety(int postition) {
        if (mTvTitle.contains("全部")) {
            switch (postition) {
                case 0:    //最近更新
                    typename_txt.setText("分类 - " + "最近更新(");
                    mPresenter.requestIQiYiMovie(6, "", "", "", 4, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 1:    //本周热播
                    typename_txt.setText("分类 - " + "本周热播(");
                    mPresenter.requestIQiYiMovie(6, "", "", "", 11, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 2:    //真人秀
                    typename_txt.setText("分类 - " + "真人秀(");
                    mPresenter.requestIQiYiMovie(6, "2224", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 3:    //戏剧相声
                    typename_txt.setText("分类 - " + "戏剧相声(");
                    mPresenter.requestIQiYiMovie(6, "293", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 4:    //音乐天籁
                    typename_txt.setText("分类 - " + "音乐天籁(");
                    mPresenter.requestIQiYiMovie(6, "2121", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 5:    //竞技益智
                    typename_txt.setText("分类 - " + "竞技益智(");
                    mPresenter.requestIQiYiMovie(6, "30278,30277", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 6:    //脱口秀
                    typename_txt.setText("分类 - " + "脱口秀(");
                    mPresenter.requestIQiYiMovie(6, "2118", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 7:    //情感调解
                    typename_txt.setText("分类 - " + "情感调解(");
                    mPresenter.requestIQiYiMovie(6, "163", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 8:    //晚会盛宴
                    typename_txt.setText("分类 - " + "晚会盛宴(");
                    mPresenter.requestIQiYiMovie(6, "292", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 9:    //名人访谈
                    typename_txt.setText("分类 - " + "名人访谈(");
                    mPresenter.requestIQiYiMovie(6, "156", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 10:    //美食日记
                    typename_txt.setText("分类 - " + "美食日记(");
                    mPresenter.requestIQiYiMovie(6, "1003", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 11:    //时尚生活
                    typename_txt.setText("分类 - " + "时尚生活(");
                    mPresenter.requestIQiYiMovie(6, "160", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
                case 12:    //新闻报导
                    typename_txt.setText("分类 - " + "新闻报导(");
                    mPresenter.requestIQiYiMovie(6, "155", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
                    break;
            }
        } else if (mTvTitle.contains("真人秀")) {
            typename_txt.setText("分类 - " + "真人秀(");
            mPresenter.requestIQiYiMovie(6, "2224", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("搞笑")) {
            typename_txt.setText("分类 - " + "搞笑(");
            mPresenter.requestIQiYiMovie(6, "157", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("音乐")) {
            typename_txt.setText("分类 - " + "音乐(");
            mPresenter.requestIQiYiMovie(6, "2121", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("脱口秀")) {
            typename_txt.setText("分类 - " + "脱口秀(");
            mPresenter.requestIQiYiMovie(6, "2118", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        } else if (mTvTitle.contains("情感")) {
            typename_txt.setText("分类 - " + "情感(");
            mPresenter.requestIQiYiMovie(6, "163", "", "", 24, mPageNum, 1, "iqiyi", 1, "", 48);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_media_list;
    }

    @Override
    protected void initData() {
//        loading(View.VISIBLE);
        videoGrid.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                //TODO 设置为focus行为
                typecount_txt.setText(position + 1 + "/" + mTotal + "部)");
            }
        });
        videoGrid.setOnLoadMoreListener(new MyVerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNum += 1;
                refreshRequest(postition);
            }

            @Override
            public void onLoadEnd() {
            }
        });
        refreshRequest(postition);
    }

    @Override
    protected void initView() {
        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        if (mTvTitle.contains("全部")) {
            videoGrid.setNumColumns(ITEM_NUM_ROW - 1);
        } else {
            videoGrid.setNumColumns(ITEM_NUM_ROW);
        }

        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING_PX);
        int right = ViewUtils.getPercentWidthSize(ITEM_RIGHT_PADDING_PX);
        videoGrid.addItemDecoration(new SpaceItemDecoration(right, top));
        mVideoSampleAdapter = new VideoSampleAdapter();
        videoGrid.setAdapter(mVideoSampleAdapter);
    }

    @Override
    public void returnIQiYiMovieList(List<IQiYiMovieBean> beans, int total) {
        mVideoList.addAll(beans);
        typecount_txt.setText(total + "部)");
        mTotal = total;
        videoGrid.endMoreRefreshComplete();
        mHandler.sendEmptyMessage(REFRESH_MOVIE_CONTENT);
    }

    @Override
    protected IQiYiMovieContract.IQiYiMoviePresenter bindPresenter() {
        return new IQiYiMoviePresenter();
    }

    public void showError(String msg) {

    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        return false;
    }

    public class VideoSampleAdapter extends RecyclerView.Adapter<VideoSampleAdapter.ViewHolder> {

        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public VideoSampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_videos_layout, null);
            // 保持影视比例.
            mItemWidth = (AutoSizeUtils.dp2px(Objects.requireNonNull(getContext()), AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRID_VIEW_LEFT_PX - GRID_VIEW_RIGHT_PX - (ITEM_RIGHT_PADDING_PX * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemHeight = mItemWidth / 3 * 4;
//            mItemHeight = mItemWidth / 16 * 9;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mItemWidth, mItemHeight);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            return new VideoSampleAdapter.ViewHolder(view);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull final VideoSampleAdapter.ViewHolder holder, final int position) {
            if (null != mVideoList) {
                final IQiYiMovieBean videoBean = mVideoList.get(position);
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(videoBean.getImageUrl().replace(".jpg", "_260_360.jpg")).into(holder.bgIv);
                holder.nameTv.setAlpha(0.80f);
                holder.nameTv.setText(videoBean.getName());
                String latestOrder = videoBean.getLatestOrder();
                String videoCount = videoBean.getVideoCount();
                if (latestOrder != null && videoCount != null) {
                    if (Objects.requireNonNull(videoCount).equals(Objects.requireNonNull(latestOrder))) {
                        holder.infoTv.setText(videoBean.getLatestOrder() + "集全");
                    } else {
                        holder.infoTv.setText("更新至" + videoBean.getLatestOrder() + "集");
                    }
                }
                if (videoBean.getPayMarkUrl() != null) {
                    holder.payIv.setImageResource(R.drawable.vip_icon2);
                }


                holder.itemView.setOnFocusChangeListener((view, hasFocus) -> {
//                    holder.boardView.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                    ViewUtils.scaleAnimator(view, hasFocus, 1.2f, 150);
                });
                holder.itemView.setOnClickListener(view -> {
                    // TODO Item点击事件
                    Intent intent = new Intent(getContext(), VideoPreviewActivity.class);
                    intent.putExtra("videoBean", videoBean);
                    startActivity(intent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return null != mVideoList ? mVideoList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView bgIv, payIv;
            TextView nameTv, infoTv;
            View boardView;

            ViewHolder(View view) {
                super(view);
                bgIv = view.findViewById(R.id.bg_iv);
                nameTv = view.findViewById(R.id.name_tv);
                boardView = view.findViewById(R.id.board_view);
                payIv = view.findViewById(R.id.pay_iv);
                infoTv = view.findViewById(R.id.info_tv);
            }

        }

    }

    private void loading(Integer visibility) {
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        loading_txt.setCompoundDrawables(null, null, mCircleDrawable, null);
        loading_txt.setVisibility(visibility);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mCircleDrawable.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TotalMediaListActivity totalMediaListActivity = (TotalMediaListActivity) context;
        totalMediaListActivity.setHandler(mHandler);
    }

    @Override
    public void onStop() {
        super.onStop();
//        mCircleDrawable.stop();
    }
}

