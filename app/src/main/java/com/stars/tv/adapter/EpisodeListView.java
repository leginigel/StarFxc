package com.stars.tv.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.stars.tv.R;

/**
 * Created by Alice on 2019/4/25.
 */
public class EpisodeListView extends RelativeLayout implements View.OnFocusChangeListener {

    public static final String TAG = EpisodeListView.class.getSimpleName();

    private Context mContext;
    private RelativeLayout mContentPanel;
    private RecyclerView mChildrenView;
    private RecyclerView mParentView;
    private LinearLayoutManager mEpisodesLayoutManager;
    private LinearLayoutManager mGroupLayoutManager;

    private EpisodeListViewAdapter mEpisodeListAdapter;
    private ChildrenAdapter mChildrenAdapter;
    private ParentAdapter mParentAdapter;
    private int groupPosition;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public EpisodeListView(Context context) {
        this(context, null);
    }

    public EpisodeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EpisodeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            mContext = context;
            init();
        }
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.episode_list, this, true);

        mChildrenView = (RecyclerView) findViewById(R.id.episodes);
        mParentView = (RecyclerView) findViewById(R.id.groups);

        mEpisodesLayoutManager = new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false);
        mGroupLayoutManager = new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false);

        mChildrenView.setLayoutManager(mEpisodesLayoutManager);
        mParentView.setLayoutManager(mGroupLayoutManager);

        mChildrenView.setItemAnimator(new DefaultItemAnimator());
        mParentView.setItemAnimator(new DefaultItemAnimator());

        mChildrenView.setOnFocusChangeListener(this);
        mParentView.setOnFocusChangeListener(this);
        this.setOnFocusChangeListener(this);
    }


    public void setAdapter(final EpisodeListViewAdapter adapter) {
        mEpisodeListAdapter = adapter;
        mChildrenAdapter = adapter.getEpisodesAdapter();
        mParentAdapter = adapter.getGroupAdapter();
        mChildrenView.setAdapter(mChildrenAdapter);
        mParentView.setAdapter(mParentAdapter);

        mParentAdapter.setOnItemClickListener(new ParentAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClick(View view, int position) {
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getChildrenPosition(position), 0);
            }
        });

        mParentAdapter.setOnItemFocusListener(new ParentAdapter.OnItemFocusListener() {
            @Override
            public void onGroupItemFocus(View view, int position, boolean hasFocus) {
                int episodePosition = adapter.getChildrenPosition(position);
                mChildrenAdapter.setCurrentPosition(episodePosition);
                mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getChildrenPosition(position), 0);
            }
        });

        mChildrenAdapter.setOnItemFocusListener(new ChildrenAdapter.OnItemFocusListener() {
            @Override
            public void onEpisodesItemFocus(View view, int position, boolean hasFocus) {
                if (hasFocus) {
                    for (int i = 0; i < (mParentAdapter.getDatas().size()); i++) {
                        View parent = mParentView.getLayoutManager().findViewByPosition(i);
                        parent.setSelected(false);
                    }

                    groupPosition = adapter.getParentPosition(position);
                    mParentAdapter.setCurrentPosition(adapter.getParentPosition(groupPosition));
                    mGroupLayoutManager.scrollToPositionWithOffset(groupPosition, 0);
                    View parent = mParentView.getLayoutManager().findViewByPosition(groupPosition);
                    parent.setSelected(true);
                    mEpisodesLayoutManager.scrollToPositionWithOffset(adapter.getChildrenPosition(groupPosition), 0);
                }
            }
        });
    }

    public void setChildrenItemClickListener(ChildrenAdapter.OnItemClickListener listener) {
        mChildrenAdapter.setOnItemClickListener(listener);
    }

    public void setLongFocusListener(ChildrenAdapter.OnItemLongFocusListener listener) {
        mChildrenAdapter.setOnItemLongFocusListener(listener);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (mParentView.hasFocus()) {
                        View parent = mParentView.getLayoutManager().findViewByPosition(mParentAdapter.getCurrentPosition());
                        parent.setSelected(true);
                        mChildrenView.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (mChildrenView.hasFocus()) {
                        View parent;
                        for (int i = 0; i < (mParentAdapter.getDatas().size()); i++) {
                            parent = mParentView.getLayoutManager().findViewByPosition(i);
                            parent.setSelected(false);
                        }
                        parent = mParentView.getLayoutManager().findViewByPosition(groupPosition);
                        parent.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mChildrenView.hasFocus()
                            && mChildrenAdapter.getCurrentPosition() >= mChildrenAdapter.getData().size() - 1) {
                        return true;
                    }
                    if (mParentView.hasFocus()
                            && mParentAdapter.getCurrentPosition() >= mParentAdapter.getDatas().size() - 1) {
                        return true;
                    }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus) {
            mChildrenView.requestFocus();
        } else if (v == mChildrenView && hasFocus) {
            View child = mChildrenView.getLayoutManager().findViewByPosition(mChildrenAdapter.getCurrentPosition());
            if (child != null) {
                child.requestFocus();
            } else {
                int lastPosition = mEpisodesLayoutManager.findLastVisibleItemPosition();
                child = mEpisodesLayoutManager.findViewByPosition(lastPosition);
                if (child != null)
                    child.requestFocus();
            }
        } else if (v == mParentView && hasFocus) {
            View parent = mParentView.getLayoutManager().findViewByPosition(mParentAdapter.getCurrentPosition());
            if (parent != null) {
                parent.requestFocus();
            }
        }
    }
}
