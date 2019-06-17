package com.stars.tv.server;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiVideoBaseInfoBean;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.youtube.data.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.EXT_VIDEO_ALBUM;
import static com.stars.tv.utils.Constants.EXT_VIDEO_COUNT;
import static com.stars.tv.utils.Constants.EXT_VIDEO_CURRENT_VIEW_ORDER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_ID;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_LATEST_ORDER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_NAME;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYPOSITION;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYURL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;
import static com.stars.tv.utils.Constants.STAR_CLOUD_ID;
import static com.stars.tv.utils.Constants.STAR_CLOUD_KEY;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_TVSERIES;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_YOUTUBE;

public class LeanCloudStorage {
  private List<ExtVideoBean> mExtVideoList;
  private final String mClassName;
  private static boolean isInitialDone=false;

  public interface cloudCheckVideoListener {
    void succeed(ExtVideoBean bean);
    void failed();
  }

  public LeanCloudStorage(String className){
    mClassName = className;
    mExtVideoList = new ArrayList<>();
  }

  public static void initLeanCloudStorage(Context context){
    boolean tmp = NetUtil.isConnected();
    if ( tmp && !isInitialDone) {
      AVOSCloud.initialize(context, STAR_CLOUD_ID, STAR_CLOUD_KEY);
      AVOSCloud.useAVCloudCN();
      AVOSCloud.setDebugLogEnabled(true);
      isInitialDone = true;
    }
    else if ( !tmp ){
      isInitialDone = false;
    }
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
        item.setVideoType(obj.getInt(EXT_VIDEO_TYPE));
        item.setAlbumId(obj.getString(EXT_VIDEO_ALBUM));
        item.setVideoId(obj.getString(EXT_VIDEO_ID));
        item.setVideoName(obj.getString(EXT_VIDEO_NAME));
        item.setVideoPlayUrl(obj.getString(EXT_VIDEO_PLAYURL));
        item.setAlbumImageUrl(obj.getString(EXT_VIDEO_IMAGE_URL));
        item.setVideoCurrentViewOrder(obj.getInt(EXT_VIDEO_CURRENT_VIEW_ORDER));
        item.setVideoLatestOrder(obj.getInt(EXT_VIDEO_LATEST_ORDER));
        item.setVideoCount(obj.getInt(EXT_VIDEO_COUNT));
        item.setVideoPlayPosition(obj.getInt(EXT_VIDEO_PLAYPOSITION));
        items.add(item);
      }
    }
    return items;
  }

  private void saveData(AVObject obj, SaveCallback cb){
    obj.saveInBackground(cb);
  }

  private ExtVideoBean assignAVObjectToVideoBean(AVObject obj){
    ExtVideoBean bean = new ExtVideoBean();
    bean.setVideoType(obj.getInt(EXT_VIDEO_TYPE));
    bean.setAlbumId(obj.getString(EXT_VIDEO_ID));
    bean.setVideoId(obj.getString(EXT_VIDEO_ID));
    bean.setVideoName(obj.getString(EXT_VIDEO_NAME));
    bean.setVideoPlayUrl(obj.getString(EXT_VIDEO_PLAYURL));
    bean.setAlbumImageUrl(obj.getString(EXT_VIDEO_IMAGE_URL));
    bean.setVideoCount(obj.getInt(EXT_VIDEO_COUNT));
    bean.setVideoLatestOrder(obj.getInt(EXT_VIDEO_LATEST_ORDER));
    bean.setVideoCurrentViewOrder(obj.getInt(EXT_VIDEO_CURRENT_VIEW_ORDER));
    bean.setVideoPlayPosition(obj.getInt(EXT_VIDEO_PLAYPOSITION));
    return bean;
  }

  private void VideoCheckListener(String album,
                                  cloudCheckVideoListener ccv){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(EXT_VIDEO_ALBUM, album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          if ( object != null ) {
            ccv.succeed(assignAVObjectToVideoBean(object));
          }
          else
            ccv.failed();
        } else {
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
            obj = assignBeanToAVObject(iQiy, updateListByID(object.getObjectId()));
          }
          else{
            obj = assignBeanToAVObject(iQiy, createClass());
          }
          saveData(obj, cb);
        }
        else{
          if ( e.getCode() == AVException.OBJECT_NOT_FOUND ) {
            saveData(assignBeanToAVObject(iQiy, createClass()), cb);
          }
        }
      }
    });
  }

  private void updateVideoByYoutube(YouTubeVideo yt, SaveCallback cb){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(EXT_VIDEO_ALBUM,yt.getId());
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          AVObject obj;
          if ( object != null ) {
            obj = assignYoutubeToAVObject(yt, updateListByID(object.getObjectId()));
          }
          else{
            obj = assignYoutubeToAVObject(yt, createClass());
          }
          saveData(obj, cb);
        }
        else{
          if ( e.getCode() == AVException.OBJECT_NOT_FOUND ) {
            saveData(assignYoutubeToAVObject(yt, createClass()), cb);
          }
        }
      }
    });
  }

  private void removeVideoByAlbum(String album, DeleteCallback dr){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(EXT_VIDEO_ALBUM,album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          object.deleteInBackground(dr);
        }
        else{
        }
      }
    });
  }

  private AVObject assignBeanToAVObject(ExtVideoBean bean, AVObject obj){
    obj.put(EXT_VIDEO_TYPE, bean.getVideoType());
    obj.put(EXT_VIDEO_ALBUM, bean.getAlbumId());
    obj.put(EXT_VIDEO_ID, bean.getVideoId());
    obj.put(EXT_VIDEO_NAME, bean.getVideoName());
    obj.put(EXT_VIDEO_PLAYURL, bean.getVideoPlayUrl());
    obj.put(EXT_VIDEO_IMAGE_URL, bean.getAlbumImageUrl());
    obj.put(EXT_VIDEO_CURRENT_VIEW_ORDER, bean.getVideoCurrentViewOrder());
    obj.put(EXT_VIDEO_LATEST_ORDER, bean.getVideoLatestOrder());
    obj.put(EXT_VIDEO_COUNT, bean.getVideoCount());
    obj.put(EXT_VIDEO_PLAYPOSITION, bean.getVideoPlayPosition());
    return obj;
  }

  private AVObject assignYoutubeToAVObject(YouTubeVideo yt, AVObject obj){
    ExtVideoBean bean = new ExtVideoBean();
    bean.setVideoId(yt.getId());
    bean.setAlbumId(yt.getId());
    bean.setVideoName(yt.getTitle());
    bean.setVideoType(80);
    bean.setAlbumImageUrl("https://i.ytimg.com/vi/"+ yt.getId() +"/0.jpg");
    return ( assignBeanToAVObject(bean, obj));
  }

  // IQIY
  public static void updateIQiyHistory(ExtVideoBean iQiy, SaveCallback cb){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).updateVideoByiQiy(iQiy, cb);
    }
  }

  public static void removeIQiyHistory(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void updateIQiyFavorite(ExtVideoBean bean, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).updateVideoByiQiy(bean, cb);
    }
  }

  public static void removeIQiyFavorite(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void getIQiyHistoryListener(String album, cloudCheckVideoListener ccv){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).VideoCheckListener(album, ccv);
    }
  }

  public static void getIQiyFavoriteListener(String album, cloudCheckVideoListener ccv){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).VideoCheckListener(album, ccv);
    }
  }

  // Youtube
  public static void updateYoutubeHistory(YouTubeVideo yt, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).updateVideoByYoutube(yt, cb);
    }
  }

  public static void removeYoutubeHistory(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void updateYoutubeFavorite(YouTubeVideo yt, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).updateVideoByYoutube(yt, cb);
    }
  }

  public static void removeYoutubeFavorite(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void getYoutubeHistoryListener(String album, cloudCheckVideoListener ccv){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).VideoCheckListener(album, ccv);
    }
  }

  public static void getYoutubeFavoriteListener(String album, cloudCheckVideoListener ccv){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).VideoCheckListener(album, ccv);
    }
  }
}
