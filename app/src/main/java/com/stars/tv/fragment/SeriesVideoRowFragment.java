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
import com.stars.tv.presenter.IQiYiMovieSimplifiedListPresenter;
import com.stars.tv.presenter.IQiYiParseBannerInfoPresenter;
import com.stars.tv.sample.HotVideoListRow;
import com.stars.tv.sample.MyPresenterSelector;
import com.stars.tv.sample.SeriesAndRecButtonItemPresenter;
import com.stars.tv.sample.SeriesButtonListRow;
import com.stars.tv.sample.SeriesAndRecVideoDataList;
import com.stars.tv.sample.SeriesBannerItemPresenter;
import com.stars.tv.sample.PortraitVideoItemPresenter;
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
import io.reactivex.disposables.CompositeDisposable;

public class SeriesVideoRowFragment extends BaseFragment {
    private static final String TAG = "SeriesVideoRowFragment";
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;
    protected Context mContext;
    List<IQiYiMovieBean> mVideoList = new ArrayList<>();

    List<List<IQiYiMovieBean>> mVideoListArray = new ArrayList<>(15);
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
    final int REFRESH_ERROR = -1;
    private int mPageNum = 1;
    private int totalPage = 5;
    private int category;
    private int curRow = 0;
    private int loadRows = 3;
    private int totalBanner = 3;
    private Circle mCircleDrawable;
    private int[] listPos = new int[15];
    private String[] orderlist = {"15", "15", "15,24", "15,1654", "15,20", "15,11992", "15,24065", "15,30,1653", "15,135", "15,139", "15,32,149", "15,148", "15,1655", "15,27", "18"};
    private int totalRow = 17;

    public SeriesVideoRowFragment() {
    }

    public static SeriesVideoRowFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static SeriesVideoRowFragment newInstance(String titleName) {
        SeriesVideoRowFragment myFragment = new SeriesVideoRowFragment();
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
                    parseIQiYiMovie();
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
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    private void showBannerData() {
        mRowsAdapter.clear();
        SeriesBannerItemPresenter seriesBannerItemPresenter = new SeriesBannerItemPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(seriesBannerItemPresenter);
        if (mBannerInfoList != null) {
            Collections.shuffle(mBannerInfoList);
            if (mBannerInfoList.size() >= totalBanner) {
                for (int i = 0; i < totalBanner; i++) {
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

        SeriesAndRecButtonItemPresenter mGridPresenter = new SeriesAndRecButtonItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        mRowsAdapter.add(new SeriesButtonListRow(gridRowAdapter));
    }

    private void showVideoData() {
        PortraitVideoItemPresenter videoItemPresenter0 = new PortraitVideoItemPresenter();
        ArrayObjectAdapter listRowAdapter0 = new ArrayObjectAdapter(videoItemPresenter0);
        mVideoList.clear();
        if (mVideoListArray != null) {
            mVideoList = mVideoListArray.get(listPos[category]);
            for (int k = 0; k < mVideoList.size(); k++) {
                listRowAdapter0.add(mVideoList.get(k));
            }
        }
        HeaderItem header0 = new HeaderItem(category, SeriesAndRecVideoDataList.SERIES_CATEGORY[category]);
        PortraitVideoListRow listRow0 = new PortraitVideoListRow(header0, listRowAdapter0);
        mRowsAdapter.add(listRow0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        // 处理item放大挡住其他Item.
        videoGrid.setClipChildren(false);
        videoGrid.setClipToPadding(false);
        // 设置间隔.
        videoGrid.setPadding(30, 30, 30, 30);
        // 设置垂直item的间隔100.
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
                mPageNum += 1;
                if (mPageNum <= totalPage) {
                    for (int i = 0; i < loadRows; i++) {
                        parseIQiYiMovieSimplifiedList((mPageNum - 1) * loadRows + i, 2, orderlist[(mPageNum - 1) * loadRows + i], "", "",
                                24, 1, 1, "iqiyi", 1, "", 12);
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


    private void parseIQiYiMovie() {
        parseIQiYiMovieSimplifiedList(0, 2, orderlist[0], "", "", 4, 1, 1, "iqiyi", 1, "", 12);
        parseIQiYiMovieSimplifiedList(1, 2, orderlist[1], "", "", 11, 1, 1, "iqiyi", 1, "", 12);
        parseIQiYiMovieSimplifiedList(2, 2, orderlist[2], "", "", 24, 1, 1, "iqiyi", 1, "", 12);
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
                        if (!list.isEmpty()) {
                            Log.v(TAG, "category" + category);
                            mVideoListArray.add(list);
                            Log.v(TAG, "mVideoListArray =：" + mVideoListArray.size());
                            listPos[category] = mVideoListArray.size() - 1;
                            Log.v(TAG, "listPos[category] =：" + listPos[category]);
                            Message msg = new Message();
                            msg.arg1 = category;
                            msg.what = REFRESH_MOVIE_CONTENT;
                            mHandler.sendMessage(msg);
                            if (mVideoListArray.size() % loadRows == 0) {
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
                if (!list.isEmpty()) {
                    mBannerInfoList.clear();
                    mBannerInfoList = list;
                    mHandler.sendEmptyMessage(REFRESH_BANNER_CONTENT);
                }
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
                mHandler.sendEmptyMessage(REFRESH_ERROR);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, "isVisibleToUser:" + isVisibleToUser);
        if (isVisibleToUser) {
            showLoad();
            if (NetUtil.isConnected()) {
                mPageNum = 1;
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
        if (NetUtil.isConnected()) {
            if (loadText != null) {
                loadText.setText("");
                loadText.setVisibility(View.VISIBLE);
            }
            if (mCircleDrawable != null) {
                mCircleDrawable.start();
            }
        } else {
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
        parseIQiYiParseBannerInfo("dianshiju");
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
        unbinder.unbind();
    }
}
