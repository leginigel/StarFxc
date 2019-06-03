package com.stars.tv.sample;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

public class PortraitVideoListRow extends ListRow {
    public PortraitVideoListRow(ObjectAdapter adapter) {
        super(adapter);
    }
    public PortraitVideoListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }
}

