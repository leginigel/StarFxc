package com.stars.tv.bean;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVFile;
import java.io.Serializable;

public class DragVideoBean implements Serializable {
  private String mVideoId;
  private String mVideoName;
  private String mVideoPlayUrl;
  private String mVideoScore;
  private String mVideoDescription;
  private AVFile mVideoImageFile;

  @NonNull
  @Override
  public String toString() {
    return this.getClass().getName() + "{" +
      "Id=" + mVideoId +
      ", Name=" + mVideoName +
      ", PlayUrl=" + mVideoPlayUrl +
      ", Score=" + mVideoScore +
      ", Description=" + mVideoDescription +
      ", ImageId=" + mVideoImageFile.getName() +
      "}";
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

  public String getVideoScore() {
    return mVideoScore;
  }

  public void setVideoScore(String videoScore) {
    mVideoScore = videoScore;
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
