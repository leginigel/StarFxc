package com.stars.tv.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class DragTitleBean implements Serializable {
  private int mTabsIcon;
  private String mTabsName;

  public DragTitleBean(String name, int resID){
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

  public String getDragName(){
    return mTabsName;
  }

  public int getDragResIcon(){
    return mTabsIcon;
  }
}
