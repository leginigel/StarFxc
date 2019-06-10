package com.stars.tv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.stars.tv.R;
import com.stars.tv.fragment.ExtTabFragment;

public class ExtMainActivity extends BaseActivity{
  ExtTabFragment mMyExtTabFragment;

  public ExtMainActivity(){}

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ext_activity_main);
    initExtFrame();
  }

  private void initExtFrame(){
    mMyExtTabFragment = ExtTabFragment.newInstance();
    getSupportFragmentManager()
      .beginTransaction()
      .add(R.id.ext_main_frame, mMyExtTabFragment)
      .commit();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
