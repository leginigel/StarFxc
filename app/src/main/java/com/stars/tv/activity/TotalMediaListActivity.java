package com.stars.tv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BaseGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.fragment.MediaListMVPFragment;
import com.stars.tv.presenter.TotalMediaListTitlePresenter;

import java.util.Arrays;

public class TotalMediaListActivity extends BaseActivity {

    String TAG = "TotalMediaListActivity";
    private Button searchBtn;
    private VerticalGridView type_vp;
    private RelativeLayout typelist;

    private String[] listType;
    private int listPosition = 0;
    private boolean isRemaining = false;
    private String buttonName;

    public Handler mHandler = new Handler();

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalmedialist);
        searchBtn = findViewById(R.id.search_btn);
        typelist = findViewById(R.id.typelist);
        buttonName = getIntent().getStringExtra("buttonName");

        if (buttonName.contains("全部")) {
            typelist.setVisibility(View.VISIBLE);
            initTypeList();
        } else {
            typelist.setVisibility(View.GONE);
        }
        initContentViews(buttonName);
    }

    private void initTypeList() {
        type_vp = findViewById(R.id.type_vg);
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new TotalMediaListTitlePresenter());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(arrayObjectAdapter);

        if (buttonName.contains("Series")) {
            listType = getResources().getStringArray(R.array.series_type_list);
        } else if (buttonName.contains("Film")) {
            listType = getResources().getStringArray(R.array.film_type_list);
        } else if (buttonName.contains("Cartoon")) {
            listType = getResources().getStringArray(R.array.cartoon_type_list);
        } else if (buttonName.contains("Variety")) {
            listType = getResources().getStringArray(R.array.variety_type_list);
        }
        listType = Arrays.copyOf(listType, listType.length - 1);

        arrayObjectAdapter.addAll(0, Arrays.asList(listType));
        type_vp.setAdapter(itemBridgeAdapter);
        type_vp.setSelectedPosition(listPosition);
        type_vp.smoothScrollToPosition(listPosition);

        type_vp.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (listPosition == 0 && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    // 避免焦点跑到搜索框去.
                    searchBtn.setFocusableInTouchMode(true);
                    searchBtn.setFocusable(true);
                    searchBtn.requestFocusFromTouch();
                }
                return false;
            }
        });

        type_vp.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                if (position != -1) {
                    child.itemView.setTag(position);
                    listPosition = position;
                    Log.v(TAG, listPosition + "");
                    child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                        TextView title = view.findViewById(R.id.item_medial_ist_title_text);
                        if (hasFocus) {
                            view.setBackgroundColor(getResources().getColor(R.color.color_focus));
                            title.setTextColor(getResources().getColor(R.color.color_all_white));
                        } else {
                            if (isRemaining) {
                                isRemaining = false;
                                title.setTextColor(getResources().getColor(R.color.color_focus));
                                view.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                            } else {
                                title.setTextColor(getResources().getColor(R.color.color_all_white));
                                view.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                            }
                        }
                    });
                    mHandler.sendEmptyMessage(listPosition);
                }
            }
        });

        searchBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchBtn.setBackgroundResource(b ? R.drawable.ic_search_btn_focus : R.drawable.ic_search_btn_normal);
            }
        });

        searchBtn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        searchBtn.setFocusable(false);
                        type_vp.requestFocusFromTouch();
                        return true;
                    }
                }
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Intent intent = new Intent(TotalMediaListActivity.this, SearchMoviceActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initContentViews(String titleName) {
        Fragment newFragment = MediaListMVPFragment.getInstance(titleName);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.mediacontent_vp, newFragment);
        transaction.commit();
    }

}
