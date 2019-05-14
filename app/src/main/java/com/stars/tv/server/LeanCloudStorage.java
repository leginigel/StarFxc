package com.stars.tv.server;

import android.app.Application;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.stars.tv.bean.DragVideoBean;

import java.util.ArrayList;
import java.util.List;

public class LeanCloudStorage extends Application {
  public final String DRAG_VIDEO_NAME = "VIDEO_NAME";
  public final String DRAG_VIDEO_PLAYURL = "VIDEO_PLAY_URL";
  public final String DRAG_VIDEO_SCORE = "VIDEO_SCORE";
  public final String DRAG_VIDEO_DESCRIPTION = "VIDEO_DESCRIPTION";
  public final String DRAG_VIDEO_IMAGEFILE = "VIDEO_IMAGE_FILE";

  private final String StarTVAPPID;
  private final String StarTVAPPKEY;
  private final String mStarClassName;
  private List<DragVideoBean> mStarVideos;

  public interface CloudBackgroundResult{
    void Done(AVException e);
  }

  public LeanCloudStorage(String className, String id, String key){
    StarTVAPPID = id;
    StarTVAPPKEY = key;
    mStarClassName = className;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(this, StarTVAPPID, StarTVAPPKEY);
    AVOSCloud.setDebugLogEnabled(true);
  }

  public List<DragVideoBean> getVideoList(){
    return mStarVideos;
  }

  public void getVideoListFromStorage(CloudBackgroundResult cr){
    AVQuery<AVObject> query = new AVQuery<>(mStarClassName);

    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        if ( e == null ) {
          List<DragVideoBean> items = new ArrayList<>();
          DragVideoBean item = new DragVideoBean();
          for (AVObject obj:avObjects){
            item.setVideoId(obj.getObjectId());
            item.setVideoName(obj.getString(DRAG_VIDEO_NAME));
            item.setVideoPlayUrl(obj.getString(DRAG_VIDEO_PLAYURL));
            item.setVideoScore(obj.getString(DRAG_VIDEO_SCORE));
            item.setVideoDescription(obj.getString(DRAG_VIDEO_DESCRIPTION));
            AVFile avf = obj.getAVFile(DRAG_VIDEO_IMAGEFILE);
            item.setVideoImageFile(avf);
            items.add(item);
          }
          mStarVideos = items;
        }
        cr.Done(e);
      }
    });
  }

  public void updateVideoListToStorage(){

  }


}
