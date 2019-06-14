package com.stars.tv.sample;

import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;

public class FilmVideoListRow extends ListRow {
    public FilmVideoListRow(ObjectAdapter adapter) {
        super(adapter);
    }
    public FilmVideoListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }
}

