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
import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.bean.DragTitleBean;
import com.stars.tv.bean.DragVideoBean;
import com.stars.tv.server.LeanCloudStorage;
import com.stars.tv.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;

public class DragTabCommonFragment extends DragBaseFragment{
  public final static String DRAG_TITLE_ID="dragTitleID";
  private int mItemPaddingPixel;
  private LeanCloudStorage mStorage;

  private String mFragID;
  Unbinder unbinder;
  @BindView(R.id.drag_frame_recycler)  RecyclerView mDragContentsRecycler;
  private List<DragVideoBean> mVideoList = new ArrayList<>();

  public DragTabCommonFragment(){
  }

  public static DragTabCommonFragment newInstance(
    DragTitleBean tab, int indicatorColor, int dividerColor){
    DragTabCommonFragment f = new DragTabCommonFragment();
    f.setTitle(tab.getDragName());
    f.setIconRes(tab.getDragResIcon());
    f.setIndicatorColor(indicatorColor);
    f.setDividerColor(dividerColor);
    Bundle bundle = new Bundle();
    bundle.putString(DRAG_TITLE_ID, tab.getDragName());
    f.setArguments(bundle);
    return f;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragID = getArguments().getString(DRAG_TITLE_ID);
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
    View v = inflater.inflate(R.layout.drag_videos_recycler_layout, container, false);
    unbinder = ButterKnife.bind(this, v);

    mItemPaddingPixel = ViewUtils.getPixelFromDp(getContext(), 4);
    mDragContentsRecycler.setLayoutManager(
      new GridLayoutManager(getContext(), 6,
        GridLayoutManager.VERTICAL, false));

    mVideoList = mStorage.getVideoList();

    DragContentAdapter adapter = new DragContentAdapter();
    mStorage.storageFetchListener(new LeanCloudStorage.cloudFetchListener() {
      @Override
      public void done(List<AVObject> objects, AVException e) {
        mStorage.assignToDragVideoList(objects);
        mVideoList = mStorage.getVideoList();
        adapter.notifyDataSetChanged();
    }
    });
    mDragContentsRecycler.setAdapter(adapter);
    return v;
  }

  private class DragContentAdapter extends RecyclerView.Adapter<DragContentAdapter.ViewHolder>{
    @NonNull
    @Override
    public DragContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View v = getLayoutInflater().inflate(R.layout.drag_videos_layout, null);
      return new DragContentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DragContentAdapter.ViewHolder vh, int i) {
      DragVideoBean vb = mVideoList.get(i);
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
        mVideoImage = itemView.findViewById(R.id.drag_contents_imageview);
        mVideoText = itemView.findViewById(R.id.drag_contents_textview);
        itemView.setFocusable(true);
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            ConstraintLayout mLayout = v.findViewById(R.id.drag_view_items_container);

            if (hasFocus){
              mLayout.setBackground(getResources().getDrawable(R.drawable.drag_content_bolder_focus));
              mLayout.setPadding(mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel);
              //mVideoText.setBackground(getResources().getDrawable(R.drawable.drag_content_bolder_focus));
            }else {
              mLayout.setBackground(getResources().getDrawable(R.drawable.drag_content_bolder_normal));
              mLayout.setPadding(mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel,mItemPaddingPixel);
              //mVideoText.setBackground(getResources().getDrawable(R.drawable.drag_content_bolder_normal));
            }
          }
        });
        itemView.setOnKeyListener(new View.OnKeyListener() {
          @Override
          public boolean onKey(View v, int keyCode, KeyEvent event) {
            int itemIdx = mDragContentsRecycler.getChildAdapterPosition(v);
            if ( itemIdx < 6 ){
              if ( keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN ){
                mDragContentsRecycler.clearFocus();
                return true;
              }
            }
            return false;
          }
        });
      }

      private void bindViewHolder (DragVideoBean vb){
        Glide.with(Objects.requireNonNull(getActivity()))
          .load(vb.getVideoImageFile()).into(mVideoImage);
        mVideoText.setText(vb.getVideoName());
      }
    }
  }
}
