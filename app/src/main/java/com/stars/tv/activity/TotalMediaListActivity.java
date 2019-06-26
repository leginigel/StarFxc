package com.stars.tv.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Button search_btn;
    private VerticalGridView type_vp;
    private RelativeLayout typelist;


    private String[] listType;
    private int listPosition = 0;
    private boolean isRemaining = false;
    private String className, buttonName;

    public Handler mHandler = new Handler();

    public void setHandler(Handler handler) {
        this.mHandler = handler;
        Log.v(TAG, mHandler.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totalmedialist);
        typelist = findViewById(R.id.typelist);
        className = getIntent().getStringExtra("className");
        buttonName = getIntent().getStringExtra("buttonName");

        if (buttonName.contains("全部")) {
            buttonName=buttonName+className;
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

        if (className.contains("Series")) {
            listType = getResources().getStringArray(R.array.series_type_list);
        }
        else if(className.contains("film")){
            listType = getResources().getStringArray(R.array.film_type_list);
        }
        listType = Arrays.copyOf(listType, listType.length - 1);

        arrayObjectAdapter.addAll(0, Arrays.asList(listType));
        type_vp.setAdapter(itemBridgeAdapter);
        type_vp.setSelectedPosition(listPosition);
        type_vp.smoothScrollToPosition(listPosition);
        type_vp.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                if (position != -1) {
                    child.itemView.setTag(position);
                    listPosition = position;
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
                    Log.v(TAG,listPosition+"");
                }
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
