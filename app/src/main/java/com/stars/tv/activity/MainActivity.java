package com.stars.tv.activity;

import com.stars.tv.R;
import com.stars.tv.bean.TvTitle;
import com.stars.tv.fragment.VideoRowSampleFragment;
import com.stars.tv.fragment.VideoVGridSampleFragment;
import com.stars.tv.model.TvTitleModel;
import com.stars.tv.presenter.TvTitlePresenter;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.SpaceItemDecoration;

import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BaseGridView;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private static final int TITLE_PADDING_LEFT_PX = 60;
    private static final int TITLE_TOP_PADDING_PC = 0;
    private static final int TITLE_RIGHT_PADDING_PC = 20;

    @BindView(R.id.hg_title) HorizontalGridView hgTitle;
    @BindView(R.id.page_vp) ViewPager pageVp;
    @BindView(R.id.search_btn) Button searchBtn;

    FragAdapter mFragAdapter;
//    List<VideoVGridSampleFragment> mFragmentList = new ArrayList<>();
//    List<VideoRowSampleFragment> mFragmentList = new ArrayList<>();
    List<Fragment> mFragmentList = new ArrayList<>();

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTitle();
        initContentViews();
        refreshRequest();
    }

    private void initTitle() {
        hgTitle.setPadding(TITLE_PADDING_LEFT_PX, 0, 0, 0);
        int top = ViewUtils.getPercentHeightSize(TITLE_TOP_PADDING_PC);
        int right = ViewUtils.getPercentWidthSize(TITLE_RIGHT_PADDING_PC);
        hgTitle.addItemDecoration(new SpaceItemDecoration(right, top));
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new TvTitlePresenter());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(arrayObjectAdapter);
        hgTitle.setAdapter(itemBridgeAdapter);
        arrayObjectAdapter.addAll(0, TvTitleModel.getTitleList());

        hgTitle.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    // 避免焦点跑到搜索框去.
                    searchBtn.setFocusableInTouchMode(true);
                    searchBtn.setFocusable(true);
                    searchBtn.requestFocusFromTouch();
                }
                return false;
            }
        });

        hgTitle.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        ViewUtils.scaleAnimator(view, hasFocus,1.2f,150);
                        TextView tv = view.findViewById(R.id.tv_title);
                        View lineView = view.findViewById(R.id.title_under_line);
                        lineView.setBackgroundColor(getResources().getColor(hasFocus ? R.color.color_focus : R.color.color_transparent));
                        tv.setTextColor(getResources().getColor(hasFocus ? R.color.color_focus : R.color.color_all_white));
                        if ((int) view.getTag() == pageVp.getCurrentItem())
                        {
                            ViewUtils.scaleAnimator(view, true,1.2f,150);
                        }
                    }
                });
                pageVp.setCurrentItem(position);
            }
        });
    }

    private void initContentViews() {
        mFragAdapter = new FragAdapter(getSupportFragmentManager());
        searchBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchBtn.setBackgroundResource(b ? R.drawable.ic_search_btn_focus : R.drawable.ic_search_btn_normal);
            }
        });
        searchBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    searchBtn.setFocusable(false);
                    hgTitle.requestFocusFromTouch();
                    return true;
                }
                return false;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        pageVp.setOffscreenPageLimit(2); // 缓存2个页面
        pageVp.setAdapter(mFragAdapter);
    }

    private void refreshRequest() {
            mFragmentList.clear();
            AtomicInteger i= new AtomicInteger();
            for (TvTitle titleMode : TvTitleModel.getTitleList()) {
                if(i.get() %2==0)
                {
                    mFragmentList.add(VideoRowSampleFragment.getInstance(titleMode.getName()));
                }else
                {
                    mFragmentList.add(VideoVGridSampleFragment.getInstance(titleMode.getName()));
                }
                i.getAndIncrement();
            }
            mFragAdapter.notifyDataSetChanged();
        //TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    /**
     * 内容 framgnt Adapter.
     */
    public class FragAdapter extends FragmentPagerAdapter {

        FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return null != mFragmentList ? mFragmentList.size() : 0;
        }

    }

}

