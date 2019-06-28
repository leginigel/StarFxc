package com.stars.tv.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import com.stars.tv.R;
import com.stars.tv.fragment.SearchMoviceFragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchMoviceActivity extends BaseActivity{
    private SearchMoviceFragment mSearchMoviceFragment;
    Unbinder unbinder;

    public SearchMoviceActivity(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movice);
        ButterKnife.bind(this);
        initSearchFrame();
    }

    private void initSearchFrame(){
        mSearchMoviceFragment = new SearchMoviceFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contentlay, mSearchMoviceFragment)
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.v("ttt",event.toString());
        if (mSearchMoviceFragment.onKeyDown(event)) {
            Log.v("ttt",event.toString());
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( unbinder != null )
            unbinder.unbind();
    }
}
