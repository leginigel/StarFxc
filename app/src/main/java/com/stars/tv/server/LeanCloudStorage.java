package com.stars.tv.server;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.bean.ExtVideoBean;
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
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_TVSERIES;

public class LeanCloudStorage {
  private List<ExtVideoBean> mExtVideoList;
  private final String mClassName;

  public interface cloudCheckVideoListener {
    void succeed();
    void failed();
  }

  public LeanCloudStorage(String className){
    mClassName = className;
    mExtVideoList = new ArrayList<>();
  }

  public void storageFetchListener(FindCallback<AVObject> cr){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereExists(EXT_VIDEO_ALBUM);
    query.findInBackground(cr);
  }

  public List<ExtVideoBean> getVideoList(){
    return mExtVideoList;
  }

  public List<ExtVideoBean> assignToExtVideoList(List<AVObject> objects){
    mExtVideoList = assignToVideoList(objects);
    return mExtVideoList;
  }

  private List<ExtVideoBean> assignToVideoList(List<AVObject> objects){
    List<ExtVideoBean> items = new ArrayList<>();
    ExtVideoBean item;
    if ( objects != null && objects.size() > 0 ) {
      for ( AVObject obj : objects ) {
        item = new ExtVideoBean();
        item.setVideoType(obj.getString(EXT_VIDEO_TYPE));
        item.setAlbumId(obj.getString(EXT_VIDEO_ALBUM));
        item.setVideoId(obj.getString(EXT_VIDEO_ID));
        item.setVideoName(obj.getString(EXT_VIDEO_NAME));
        item.setVideoPlayUrl(obj.getString(EXT_VIDEO_PLAYURL));
        item.setVideoCurrentViewOrder(obj.getString(EXT_VIDEO_CURRENT_VIEW_ORDER));
        item.setVideoLatestOrder(obj.getString(EXT_VIDEO_LATEST_ORDER));
        item.setVideoCounter(obj.getString(EXT_VIDEO_COUNTER));
        item.setVideoDescription(obj.getString(EXT_VIDEO_DESCRIPTION));
        item.setVideoImageUrl(obj.getString(EXT_VIDEO_IMAGE_URL));
        items.add(item);
      }
    }
    return items;
  }

  private void saveData(AVObject obj, SaveCallback cb){
    obj.saveInBackground(cb);
  }

