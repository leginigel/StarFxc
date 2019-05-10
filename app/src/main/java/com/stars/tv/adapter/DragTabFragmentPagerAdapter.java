package com.stars.tv.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stars.tv.fragment.DragBaseFragment;

import java.util.ArrayList;
import java.util.List;

public class DragTabFragmentPagerAdapter extends FragmentPagerAdapter {
  List<DragBaseFragment> mFragments = null;

  public DragTabFragmentPagerAdapter(FragmentManager fm, List<DragBaseFragment> fragments ){
    super(fm);
    if ( fragments == null ){
      mFragments = new ArrayList<>();
    }
    else{
      mFragments = fragments;
    }
  }

  @Override
  public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override
  public int getCount() {
    return mFragments.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mFragments.get(position).getTitle();
  }

  public int getIconRes(int position) {
    return mFragments.get(position).getIconRes();
  }
}
