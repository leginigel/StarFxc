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
import com.stars.tv.bean.IQiYiTopListBean;

import java.util.Objects;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class RecTopVideoItemPresenter extends Presenter {
    private static final String TAG = "RecTopVideoItemPresente";
    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int ITEM_RIGHT_PADDING_PX = 25;
    private static final int ITEM_NUM_ROW = 5;
    int CARD_WIDTH = 0;
    int CARD_HEIGHT = 0;
//    private static final int CARD_WIDTH = 250;
//    private static final int CARD_HEIGHT = 200;
    ImageView bgIv, payIv;
    TextView nameTv, infoTv;
    View boardView;
    protected Context mContext;
    boolean isValue;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_videos_layout, null);
        mContext = parent.getContext();
        CARD_WIDTH = (AutoSizeUtils.dp2px(Objects.requireNonNull(parent.getContext()),AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRID_VIEW_LEFT_PX - GRID_VIEW_RIGHT_PX - (ITEM_RIGHT_PADDING_PX * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
        CARD_HEIGHT = CARD_WIDTH / 3 * 4;
//        CARD_HEIGHT = 410;
        Log.v(TAG,"height"+CARD_HEIGHT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(CARD_WIDTH, CARD_HEIGHT);
        view.setLayoutParams(lp);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        IQiYiTopListBean videoBean = (IQiYiTopListBean)item;
//        Log.v(TAG,"videoBean"+videoBean.toString());
        bgIv = viewHolder.view.findViewById(R.id.bg_iv);
        nameTv = viewHolder.view.findViewById(R.id.name_tv);
        boardView = viewHolder.view.findViewById(R.id.board_view);
        payIv = viewHolder.view.findViewById(R.id.pay_iv);
        infoTv = viewHolder.view.findViewById(R.id.info_tv);
        infoTv.setVisibility(View.VISIBLE);
        payIv.setVisibility(View.VISIBLE);
        nameTv.setAlpha(0.80f);
        int latestOrder = videoBean.getLatest_order();
        int videoCount = videoBean.getVideo_count();
        String score = String.valueOf(videoBean.getSns_score());
        if (latestOrder != 0) {
            if (videoCount != 1) {
                if (Objects.requireNonNull(videoCount).equals(Objects.requireNonNull(latestOrder))) {
                    infoTv.setText(latestOrder + "集全");
                } else {
                    infoTv.setText("更新至" + latestOrder + "集");
                }
            } else{
                infoTv.setText(score);
            }
        }

        nameTv.setText(videoBean.getAlbum_name());
        String imageUrl = videoBean.getAlbum_pic_url();
        if (imageUrl != null) {
            String size = "_260_360.jpg";
            String newUrl = imageUrl.replace(".jpg", size);
            Glide.with(Objects.requireNonNull(viewHolder.view.getContext()))
                    .load(newUrl).into(bgIv);
        }
        if(videoBean.getCharge_pay_mark()!=0) {
            payIv.setImageResource(R.drawable.vip_icon2);
        }
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"click the" + nameTv.getText().toString(),Toast.LENGTH_LONG).show();
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
