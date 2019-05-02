package com.stars.tv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.KeyEvent;

import com.stars.tv.R;
import com.stars.tv.fragment.DragTabFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DragMainActivity extends BaseActivity{
  DragTabFragment mMyDragTabFragment;
  Unbinder unbinder;

  public DragMainActivity(){}

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.drag_activity_main);
    ButterKnife.bind(this);
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
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    Log.d("Test", "Jack, activity,keycode="+keyCode+", event="+event );
    return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if ( unbinder != null )
      unbinder.unbind();
  }
}
