package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.bean.IQiYiListBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.contract.IQiYiMovieContract;
import com.stars.tv.model.IQiYiMovieModel;
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

import static com.stars.tv.utils.Utils.getIQiYiListUrl;

public class VideoVGridSampleMVPFragment
        extends IQiYiBaseFragment<IQiYiMovieContract.IQiYiMoviePresenter, IQiYiMovieContract.IQiYiMovieModel>
        implements IQiYiMovieContract.IQiYiMovieView {

    final int REFRESH_MOVIE_CONTENT = 0;

    private static final int ITEM_NUM_ROW = 5; // 一行多少个row item.
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;

    private static final int ITEM_TOP_PADDING_PX = 15;
    private static final int ITEM_RIGHT_PADDING_PX = 25;

    List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    String mTvTitle;

    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;

    VideoVGridSampleMVPFragment.VideoSampleAdapter mVideoSampleAdapter;


    int mItemWidth = 0;
    int mItemHeight = 0;
    int mPageNum = 1;

    public static VideoVGridSampleMVPFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static VideoVGridSampleMVPFragment newInstance(String titleName){
        VideoVGridSampleMVPFragment myFragment = new VideoVGridSampleMVPFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName",titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_MOVIE_CONTENT) {
                mVideoSampleAdapter.notifyDataSetChanged();
                if (mVideoList.size() > 200) {
                    videoGrid.endRefreshingWithNoMoreData();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
    }

    private void refreshRequest() {

        IQiYiListBean listBean = new IQiYiListBean("1", "", "","",
                "", "","","","", "",  "",
                "24",String.valueOf(mPageNum), "","iqiyi","","");
        mPresenter.requestIQiYiMovie(getIQiYiListUrl(listBean));
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_viewo_sample;
    }

    @Override
    protected void initData(){
        mVideoSampleAdapter = new VideoVGridSampleMVPFragment.VideoSampleAdapter();
        videoGrid.setAdapter(mVideoSampleAdapter);
        videoGrid.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                //TODO 设置为focus行为
            }
        });
        videoGrid.setOnLoadMoreListener(() -> {
            mPageNum += 1;
            refreshRequest();
        });
        refreshRequest();
    }

    @Override
    protected void initView(){
        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        videoGrid.setNumColumns(ITEM_NUM_ROW);
        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING_PX);
        int right = ViewUtils.getPercentWidthSize(ITEM_RIGHT_PADDING_PX);
        videoGrid.addItemDecoration(new SpaceItemDecoration(right, top));
    }


    @Override
    public void returnIQiYiMovieList(ArrayList<IQiYiMovieBean> beans) {
        mVideoList.addAll(beans);
        videoGrid.endMoreRefreshComplete();
        mHandler.sendEmptyMessage(REFRESH_MOVIE_CONTENT);
    }


    @Override
    protected IQiYiMovieContract.IQiYiMoviePresenter bindPresenter() {
        return new IQiYiMoviePresenter();
    }

    @Override
    protected IQiYiMovieContract.IQiYiMovieModel bindModel() {
        return new IQiYiMovieModel();
    }

    @Override
    public void showError(String msg) {

    }

    public class VideoSampleAdapter extends RecyclerView.Adapter<VideoSampleAdapter.ViewHolder> {

        @NonNull
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public VideoSampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_video_sample_layout, null);
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
                        .load((position/ITEM_NUM_ROW)%2==0 ? videoBean.getPosterLdUrl() : videoBean.getPosterUrl()).into(holder.bgIv);
                holder.nameTv.setText(videoBean.getName());
                holder.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                        holder.boardView.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                        ViewUtils.scaleAnimator(view, hasFocus,1.2f,150);
                });
                holder.itemView.setOnClickListener(view -> {
                        // TODO Item点击事件
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    }

