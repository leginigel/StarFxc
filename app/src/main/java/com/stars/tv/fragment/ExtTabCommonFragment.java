package com.stars.tv.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.activity.FullPlaybackActivity;
import com.stars.tv.activity.VideoPreviewActivity;
import com.stars.tv.bean.ExtTitleBean;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.Utils;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.youtube.YoutubeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_YT_HISTORY_CLASS;
import static com.stars.tv.utils.Constants.EXT_VIDEO_COUNT;
import static com.stars.tv.utils.Constants.EXT_VIDEO_IMAGE_URL;
import static com.stars.tv.utils.Constants.EXT_VIDEO_TYPE;

public class ExtTabCommonFragment extends ExtBaseFragment{
  public final static String EXT_TITLE_ID="extTitleID";
  private int mItemPaddingPixel;
  private LeanCloudStorage mStorage;
  private ExtContentAdapter mAdapter;

  private String mFragID;
  private int mFragSel;
  Unbinder unbinder;
  @BindView(R.id.ext_frame_recycler)
  RecyclerView mExtContentsRecycler;
  private List<ExtVideoBean> mVideoList = new ArrayList<>();

  public ExtTabCommonFragment(){
  }

  public static ExtTabCommonFragment newInstance(
    ExtTitleBean tab, int indicatorColor, int dividerColor){
    ExtTabCommonFragment f = new ExtTabCommonFragment();
    f.setTitle(tab.getTabsText());
    f.setIconRes(tab.getExtResIcon());
    f.setIndicatorColor(indicatorColor);
    f.setDividerColor(dividerColor);
    Bundle bundle = new Bundle();
    bundle.putString(EXT_TITLE_ID, tab.getExtName());
    f.setArguments(bundle);
    return f;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragID = getArguments().getString(EXT_TITLE_ID);
    if ( mFragID.compareTo(CLOUD_HISTORY_CLASS) == 0 ){
      mFragSel = 0;
    }
    else if ( mFragID.compareTo(CLOUD_FAVORITE_CLASS) == 0 ){
      mFragSel = 1;
    }
    else if ( mFragID.compareTo(CLOUD_YT_HISTORY_CLASS) == 0 ){
      mFragSel = 2;
    }
    else
      mFragSel = 3;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1){
      if ( resultCode == RESULT_OK ){
        boolean exit = data.getBooleanExtra("exit", false);
        if ( exit ){
          getActivity().finish();
        }
      }
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.ext_videos_recycler_layout, container, false);
    unbinder = ButterKnife.bind(this, v);

    mItemPaddingPixel = ViewUtils.getPixelFromDp(getContext(), 4);
    mExtContentsRecycler.setLayoutManager(
      new GridLayoutManager(getContext(), 5,
        GridLayoutManager.VERTICAL, false));