  public static void VideoFavoriteCheckListener(String album, cloudCheckVideoListener ccv){
    AVQuery<AVObject> query = new AVQuery<>(CLOUD_FAVORITE_CLASS);
    query.whereEqualTo(EXT_VIDEO_ALBUM, album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if (e == null){
          if ( object != null )
            ccv.succeed();
          else
            ccv.failed();
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

  private void updateVideoByiQiy(ExtVideoBean iQiy, SaveCallback cb){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(EXT_VIDEO_ALBUM,iQiy.getAlbumId());
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          AVObject obj;
          if ( object != null ) {
            obj = assignIQiyToAVObject(iQiy, updateListByID(object.getObjectId()));
          }
          else{
            obj = assignIQiyToAVObject(iQiy, createClass());
          }
          saveData(obj, cb);
        }
        else{
          if ( e.getCode() == AVException.OBJECT_NOT_FOUND ) {
            saveData(assignIQiyToAVObject(iQiy, createClass()), cb);
          }
        }
      }
    });
  }

  private void removeVideoByIQiy(String album, DeleteCallback cr){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(EXT_VIDEO_ALBUM,album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          object.deleteInBackground(cr);
        }
        else{
        }
      }
    });
  }

  private AVObject assignIQiyToAVObject(ExtVideoBean bean, AVObject obj){
    obj.put(EXT_VIDEO_TYPE, bean.getVideoType());
    obj.put(EXT_VIDEO_ALBUM, bean.getAlbumId());
    obj.put(EXT_VIDEO_ID, bean.getVideoId());
    obj.put(EXT_VIDEO_NAME, bean.getVideoName());
    obj.put(EXT_VIDEO_PLAYURL, bean.getVideoPlayUrl());
    obj.put(EXT_VIDEO_IMAGE_URL, bean.getVideoImageUrl());
    obj.put(EXT_VIDEO_CURRENT_VIEW_ORDER, bean.getVideoCurrentViewOrder());
    obj.put(EXT_VIDEO_LATEST_ORDER, bean.getVideoLatestOrder());
    obj.put(EXT_VIDEO_COUNTER, bean.getVideoCounter());
    obj.put(EXT_VIDEO_DESCRIPTION, bean.getVideoDescription());

    return obj;
  }


  private ExtVideoBean createIQiyTVSeriesInfo(IQiYiMovieBean IQiy, IQiYiMovieBean episode, int chapter){
    ExtVideoBean bean = new ExtVideoBean();

    bean.setAlbumId(IQiy.getAlbumId());
    bean.setVideoCurrentViewOrder(String.valueOf(chapter));
    bean.setVideoCounter(IQiy.getVideoCount());
    bean.setVideoLatestOrder(IQiy.getLatestOrder());
    bean.setVideoDescription(IQiy.getDescription());
    bean.setVideoType(VIDEO_TYPE_TVSERIES);
    bean.setVideoId(episode.getTvId());
    bean.setVideoName(episode.getName());
    bean.setVideoImageUrl(episode.getImageUrl());
    bean.setVideoPlayUrl(episode.getPlayUrl());

    return bean;
  }

  private ExtVideoBean createIQiyOtherInfoByType (IQiYiMovieBean iQiy, String types){
    ExtVideoBean bean = new ExtVideoBean();

    bean.setAlbumId(iQiy.getAlbumId());
    bean.setVideoCurrentViewOrder(null);
    bean.setVideoCounter(iQiy.getVideoCount());
    bean.setVideoLatestOrder(iQiy.getLatestOrder());
    bean.setVideoDescription(iQiy.getDescription());
    bean.setVideoType(types);
    bean.setVideoId(iQiy.getTvId());
    bean.setVideoName(iQiy.getName());
    bean.setVideoImageUrl(iQiy.getImageUrl());
    bean.setVideoPlayUrl(iQiy.getPlayUrl());

    return bean;
  }

  public static void updateIQiyHistory(IQiYiMovieBean iQiy, String types, SaveCallback cb){
    LeanCloudStorage storage = new LeanCloudStorage(CLOUD_HISTORY_CLASS);
    ExtVideoBean bean;
    bean = storage.createIQiyOtherInfoByType(iQiy, types);
    storage.updateVideoByiQiy(bean, cb);
  }

  public static void updateIQiyHistory(IQiYiMovieBean iQiy,
                                       IQiYiMovieBean episode, int chapter, SaveCallback cb) {
    LeanCloudStorage storage = new LeanCloudStorage(CLOUD_HISTORY_CLASS);
    ExtVideoBean bean;
    bean = storage.createIQiyTVSeriesInfo(iQiy, episode, chapter);
    storage.updateVideoByiQiy(bean, cb);
  }

  public static void updateIQiyFavorite(IQiYiMovieBean iQiy, String types, SaveCallback cb){
    LeanCloudStorage storage = new LeanCloudStorage(CLOUD_FAVORITE_CLASS);
    ExtVideoBean bean;
    bean = storage.createIQiyOtherInfoByType(iQiy, types);
    storage.updateVideoByiQiy(bean, cb);
  }

  public static void updateBeanFavorite(ExtVideoBean bean, SaveCallback cb){
    new LeanCloudStorage(CLOUD_FAVORITE_CLASS).updateVideoByiQiy(bean, cb);

  }

  public static void removeIQiyFavorite(String album, DeleteCallback dr){
    new LeanCloudStorage(CLOUD_FAVORITE_CLASS).removeVideoByIQiy(album, dr);
  }

}
