package com.stars.tv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stars.tv.R;
import com.stars.tv.fragment.DragTabFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DragMainActivity extends BaseActivity{
  DragTabFragment mMyDragTabFragment;

  public DragMainActivity(){}

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.drag_activity_main);
    initDragFrame();
  }

  private void initDragFrame(){
    mMyDragTabFragment = DragTabFragment.newInstance();
    getSupportFragmentManager()
      .beginTransaction()
      .add(R.id.drag_main_frame, mMyDragTabFragment)
      .commit();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
