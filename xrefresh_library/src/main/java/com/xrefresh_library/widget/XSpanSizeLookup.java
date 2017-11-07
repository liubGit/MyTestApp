package com.xrefresh_library.widget;

import android.support.v7.widget.GridLayoutManager;

import com.xrefresh_library.base.BaseXRecyclerAdapter;

/**
 * use this class to let the footerview have full width
 */
public class XSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private BaseXRecyclerAdapter adapter;
    private int mSpanSize = 1;

    public XSpanSizeLookup(BaseXRecyclerAdapter adapter, int spanSize) {
        this.adapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = adapter.isFooter(position) || adapter.isHeader(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }
}