package com.stars.tv.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * View 常用函数
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.15
 */
public class ViewUtils {

    /**
     * 动画 放大/缩小
     *
     * @param view View
     * @param hasFocus boolean
     */
    public static void scaleAnimator(View view, boolean hasFocus, float focusScale, long duration) {
        float scale = hasFocus ? focusScale : 1.0f;
        view.animate().scaleX(scale).scaleY(scale).setInterpolator(new AccelerateInterpolator()).setDuration(duration);
    }

    public static int getPercentHeightSize(int val)
    {
        int screenHeight = AutoSizeConfig.getInstance().getScreenHeight();
        int designHeight = AutoSizeConfig.getInstance().getDesignHeightInDp();

        int res = val * screenHeight;
        if (res % designHeight == 0)
        {
            return res / designHeight;
        } else
        {
            return res / designHeight + 1;
        }
    }

    public static int getPercentWidthSize(int val)
    {
        int screenWidth = AutoSizeConfig.getInstance().getScreenWidth();
        int designWidth = AutoSizeConfig.getInstance().getDesignWidthInDp();

        int res = val * screenWidth;
        if (res % designWidth == 0)
        {
            return res / designWidth;
        } else
        {
            return res / designWidth + 1;
        }

    }

    /**
     * 改变图片的颜色
     * @param view ImageView
     * @param color 颜色格式：0xA6FFFFFF
     */
    public static void setViewColorFilter(ImageView view, int color) {
        view.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static int getPixelFromDp(Context context, int dp) {
      float density = context.getResources().getDisplayMetrics().density;
      return ((int)(4 * density));
    }
}
