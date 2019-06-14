package com.stars.tv.presenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stars.tv.R;
import com.stars.tv.bean.LiveTvBean;
import com.stars.tv.bean.LiveTvEpgBean;
import com.stars.tv.utils.CallBack;

import java.util.List;

public class LiveTvItemPresenter extends Presenter {
    private int type = 0;

    public LiveTvItemPresenter(int type) {
        this.type = type;
    }

    public LiveTvItemPresenter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_tv_channel_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Presenter.ViewHolder viewHolder, Object o) {
        if (o instanceof LiveTvBean) {
            LiveTvBean curBean = ((LiveTvBean) o);
            final ViewHolder vh = (ViewHolder) viewHolder;

            vh.channelNumber.setText(String.valueOf(curBean.getChannelNumber()));
            vh.channelName.setText(curBean.getChannelName());
            vh.channelName.setSelected(true);

            if (type != 0) {
                vh.channelEpg.setVisibility(View.GONE);
                vh.channelLogo.setVisibility(View.GONE);
                vh.channelFav.setVisibility(View.GONE);
                vh.channelWave.setVisibility(View.GONE);
            } else {
                if (curBean.getIsFav()) {
                    vh.channelFav.setVisibility(View.VISIBLE);
                } else {
                    vh.channelFav.setVisibility(View.GONE);
                }
                parseTvMaoEpgData(curBean, vh);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_logo_tv)
                        .error(R.drawable.ic_logo_tv);
                Glide.with(viewHolder.view.getContext())
                        .applyDefaultRequestOptions(requestOptions)
                        .load(curBean.getLogo()).into(vh.channelLogo);
            }
        }
    }

    private void parseTvMaoEpgData(LiveTvBean tvBean, ViewHolder viewHolder) {
        ParseLiveTVEpgPresenter ps = new ParseLiveTVEpgPresenter();
        ps.requestTvMaoEpgData(tvBean, new CallBack<List<LiveTvEpgBean>>() {
            @Override
            public void success(List<LiveTvEpgBean> list) {
                if (!list.isEmpty()) {
                    viewHolder.channelEpg.setText(list.get(0).getProgramName());
                } else {
                    viewHolder.channelEpg.setText(viewHolder.view.getContext().getString(R.string.str_live_tv_epg_empty));
                }
            }

            @Override
            public void error(String msg) {
                viewHolder.channelEpg.setText(viewHolder.view.getContext().getString(R.string.str_live_tv_epg_empty));
            }
        });
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    static class ViewHolder extends Presenter.ViewHolder {
        TextView channelName;
        TextView channelNumber;
        TextView channelEpg;
        ImageView channelLogo;
        ImageView channelFav;
        ImageView channelWave;

        ViewHolder(View view) {
            super(view);
            channelName = view.findViewById(R.id.item_live_tv_channel_name);
            channelNumber = view.findViewById(R.id.item_live_tv_channel_num);
            channelEpg = view.findViewById(R.id.item_live_tv_channel_epg_text);
            channelLogo = view.findViewById(R.id.item_live_tv_channel_logo);
            channelFav = view.findViewById(R.id.item_live_tv_channel_favourite);
            channelWave = view.findViewById(R.id.item_live_tv_channel_wave);
        }

    }
}
