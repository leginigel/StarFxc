package com.stars.tv.server;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.bean.DragVideoBean;
import com.stars.tv.bean.IQiYiMovieBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;

public class LeanCloudStorage {
  private static final String DRAG_VIDEO_ID = "VideoID";
  private static final String DRAG_VIDEO_NAME = "VideoName";
  private static final String DRAG_VIDEO_PLAYURL = "VideoPlayUrl";
  private static final String DRAG_VIDEO_CURRENT_VIEW_ORDER = "VideoCurrentViewOrder";
  private static final String DRAG_VIDEO_LATEST_ORDER = "VideoLatestOrder";
  private static final String DRAG_VIDEO_DESCRIPTION = "VideoDescription";
  private static final String DRAG_VIDEO_IMAGEFILE = "VideoImageFile";
  private List<DragVideoBean> mHistoryList;
  private List<DragVideoBean> mFavoriteList;

  public interface cloudFetchListener {
    void done(List<AVObject> objects, AVException e);
  }

  public LeanCloudStorage(){
    mHistoryList = new ArrayList<>();
    mFavoriteList = new ArrayList<>();
  }

  public void storageFetchHistoryListner (cloudFetchListener cr){
    AVQuery<AVObject> query = new AVQuery<>(CLOUD_HISTORY_CLASS);
    query.whereExists(DRAG_VIDEO_ID);
    query.include(DRAG_VIDEO_IMAGEFILE);
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        cr.done(avObjects,e);
      }
    });
  }

  public void storageFetchFavoriteListener (cloudFetchListener cr){
    AVQuery<AVObject> query = new AVQuery<>(CLOUD_FAVORITE_CLASS);
    query.whereExists(DRAG_VIDEO_ID);
    query.include(DRAG_VIDEO_IMAGEFILE);
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        cr.done(avObjects, e);
      }
    });
  }

  public List<DragVideoBean> getHistoryList(){
    return mHistoryList;
  }

  public List<DragVideoBean> getFavoriteList(){
    return mFavoriteList;
  }

  public void assignToHistoryList(List<AVObject> objects){
    mHistoryList = assignToVideoList(objects);
  }

  public void assignToFavoriteList(List<AVObject> objects){
    mFavoriteList = assignToVideoList(objects);
  }

  private List<DragVideoBean> assignToVideoList(List<AVObject> objects){
    List<DragVideoBean> items = new ArrayList<>();
    DragVideoBean item;
    if ( objects != null && objects.size() > 0 ) {
      for ( AVObject obj : objects ) {
        item = new DragVideoBean();
        item.setVideoId(obj.getString(DRAG_VIDEO_ID));
        item.setVideoName(obj.getString(DRAG_VIDEO_NAME));
        item.setVideoPlayUrl(obj.getString(DRAG_VIDEO_PLAYURL));
        item.setVideoCurrentViewOrder(obj.getString(DRAG_VIDEO_CURRENT_VIEW_ORDER));
        item.setVideoLatestOrder(obj.getString(DRAG_VIDEO_LATEST_ORDER));
        item.setVideoDescription(obj.getString(DRAG_VIDEO_DESCRIPTION));
        item.setVideoImageFile(obj.getAVFile(DRAG_VIDEO_IMAGEFILE));
        items.add(item);
      }
    }
    return items;
  }

  private void saveData(AVObject obj){
    obj.saveInBackground(new SaveCallback() {
      @Override
      public void done(AVException e) {
        if ( e == null ) {
        }
        else{
        }
      }
    });
  }

  public int findHistory(DragVideoBean history){
    int idx = 0;
    if ( mHistoryList != null && mHistoryList.size() > 0) {
      for ( DragVideoBean item : mHistoryList ) {
        if ( item.getVideoId().compareTo(history.getVideoId()) == 0 ) {
          break;
        }
        idx++;
      }
    }
    else
      return -1;
    return idx;
  }

  private AVObject createHistory(){
    return new AVObject(CLOUD_HISTORY_CLASS);
  }

  private void updateHistory(AVObject history){
    AVQuery<AVObject> query = new AVQuery<>(CLOUD_HISTORY_CLASS);
    query.whereEqualTo(DRAG_VIDEO_ID,history.get(DRAG_VIDEO_ID));
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        if ( e == null ) {
          AVObject obj;
          if ( avObjects.size() > 0 ) {
            obj = AVObject.createWithoutData(CLOUD_HISTORY_CLASS, avObjects.get(0).getObjectId());
          }
          else{
            obj = createHistory();
          }
          saveData(obj);
        }
        else{
          if ( e.getCode() == AVException.OBJECT_NOT_FOUND ) {
            saveData(createHistory());
          }
        }
      }
    });
  }

  private AVObject IQiYToCloud(IQiYiMovieBean iQiy, String className){
    AVObject obj = new AVObject(className);
    obj.put(DRAG_VIDEO_ID, iQiy.getTvId());
    obj.put(DRAG_VIDEO_NAME, iQiy.getName());
    obj.put(DRAG_VIDEO_PLAYURL, iQiy.getPlayUrl());
    obj.put(DRAG_VIDEO_CURRENT_VIEW_ORDER, iQiy.getVideoCount());
    obj.put(DRAG_VIDEO_LATEST_ORDER, iQiy.getLatestOrder());
    obj.put(DRAG_VIDEO_DESCRIPTION, iQiy.getDescription());
    int i = iQiy.getImageUrl().lastIndexOf('/');
    String imgName = iQiy.getImageUrl().substring(i+1);
    AVFile file = new AVFile(imgName, iQiy.getImageUrl(), new HashMap<String,Object>());
    obj.put(DRAG_VIDEO_IMAGEFILE, file);
    return obj;
  }


  public void updateHistoryByIQIY(IQiYiMovieBean iQiy){
    updateHistory(IQiYToCloud(iQiy, CLOUD_HISTORY_CLASS));
  }

  public void createSampleListByIQIY(List<IQiYiMovieBean> iQiys, String className){
    List<AVObject> list = new ArrayList<>();
    for ( IQiYiMovieBean iQiy : iQiys ){
      list.add(IQiYToCloud(iQiy, className));
    }
    if ( list.size() > 0 ) {
      AVObject.saveAllInBackground(list, new SaveCallback() {
        @Override
        public void done(AVException e) {
        }
      });
    }
  }
}
