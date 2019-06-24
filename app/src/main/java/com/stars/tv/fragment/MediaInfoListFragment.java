package com.stars.tv.fragment;

//Alice@20190424

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.stars.tv.R;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.server.LeanCloudStorage;

import static com.stars.tv.utils.Constants.EXT_VIDEO_ALBUM;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_PLAYURL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;

public class MediaInfoListFragment extends Fragment {
  private TextView mName,mScore;
  private Button mplot;
  private Button mFullScreen, mFavorite;
  private ViewGroup viewGroup;
  private ListView mMediaListView;
  private MediaAdapter mAdapter;
  private String medName, medScore, minfo1, minfo1Val, minfo2, minfo2Val, minfo3, minfo3Val,medplot;
  private ExtVideoBean mVideoInfo;
  private boolean isFavorite;

 final int TURNTOFULLSCREEN = 2;

  public static MediaInfoListFragment newInstance() {
    MediaInfoListFragment f = new MediaInfoListFragment();
    return f;
  }

    public Handler mHandler = new Handler();

    public void setHandler(Handler handler) {
        this.mHandler = handler;
        Log.v("HHHonmHandlerinput1", mHandler.toString());
    }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_media_info, container, false);

    initView();
    getMediaInfo();
    setMediaInfo();
    return viewGroup;

  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final Activity activity = getActivity();

    mAdapter = new MediaAdapter(activity);
    mMediaListView.setAdapter(mAdapter);

    mAdapter.addItem(minfo1,minfo1Val);
    mAdapter.addItem(minfo2, minfo2Val);
    mAdapter.addItem(minfo3, minfo3Val);
    isFavorite = false;
  }


  final class MediaItem {
    String mNmae;
    String mValue;

    public MediaItem(String name, String value) {
      mNmae = name;
      mValue = value;
    }
  }

  final class MediaAdapter extends ArrayAdapter<MediaItem> {
    public MediaAdapter(Context context) {
      super(context, R.layout.info_list_item_2);
    }

    public void addItem(String name, String value) {
      add(new MediaItem(name, value));
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      if (view == null) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.info_list_item_2, parent, false);
      }

      MediaAdapter.ViewHolder viewHolder = (MediaAdapter.ViewHolder) view.getTag();
      if (viewHolder == null) {
        viewHolder = new MediaAdapter.ViewHolder();
        viewHolder.mNameTextView = (TextView) view.findViewById(R.id.text1);
        viewHolder.mValueTextView = (TextView) view.findViewById(R.id.text2);
      }

      MediaItem item = getItem(position);
      viewHolder.mNameTextView.setText(item.mNmae);
      viewHolder.mValueTextView.setText(item.mValue);

      return view;
    }

    @Override
    public boolean isEnabled(int position) {
      return false;
    }

    final class ViewHolder {
      public TextView mNameTextView;
      public TextView mValueTextView;
    }
  }

  public void initView(){
    mName = (TextView) viewGroup.findViewById(R.id.name_txt);
    mScore= (TextView) viewGroup.findViewById(R.id.score_txt);
    mplot = (Button) viewGroup.findViewById(R.id.plot_txt);
    mplot.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new AlertDialog.Builder(getContext(),R.style.dialog).setMessage(medplot)
          .show();
      }
    });
    mFullScreen=(Button) viewGroup.findViewById(R.id.full_btn);
        mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(TURNTOFULLSCREEN);
            }
        });
    mFavorite=(Button) viewGroup.findViewById(R.id.fav_btn);
    /* for Favorite usage */
    mFavorite.setClickable(true);
    // ------------------

    mMediaListView = (ListView) viewGroup.findViewById(R.id.mediainfo_list);
    mMediaListView.setFocusable(false);
  }

  public void getMediaInfo(){
    SharedPreferences videoinfoshare=getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
    medName=videoinfoshare.getString("name","");
    medScore=videoinfoshare.getString("score","");

    minfo1="集数：";
    String latestOrder=videoinfoshare.getString("latestOrder","");
    String videoCount=videoinfoshare.getString("videoCount","");
    minfo1Val="更新至"+latestOrder+"集/共"+videoCount+"集";

        if(null!=(videoinfoshare.getString("directorname",""))) {
            minfo2 = "导演：";
            minfo2Val = videoinfoshare.getString("directorname", "");
        }
        else if(null!=(videoinfoshare.getString("hostname",""))){
            minfo2 = "主持人：";
            minfo2Val = videoinfoshare.getString("hostname", "");
        }

        if(null!=(videoinfoshare.getString("main_charactorname",""))) {
            minfo3 = "主演：";
            minfo3Val = videoinfoshare.getString("main_charactorname", "");
        }
        else if(null!=(videoinfoshare.getString("guestname",""))){
            minfo3 = "嘉宾：";
            minfo3Val = videoinfoshare.getString("guestname", "");
        }

        if(null!=(videoinfoshare.getString("description",""))) {
            medplot = videoinfoshare.getString("description", "");
        } else {
            medplot="无";
        }

    /* for Favorite usage */
    mVideoInfo = new ExtVideoBean();
    mVideoInfo.setAlbumId(videoinfoshare.getString(EXT_VIDEO_ALBUM,""));
    mVideoInfo.setVideoType(videoinfoshare.getInt(EXT_VIDEO_TYPE,0));
    mVideoInfo.setVideoId(videoinfoshare.getString("tvId",""));
    mVideoInfo.setVideoName(medName);
    mVideoInfo.setVideoPlayUrl(videoinfoshare.getString(EXT_VIDEO_PLAYURL,""));
    mVideoInfo.setAlbumImageUrl(videoinfoshare.getString(EXT_VIDEO_IMAGE_URL,""));
    mVideoInfo.setVideoCount(Integer.valueOf(videoCount));
    mVideoInfo.setVideoLatestOrder(Integer.valueOf(latestOrder));
    mVideoInfo.setVideoCurrentViewOrder(0);
    mVideoInfo.setVideoPlayPosition(0);
    // ----------------------
    checkVideo();
    mFavorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          LeanCloudStorage.getIQiyFavoriteListener(mVideoInfo.getAlbumId(), new LeanCloudStorage.VideoSeeker() {
            @Override
            public void succeed(ExtVideoBean bean) {
              if ( bean != null ) {
                if ( isFavorite ) {
                  try {
                    LeanCloudStorage.removeIQiyFavorite(mVideoInfo.getAlbumId(), new DeleteCallback() {
                      @Override
                      public void done(AVException e) {
                        setFavoriteIcon(false);
                      }
                    });
                  } catch ( Exception e ) {
                  }
                } else {
                  try {
                    LeanCloudStorage.updateIQiyFavorite(mVideoInfo, new SaveCallback() {
                      @Override
                      public void done(AVException e) {
                        if ( e == null ) {
                          setFavoriteIcon(true);
                        }
                      }
                    });
                  } catch ( Exception e ) {
                  }
                }
              }
              else{
                setFavoriteIcon(false);
              }
            }

            @Override
            public void failed() {
              setFavoriteIcon(false);
            }
          });
        }
        catch(Exception e){
        }
      }
    });

    /* ------------------------------- */
  }

  private void setFavoriteIcon(boolean isTrue){
      if ( isTrue ){
        mFavorite.setSelected(true);
        isFavorite = true;
      }
      else{
        mFavorite.setSelected(false);
        isFavorite = false;
      }
  }


  private void checkVideo(){
    try {
      LeanCloudStorage.getIQiyFavoriteListener(mVideoInfo.getAlbumId(),
        new LeanCloudStorage.VideoSeeker() {
          @Override
          public void succeed(ExtVideoBean bean) {
            if ( bean != null ) {
              setFavoriteIcon(true);
            }
            else{
              setFavoriteIcon(false);
            }
          }

          @Override
          public void failed() {
            setFavoriteIcon(false);
          }
        });
    }catch (Exception e){
    }
  }


  public void setMediaInfo(){
    mName.setText(medName);
    mScore.setText(medScore);
    mplot.setText(medplot);
  }
}
