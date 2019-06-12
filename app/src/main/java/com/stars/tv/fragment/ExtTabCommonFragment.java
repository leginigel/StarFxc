package com.stars.tv.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.bean.ExtTitleBean;
import com.stars.tv.bean.ExtVideoBean;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ExtTabCommonFragment extends ExtBaseFragment{
  public final static String EXT_TITLE_ID="extTitleID";
  private int mItemPaddingPixel;
  private LeanCloudStorage mStorage;

  private String mFragID;
  Unbinder unbinder;
  @BindView(R.id.ext_frame_recycler)
  RecyclerView mExtContentsRecycler;
  private List<ExtVideoBean> mVideoList = new ArrayList<>();

  public ExtTabCommonFragment(){
  }

  public static ExtTabCommonFragment newInstance(
    ExtTitleBean tab, int indicatorColor, int dividerColor){
    ExtTabCommonFragment f = new ExtTabCommonFragment();
    f.setTitle(tab.getExtName());
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
    mStorage = new LeanCloudStorage(mFragID);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
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

    mVideoList = mStorage.getVideoList();

    ExtContentAdapter adapter = new ExtContentAdapter();
    mStorage.storageFetchListener(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> avObjects, AVException e) {
        if ( e == null ) {
          mVideoList = mStorage.assignToExtVideoList(avObjects);
          adapter.notifyDataSetChanged();
        }
      }
    });
    mExtContentsRecycler.setAdapter(adapter);
    return v;
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
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            ConstraintLayout mLayout = v.findViewById(R.id.ext_view_items_container);

            if (hasFocus){
              mLayout.setBackground(getResources().getDrawable(R.drawable.ext_content_bolder_focus));
              mLayout.setPadding(mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel);
              //mVideoText.setBackground(getResources().getDrawable(R.drawable.ext_content_bolder_focus));
            }else {
              mLayout.setBackground(getResources().getDrawable(R.drawable.ext_content_bolder_normal));
              mLayout.setPadding(mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel);
              //mVideoText.setBackground(getResources().getDrawable(R.drawable.ext_content_bolder_normal));
            }
          }
        });
        itemView.setOnKeyListener(new View.OnKeyListener() {
          @Override
          public boolean onKey(View v, int keyCode, KeyEvent event) {
            int itemIdx = mExtContentsRecycler.getChildAdapterPosition(v);
            if ( itemIdx < 6 ){
              if ( keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN ){
                mExtContentsRecycler.clearFocus();
                return true;
              }
            }
            return false;
          }
        });
      }

      private void bindViewHolder (ExtVideoBean vb){
        Glide.with(Objects.requireNonNull(getActivity()))
          .load(vb.getVideoImageUrl()).into(mVideoImage);
        mVideoText.setText(vb.getVideoName());
      }
    }
  }
}
