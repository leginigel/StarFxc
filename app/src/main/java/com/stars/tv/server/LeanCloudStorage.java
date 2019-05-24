package com.stars.tv.server;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.bean.DragVideoBean;
import com.stars.tv.bean.IQiYiMovieBean;
import java.util.ArrayList;
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
  private List<DragVideoBean> mDragVideoList;
  private final String mClassName;

  public interface cloudFetchListener {
    void done(List<AVObject> objects, AVException e);
  }

  public LeanCloudStorage(String className){
    mClassName = className;
    mDragVideoList = new ArrayList<>();
  }

  public void storageFetchListener(cloudFetchListener cr){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereExists(DRAG_VIDEO_ID);
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        cr.done(avObjects,e);
      }
    });
  }

  public List<DragVideoBean> getVideoList(){
    return mDragVideoList;
  }

  public void assignToDragVideoList(List<AVObject> objects){
    mDragVideoList = assignToVideoList(objects);
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
        item.setVideoImageFile(obj.getString(DRAG_VIDEO_IMAGEFILE));
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

  public int isVideoExist(DragVideoBean video){
    int idx = 0;
    if ( mDragVideoList != null && mDragVideoList.size() > 0) {
      for ( DragVideoBean item : mDragVideoList ) {
        if ( item.getVideoId().compareTo(video.getVideoId()) == 0 ) {
          break;
        }
        idx++;
      }
    }
    else
      return -1;
    return idx;
  }

  private AVObject createClass(){
    return new AVObject(mClassName);
  }
  private AVObject updateListByID(String id) {
    return AVObject.createWithoutData(mClassName, id);
  }

  public void updateVideoByiQiy(IQiYiMovieBean iQiy){
    AVQuery<AVObject> query = new AVQuery<>(CLOUD_HISTORY_CLASS);
    query.whereEqualTo(DRAG_VIDEO_ID,iQiy.getTvId());
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          AVObject obj;
          if ( object != null ) {
            obj = updateListByID(object.getObjectId());
            saveData(assignIQiyToAVObject(iQiy, obj));
          }
          else{
            obj = createClass();
          }
          saveData(obj);
        }
        else{
          if ( e.getCode() == AVException.OBJECT_NOT_FOUND ) {
            saveData(createClass());
          }
        }
      }
    });
  }

  private AVObject IQiYToCloud(IQiYiMovieBean iQiy){
    return assignIQiyToAVObject(iQiy, new AVObject(mClassName));
  }

  private AVObject assignIQiyToAVObject(IQiYiMovieBean iQiy, AVObject obj){
    obj.put(DRAG_VIDEO_ID, iQiy.getTvId());
    obj.put(DRAG_VIDEO_NAME, iQiy.getName());
    obj.put(DRAG_VIDEO_PLAYURL, iQiy.getPlayUrl());
    obj.put(DRAG_VIDEO_CURRENT_VIEW_ORDER, iQiy.getVideoCount());
    obj.put(DRAG_VIDEO_LATEST_ORDER, iQiy.getLatestOrder());
    obj.put(DRAG_VIDEO_DESCRIPTION, iQiy.getDescription());
    obj.put(DRAG_VIDEO_IMAGEFILE, iQiy.getImageUrl());
    return obj;
  }

  public void createSampleListByIQIY(List<IQiYiMovieBean> iQiys){
    List<AVObject> list = new ArrayList<>();
    for ( IQiYiMovieBean iQiy : iQiys ){
      list.add(IQiYToCloud(iQiy));
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
