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
import com.stars.tv.activity.VideoPreview;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.fragment.MediaInfoListFragment;
import com.stars.tv.fragment.PreVideoRowFragment;

import java.util.Objects;

public class PreVideoItemPresenter extends Presenter {

    private static final int CARD_WIDTH = 250;
    private static final int CARD_HEIGHT = 200;
    ImageView bgIv;
    TextView nameTv;
    View boardView;
    private Context mcontext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mcontext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_video_sample_layout, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(CARD_WIDTH, CARD_HEIGHT);
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

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
//            @Override

            public void onClick(View v) {
//                Intent intent = new Intent( mcontext, VideoPreview.class);
//                intent.putExtra("videoBean", videoBean);
//                mcontext.startActivity(intent);
                Log.v("tttPClickvideoBean", videoBean.toString());
            }
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

}
