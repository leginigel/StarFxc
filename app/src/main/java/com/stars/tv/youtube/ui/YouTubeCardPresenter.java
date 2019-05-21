package com.stars.tv.youtube.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.stars.tv.R;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.ui.youtube.YoutubeFragment;
import com.stars.tv.youtube.util.Utils;

public class YouTubeCardPresenter extends Presenter {
    private Context mContext;
    private YoutubeFragment mFragment;
    private ViewGroup mLeftNav, mTopNav;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        Log.v("YouTubeCardPresenter" , "onCreateViewHolder");
        mContext = viewGroup.getContext();
        mFragment = (YoutubeFragment) ((FragmentActivity) mContext)
                .getSupportFragmentManager().findFragmentById(R.id.container_home);
        mLeftNav = ((FragmentActivity) mContext).findViewById(R.id.left_nav);
        mTopNav = ((FragmentActivity) mContext).findViewById(R.id.top_nav);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_image, viewGroup, false);

//        CustomCardView customCardView = new CustomCardView(mContext);
//        customCardView.setNextFocusLeftId(R.id.home_btn);
//        customCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
//        customCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
//        customCardView.setFocusable(true);
//        customCardView.setFocusableInTouchMode(true);
//        customCardView.setMainImageDimensions(500, 281);
//        customCardView.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
//        customCardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_loading));
//
//        ((TextView) customCardView.findViewById(R.id.title_text)).setMaxLines(2);
//        ((TextView) customCardView.findViewById(R.id.title_text)).setTextSize(20);
//        ((TextView) customCardView.findViewById(R.id.content_text)).setLines(2);
//        ((TextView) customCardView.findViewById(R.id.content_text)).setTextSize(14);
//        ((TextView) customCardView.findViewById(R.id.content_text))
//                .setTextColor(mContext.getResources().getColor(R.color.card_content));
        return new CardViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        Log.v("YouTubeCardPresenter" , "onBindViewHolder");
        CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        YouTubeVideo youTubeVideo = (YouTubeVideo) o;

        // Set RowFragment Focus Navigation
        switch (mFragment.getTabCategory()){
            case Recommended:
                cardViewHolder.view.setNextFocusUpId(R.id.recommend_btn);
                break;
            case Music:
                cardViewHolder.view.setNextFocusUpId(R.id.music_btn);
                break;
            case Entertainment:
                cardViewHolder.view.setNextFocusUpId(R.id.entertainment_btn);
                break;
            case Gaming:
                cardViewHolder.view.setNextFocusUpId(R.id.gaming_btn);
                break;
        }
        cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                else {
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
                    mTopNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                else {
                    mTopNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        mTopNav.requestFocus();
                        return true;
                    }
                }
            }
            return false;
        });

        // Set TimeStamp Value
        TextView timeStamp = cardViewHolder.mTimeStamp;
        if(youTubeVideo.getDuration() == null){
            timeStamp.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            timeStamp.setBackgroundColor(Color.parseColor("#cc121212"));
            timeStamp.setText(Utils.DurationConverter(youTubeVideo.getDuration()));
        }

        // Load ImageCardView
        ImageCardView imgCard = cardViewHolder.mImageCardView;
        if(youTubeVideo.getId() != null){
            Log.v("YouTubeCardPresenter" , "!cardViewHolder.isLoading");
            cardViewHolder.setCardInfo();
            imgCard.setTitleText(Html.fromHtml(youTubeVideo.getTitle()));
            imgCard.setContentText(youTubeVideo.getChannel() + "\n"
                    + Utils.CountConverter(youTubeVideo.getNumber_views()) +" views ‧ "
                    + Utils.TimeConverter(youTubeVideo.getTime()) + "ago");
        }
        Glide.with(mContext)
                .asBitmap()
//                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_folder_24dp))
                .load("https://i.ytimg.com/vi/"+ youTubeVideo.getId() +"/0.jpg")
                .into(imgCard.getMainImageView());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to unbind for TextView, but if this viewHolder had
        // allocated bitmaps, they can be released here.

    }

    public class CardViewHolder extends ViewHolder{

        private ImageCardView mImageCardView;
        private TextView mTimeStamp;
        private TextView mTitle;
        private TextView mContent;
//        private CustomCardView mImageCardView;
        private CardViewHolder(View view) {
            super(view);
//            mImageCardView = (CustomCardView) view;
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setClickable(true);
            view.setNextFocusLeftId(R.id.home_btn);

            mImageCardView = view.findViewById(R.id.img_card_view);
            mTimeStamp = view.findViewById(R.id.img_card_time_stamp);
            mTitle = mImageCardView.findViewById(R.id.title_text);
            mContent = mImageCardView.findViewById(R.id.content_text);

            mImageCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
            mImageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
            mImageCardView.setMainImageDimensions(500, 281);
            mImageCardView.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
            mImageCardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_loading));

            setLoadingCardInfo();
        }

        private void setLoadingCardInfo(){
            Log.v("YouTubeCardPresenter" , "setLoadingCardInfo");
            mTitle.setMaxLines(2);
            mTitle.setTextColor(mContext.getResources().getColor(R.color.card_loading));
            mTitle.setBackground(mContext.getDrawable(R.drawable.card_text_bg));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(100);
            params.addRule(RelativeLayout.BELOW, mTitle.getId());
            mContent.setLayoutParams(params);

            mContent.setBackground(mContext.getDrawable(R.drawable.card_text_bg));
            mContent.setTextColor(mContext.getResources().getColor(R.color.card_loading));

        }

        private void setCardInfo(){
            Log.v("YouTubeCardPresenter" , "setCardInfo");
            mTitle.setTextSize(20);
            mTitle.setTextColor(Color.WHITE);
            mTitle.setBackgroundColor(Color.TRANSPARENT);
            mTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);

            mContent.setLines(2);
            mContent.setTextSize(12);
            mContent.setTextColor(mContext.getResources().getColor(R.color.card_content));
            mContent.setBackgroundColor(Color.TRANSPARENT);
        }

        public ImageCardView getImageCardView() {
            return mImageCardView;
        }
    }
}
