package com.stars.tv.fragment;

import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.stars.tv.R;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.sample.ButtonListRow;
import com.stars.tv.sample.ButtonItemPresenter;
import com.stars.tv.sample.VideoItemPresenter;
import com.stars.tv.sample.VideoSampleDataList;
import com.stars.tv.sample.MyPresenterSelector;
import com.stars.tv.view.MyVerticalGridView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 内容界面示例
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.15
 */
public class VideoRowSampleFragment extends BaseFragment {

    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;

    @BindView(R.id.video_content_v_grid) MyVerticalGridView videoGrid;

    Unbinder unbinder;

    ArrayObjectAdapter mRowsAdapter;
    ItemBridgeAdapter mItemBridgeAdapter;
    int mSubPosition;
    ItemBridgeAdapter.ViewHolder mSelectedViewHolder;
    String mTvTitle;

    public VideoRowSampleFragment() {
    }

    public static VideoRowSampleFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static VideoRowSampleFragment newInstance(String titleName){
        VideoRowSampleFragment myFragment = new VideoRowSampleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName",titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewo_sample, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        // 处理item放大挡住其他Item.
        videoGrid.setClipChildren(false);
        videoGrid.setClipToPadding(false);
        // 设置间隔.
        videoGrid.setPadding(30, 30, 30, 30);
        // 设置垂直item的间隔.
        videoGrid.setVerticalSpacing(100);
        // 设置缓存.
        videoGrid.getRecycledViewPool().setMaxRecycledViews(0, 100);

        List<IQiYiMovieBean> list = VideoSampleDataList.setupMovies();
        final MyPresenterSelector myPresenterSelector = new MyPresenterSelector();
        mRowsAdapter = new ArrayObjectAdapter(myPresenterSelector);

        VideoItemPresenter videoItemPresenter = new VideoItemPresenter();

        int i;
        // 测试数据
        for (i = 0; i < VideoSampleDataList.MOVIE_CATEGORY.length; i++) {
            if (i != 0) {
                Collections.shuffle(list);
            }
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(videoItemPresenter);
            for (int j = 0; j < VideoSampleDataList.setupMovies().size(); j++) {
                listRowAdapter.add(list.get(j));
            }
            HeaderItem header = new HeaderItem(i, VideoSampleDataList.MOVIE_CATEGORY[i]);
            ListRow listRow = new ListRow(header, listRowAdapter);
            mRowsAdapter.add(listRow);
        }

        // 测试其它数据.
        HeaderItem gridHeader = new HeaderItem("Button Row");
        ButtonItemPresenter mGridPresenter = new ButtonItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        mRowsAdapter.add(new ButtonListRow(gridHeader, gridRowAdapter));

        mItemBridgeAdapter = new ItemBridgeAdapter(mRowsAdapter);
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
        return view;
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
        vh.itemView.setBackground(new ColorDrawable(selected ? getResources().getColor(R.color.color_focus) : getResources().getColor(R.color.color_transparent)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(videoGrid.getChildCount()>0) {
                videoGrid.setSelectedPosition(0);
                videoGrid.smoothScrollToPosition(0);
            }
            return true;
        }
        return false;
    }
}
