package com.stars.tv.sample;

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
import com.stars.tv.bean.IQiYiBannerInfoBean;

import java.util.Objects;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class RecBannerItem1Presenter extends Presenter {
    private static final String TAG = "RecBannerItemPresenter";
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int ITEM_RIGHT_PADDING_PX = 25;
    private static final int ITEM_NUM_ROW = 4;
    int CARD_WIDTH = 0;
    int CARD_HEIGHT =0;
    ImageView bgIv;
    TextView nameTv;
    View boardView;
    protected static Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_rec_banner_layout, null);
        mContext = parent.getContext();
        CARD_WIDTH = (AutoSizeUtils.dp2px(Objects.requireNonNull(parent.getContext()),AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRID_VIEW_LEFT_PX - GRID_VIEW_RIGHT_PX - (ITEM_RIGHT_PADDING_PX * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
        CARD_HEIGHT = CARD_WIDTH/4*3;
        Log.v(TAG,"width"+CARD_WIDTH+"height:"+CARD_HEIGHT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(CARD_WIDTH, CARD_HEIGHT);
        view.setLayoutParams(lp);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Object item) {
        IQiYiBannerInfoBean videoBean = (IQiYiBannerInfoBean)item;
        bgIv = viewHolder.view.findViewById(R.id.bg_iv);
        boardView = viewHolder.view.findViewById(R.id.board_view);
        nameTv = viewHolder.view.findViewById(R.id.name_tv);
        nameTv.setText(videoBean.getName());
        Glide.with(Objects.requireNonNull(viewHolder.view.getContext()))
                .load(videoBean.getImageUrl()).into(bgIv);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"click the Video",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, VideoPreviewActivity.class);
                intent.putExtra("videoBean", videoBean);
                intent.putExtra("titleName","Recommand");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

}
