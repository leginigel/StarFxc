package com.stars.tv.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ExtVideoBean implements Serializable {
  private String mVideoType;
  private String mAlbumId;
  private String mVideoId;
  private String mVideoName;
  private String mVideoPlayUrl;
  private String mVideoCurrentViewOrder;
  private String mVideoLatestOrder;
  private String mVideoCounter;
  private String mVideoDescription;
  private String mVideoImageUrl;

  @NonNull
  @Override
  public String toString() {
    return this.getClass().getName() + "={" +
      "\nVideoType=" + mVideoType +
      "\nAlbumId=" + mAlbumId +
      "\nVideoId=" + mVideoId +
      "\nVideoName=" + mVideoName +
      "\nPlayUrl=" + mVideoPlayUrl +
      "\nCurrentViewOrder=" + mVideoCurrentViewOrder +
      "\nLatestOrder=" + mVideoLatestOrder +
      "\nVideoCounter=" + mVideoLatestOrder +
      "\nDescription=" + mVideoDescription +
      "\nImageUrl=" + mVideoImageUrl +
      "\n}";
  }
  public String getVideoType() {
    return mVideoType;
  }

  public void setVideoType(String videoType) {
    mVideoType = videoType;
  }

  public String getAlbumId() {
    return mAlbumId;
  }

  public void setAlbumId(String albumId) {
    mAlbumId = albumId;
  }

  public String getVideoCounter() {
    return mVideoCounter;
  }

  public void setVideoCounter(String videoCounter) {
    mVideoCounter = videoCounter;
  }

  public String getVideoId() {
    return mVideoId;
  }

  public void setVideoId(String videoId) {
    mVideoId = videoId;
  }

  public String getVideoName() {
    return mVideoName;
  }

  public void setVideoName(String videoName) {
    mVideoName = videoName;
  }

  public String getVideoPlayUrl() {
    return mVideoPlayUrl;
  }

  public void setVideoPlayUrl(String videoPlayUrl) {
    mVideoPlayUrl = videoPlayUrl;
  }

  public String getVideoCurrentViewOrder() {
    return mVideoCurrentViewOrder;
  }

  public void setVideoCurrentViewOrder(String videoCurrentViewOrder) {
    mVideoCurrentViewOrder = videoCurrentViewOrder;
  }

  public String getVideoLatestOrder() {
    return mVideoLatestOrder;
  }

  public void setVideoLatestOrder(String videoLatestOrder) {
    mVideoLatestOrder = videoLatestOrder;
  }

  public String getVideoDescription() {
    return mVideoDescription;
  }

  public void setVideoDescription(String videoDescription) {
    mVideoDescription = videoDescription;
  }

  public String getVideoImageUrl() {
    return mVideoImageUrl;
  }

  public void setVideoImageUrl(String videoImageUrl) {
    mVideoImageUrl = videoImageUrl;
  }
}
