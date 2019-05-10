package com.stars.tv.youtube.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v17.leanback.widget.BaseCardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stars.tv.R;

public class CustomCardView extends BaseCardView {
    public static final int CARD_TYPE_FLAG_IMAGE_ONLY = 0;
    public static final int CARD_TYPE_FLAG_TITLE = 1;
    public static final int CARD_TYPE_FLAG_CONTENT = 2;
    public static final int CARD_TYPE_FLAG_ICON_RIGHT = 4;
    public static final int CARD_TYPE_FLAG_ICON_LEFT = 8;
    private static final String ALPHA = "alpha";
    private ImageView mImageView;
    private ViewGroup mInfoArea;
    private TextView mTitleView;
    private TextView mContentView;
    private ImageView mBadgeImage;
    private boolean mAttachedToWindow;
    ObjectAnimator mFadeInAnimator;


    public CustomCardView(Context context) {
        this(context, (AttributeSet)null);
    }

    public CustomCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.buildCardView(attrs, defStyleAttr, R.style.Widget_Leanback_ImageCardView);
    }

    private void buildCardView(AttributeSet attrs, int defStyleAttr, int defStyle) {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        inflater.inflate(R.layout.custom_image_card_view, this);
        TypedArray cardAttrs = this.getContext().obtainStyledAttributes(attrs, R.styleable.lbImageCardView, defStyleAttr, defStyle);
        int cardType = cardAttrs.getInt(R.styleable.lbImageCardView_lbImageCardViewType, 0);
        boolean hasImageOnly = cardType == 0;
        boolean hasTitle = (cardType & 1) == 1;
        boolean hasContent = (cardType & 2) == 2;
        boolean hasIconRight = (cardType & 4) == 4;
        boolean hasIconLeft = !hasIconRight && (cardType & 8) == 8;
        this.mImageView = (ImageView)this.findViewById(R.id.main_image);
        if (this.mImageView.getDrawable() == null) {
            this.mImageView.setVisibility(GONE);
        }

        this.mFadeInAnimator = ObjectAnimator.ofFloat(this.mImageView, "alpha", new float[]{1.0F});
        this.mFadeInAnimator.setDuration((long)17694720);
        this.mInfoArea = (ViewGroup)this.findViewById(R.id.info_field);
        if (hasImageOnly) {
            this.removeView(this.mInfoArea);
            cardAttrs.recycle();
        } else {
            if (hasTitle) {
                this.mTitleView = (TextView)inflater.inflate(R.layout.lb_image_card_view_themed_title, this.mInfoArea, false);
                this.mInfoArea.addView(this.mTitleView);
            }

            if (hasContent) {
                this.mContentView = (TextView)inflater.inflate(R.layout.lb_image_card_view_themed_content, this.mInfoArea, false);
                this.mInfoArea.addView(this.mContentView);
            }

            if (hasIconRight || hasIconLeft) {
                int layoutId = R.layout.lb_image_card_view_themed_badge_right;
                if (hasIconLeft) {
                    layoutId = R.layout.lb_image_card_view_themed_badge_left;
                }

                this.mBadgeImage = (ImageView)inflater.inflate(layoutId, this.mInfoArea, false);
                this.mInfoArea.addView(this.mBadgeImage);
            }

            LayoutParams relativeLayoutParams;
            if (hasTitle && !hasContent && this.mBadgeImage != null) {
                relativeLayoutParams = (LayoutParams)this.mTitleView.getLayoutParams();
                if (hasIconLeft) {
//                        relativeLayoutParams.addRule(17, this.mBadgeImage.getId());
                } else {
//                        relativeLayoutParams.addRule(16, this.mBadgeImage.getId());
                }

                this.mTitleView.setLayoutParams(relativeLayoutParams);
            }

            if (hasContent) {
                relativeLayoutParams = (LayoutParams)this.mContentView.getLayoutParams();
                if (!hasTitle) {
//                        relativeLayoutParams.addRule(10);
                }

                if (hasIconLeft) {
//                        relativeLayoutParams.removeRule(16);
//                        relativeLayoutParams.removeRule(20);
//                        relativeLayoutParams.addRule(17, this.mBadgeImage.getId());
                }

                this.mContentView.setLayoutParams(relativeLayoutParams);
            }

            if (this.mBadgeImage != null) {
                relativeLayoutParams = (LayoutParams)this.mBadgeImage.getLayoutParams();
                if (hasContent) {
//                        relativeLayoutParams.addRule(8, this.mContentView.getId());
                } else if (hasTitle) {
//                        relativeLayoutParams.addRule(8, this.mTitleView.getId());
                }

                this.mBadgeImage.setLayoutParams(relativeLayoutParams);
            }

            Drawable background = cardAttrs.getDrawable(R.styleable.lbImageCardView_infoAreaBackground);
            if (null != background) {
                this.setInfoAreaBackground(background);
            }

            if (this.mBadgeImage != null && this.mBadgeImage.getDrawable() == null) {
                this.mBadgeImage.setVisibility(VISIBLE);
            }

            cardAttrs.recycle();
        }
    }

    public final ImageView getMainImageView() {
        return this.mImageView;
    }

    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (this.mImageView != null) {
            this.mImageView.setAdjustViewBounds(adjustViewBounds);
        }

    }

    public void setMainImageScaleType(ImageView.ScaleType scaleType) {
        if (this.mImageView != null) {
            this.mImageView.setScaleType(scaleType);
        }

    }

    public void setMainImage(Drawable drawable) {
        this.setMainImage(drawable, true);
    }

    public void setMainImage(Drawable drawable, boolean fade) {
        if (this.mImageView != null) {
            this.mImageView.setImageDrawable(drawable);
            if (drawable == null) {
                this.mFadeInAnimator.cancel();
                this.mImageView.setAlpha(1.0F);
                this.mImageView.setVisibility(GONE);
            } else {
                this.mImageView.setVisibility(VISIBLE);
                if (fade) {
                    this.fadeIn();
                } else {
                    this.mFadeInAnimator.cancel();
                    this.mImageView.setAlpha(1.0F);
                }
            }

        }
    }

    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = this.mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        this.mImageView.setLayoutParams(lp);
    }

    public Drawable getMainImage() {
        return this.mImageView == null ? null : this.mImageView.getDrawable();
    }

    public Drawable getInfoAreaBackground() {
        return this.mInfoArea != null ? this.mInfoArea.getBackground() : null;
    }

    public void setInfoAreaBackground(Drawable drawable) {
        if (this.mInfoArea != null) {
            this.mInfoArea.setBackground(drawable);
        }

    }

    public void setInfoAreaBackgroundColor(@ColorInt int color) {
        if (this.mInfoArea != null) {
            this.mInfoArea.setBackgroundColor(color);
        }

    }

    public void setTitleText(CharSequence text) {
        if (this.mTitleView != null) {
            this.mTitleView.setText(text);
        }
    }

    public CharSequence getTitleText() {
        return this.mTitleView == null ? null : this.mTitleView.getText();
    }

    public void setContentText(CharSequence text) {
        if (this.mContentView != null) {
            this.mContentView.setText(text);
        }
    }

    public CharSequence getContentText() {
        return this.mContentView == null ? null : this.mContentView.getText();
    }

    public void setBadgeImage(Drawable drawable) {
        if (this.mBadgeImage != null) {
            this.mBadgeImage.setImageDrawable(drawable);
            if (drawable != null) {
                this.mBadgeImage.setVisibility(VISIBLE);
            } else {
                this.mBadgeImage.setVisibility(GONE);
            }

        }
    }

    public Drawable getBadgeImage() {
        return this.mBadgeImage == null ? null : this.mBadgeImage.getDrawable();
    }

    private void fadeIn() {
        this.mImageView.setAlpha(0.0F);
        if (this.mAttachedToWindow) {
            this.mFadeInAnimator.start();
        }

    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        if (this.mImageView.getAlpha() == 0.0F) {
            this.fadeIn();
        }

    }

    protected void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        this.mFadeInAnimator.cancel();
        this.mImageView.setAlpha(1.0F);
        super.onDetachedFromWindow();
    }
}
