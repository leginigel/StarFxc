package com.stars.tv.adapter;

import android.util.Log;

import java.util.List;

/**
 * Created by Alice on 2019/4/25.
 */
public abstract class EpisodeListViewAdapter<T>{

    private ChildrenAdapter mChildrenAdapter;
    private ParentAdapter mParentAdapter;

    public EpisodeListViewAdapter() {
        mChildrenAdapter = new ChildrenAdapter(getChildrenList());
        mParentAdapter = new ParentAdapter(getParentList());
    }

    public ChildrenAdapter getEpisodesAdapter() {
        return mChildrenAdapter;
    }

    public ParentAdapter getGroupAdapter() {
        return mParentAdapter;
    }

    public void setSelectedPositions(List<Integer> positions) {
        mChildrenAdapter.setSelectedPositions(positions);
    }

    public abstract List<String> getChildrenList();

    public abstract List<String> getParentList();

    public abstract int getChildrenPosition(int childPosition);

    public abstract int getParentPosition(int parentPosition);

}
