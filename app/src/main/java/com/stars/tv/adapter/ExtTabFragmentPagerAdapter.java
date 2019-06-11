package com.stars.tv.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stars.tv.fragment.ExtBaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ExtTabFragmentPagerAdapter extends FragmentPagerAdapter {
  List<ExtBaseFragment> mFragments = null;

  public ExtTabFragmentPagerAdapter(FragmentManager fm, List<ExtBaseFragment> fragments ){
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
