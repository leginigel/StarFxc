package com.stars.tv.bean;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVFile;
import java.io.Serializable;

public class DragVideoBean implements Serializable {
  private String mCloudId;
  private String mVideoId;
  private String mVideoName;
  private String mVideoPlayUrl;
  private String mVideoCurrentViewOrder;
  private String mVideoLatestOrder;
  private String mVideoDescription;
  private AVFile mVideoImageFile;

  @NonNull
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
      "CloudId=" + mCloudId +
      ", VideoId=" + mVideoId +
      ", Name=" + mVideoName +
      ", PlayUrl=" + mVideoPlayUrl +
      ", CurrentViewOrder=" + mVideoCurrentViewOrder +
      ", LatestOrder=" + mVideoLatestOrder +
      ", Description=" + mVideoDescription +
      ", ImageUrl=" + mVideoImageFile.getUrl()+
      "}";
  }
  public String getCloudId() {
    return mCloudId;
  }

  public void setCloudId(String cloudId) {
    mCloudId = cloudId;
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

  public AVFile getVideoImageFile() {
    return mVideoImageFile;
  }

  public void setVideoImageFile(AVFile videoImageFile) {
    mVideoImageFile = videoImageFile;
  }
}
