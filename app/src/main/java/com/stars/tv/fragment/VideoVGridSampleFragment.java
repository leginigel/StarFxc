package com.stars.tv.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.bean.VideoBean;
import com.stars.tv.view.MyVerticalGridView;
import com.stars.tv.view.SpaceItemDecoration;
import com.stars.tv.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 内容界面示例
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.16
 */
public class VideoVGridSampleFragment extends Fragment {

    private static final int ITEM_NUM_ROW = 8; // 一行多少个row item.
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;

    private static final int ITEM_TOP_PADDING_PX = 15;
    private static final int ITEM_RIGHT_PADDING_PX = 25;

    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;

    Unbinder unbinder;

    VideoSampleAdapter mVideoSampleAdapter;
    List<VideoBean> mVideoList = new ArrayList<>();
    String mTvTitle;

    int mItemWidth = 0;
    int mItemHeight = 0;
    int mPageNum = 0;

    public VideoVGridSampleFragment() {
    }

    public static VideoVGridSampleFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static VideoVGridSampleFragment newInstance(String titleName){
        VideoVGridSampleFragment myFragment = new VideoVGridSampleFragment();
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
        mVideoSampleAdapter = new VideoSampleAdapter();

        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        videoGrid.setNumColumns(ITEM_NUM_ROW);
        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING_PX);
        int right = ViewUtils.getPercentWidthSize(ITEM_RIGHT_PADDING_PX);
        videoGrid.addItemDecoration(new SpaceItemDecoration(right, top));
        videoGrid.setAdapter(mVideoSampleAdapter);
        videoGrid.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                //TODO 设置为focus时的特殊设置
            }
        });
        videoGrid.setOnLoadMoreListener(new MyVerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //TODO 加载网络数据
                //测试数据
                int count = mVideoList.size();
                for (int i = count; i < count + 30; i++) {
                    mVideoList.add(new VideoBean());
                }
                videoGrid.endMoreRefreshComplete();
                mVideoSampleAdapter.notifyDataSetChanged();
                if (mVideoList.size() > 200) {
                    videoGrid.endRefreshingWithNoMoreData();
                }
                mPageNum += 1;
                refreshRequest();
            }
        });
        refreshRequest();
        return view;
    }

    private void refreshRequest() {
        //TODO 实现刷新界面方法
        //测试数据，实际数据请在新线程中网络获取实现
        for (int i = 0; i < 30; i++) {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(mTvTitle +i);
            mVideoList.add(videoBean);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class VideoSampleAdapter extends RecyclerView.Adapter<VideoSampleAdapter.ViewHolder> {

        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_video_sample_layout, null);
            // 保持影视比例.
            mItemWidth = (AutoSizeUtils.dp2px(Objects.requireNonNull(getContext()),AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRID_VIEW_LEFT_PX - GRID_VIEW_RIGHT_PX - (ITEM_RIGHT_PADDING_PX * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemHeight = mItemWidth / 3 * 4 + 25;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mItemWidth, mItemHeight);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            return new ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            if (null != mVideoList) {
                final VideoBean videoBean = mVideoList.get(position);
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(videoBean.getPoster_url()).into(holder.bgIv);
                holder.nameTv.setText(videoBean.getName());
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        holder.boardView.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                        ViewUtils.scaleAnimator(view, hasFocus,1.2f,150);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Item点击事件
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return null != mVideoList ? mVideoList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView bgIv;
            TextView nameTv;
            View boardView;

            ViewHolder(View view) {
                super(view);
                bgIv = view.findViewById(R.id.bg_iv);
                nameTv = view.findViewById(R.id.name_tv);
                boardView = view.findViewById(R.id.board_view);
            }
        }

    }

}
