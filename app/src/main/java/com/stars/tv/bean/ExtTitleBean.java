package com.stars.tv.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ExtTitleBean implements Serializable {
  private int mTabsIcon;
  private String mTabsName;

  public ExtTitleBean(String name, int resID){
    mTabsIcon = resID;
    mTabsName = name;
  }

  @NonNull
  @Override
  public String toString() {
    String tmp = this.getClass().getName() + "{" +
      "Name=" + mTabsName +
      ", IconRes=" + mTabsIcon +
      "}";
    return tmp;
  }

  public String getExtName(){
    return mTabsName;
  }

  public int getExtResIcon(){
    return mTabsIcon;
  }
}
