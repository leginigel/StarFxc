package com.stars.tv.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;

public class ExtBaseFragment extends Fragment {
  private String mTitle = "";
  private int mIconRes;
  private int indicatorColor = Color.BLUE;
  private int dividerColor = Color.GRAY;

  public String getTitle() {
    return mTitle;
  }
  public void setTitle(String title) {
    mTitle = title;
  }
  public int getIconRes() {
    return mIconRes;
  }
  public void setIconRes(int resId){
    mIconRes = resId;
  }

  public int getIndicatorColor() {
    return indicatorColor;
  }
  public void setIndicatorColor(int indicatorColor) {
    this.indicatorColor = indicatorColor;
  }
  public int getDividerColor() {
    return dividerColor;
  }
  public void setDividerColor(int dividerColor) {
    this.dividerColor = dividerColor;
  }
}
