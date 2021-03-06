package com.stars.tv.server;

import android.accounts.NetworkErrorException;
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
import com.stars.tv.utils.NetUtil;
import com.stars.tv.youtube.data.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.EXT_VIDEO_ALBUM;
import static com.stars.tv.utils.Constants.EXT_VIDEO_CHANNEL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_COUNT;
import static com.stars.tv.utils.Constants.EXT_VIDEO_CURRENT_VIEW_ORDER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_DURATION;
import static com.stars.tv.utils.Constants.EXT_VIDEO_ID;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_LATEST_ORDER;
import static com.stars.tv.utils.Constants.EXT_VIDEO_NAME;
import static com.stars.tv.utils.Constants.EXT_VIDEO_NUMBERVIEWS;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYPOSITION;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYURL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TIME;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;
import static com.stars.tv.utils.Constants.STAR_CLOUD_ID;
import static com.stars.tv.utils.Constants.STAR_CLOUD_KEY;

public class LeanCloudStorage {
  private List<ExtVideoBean> mExtVideoList;
  private final String mClassName;
  private static boolean isInitialDone=false;

  public interface VideoSeeker {
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
    query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    query.whereExists(EXT_VIDEO_ALBUM);
    query.findInBackground(cr);
  }

  public List<ExtVideoBean> getVideoList(){
    return mExtVideoList;
  }

  public List<ExtVideoBean> assignToExtVideoList(List<AVObject> objects){
    return mExtVideoList = assignToVideoList(objects);
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
        item.setAlbumImageUrl(obj.getString(EXT_VIDEO_IMAGE_URL));
        if ( mClassName.compareTo(CLOUD_YT_HISTORY_CLASS) == 0 ||
            mClassName.compareTo(CLOUD_YT_FAVORITE_CLASS) == 0) {
            item.setChannel(obj.getString(EXT_VIDEO_CHANNEL));
            item.setNumberViews(obj.getInt(EXT_VIDEO_NUMBERVIEWS));
            item.setTime(obj.getString(EXT_VIDEO_TIME));
            item.setDuration(obj.getString(EXT_VIDEO_DURATION));
        }
        else{
            item.setVideoPlayUrl(obj.getString(EXT_VIDEO_PLAYURL));
            item.setVideoCurrentViewOrder(obj.getInt(EXT_VIDEO_CURRENT_VIEW_ORDER));
            item.setVideoLatestOrder(obj.getInt(EXT_VIDEO_LATEST_ORDER));
            item.setVideoCount(obj.getInt(EXT_VIDEO_COUNT));
            item.setVideoPlayPosition(obj.getInt(EXT_VIDEO_PLAYPOSITION));
        }
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
    bean.setDuration(obj.getString(EXT_VIDEO_DURATION));
    bean.setTime(obj.getString(EXT_VIDEO_TIME));
    bean.setChannel(obj.getString(EXT_VIDEO_CHANNEL));
    bean.setNumberViews(obj.getInt(EXT_VIDEO_NUMBERVIEWS));
    return bean;
  }

  private void VideoSeekerListener(String album,
                                   VideoSeeker ccv){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    query.whereEqualTo(EXT_VIDEO_ALBUM, album);
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          if ( object != null ) {
            ccv.succeed(assignAVObjectToVideoBean(object));
          }
          else
            ccv.succeed(null);
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

  private void updateVideoByiQiy(ExtVideoBean bean, SaveCallback cb){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    query.whereEqualTo(EXT_VIDEO_ALBUM,bean.getAlbumId());
    query.getFirstInBackground(new GetCallback<AVObject>() {
      @Override
      public void done(AVObject object, AVException e) {
        if ( e == null ) {
          AVObject obj;
          if ( object != null ) {
            obj = assignBeanToAVObject(bean, updateListByID(object.getObjectId()));
          }
          else{
            obj = assignBeanToAVObject(bean, createClass());
          }
          saveData(obj, cb);
        }
        else{
          if ( e.getCode() == AVException.OBJECT_NOT_FOUND ) {
            saveData(assignBeanToAVObject(bean, createClass()), cb);
          }
        }
      }
    });
  }

  private void updateVideoByYoutube(YouTubeVideo yt, SaveCallback cb){
    AVQuery<AVObject> query = new AVQuery<>(mClassName);
    query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
    query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
    obj.put(EXT_VIDEO_TYPE, 80);
    obj.put(EXT_VIDEO_ALBUM, yt.getId());
    obj.put(EXT_VIDEO_ID, yt.getId());
    obj.put(EXT_VIDEO_NAME, yt.getTitle());
    obj.put(EXT_VIDEO_CHANNEL, yt.getChannel());
    obj.put(EXT_VIDEO_NUMBERVIEWS, yt.getNumber_views());
    obj.put(EXT_VIDEO_TIME, yt.getTime());
    obj.put(EXT_VIDEO_DURATION, yt.getDuration());
    obj.put(EXT_VIDEO_PLAYPOSITION, 0);
    obj.put(EXT_VIDEO_IMAGE_URL, "https://i.ytimg.com/vi/"+ yt.getId() +"/0.jpg");
    return obj;
  }

  // IQIY
  public static void updateIQiyHistory(ExtVideoBean bean,
                                       SaveCallback cb) throws NetworkErrorException{
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).updateVideoByiQiy(bean, cb);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void removeIQiyHistory(String album,
                                       DeleteCallback dr) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).removeVideoByAlbum(album, dr);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void updateIQiyFavorite(ExtVideoBean bean,
                                        SaveCallback cb) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).updateVideoByiQiy(bean, cb);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void removeIQiyFavorite(String album,
                                        DeleteCallback dr) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).removeVideoByAlbum(album, dr);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void getIQiyHistoryListener(String album,
                                            VideoSeeker ccv) throws NetworkErrorException{
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_HISTORY_CLASS).VideoSeekerListener(album, ccv);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void getIQiyFavoriteListener(String album,
                                             VideoSeeker ccv) throws NetworkErrorException{
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_FAVORITE_CLASS).VideoSeekerListener(album, ccv);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  // Youtube
  public static void updateYoutubeHistory(YouTubeVideo yt,
                                          SaveCallback cb) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).updateVideoByYoutube(yt, cb);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void removeYoutubeHistory(String album,
                                          DeleteCallback dr) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).removeVideoByAlbum(album, dr);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void updateYoutubeFavorite(YouTubeVideo yt,
                                           SaveCallback cb) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).updateVideoByYoutube(yt, cb);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void removeYoutubeFavorite(String album,
                                           DeleteCallback dr) throws NetworkErrorException{
    if ( NetUtil.isConnected() ) {
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).removeVideoByAlbum(album, dr);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void getYoutubeHistoryListener(String album,
                                               VideoSeeker ccv) throws NetworkErrorException{
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_YT_HISTORY_CLASS).VideoSeekerListener(album, ccv);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }

  public static void getYoutubeFavoriteListener(String album,
                                                VideoSeeker ccv) throws NetworkErrorException{
    if ( NetUtil.isConnected() ){
      new LeanCloudStorage(CLOUD_YT_FAVORITE_CLASS).VideoSeekerListener(album, ccv);
    }
    else{
      throw new NetworkErrorException("Network Inactivity");
    }
  }
}
