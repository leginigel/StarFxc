package com.stars.tv.sample;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

public class SeriesVideoListRow extends ListRow {
    public SeriesVideoListRow(ObjectAdapter adapter) {
        super(adapter);
    }
    public SeriesVideoListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }
}

