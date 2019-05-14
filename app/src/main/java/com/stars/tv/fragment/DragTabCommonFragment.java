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
import com.stars.tv.R;
import com.stars.tv.bean.DragTitleBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.sample.DragFavoriteSampleDataList;
import com.stars.tv.sample.DragHistorySampleDataList;
import com.stars.tv.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DragTabCommonFragment extends DragBaseFragment{
  public final static String DRAG_TITLE_ID="dragTitleID";
  public final static String HISTORY_FRAGMENT_ID="History";
  public final static String FAVORITE_FRAGMENT_ID="Favorite";
  private int mItemPaddingPixel;

  private String mFragID;
  Unbinder unbinder;
  @BindView(R.id.drag_frame_recycler)  RecyclerView mDragContentsRecycler;
  List<IQiYiMovieBean> mVideoList = new ArrayList<>();

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
    if ( mFragID.compareTo(HISTORY_FRAGMENT_ID) == 0 ) {
      mVideoList = DragHistorySampleDataList.setupMovies();
    }
    else{
      mVideoList = DragFavoriteSampleDataList.setupMovies();
    }
    DragContentAdapter adapter = new DragContentAdapter();
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
      IQiYiMovieBean vb = mVideoList.get(i);
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

      private void bindViewHolder (IQiYiMovieBean vb){
        mVideoImage.setImageResource(R.drawable.temp_tv_icon);
        mVideoText.setText(vb.getName());
      }
    }
  }
}
