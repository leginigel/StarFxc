package com.stars.tv.youtube.ui.player;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.ui.YouTubeCardPresenter;
import com.stars.tv.youtube.util.Utils;

public class ControlCardPresenter extends YouTubeCardPresenter {

    PlayerControlsFragment playerControlsFragment;
    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        playerControlsFragment = (PlayerControlsFragment) ((FragmentActivity) context)
                        .getSupportFragmentManager().findFragmentByTag("dialog");
        return super.onCreateViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        super.onBindViewHolder(viewHolder, o);
        CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        YouTubeVideo youTubeVideo = (YouTubeVideo) o;
        cardViewHolder.getImageCardView().setMainImageDimensions(410, 230);
        cardViewHolder.getTitle().setTextSize(14);
        cardViewHolder.getImageCardView().setInfoAreaBackgroundColor(Color.TRANSPARENT);
        cardViewHolder.getImageCardView().setBackgroundColor(Color.TRANSPARENT);
        if(youTubeVideo.getId() != null){
            cardViewHolder.getImageCardView().setTitleText(Html.fromHtml(youTubeVideo.getTitle()));
            TextView stamp = cardViewHolder.view.findViewById(R.id.img_card_time_stamp);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) stamp.getLayoutParams();
            params.topMargin = 195;
            stamp.setLayoutParams(params);
            TextView contentText = cardViewHolder.getImageCardView().findViewById(R.id.content_text);
            contentText.setMaxLines(1);
            contentText.setLines(1);
            contentText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            cardViewHolder.getImageCardView().setContentText(youTubeVideo.getChannel() + " ‧ "
                    + Utils.CountConverter(youTubeVideo.getNumber_views()) +" views");
        }
    }

    @Override
    protected void setFocusNavigation(CardViewHolder cardViewHolder) {
        PlayerControlsFragment.PlayerState state
                = playerControlsFragment.getPlayerStateChangeListener().getPlayerState();
        cardViewHolder.view.setNextFocusUpId(R.id.play_button);
        cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                setDefaultFocus(v, keyCode);
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    if(state == PlayerControlsFragment.PlayerState.VIDEO_ENDED){
                        playerControlsFragment.close();
                        playerControlsFragment.getActivity().onBackPressed();
                        return true;
                    }
                    else {
                        setPressBack(v);
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @Override
    public void setDefaultFocus(View v, int keyCode) {
        // playerControlsFragment close after 10s
        playerControlsFragment.setCountDown(10);
        PlayerControlsFragment.PlayerState state
                = playerControlsFragment.getPlayerStateChangeListener().getPlayerState();
        if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if(state == PlayerControlsFragment.PlayerState.VIDEO_ENDED){

            }
            else {
                playerControlsFragment.closeRow();
            }
        }
    }

    @Override
    public void setPressBack(View v) {
        playerControlsFragment.close();
    }
}
