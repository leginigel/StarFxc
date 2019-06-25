package com.stars.tv.sample;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

public class TotalMediaListVideoListRow extends ListRow {
    public TotalMediaListVideoListRow(ObjectAdapter adapter) {
        super(adapter);
    }
    public TotalMediaListVideoListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }
}

