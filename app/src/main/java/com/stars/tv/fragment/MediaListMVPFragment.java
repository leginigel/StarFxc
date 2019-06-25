package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    String mTvTitle;
    int postition = 0;

    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;
    @BindView(R.id.typename_txt)
    TextView typename_txt;
    @BindView(R.id.typecount_txt)
    TextView typecount_txt;

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
                    mVideoSampleAdapter = new VideoSampleAdapter();
                    videoGrid.setAdapter(mVideoSampleAdapter);
                    mVideoSampleAdapter.notifyDataSetChanged();
                    if (mVideoList.size() > 900) {
                        videoGrid.endRefreshingWithNoMoreData();
                    }
                    break;
                default:
                    postition = msg.what;
                    refreshRequest(postition);
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
        // orderList必须用逗号分隔id，否则会出错
        if (mTvTitle.contains("Series") && mTvTitle.contains("全部剧集")) {
            switch (postition) {
                case 0:    //最近更新
                    typename_txt.setText("分类-"+"最近更新(");
                    mPresenter.requestIQiYiMovie(2, "15", "", "", 4, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 1:    //本周热播
                    typename_txt.setText("分类-"+"本周热播(");
                    mPresenter.requestIQiYiMovie(2, "15", "", "", 11, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 2:    //古装传奇
                    typename_txt.setText("分类-"+"古装传奇(");
                    mPresenter.requestIQiYiMovie(2, "15,24", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 3:    //时尚都市
                    typename_txt.setText("分类-"+"时尚都市(");
                    mPresenter.requestIQiYiMovie(2, "15,24064", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 4:    //悬疑惊悚
                    typename_txt.setText("分类-"+"悬疑惊悚(");
                    mPresenter.requestIQiYiMovie(2, "15,32", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 5:    //热血抗战
                    typename_txt.setText("分类-"+"热血抗战(");
                    mPresenter.requestIQiYiMovie(2, "15,27916", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 6:    //偶像经典
                    typename_txt.setText("分类-"+"偶像经典(");
                    mPresenter.requestIQiYiMovie(2, "15,30", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 7:    //精彩网剧
                    typename_txt.setText("分类-"+"精彩网剧(");
                    mPresenter.requestIQiYiMovie(2, "15,24065", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 8:    //年代传奇
                    typename_txt.setText("分类-"+"年代传奇(");
                    mPresenter.requestIQiYiMovie(2, "15,27", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 9:    //罪案追击
                    typename_txt.setText("分类-"+"罪案追击(");
                    mPresenter.requestIQiYiMovie(2, "15,149", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 10:    //口碑神剧
                    typename_txt.setText("分类-"+"口碑神剧(");
                    mPresenter.requestIQiYiMovie(2, "15,145", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 11:    //家庭生活
                    typename_txt.setText("分类-"+"家庭生活(");
                    mPresenter.requestIQiYiMovie(2, "15,1654", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
                case 12:    //武侠传奇
                    typename_txt.setText("分类-"+"武侠传奇(");
                    mPresenter.requestIQiYiMovie(2, "15,23", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
                    break;
            }
        } else if (mTvTitle.contains("古装大剧")) {
            typename_txt.setText("分类-"+"武侠传奇(");
            mPresenter.requestIQiYiMovie(2, "15,24", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
        }
        else if (mTvTitle.contains("家庭")) {
            typename_txt.setText("分类-"+"武侠传奇(");
            mPresenter.requestIQiYiMovie(2, "15,24,1654", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
        }
        else if (mTvTitle.contains("言情")) {
            typename_txt.setText("分类-"+"武侠传奇(");
            mPresenter.requestIQiYiMovie(2, "15,24,20", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
        }
        else if (mTvTitle.contains("军旅")) {
            typename_txt.setText("分类-"+"武侠传奇(");
            mPresenter.requestIQiYiMovie(2, "15,1655,27916", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
        }
        else if (mTvTitle.contains("青春偶像")) {
            typename_txt.setText("分类-"+"武侠传奇(");
            mPresenter.requestIQiYiMovie(2, "15,24,30,1653", "", "", 24, 1, 1, "iqiyi", 1, "", 900);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_media_list;
    }

    @Override
    protected void initData() {
        videoGrid.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                //TODO 设置为focus行为
                typecount_txt.setText(position+1+"/"+mVideoList.size()+"部)");
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
        if (mTvTitle.contains("全部")){
            videoGrid.setNumColumns(ITEM_NUM_ROW-1);
        }else {
            videoGrid.setNumColumns(ITEM_NUM_ROW);
        }

        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING_PX);
        int right = ViewUtils.getPercentWidthSize(ITEM_RIGHT_PADDING_PX);
        videoGrid.addItemDecoration(new SpaceItemDecoration(right, top));
    }


    @Override
    public void returnIQiYiMovieList(List<IQiYiMovieBean> beans) {
        mVideoList.clear();
        mVideoList.addAll(beans);
        typecount_txt.setText(mVideoList.size()+"部)");
        typename_txt.setVisibility(View.VISIBLE);
        typecount_txt.setVisibility(View.VISIBLE);
        videoGrid.endMoreRefreshComplete();
        mHandler.sendEmptyMessage(REFRESH_MOVIE_CONTENT);
    }

    @Override
    protected IQiYiMovieContract.IQiYiMoviePresenter bindPresenter() {
        return new IQiYiMoviePresenter();
    }

    public void showError(String msg) {

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
                        .load(videoBean.getImageUrl()).into(holder.bgIv);
                holder.nameTv.setAlpha(0.80f);
                holder.nameTv.setText(videoBean.getName());
                holder.infoTv.setVisibility(View.VISIBLE);
                holder.payIv.setVisibility(View.VISIBLE);
                String latestOrder = videoBean.getLatestOrder();
                String videoCount = videoBean.getVideoCount();
                if(latestOrder!=null &&videoCount!=null)
                {
                    if (Objects.requireNonNull(videoCount).equals(Objects.requireNonNull(latestOrder))) {
                        holder.infoTv.setText(videoBean.getLatestOrder() + "集全");
                    } else {
                        holder.infoTv.setText("更新至" + videoBean.getLatestOrder() + "集");
                    }
                }
                if(videoBean.getPayMarkUrl()!=null) {
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
            ImageView bgIv,payIv;
            TextView nameTv,infoTv;
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TotalMediaListActivity totalMediaListActivity = (TotalMediaListActivity) context;
        totalMediaListActivity.setHandler(mHandler);
    }
}

