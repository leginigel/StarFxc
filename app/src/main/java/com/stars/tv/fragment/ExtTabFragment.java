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
import com.stars.tv.adapter.ExtTabFragmentPagerAdapter;
import com.stars.tv.bean.ExtTitleBean;
import com.stars.tv.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.stars.tv.utils.Constants.CLOUD_FAVORITE_CLASS;
import static com.stars.tv.utils.Constants.CLOUD_HISTORY_CLASS;

public class ExtTabFragment extends Fragment {
  private SlidingTabLayout tabs;
  private ViewPager pager;
  private FragmentPagerAdapter adapter;

  public static ExtTabFragment newInstance(){
    return new ExtTabFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.ext_tab_layout, container, false);
    return v;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    final List<ExtBaseFragment> fgs = getFragments();
    adapter = new ExtTabFragmentPagerAdapter(getFragmentManager(), fgs);

    pager = view.findViewById(R.id.main_ext_viewpager);
    pager.setAdapter(adapter);

    tabs = view.findViewById(R.id.main_ext_tablayout);
    tabs.setCustomTabView(R.layout.ext_tab_title,
      R.id.ext_txtTabTitle, R.id.ext_imgTabIcon);
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

  public List<ExtBaseFragment> getFragments(){
    int indicatorColor = getResources().getColor(R.color.color_focus);
    int dividerColor = Color.TRANSPARENT;

    List<ExtBaseFragment> fgs = new ArrayList<>();
    List<ExtTitleBean> title = new ArrayList<>();
    title.add(new ExtTitleBean(CLOUD_HISTORY_CLASS, R.drawable.history_40x32));
    title.add(new ExtTitleBean(CLOUD_FAVORITE_CLASS, R.drawable.star_40x32));

    for ( int i = 0 ; i < title.size() ; i++ ){
      fgs.add(ExtTabCommonFragment.newInstance(title.get(i), indicatorColor, dividerColor));
    }
    return fgs;
  }

}
