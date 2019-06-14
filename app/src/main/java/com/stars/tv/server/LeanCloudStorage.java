package com.stars.tv.server;

import android.content.Context;
import android.util.Log;

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
import com.stars.tv.utils.NetUtil;
import com.stars.tv.youtube.data.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_HISTORY_CLASS;
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
import static com.stars.tv.utils.Constants.STAR_CLOUD_ID;
import static com.stars.tv.utils.Constants.STAR_CLOUD_KEY;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_TVSERIES;
import static com.stars.tv.utils.Constants.VIDEO_TYPE_YOUTUBE;

public class LeanCloudStorage {
  private List<ExtVideoBean> mExtVideoList;
  private final String mClassName;
  private static boolean isInitialDone=false;

  public interface cloudCheckVideoListener {
    void succeed();
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

  private void VideoFavoriteCheckListener(String album,
                                                 cloudCheckVideoListener ccv){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.whereEqualTo(EXT_VIDEO_ALBUM, album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          if ( object != null )
            ccv.succeed();
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
    obj.put(EXT_VIDEO_IMAGE_URL, bean.getVideoImageUrl());
    obj.put(EXT_VIDEO_CURRENT_VIEW_ORDER, bean.getVideoCurrentViewOrder());
    obj.put(EXT_VIDEO_LATEST_ORDER, bean.getVideoLatestOrder());
    obj.put(EXT_VIDEO_COUNTER, bean.getVideoCounter());
    obj.put(EXT_VIDEO_DESCRIPTION, bean.getVideoDescription());

    return obj;
  }

  private AVObject assignYoutubeToAVObject(YouTubeVideo yt, AVObject obj){
    ExtVideoBean bean = new ExtVideoBean();
    bean.setVideoId(yt.getId());
    bean.setAlbumId(yt.getId());
    bean.setVideoName(yt.getTitle());
    bean.setVideoType(VIDEO_TYPE_YOUTUBE);
    bean.setVideoImageUrl("https://i.ytimg.com/vi/"+ yt.getId() +"/0.jpg");
    return ( assignBeanToAVObject(bean, obj));
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
    bean.setVideoImageUrl(IQiy.getImageUrl());
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
    if ( NetUtil.isConnected() ) {
      LeanCloudStorage storage = new LeanCloudStorage(CLOUD_HISTORY_CLASS);
      storage.updateVideoByiQiy(storage.createIQiyOtherInfoByType(iQiy, types), cb);
    }
  }

  public static void updateIQiyHistory(IQiYiMovieBean iQiy,
                                       IQiYiMovieBean episode, int chapter, SaveCallback cb) {
    if ( NetUtil.isConnected() ) {
      LeanCloudStorage storage = new LeanCloudStorage(CLOUD_HISTORY_CLASS);
      storage.updateVideoByiQiy(storage.createIQiyTVSeriesInfo(iQiy, episode, chapter), cb);
    }
  }

  public static void updateIQiyFavorite(IQiYiMovieBean iQiy, String types, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      LeanCloudStorage storage = new LeanCloudStorage(CLOUD_FAVORITE_CLASS);
      storage.updateVideoByiQiy(storage.createIQiyOtherInfoByType(iQiy, types), cb);
    }
  }

  public static void updateIQiyBeanFavorite(ExtVideoBean bean, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).updateVideoByiQiy(bean, cb);
    }
  }

  public static void removeIQiyFavorite(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void removeYoutubeFavorite(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void removeIQiyHistory(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void removeYoutubeHistory(String album, DeleteCallback dr){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).removeVideoByAlbum(album, dr);
    }
  }

  public static void updateYoutubeHistory(YouTubeVideo yt, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).updateVideoByYoutube(yt, cb);
    }
  }

  public static void updateYoutubeFavorite(YouTubeVideo yt, SaveCallback cb){
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).updateVideoByYoutube(yt, cb);
    }
  }

  public static void YoutubeFavoriteCheckListener(String album, cloudCheckVideoListener ccv){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).VideoFavoriteCheckListener(album, ccv);
    }
  }

  public static void IQiyFavoriteCheckListener(String album, cloudCheckVideoListener ccv){
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).VideoFavoriteCheckListener(album, ccv);
    }
  }

}
