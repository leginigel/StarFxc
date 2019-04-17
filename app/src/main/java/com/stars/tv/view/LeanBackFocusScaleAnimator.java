package com.stars.tv.view;

import android.animation.TimeAnimator;
import android.support.v17.leanback.graphics.ColorOverlayDimmer;
import android.support.v17.leanback.widget.ShadowOverlayContainer;
import android.support.v17.leanback.widget.ShadowOverlayHelper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.15
 */

public class LeanBackFocusScaleAnimator extends TimeAnimator implements TimeAnimator.TimeListener {
    private final View mView;
    private final int mDuration;
    private final ShadowOverlayContainer mWrapper;
    private final float mScaleDiff;
    private float mFocusLevel = 0.0F;
    private float mFocusLevelStart;
    private float mFocusLevelDelta;
    private final TimeAnimator mAnimator = new TimeAnimator();
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final ColorOverlayDimmer mDimmer;

    public void animateFocus(boolean select, boolean immediate) {
        this.endAnimation();
        float end = select ? 1.0F : 0.0F;
        if (immediate) {
            this.setFocusLevel(end);
        } else if (this.mFocusLevel != end) {
            this.mFocusLevelStart = this.mFocusLevel;
            this.mFocusLevelDelta = end - this.mFocusLevelStart;
            this.mAnimator.start();
        }

    }

    public LeanBackFocusScaleAnimator(View view, float scale, boolean useDimmer, int duration) {
        this.mView = view;
        this.mDuration = duration;
        this.mScaleDiff = scale - 1.0F;
        if (view instanceof ShadowOverlayContainer) {
            this.mWrapper = (ShadowOverlayContainer)view;
        } else {
            this.mWrapper = null;
        }

        this.mAnimator.setTimeListener(this);
        if (useDimmer) {
            this.mDimmer = ColorOverlayDimmer.createDefault(view.getContext());
        } else {
            this.mDimmer = null;
        }

    }

    void setFocusLevel(float level) {
        this.mFocusLevel = level;
        float scale = 1.0F + this.mScaleDiff * level;
        this.mView.setScaleX(scale);
        this.mView.setScaleY(scale);
        if (this.mWrapper != null) {
            this.mWrapper.setShadowFocusLevel(level);
        } else {
            ShadowOverlayHelper.setNoneWrapperShadowFocusLevel(this.mView, level);
        }

        if (this.mDimmer != null) {
            this.mDimmer.setActiveLevel(level);
            int color = this.mDimmer.getPaint().getColor();
            if (this.mWrapper != null) {
                this.mWrapper.setOverlayColor(color);
            } else {
                ShadowOverlayHelper.setNoneWrapperOverlayColor(this.mView, color);
            }
        }

    }

    float getFocusLevel() {
        return this.mFocusLevel;
    }

    void endAnimation() {
        this.mAnimator.end();
    }

    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
        float fraction;
        if (totalTime >= (long)this.mDuration) {
            fraction = 1.0F;
            this.mAnimator.end();
        } else {
            fraction = (float)((double)totalTime / (double)this.mDuration);
        }

        if (this.mInterpolator != null) {
            fraction = this.mInterpolator.getInterpolation(fraction);
        }

        this.setFocusLevel(this.mFocusLevelStart + fraction * this.mFocusLevelDelta);
    }
}
