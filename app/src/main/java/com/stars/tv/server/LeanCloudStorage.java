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

import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_ALBUM;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_COUNTER;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_CURRENT_VIEW_ORDER;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_DESCRIPTION;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_ID;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_LATEST_ORDER;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_NAME;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_PLAYURL;
import static com.stars.tv.utils.Constants.DRAG_VIDEO_TYPE;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_ANIMATION;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_CINEMA;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_DRAMA;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_LIVE;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_TVCHANNEL;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_VARIETY;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_YOUTUBE;

public class LeanCloudStorage {
  private List<DragVideoBean> mDragVideoList;
  private final String mClassName;

  public interface cloudFetchListener {
    void done(List<AVObject> objects, AVException e);
  }

  public interface cloudCheckVideoListener {
    void successed();
    void failed();
  }


  public LeanCloudStorage(String className){
    mClassName = className;
    mDragVideoList = new ArrayList<>();
  }

  public void storageFetchListener(cloudFetchListener cr){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereExists(DRAG_VIDEO_ALBUM);
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

  public List<DragVideoBean> assignToDragVideoList(List<AVObject> objects){
    mDragVideoList = assignToVideoList(objects);
    return mDragVideoList;
  }

  private List<DragVideoBean> assignToVideoList(List<AVObject> objects){
    List<DragVideoBean> items = new ArrayList<>();
    DragVideoBean item;
    if ( objects != null && objects.size() > 0 ) {
      for ( AVObject obj : objects ) {
        item = new DragVideoBean();
        item.setVideoType(obj.getString(DRAG_VIDEO_TYPE));
        item.setAlbumId(obj.getString(DRAG_VIDEO_ALBUM));
        item.setVideoId(obj.getString(DRAG_VIDEO_ID));
        item.setVideoName(obj.getString(DRAG_VIDEO_NAME));
        item.setVideoPlayUrl(obj.getString(DRAG_VIDEO_PLAYURL));
        item.setVideoCurrentViewOrder(obj.getString(DRAG_VIDEO_CURRENT_VIEW_ORDER));
        item.setVideoLatestOrder(obj.getString(DRAG_VIDEO_LATEST_ORDER));
        item.setVideoCounter(obj.getString(DRAG_VIDEO_COUNTER));
        item.setVideoDescription(obj.getString(DRAG_VIDEO_DESCRIPTION));
        item.setVideoImageUrl(obj.getString(DRAG_VIDEO_IMAGE_URL));
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

  public void VideoExistCheck(String album, cloudCheckVideoListener ccv){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(DRAG_VIDEO_ALBUM, album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if (e != null){
          ccv.successed();
        }
        else{
          ccv.failed();
        }
      }
    });
  }

  private AVObject createClass(){
    return new AVObject(mClassName);
  }
  private AVObject updateListByID(String id) {
    return AVObject.createWithoutData(mClassName, id);
  }

  public void updateVideoByiQiy(DragVideoBean iQiy){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(DRAG_VIDEO_ALBUM,iQiy.getAlbumId());
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
      if ( e == null ) {
        AVObject obj;
        if ( object != null ) {
          obj = assignIQiyToAVObject(iQiy, updateListByID(object.getObjectId()));
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

  private AVObject assignIQiyToAVObject(DragVideoBean bean, AVObject obj){
    obj.put(DRAG_VIDEO_TYPE, bean.getVideoType());
    obj.put(DRAG_VIDEO_ALBUM, bean.getAlbumId());
    obj.put(DRAG_VIDEO_ID, bean.getVideoId());
    obj.put(DRAG_VIDEO_NAME, bean.getVideoName());
    obj.put(DRAG_VIDEO_PLAYURL, bean.getVideoPlayUrl());
    obj.put(DRAG_VIDEO_IMAGE_URL, bean.getVideoImageUrl());
    obj.put(DRAG_VIDEO_CURRENT_VIEW_ORDER, bean.getVideoCurrentViewOrder());
    obj.put(DRAG_VIDEO_LATEST_ORDER, bean.getVideoLatestOrder());
    obj.put(DRAG_VIDEO_COUNTER, bean.getVideoCounter());
    obj.put(DRAG_VIDEO_DESCRIPTION, bean.getVideoDescription());

    return obj;
  }


  public DragVideoBean createIQiyDramaInfo (IQiYiMovieBean drama, IQiYiMovieBean episode, int chapter){
    DragVideoBean bean = new DragVideoBean();

    bean.setAlbumId(drama.getAlbumId());
    bean.setVideoCurrentViewOrder(String.valueOf(chapter));
    bean.setVideoCounter(drama.getVideoCount());
    bean.setVideoLatestOrder(drama.getLatestOrder());
    bean.setVideoDescription(drama.getDescription());
    bean.setVideoType(String.valueOf(VIDEO_TYPE_DRAMA));
    bean.setVideoId(episode.getTvId());
    bean.setVideoName(episode.getName());
    bean.setVideoImageUrl(episode.getImageUrl());
    bean.setVideoPlayUrl(episode.getPlayUrl());

    return bean;
  }

  public DragVideoBean createIQiyOtherInfoByType (IQiYiMovieBean iQiy, int types){
    DragVideoBean bean = new DragVideoBean();

    bean.setAlbumId(iQiy.getAlbumId());
    bean.setVideoCurrentViewOrder(null);
    bean.setVideoCounter(iQiy.getVideoCount());
    bean.setVideoLatestOrder(iQiy.getLatestOrder());
    bean.setVideoDescription(iQiy.getDescription());
    bean.setVideoType(String.valueOf(types));
    bean.setVideoId(iQiy.getTvId());
    bean.setVideoName(iQiy.getName());
    bean.setVideoImageUrl(iQiy.getImageUrl());
    bean.setVideoPlayUrl(iQiy.getPlayUrl());

    return bean;
  }
}
