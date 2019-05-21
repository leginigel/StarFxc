package com.stars.tv.sample;

import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.server.LeanCloudStorage;

import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;

public class DragSampleCloud {
  public static void addIQIYSampleList(List<IQiYiMovieBean> objectes){
    LeanCloudStorage lcs = new LeanCloudStorage();
    lcs.createSampleListByIQIY(objectes, CLOUD_HISTORY_CLASS);
    lcs.createSampleListByIQIY(objectes, CLOUD_FAVORITE_CLASS);
  }
}
