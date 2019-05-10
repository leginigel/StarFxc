package com.stars.tv.bean;

public class DragTitle {
  private int mTabsIcon;
  private String mTabsName;


  public DragTitle(String name, int resID){
      mTabsIcon = resID;
      mTabsName = name;
  }

  public String getDragName(){
    return mTabsName;
  }

  public int getDragResIcon(){
    return mTabsIcon;
  }
}
