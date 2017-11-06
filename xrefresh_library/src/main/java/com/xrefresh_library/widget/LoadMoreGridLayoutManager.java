package com.xrefresh_library.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xrefresh_library.base.BaseLoadMoreViewAdapter;


/**
 * Created by liub on 2017/11/2 .
 * 下拉加载更多的网格布局管理器
 */
public class LoadMoreGridLayoutManager extends GridLayoutManager {

    public LoadMoreGridLayoutManager(Context context, int spanCount, RecyclerView recyclerView) {
        super(context, spanCount);
        init(spanCount,recyclerView);
    }

    private void init(final int span, final RecyclerView recyclerView) {
        setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof BaseLoadMoreViewAdapter) {
                    if (adapter.getItemViewType(position) == BaseLoadMoreViewAdapter.FOOTER_VIEW_TYPE) {
                        return span;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
    }
}
