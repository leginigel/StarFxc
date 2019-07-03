package com.stars.tv.youtube.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.app.Fragment;
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
    protected Fragment mFragment;
    protected ViewGroup mLeftNav, mTopNav;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        Log.v("YouTubeCardPresenter" , "onCreateViewHolder");
        mContext = viewGroup.getContext();
        mFragment = ((FragmentActivity) mContext)
                .getSupportFragmentManager().findFragmentById(R.id.container_home);
        mLeftNav = ((FragmentActivity) mContext).findViewById(R.id.left_nav);
        mTopNav = ((FragmentActivity) mContext).findViewById(R.id.top_nav);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_image, viewGroup, false);
        return new CardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        Log.v("YouTubeCardPresenter" , "onBindViewHolder");
        CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        YouTubeVideo youTubeVideo = (YouTubeVideo) o;

        setFocusNavigation(cardViewHolder);

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
                .load(youTubeVideo.getId() != null ? "https://i.ytimg.com/vi/"+ youTubeVideo.getId() +"/0.jpg" : null)
                .into(imgCard.getMainImageView());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to unbind for TextView, but if this viewHolder had
        // allocated bitmaps, they can be released here.

    }

    protected void setFocusNavigation(CardViewHolder cardViewHolder){
        cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                setDefaultFocus(v, keyCode);
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    setPressBack(v);
                    return true;
                }
            }
            return false;
        });
    }

    public void setDefaultFocus(View v, int keyCode){
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        else {
            mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            mTopNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
        else {
            mTopNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        }
    }

    public void setPressBack(View v){
        switch (((YoutubeFragment) mFragment).getTabCategory()){
            case Recommended:
                mTopNav.getChildAt(0).requestFocus();
                break;
            case Latest:
                mTopNav.getChildAt(1).requestFocus();
                break;
            case Music:
                mTopNav.getChildAt(2).requestFocus();
                break;
            case Entertainment:
                mTopNav.getChildAt(3).requestFocus();
                break;
            case Gaming:
                mTopNav.getChildAt(4).requestFocus();
                break;
        }
        ImageCardView imgCard = v.findViewById(R.id.img_card_view);
        setCardUnfocused(imgCard);
    }

    public void setCardUnfocused(ImageCardView imgCard){
        imgCard.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
        ((TextView) imgCard.findViewById(R.id.title_text)).setTextColor(Color.WHITE);
        ((TextView) imgCard.findViewById(R.id.content_text))
                .setTextColor(mContext.getResources().getColor(R.color.card_content_text));
    }

    public void setCardFocused(ImageCardView imgCard){
        imgCard.setInfoAreaBackgroundColor(Color.WHITE);
        ((TextView) imgCard.findViewById(R.id.title_text))
                .setTextColor(mContext.getResources().getColor(R.color.background));
        ((TextView) imgCard.findViewById(R.id.content_text))
                .setTextColor(mContext.getResources().getColor(R.color.card_content_text_focused));
    }


    public class CardViewHolder extends Presenter.ViewHolder{

        private ImageCardView mImageCardView;
        private TextView mTimeStamp;
        private TextView mTitle;
        private TextView mContent;
//        private CustomCardView mImageCardView;
        public CardViewHolder(View view) {
            super(view);
//            mImageCardView = (CustomCardView) view;
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setClickable(true);
//            view.setNextFocusLeftId(R.id.home_btn);

            mImageCardView = view.findViewById(R.id.img_card_view);
            mTimeStamp = view.findViewById(R.id.img_card_time_stamp);
            mTitle = mImageCardView.findViewById(R.id.title_text);
            mContent = mImageCardView.findViewById(R.id.content_text);
            mContent.setTextColor(mContext.getResources().getColor(R.color.card_content_text));

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

        public TextView getTitle() {
            return mTitle;
        }
    }
}
