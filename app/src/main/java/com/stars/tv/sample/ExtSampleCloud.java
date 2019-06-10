package com.stars.tv.sample;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.bean.IQiYiMovieBean;

import java.util.ArrayList;
import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.EXT_VIDEO_ALBUM;
import static com.stars.tv.utils.Constants.EXT_VIDEO_COUNTER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_CURRENT_VIEW_ORDER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_DESCRIPTION;
import static com.stars.tv.utils.Constants.EXT_VIDEO_ID;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_LATEST_ORDER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_NAME;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYURL;

public class ExtSampleCloud {
  public static void addIQIYSampleList(List<IQiYiMovieBean> objectes){
    createSampleListByIQIY(objectes, CLOUD_HISTORY_CLASS);
    createSampleListByIQIY(objectes, CLOUD_FAVORITE_CLASS);
  }

  public static void createSampleListByIQIY(List<IQiYiMovieBean> iQiys, String classname){
    List<AVObject> list = new ArrayList<>();
    for ( IQiYiMovieBean iQiy : iQiys ){
      list.add(IQiYToCloud(iQiy, classname));
    }
    if ( list.size() > 0 ) {
      AVObject.saveAllInBackground(list, new SaveCallback() {
        @Override
        public void done(AVException e) {
        }
      });
    }
  }

  private static AVObject IQiYToCloud(IQiYiMovieBean iQiy, String classname){
    AVObject obj = new AVObject(classname);
    obj.put(EXT_VIDEO_ALBUM, iQiy.getAlbumId());
    obj.put(EXT_VIDEO_ID, iQiy.getTvId());
    obj.put(EXT_VIDEO_NAME, iQiy.getName());
    obj.put(EXT_VIDEO_PLAYURL, iQiy.getPlayUrl());
    obj.put(EXT_VIDEO_IMAGE_URL, iQiy.getImageUrl());
    obj.put(EXT_VIDEO_CURRENT_VIEW_ORDER, null);
    obj.put(EXT_VIDEO_LATEST_ORDER, iQiy.getLatestOrder());
    obj.put(EXT_VIDEO_COUNTER, iQiy.getVideoCount());
    obj.put(EXT_VIDEO_DESCRIPTION, iQiy.getDescription());
    return obj;
  }
}
