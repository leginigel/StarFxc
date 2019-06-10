package com.stars.tv.sample;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

public class LandscapeVideoListRow extends ListRow {
    public LandscapeVideoListRow(ObjectAdapter adapter) {
        super(adapter);
    }
    public LandscapeVideoListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }
}

