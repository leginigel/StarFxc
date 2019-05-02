package com.stars.tv.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stars.tv.R;
import com.stars.tv.adapter.DragTabFragmentPagerAdapter;
import com.stars.tv.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class DragTabFragment extends Fragment {
  private SlidingTabLayout tabs;
  private ViewPager pager;
  private FragmentPagerAdapter adapter;

  public static DragTabFragment newInstance(){
    return new DragTabFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.drag_tab_layout, container, false);
    return v;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    final List<DragBaseFragment> fgs = getFragments();
    adapter = new DragTabFragmentPagerAdapter(getFragmentManager(), fgs);

    pager = view.findViewById(R.id.main_drag_viewpager);
    pager.setAdapter(adapter);

    tabs = view.findViewById(R.id.main_drag_tablayout);
    tabs.setCustomTabView(R.layout.drag_tab_title,
      R.id.drag_txtTabTitle, R.id.drag_imgTabIcon);
    tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
      @Override
      public int getIndicatorColor(int position) {
        return fgs.get(position).getIndicatorColor();
      }

      @Override
      public int getDividerColor(int position) {
        return fgs.get(position).getDividerColor();
      }
    });
    tabs.setBackgroundResource(R.color.color_transparent);
    tabs.setViewPager(pager);
  }

  public List<DragBaseFragment> getFragments(){
    int indicatorColor = getResources().getColor(R.color.color_focus);
    int dividerColor = Color.TRANSPARENT;

    List<DragBaseFragment> fgs = new ArrayList<>();
    fgs.add(DragTabCommonFragment.newInstance("History", R.drawable.history_40x32,
      indicatorColor, dividerColor));
    fgs.add(DragTabCommonFragment.newInstance("Favorite", R.drawable.star_40x32,
      indicatorColor, dividerColor));

    return fgs;
 }

}
