package com.stars.tv.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.activity.VideoPreviewActivity;
import com.stars.tv.bean.IQiYiMovieBean;

import java.util.Objects;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class PreVideoItemPresenter extends Presenter {

    private static final int ITEM_NUM_ROW = 6; // 一行多少个row item.
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int ITEM_RIGHT_PADDING_PX = 25;
    int mItemWidth = 0;
    int mItemHeight = 0;

    ImageView bgIv;
    TextView nameTv;
    View boardView;
    private Context mcontext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mcontext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_video_sample_layout, null);
        // 保持影视比例.
        mItemWidth = (AutoSizeUtils.dp2px(mcontext, AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRID_VIEW_LEFT_PX - GRID_VIEW_RIGHT_PX - (ITEM_RIGHT_PADDING_PX * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
        mItemHeight = mItemWidth / 3 * 4;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mItemWidth, mItemHeight);
        view.setLayoutParams(lp);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        IQiYiMovieBean videoBean = (IQiYiMovieBean) item;
        bgIv = viewHolder.view.findViewById(R.id.bg_iv);
        nameTv = viewHolder.view.findViewById(R.id.name_tv);
        boardView = viewHolder.view.findViewById(R.id.board_view);
        nameTv.setText(videoBean.getName());
        Glide.with(Objects.requireNonNull(viewHolder.view.getContext()))
                .load(videoBean.getImageUrl()).into(bgIv);
//
//        viewHolder.view.setOnClickListener(new View.OnClickListener() {
////            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent( mcontext, VideoPreviewActivity.class);
//                intent.putExtra("videoBean", videoBean);
//                mcontext.startActivity(intent);
//                Log.v("tttPClickvideoBean", videoBean.toString());
//            }
//        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

}
