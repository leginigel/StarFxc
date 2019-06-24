package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
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
import android.widget.Toast;

import com.stars.tv.R;
import com.stars.tv.bean.IQiYiBannerInfoBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiMovieSimplifiedBean;
import com.stars.tv.presenter.IQiYiMovieSimplifiedListPresenter;
import com.stars.tv.presenter.IQiYiParseBannerInfoPresenter;
import com.stars.tv.sample.FilmBannerItemPresenter;
import com.stars.tv.sample.FilmButtonItemPresenter;
import com.stars.tv.sample.FilmButtonListRow;
import com.stars.tv.sample.FilmHotListRow;
import com.stars.tv.sample.FilmVideoDataList;
import com.stars.tv.sample.FilmVideoItemPresenter;
import com.stars.tv.sample.FilmVideoListRow;
import com.stars.tv.sample.MyPresenterSelector;
import com.stars.tv.utils.CallBack;
import com.stars.tv.view.MyVerticalGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FilmVideoRowSampleFragment extends BaseFragment {
    private static final String TAG = "FilmVideoRowSampleFragment";
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;
    protected static Context mContext;
    List<IQiYiMovieBean> mVideoList = new ArrayList<>();

    List<List<IQiYiMovieBean>> mVideoListArray = new ArrayList<>(15);
    List<IQiYiBannerInfoBean> mBannerInfoList = new ArrayList<>();
    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;
    Unbinder unbinder;

    ArrayObjectAdapter mRowsAdapter;
    ItemBridgeAdapter mItemBridgeAdapter;
    int mSubPosition;
    ItemBridgeAdapter.ViewHolder mSelectedViewHolder;
    String mTvTitle;
    final int REFRESH_BANNER_CONTENT = 0;
    final int REFRESH_MOVIE_CONTENT = 1;
    private int mPageNum = 1;
    private int category;
    private int countRow = 0;
    private int loadRows = 3;
    private int totalBanner = 3;

    private int[] listPos = new  int[15];
    private String[] orderlist = {"27815", "30149", "27401", "1,27976", "1,1284", "2,128", "2,11", "8,27976", "128,27976", "291,289", "8,130", "11,131", "2,9",  "6,8,130"};

    public FilmVideoRowSampleFragment() {
    }

    public static FilmVideoRowSampleFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static FilmVideoRowSampleFragment newInstance(String titleName){
        FilmVideoRowSampleFragment myFragment = new FilmVideoRowSampleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName",titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.v("tttt","msg.what:"+msg.what);
            switch (msg.what){
                case REFRESH_BANNER_CONTENT:
                    showBannerData();
                    parseIQiYiMovie();
                    break;
                case REFRESH_MOVIE_CONTENT:
                    category = msg.arg1;
                    showVideoData();
                    countRow = countRow +1;
                    if(countRow == 15){
                        Toast.makeText(mContext,"没有更多视频加载",Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
            mItemBridgeAdapter.notifyDataSetChanged();
            if(category>=14){
                videoGrid.endRefreshingWithNoMoreData();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    private void showBannerData(){
        FilmBannerItemPresenter filmBannerItemPresenter = new FilmBannerItemPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(filmBannerItemPresenter);
        if(mBannerInfoList!=null) {
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
        FilmHotListRow listRow = new FilmHotListRow(listRowAdapter);
        mRowsAdapter.add(listRow);

        FilmButtonItemPresenter mGridPresenter = new FilmButtonItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        mRowsAdapter.add(new FilmButtonListRow(gridRowAdapter));
    }

    private void showVideoData(){
        FilmVideoItemPresenter videoItemPresenter0 = new FilmVideoItemPresenter();
        ArrayObjectAdapter listRowAdapter0 = new ArrayObjectAdapter(videoItemPresenter0);
        mVideoList.clear();
        mVideoList = mVideoListArray.get(listPos[category]);
        for (int k = 0; k < mVideoList.size(); k++) {
            listRowAdapter0.add(mVideoList.get(k));
        }
        HeaderItem header0 = new HeaderItem(category, FilmVideoDataList.FILM_CATEGORY[category]);
        FilmVideoListRow listRow0 = new FilmVideoListRow(header0, listRowAdapter0);
        mRowsAdapter.add(listRow0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_video, container, false);
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
                if(mSelectedViewHolder != viewHolder || mSubPosition != subposition) {
                    mSubPosition = subposition;
                    if(mSelectedViewHolder != null) {
                        setRowSelected(mSelectedViewHolder, false);
                    }
                    mSelectedViewHolder = (ItemBridgeAdapter.ViewHolder)viewHolder;
                    if(mSelectedViewHolder != null) {
                        setRowSelected(mSelectedViewHolder, true);
                    }
                }
            }
        });

        parseIQiYiParseBannerInfo("dianying");
        videoGrid.setOnLoadMoreListener(new MyVerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNum += 1;
                for(int i=0;i<loadRows; i++) {
                    parseIQiYiMovieSimplifiedList((mPageNum - 1) * 3 + i, 2, orderlist[(mPageNum - 1) * 3 + i], "", "",
                            24, 1, 1, "iqiyi", 1, "", 12);
                }
            }

            @Override
            public void onLoadEnd() {

            }
        });
        return view;
    }

    private void parseIQiYiMovie(){
        parseIQiYiMovieSimplifiedList(0, 1, orderlist[0], "", "", 4, 1, 1, "iqiyi", 1, "", 12);
        parseIQiYiMovieSimplifiedList(1, 1, orderlist[1], "", "", 11, 1, 1, "iqiyi", 1, "", 12);
        parseIQiYiMovieSimplifiedList(2, 1, orderlist[2], "", "", 24, 1, 1, "iqiyi", 1, "", 12);
    }

    private void parseIQiYiMovieSimplifiedList(int category,int channel, String orderList, String payStatus, String myYear,
                                               int sortType, int pageNum, int dataType, String siteType,
                                               int sourceType, String comicsStatus, int pageSize){
        IQiYiMovieSimplifiedListPresenter ps = new IQiYiMovieSimplifiedListPresenter();
        ps.requestIQiYiMovieSimplifiedList(channel,orderList,payStatus,myYear,sortType,pageNum,
                dataType,siteType,sourceType,comicsStatus,pageSize, new CallBack<IQiYiMovieSimplifiedBean>() {
                    @Override
                    public void success(IQiYiMovieSimplifiedBean bean) {
                        List<IQiYiMovieBean> list = bean.getList();
                        mVideoListArray.add(list);
                        listPos[category] = mVideoListArray.size()-1;
                        Message msg = new Message();
                        msg.arg1 = category;
                        msg.what = REFRESH_MOVIE_CONTENT;
                        mHandler.sendMessage(msg);

                        if(mVideoListArray.size()%loadRows==0) {
                            videoGrid.endMoreRefreshComplete();
                        }

                    }

                    @Override
                    public void error(String msg) {
                        //TODO 获取失败
                    }
                });
    }

    private void parseIQiYiParseBannerInfo(String channel){
        IQiYiParseBannerInfoPresenter ps = new IQiYiParseBannerInfoPresenter();
        ps.requestIQiYiBannerInfo( channel, new CallBack<List<IQiYiBannerInfoBean>>() {
            @Override
            public void success(List<IQiYiBannerInfoBean> list) {
                mBannerInfoList.clear();
                mBannerInfoList = list;
                mHandler.sendEmptyMessage(REFRESH_BANNER_CONTENT);
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
     *  测试改变选中ROW的颜色.
     */
    @TargetApi(Build.VERSION_CODES.M)
    void setRowSelected(ItemBridgeAdapter.ViewHolder vh, boolean selected) {
//        vh.itemView.setBackground(new ColorDrawable(selected ? getResources().getColor(R.color.color_focus) : getResources().getColor(R.color.color_transparent)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        return false;
    }
}