    initDataLink();
    return v;
  }

  private void initDataLink(){
    mStorage = new LeanCloudStorage(mFragID);
    mAdapter = new ExtContentAdapter();
    mExtContentsRecycler.setAdapter(mAdapter);
    mStorage.storageFetchListener(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        if ( e == null ) {
          mVideoList = mStorage.assignToExtVideoList(avObjects);
          mAdapter.notifyDataSetChanged();
        }
      }
    });
  }

  private class ExtContentAdapter extends RecyclerView.Adapter<ExtContentAdapter.ViewHolder>{
    @NonNull
    @Override
    public ExtContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View v = getLayoutInflater().inflate(R.layout.ext_videos_layout, null);
      return new ExtContentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtContentAdapter.ViewHolder vh, int i) {
      ExtVideoBean vb = mVideoList.get(i);
      vh.bindViewHolder(vb);
    }

    @Override
    public int getItemCount() {
      return mVideoList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
      ImageView mVideoImage;
      TextView mVideoText;

      public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mVideoImage = itemView.findViewById(R.id.ext_contents_imageview);
        mVideoText = itemView.findViewById(R.id.ext_contents_textview);
        itemView.setFocusable(true);
        itemView.setFocusableInTouchMode(true);

        mVideoImage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //dummy bypass icon onclick for mouse click issue
          }
        });
        itemView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
          @Override
          public boolean onGenericMotion(View v, MotionEvent event) {
            //dummy bypass container mouse event for mouse click issue
            return true;
          }
        });

        itemView.setOnFocusChangeListener(new itemSetFocusChangeListener());
        itemView.setOnKeyListener(new itemSetKeyEventListener());
        itemView.setOnClickListener(new itemSetOnClickListener());
        itemView.setOnLongClickListener(new itemSetLongClickListener());
      }

      private void bindViewHolder (ExtVideoBean vb){
        StringBuffer sb = new StringBuffer();
        Glide.with(Objects.requireNonNull(getActivity()))
          .load(vb.getAlbumImageUrl()).into(mVideoImage);
        sb.append(vb.getVideoName());
        if ( mFragSel == 0 ) {
          sb.append("/當前撥放至 ");
          sb.append(Utils.stringForTime(vb.getVideoPlayPosition()));
        }
        mVideoText.setText(sb);
      }
    }
  }

  private class itemSetOnClickListener implements View.OnClickListener{
    private Class mClass;

    itemSetOnClickListener(){
      if ( mFragSel == 0 ){
        mClass = FullPlaybackActivity.class;
      }
      else if ( mFragSel == 1 ){
        mClass = VideoPreviewActivity.class;
      }
      else{
        mClass = YoutubeActivity.class;
      }
    }

    @Override
    public void onClick(View v) {
      int itemIdx = mExtContentsRecycler.getChildAdapterPosition(v);
      Intent intent = new Intent();
      Activity activity = getActivity();
      ExtVideoBean bean;
      switch (mFragSel){
        case 0:
          bean = mVideoList.get(itemIdx);
          intent.putExtra("name", bean.getVideoName());
          intent.putExtra("albumId", bean.getAlbumId());
          intent.putExtra("latestOrder", String.valueOf(bean.getVideoLatestOrder()));
          intent.putExtra("currentPosition", bean.getVideoPlayPosition());
          intent.putExtra("mEpisode", bean.getVideoCurrentViewOrder());
          intent.putExtra("tvId", bean.getVideoId());
          intent.putExtra(EXT_VIDEO_TYPE, bean.getVideoType());
          intent.putExtra(EXT_VIDEO_COUNT, bean.getVideoCount());
          intent.putExtra(EXT_VIDEO_IMAGE_URL, bean.getAlbumImageUrl());

          intent.setClass(activity, mClass);
          startActivityForResult(intent, 1);
          break;
        case 1:
          bean = mVideoList.get(itemIdx);
          IQiYiMovieBean basebean = new IQiYiMovieBean();
          basebean.setAlbumId(bean.getAlbumId());
          basebean.setTvId(bean.getVideoId());
          basebean.setName(bean.getVideoName());
          basebean.setPlayUrl(bean.getVideoPlayUrl());
          basebean.setAlbumImageUrl(bean.getAlbumImageUrl());
          intent.putExtra("videoBean", basebean);
          intent.setClass(activity, mClass);
          startActivityForResult(intent, 1);
          break;
        default:
          String mExtra = "Youtube";
          intent.putExtra(mExtra, mVideoList.get(itemIdx));
          intent.setClass(activity, mClass);
          startActivityForResult(intent, 1);
          break;
      }
    }
  }
  private class itemSetLongClickListener implements View.OnLongClickListener{
    @Override
    public boolean onLongClick(View v) {
      final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
      int itemIdx = mExtContentsRecycler.getChildAdapterPosition(v);
      dialog.setTitle("刪除視頻");
      dialog.setMessage("\n請確認是否從列表中刪除\n\n"+mVideoList.get(itemIdx).getVideoName());
      dialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
           String albumId = mVideoList.get(itemIdx).getAlbumId();
            List<ExtVideoBean> tmp = new ArrayList<>();
            tmp.add(mVideoList.get(itemIdx));
            if ( mAdapter.getItemCount() == 1 ){
              mExtContentsRecycler.clearFocus();
            }
            else if ( itemIdx == mAdapter.getItemCount() - 1){
              View view = mExtContentsRecycler.getChildAt(itemIdx-1);
              view.requestFocus();
            }
            else{
              View view = mExtContentsRecycler.getChildAt(itemIdx+1);
              view.requestFocus();
            }

            mVideoList.removeAll(tmp);
            mAdapter.notifyItemRemoved(itemIdx);

            try {
              if ( mFragSel == 0 ) {
                LeanCloudStorage.removeIQiyHistory(albumId,
                  new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                    }
                  });
              }
              else if ( mFragSel == 1 ){
                LeanCloudStorage.removeIQiyFavorite(albumId, new DeleteCallback() {
                  @Override
                  public void done(AVException e) {

                  }
                });
              }
              else if ( mFragSel == 2 ){
                LeanCloudStorage.removeYoutubeHistory(albumId, new DeleteCallback() {
                  @Override
                  public void done(AVException e) {

                  }
                });
              }
              else {
                LeanCloudStorage.removeYoutubeFavorite(albumId, new DeleteCallback() {
                  @Override
                  public void done(AVException e) {

                  }
                });
              }
            }
            catch(Exception e){
            }
          }
      });
      dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
      });

      dialog.setCancelable(false);
      dialog.show();
      return true;
    }
  }
  private class itemSetFocusChangeListener implements View.OnFocusChangeListener{
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
      ConstraintLayout mLayout = v.findViewById(R.id.ext_view_items_container);
      TextView vi = v.findViewById(R.id.ext_contents_textview);

      if (hasFocus){
        mLayout.setBackground(getResources().getDrawable(R.drawable.ext_content_bolder_focus));
        vi.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        vi.setSelected(true);
        mLayout.setPadding(mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel);
      }else {
        mLayout.setBackground(getResources().getDrawable(R.drawable.ext_content_bolder_normal));
        vi.setEllipsize(TextUtils.TruncateAt.END);
        vi.setSelected(false);
        mLayout.setPadding(mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel);
      }
    }
  }
  private class itemSetKeyEventListener implements View.OnKeyListener{
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
      int itemIdx = mExtContentsRecycler.getChildAdapterPosition(v);
      if ( itemIdx < 5 ){
        if ( keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN ){
          mExtContentsRecycler.clearFocus();
          return true;
        }
      }
      return false;
    }
  }
}
