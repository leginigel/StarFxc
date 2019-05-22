package com.stars.tv.sample;

import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.server.LeanCloudStorage;

import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;

public class DragSampleCloud {
  public static void addIQIYSampleList(List<IQiYiMovieBean> objectes){
    new LeanCloudStorage(CLOUD_HISTORY_CLASS).createSampleListByIQIY(objectes);
    new LeanCloudStorage(CLOUD_FAVORITE_CLASS).createSampleListByIQIY(objectes);
  }
}
