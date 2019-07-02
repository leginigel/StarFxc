package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Circle;
import com.stars.tv.R;
import com.stars.tv.bean.IQiYiBannerInfoBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiMovieSimplifiedBean;
import com.stars.tv.bean.IQiYiTopListBean;
import com.stars.tv.presenter.IQiYiMovieSimplifiedListPresenter;
import com.stars.tv.presenter.IQiYiParseBannerInfoPresenter;
import com.stars.tv.presenter.IQiYiParseTopListPresenter;
import com.stars.tv.sample.HotVideoListRow;
import com.stars.tv.sample.LandscapeVideoItemPresenter;
import com.stars.tv.sample.LandscapeVideoListRow;
import com.stars.tv.sample.MyPresenterSelector;
import com.stars.tv.sample.PortraitVideoItem1Presenter;
import com.stars.tv.sample.PortraitVideoListRow1;
import com.stars.tv.sample.RecBannerItem1Presenter;
import com.stars.tv.sample.RecBannerItemPresenter;
import com.stars.tv.sample.RecButtonListRow;
import com.stars.tv.sample.RecTopVideoItemPresenter;
import com.stars.tv.sample.SeriesAndRecButtonItemPresenter;
import com.stars.tv.sample.SeriesAndRecVideoDataList;
import com.stars.tv.sample.PortraitVideoListRow;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.view.MyVerticalGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecommandVideoRowFragment extends BaseFragment {
    private static final String TAG = "RecommandVideoFragment";
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;
    protected Context mContext;
    List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    List<IQiYiTopListBean> mVideoTopList = new ArrayList<>();
    List<List<IQiYiTopListBean>> mVideoTopListArray = new ArrayList<>(3);
    List<List<IQiYiMovieBean>> mVideoListArray = new ArrayList<>(26);
    List<IQiYiBannerInfoBean> mBannerInfoList = new ArrayList<>();

    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;
    @BindView(R.id.loading)
    TextView loadText;
    Unbinder unbinder;

    ArrayObjectAdapter mRowsAdapter;
    ItemBridgeAdapter mItemBridgeAdapter;
    int mSubPosition;
    ItemBridgeAdapter.ViewHolder mSelectedViewHolder;
    String mTvTitle;
    final int REFRESH_BANNER_CONTENT = 0;
    final int REFRESH_MOVIE_CONTENT = 1;
    final int REFRESH_TOP_CONTENT = 2;
    final int REFRESH_ERROR = -1;
    private int mPageNum = 0;
    private int totalPage = 7;
    private int curRow = 0;
    private int category;
    private int loadRows = 4;
    private Circle mCircleDrawable;
    private int[] toplistPos = new int[3];
    private int[] listPos = new int[26];
    private int[] channel = {2, 1, 6, 4, 3, 8, 25, 7, 24, 16, 10, 5, 28, 12, 17, 15, 9, 13, 21, 26, 22, 27, 29, 30, 31, 32};

    private int bannerItem = 2;
    private int bannerTotalItem = 6;
    private int topItem = 10;
    private int topRow = 3;
    private int videoItem = 6;
    private int videoItem1 = 4;
    private int videoTotalRow = 26;
    private int totalRow = 32;
    private int count = 0;
    private boolean isViewCreated = false;

    public RecommandVideoRowFragment() {
    }

    public static RecommandVideoRowFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static RecommandVideoRowFragment newInstance(String titleName) {
        RecommandVideoRowFragment myFragment = new RecommandVideoRowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v(TAG, "msg.what:" + msg.what);
            switch (msg.what) {
                case REFRESH_BANNER_CONTENT:
                    if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
                        mCircleDrawable.stop();
                    }
                    if (loadText != null && loadText.getVisibility() == View.VISIBLE) {
                        loadText.setVisibility(View.GONE);
                    }
                    showBannerData();
                    parseIQiYiMovieTop();
                    break;
                case REFRESH_TOP_CONTENT:
                    category = msg.arg1;
                    showVideoTopData();
                    if (count < topRow) {
                        count = count + 1;
                    }
                    if (count == topRow) {
                        Log.v(TAG, "count" + count);
                        showButton();
                        videoGrid.endMoreRefreshComplete();
//                        loadMoreVideo();
                    }
                    break;
                case REFRESH_MOVIE_CONTENT:
                    category = msg.arg1;
                    showVideoData();
                    break;
                case REFRESH_ERROR:
                    if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
                        mCircleDrawable.stop();
                    }
                    if (loadText != null) {
                        loadText.setText("加载失败，网络简析错误！");
                        loadText.setTextColor(Color.WHITE);
                        loadText.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            mItemBridgeAdapter.notifyDataSetChanged();

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    private void showBannerData() {
        mRowsAdapter.clear();
        RecBannerItemPresenter recBannerItemPresenter = new RecBannerItemPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(recBannerItemPresenter);
        if (mBannerInfoList != null) {
            Collections.shuffle(mBannerInfoList);
            if (mBannerInfoList.size() >= bannerItem) {
                for (int i = 0; i < bannerItem; i++) {
                    listRowAdapter.add(mBannerInfoList.get(i));
                }
            } else {
                for (int i = 0; i < mBannerInfoList.size(); i++) {
                    listRowAdapter.add(mBannerInfoList.get(i));
                }
            }
        }
        HotVideoListRow listRow = new HotVideoListRow(listRowAdapter);
        mRowsAdapter.add(listRow);

        RecBannerItem1Presenter recBannerItem1Presenter = new RecBannerItem1Presenter();
        ArrayObjectAdapter listRowAdapter1 = new ArrayObjectAdapter(recBannerItem1Presenter);
        if (mBannerInfoList != null) {
            if (mBannerInfoList.size() >= bannerTotalItem) {
                for (int i = bannerItem; i < bannerTotalItem; i++) {
                    listRowAdapter1.add(mBannerInfoList.get(i));
                }
            } else {
                for (int i = bannerItem; i < mBannerInfoList.size(); i++) {
                    listRowAdapter1.add(mBannerInfoList.get(i));
                }
            }
        }
        HotVideoListRow listRow1 = new HotVideoListRow(listRowAdapter1);
        mRowsAdapter.add(listRow1);
    }

    private void showVideoTopData() {
        RecTopVideoItemPresenter videoItemPresenter = new RecTopVideoItemPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(videoItemPresenter);
        mVideoTopList.clear();
        if (mVideoTopListArray != null) {
            mVideoTopList = mVideoTopListArray.get(toplistPos[category]);
            if (mVideoTopList.size() >= topItem) {
                for (int k = 0; k < topItem; k++) {
                    listRowAdapter.add(mVideoTopList.get(k));
                }
            } else {
                for (int k = 0; k < mVideoTopList.size(); k++) {
                    listRowAdapter.add(mVideoTopList.get(k));
                }
            }
        }
        HeaderItem header = new HeaderItem(category, SeriesAndRecVideoDataList.TOP_CATEGORY[category]);
        PortraitVideoListRow listRow = new PortraitVideoListRow(header, listRowAdapter);
        mRowsAdapter.add(listRow);
    }

    private void showButton() {
        SeriesAndRecButtonItemPresenter mGridPresenter = new SeriesAndRecButtonItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        mRowsAdapter.add(new RecButtonListRow(gridRowAdapter));
    }

    private void showVideoData() {
        if (category % 2 != 0) {
            PortraitVideoItem1Presenter videoItemPresenter = new PortraitVideoItem1Presenter();
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(videoItemPresenter);
            mVideoList.clear();
            if (mVideoListArray != null) {
                mVideoList = mVideoListArray.get(listPos[category]);
                if (mVideoList.size() >= videoItem) {
                    for (int k = 0; k < videoItem; k++) {
                        listRowAdapter.add(mVideoList.get(k));
                    }
                } else {
                    for (int k = 0; k < mVideoList.size(); k++) {
                        listRowAdapter.add(mVideoList.get(k));
                    }
                }
            }
            HeaderItem header = new HeaderItem(category, SeriesAndRecVideoDataList.REC_CATEGORY[category]);
            PortraitVideoListRow1 listRow = new PortraitVideoListRow1(header, listRowAdapter);
            mRowsAdapter.add(listRow);
        } else {

            LandscapeVideoItemPresenter videoItemPresenter = new LandscapeVideoItemPresenter();
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(videoItemPresenter);
            mVideoList.clear();
            if (mVideoListArray != null) {
                mVideoList = mVideoListArray.get(listPos[category]);
                if (mVideoList.size() >= videoItem1) {
                    for (int k = 0; k < videoItem1; k++) {
                        listRowAdapter.add(mVideoList.get(k));
                    }
                } else {
                    for (int k = 0; k < mVideoList.size(); k++) {
                        listRowAdapter.add(mVideoList.get(k));
                    }
                }
            }
            HeaderItem header = new HeaderItem(category, SeriesAndRecVideoDataList.REC_CATEGORY[category]);
            LandscapeVideoListRow listRow = new LandscapeVideoListRow(header, listRowAdapter);
            mRowsAdapter.add(listRow);
        }

    }

    private void loadMoreVideo() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommand_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        isViewCreated = true;
        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        // 处理item放大挡住其他Item.
        videoGrid.setClipChildren(false);
        videoGrid.setClipToPadding(false);
        // 设置间隔.
        videoGrid.setPadding(30, 30, 30, 30);
        // 设置垂直item的间隔
        videoGrid.setVerticalSpacing(50);
        // 设置缓存.
        videoGrid.getRecycledViewPool().setMaxRecycledViews(0, 100);
        mContext = container.getContext();
        final MyPresenterSelector myPresenterSelector = new MyPresenterSelector();
        mRowsAdapter = new ArrayObjectAdapter(myPresenterSelector);
        mItemBridgeAdapter = new ItemBridgeAdapter(mRowsAdapter, myPresenterSelector);
        mItemBridgeAdapter.setAdapterListener(mBridgeAdapterListener); // 测试一行选中颜色的改变.
        videoGrid.setAdapter(mItemBridgeAdapter);
        videoGrid.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder viewHolder, int position, int subposition) {
                if (mSelectedViewHolder != viewHolder || mSubPosition != subposition) {
                    mSubPosition = subposition;
                    if (mSelectedViewHolder != null) {
                        setRowSelected(mSelectedViewHolder, false);
                    }
                    mSelectedViewHolder = (ItemBridgeAdapter.ViewHolder) viewHolder;
                    if (mSelectedViewHolder != null) {
                        setRowSelected(mSelectedViewHolder, true);
                    }
                }
            }
        });
        initLoading();
        videoGrid.setOnLoadMoreListener(new MyVerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.v(TAG, "loadMoreVideo");
                mPageNum += 1;
                if (mPageNum <= totalPage) {
                    if (mPageNum != (channel.length / loadRows) + 1) {
                        for (int i = 0; i < loadRows; i++) {
                            parseIQiYiMovieSimplifiedList((mPageNum - 1) * loadRows + i, channel[(mPageNum - 1) * loadRows + i], "", "", "",
                                    24, 1, 1, "iqiyi", 1, "", 6);
                        }
                    } else {
                        for (int i = 0; i < channel.length % loadRows; i++) {
                            parseIQiYiMovieSimplifiedList((mPageNum - 1) * loadRows + i, channel[(mPageNum - 1) * loadRows + i], "", "", "",
                                    24, 1, 1, "iqiyi", 1, "", 6);
                        }

                    }
                } else {
                    videoGrid.endRefreshingWithNoMoreData();
                }
            }

            @Override
            public void onLoadEnd() {
                if (getUserVisibleHint() && (curRow == totalRow - 1)) {
                    Toast.makeText(mContext, "没有更多视频加载", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void parseIQiYiMovieTop() {
        parseIQiYiTopList(0, "1", "realTime", 50, 1);
        parseIQiYiTopList(1, "2", "realTime", 50, 1);
        parseIQiYiTopList(2, "4", "realTime", 50, 1);
    }

    /**
     * 获取爱奇艺片库筛选结果List
     * 参数请参看iqiyidata.json
     *
     * @param orderList 筛选组合，请参看iqiyidata.json中order-list部分,  "15,24"; 为内地，古装筛选组合
     * @param pageSize  每页个数
     */
    private void parseIQiYiMovieSimplifiedList(int category, int channel, String orderList, String payStatus, String myYear,
                                               int sortType, int pageNum, int dataType, String siteType,
                                               int sourceType, String comicsStatus, int pageSize) {
        IQiYiMovieSimplifiedListPresenter ps = new IQiYiMovieSimplifiedListPresenter();
        ps.requestIQiYiMovieSimplifiedList(channel, orderList, payStatus, myYear, sortType, pageNum,
                dataType, siteType, sourceType, comicsStatus, pageSize, new CallBack<IQiYiMovieSimplifiedBean>() {
                    @Override
                    public void success(IQiYiMovieSimplifiedBean bean) {

                        Log.v(TAG, "result_num =：" + bean.getResult_num());
                        List<IQiYiMovieBean> list = bean.getList();
//                        for(int i=0;i<list.size();i++) {
//                            Log.v(TAG, list.get(i).toString());
//                        }
                        Log.v(TAG, "category" + category + "channel" + channel);

                        mVideoListArray.add(list);
                        listPos[category] = mVideoListArray.size() - 1;
                        Message msg = new Message();
                        msg.arg1 = category;
                        msg.what = REFRESH_MOVIE_CONTENT;
                        mHandler.sendMessage(msg);
                        if (mVideoListArray.size() <= videoTotalRow - (videoTotalRow % loadRows)) {
                            if (mVideoListArray.size() % loadRows == 0) {
                                videoGrid.endMoreRefreshComplete();
                            }
                        } else {
                            if (mVideoListArray.size() % 2 == 0) {
                                videoGrid.endMoreRefreshComplete();
                            }
                        }
                    }

                    @Override
                    public void error(String msg) {
                        //TODO 获取失败
                    }
                });
    }

    /**
     * 获取推荐栏位基本信息
     *
     * @param channel 电视剧：dianshiju    电影：dianying  综艺：zongyi   动漫：dongman     微电影：weidianying     推荐：""
     */
    private void parseIQiYiParseBannerInfo(String channel) {
        IQiYiParseBannerInfoPresenter ps = new IQiYiParseBannerInfoPresenter();
        ps.requestIQiYiBannerInfo(channel, new CallBack<List<IQiYiBannerInfoBean>>() {
            @Override
            public void success(List<IQiYiBannerInfoBean> list) {
//                for (IQiYiBannerInfoBean bean : list) {
//                    Log.v(TAG, bean.toString());
//                }
                mBannerInfoList.clear();
                mBannerInfoList = list;
                Log.v("TAG", "mBannerInfoList" + mBannerInfoList.size());
                mHandler.sendEmptyMessage(REFRESH_BANNER_CONTENT);

            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                mHandler.sendEmptyMessage(REFRESH_ERROR);
            }
        });
    }

    /**
     * 获取Top list
     *
     * @param cid  channel id , 热播榜为-1，其余根据list定义， 电视剧为2，电影为1
     * @param type 播放指数榜：playindex   飙升榜：rise  热度榜：realTime
     * @param size 获取个数
     * @param page 获取页面page number
     */
    private void parseIQiYiTopList(int category, String cid, String type, int size, int page) {
        IQiYiParseTopListPresenter ps = new IQiYiParseTopListPresenter();
        ps.requestIQiYiTopList(cid, type, size, page, new CallBack<List<IQiYiTopListBean>>() {
            @Override
            public void success(List<IQiYiTopListBean> list) {
//                for(IQiYiTopListBean bean:list) {
//                    Log.v(TAG, bean.toString());
//                }
                Log.v(TAG, "mVideoTopListArray" + mVideoTopListArray.size());
                mVideoTopListArray.add(list);
                toplistPos[category] = mVideoTopListArray.size() - 1;
                Message msg = new Message();
                msg.arg1 = category;
                msg.what = REFRESH_TOP_CONTENT;
                mHandler.sendMessage(msg);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    ItemBridgeAdapter.AdapterListener mBridgeAdapterListener = new ItemBridgeAdapter.AdapterListener() {
        @Override
        public void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            setRowSelected(viewHolder, false);
        }

        @Override
        public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder vh) {
            if (mSelectedViewHolder == vh) {
                setRowSelected(mSelectedViewHolder, false);
                mSelectedViewHolder = null;
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder vh) {
            setRowSelected(vh, false);
        }
    };


    /**
     * 测试改变选中ROW的颜色.
     */
    @TargetApi(Build.VERSION_CODES.M)
    void setRowSelected(ItemBridgeAdapter.ViewHolder vh, boolean selected) {
//        vh.itemView.setBackground(new ColorDrawable(selected ? getResources().getColor(R.color.color_focus) : getResources().getColor(R.color.color_transparent)));
        curRow = videoGrid.getSelectedPosition();
        Log.v(TAG, "pos" + videoGrid.getSelectedPosition());

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        if (getUserVisibleHint()) {
            showLoad();
            if (NetUtil.isConnected()) {
                count = 0;
                loadData();
            }
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, "isVisibleToUser:" + isVisibleToUser);
        if (isVisibleToUser && isViewCreated) {
            showLoad();
            if (NetUtil.isConnected()) {
                count = 0;
                loadData();
            }
        }
    }

    private void initLoading() {
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.WHITE);
        loadText.setCompoundDrawables(null, null, mCircleDrawable, null);
    }

    private void showLoad() {
        Log.v(TAG, "net" + NetUtil.isConnected());
        if (NetUtil.isConnected()) {
            if (loadText != null) {
                loadText.setText("");
                loadText.setVisibility(View.VISIBLE);
            }
            if (mCircleDrawable != null) {
                mCircleDrawable.start();
            }
        } else {
            Log.v(TAG, "net" + NetUtil.isConnected());
            if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
                mCircleDrawable.stop();
            }
            if (loadText != null) {
                loadText.setText("网络连接失败，请检查网络！");
                loadText.setTextColor(Color.WHITE);
                loadText.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void loadData() {
        Log.v(TAG, "info" + getUserVisibleHint());
        parseIQiYiParseBannerInfo("");
    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (videoGrid.getChildCount() > 0) {
                videoGrid.setSelectedPosition(0);
                videoGrid.smoothScrollToPosition(0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView");
        isViewCreated = false;
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
        unbinder.unbind();
    }
}
