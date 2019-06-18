package com.stars.tv.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ExtVideoBean implements Serializable {
  private int mVideoType;
  private String mAlbumId;
  private String mVideoId;
  private String mVideoName;
  private String mVideoPlayUrl;
  private String mAlbumImageUrl;
  private int mVideoCurrentViewOrder;
  private int mVideoLatestOrder;
  private int mVideoCount;
  private int mVideoPlayPosition;
  private String mChannel;
  private int mNumberViews;
  private String mTime;
  private String mDuration;

  @NonNull
  @Override
  public String toString() {
    return this.getClass().getName() + "={" +
      "\nVideoType=" + mVideoType +
      "\nAlbumId=" + mAlbumId +
      "\nVideoId=" + mVideoId +
      "\nVideoName=" + mVideoName +
      "\nPlayUrl=" + mVideoPlayUrl +
      "\nAlbumImageUrl=" + mAlbumImageUrl +
      "\nCurrentViewOrder=" + mVideoCurrentViewOrder +
      "\nLatestOrder=" + mVideoLatestOrder +
      "\nVideoCounter=" + mVideoCount +
      "\nVideoPlayPosition=" + mVideoPlayPosition +
      "\nChannel=" + mChannel +
      "\nNumberViews=" + mNumberViews +
      "\nTime=" + mTime +
      "\nDuration=" + mDuration +
      "\n}";
  }
  public int getVideoType() {
    return mVideoType;
  }

  public void setVideoType(int videoType) {
    mVideoType = videoType;
  }

  public String getAlbumId() {
    return mAlbumId;
  }

  public void setAlbumId(String albumId) {
    mAlbumId = albumId;
  }

  public int getVideoCount() {
    return mVideoCount;
  }

  public void setVideoCount(int videoCounter) {
    mVideoCount = videoCounter;
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

  public String getAlbumImageUrl() {
    return mAlbumImageUrl;
  }

  public void setAlbumImageUrl(String albumImageUrl) {
    mAlbumImageUrl = albumImageUrl;
  }

  public int getVideoCurrentViewOrder() {
    return mVideoCurrentViewOrder;
  }

  public void setVideoCurrentViewOrder(int videoCurrentViewOrder) {
    mVideoCurrentViewOrder = videoCurrentViewOrder;
  }

  public int getVideoLatestOrder() {
    return mVideoLatestOrder;
  }

  public void setVideoLatestOrder(int videoLatestOrder) {
    mVideoLatestOrder = videoLatestOrder;
  }

  public int getVideoPlayPosition() {
    return mVideoPlayPosition;
  }

  public void setVideoPlayPosition(int videoPlayPosition) {
    mVideoPlayPosition = videoPlayPosition;
  }

  public String getDuration() {
    return mDuration;
  }

  public void setDuration(String duration) {
    mDuration = duration;
  }

  public String getChannel() {
    return mChannel;
  }

  public void setChannel(String channel) {
    mChannel = channel;
  }

  public int getNumberViews() {
    return mNumberViews;
  }

  public void setNumberViews(int numberViews) {
    mNumberViews = numberViews;
  }

  public String getTime() {
    return mTime;
  }

  public void setTime(String time) {
    mTime = time;
  }
}
