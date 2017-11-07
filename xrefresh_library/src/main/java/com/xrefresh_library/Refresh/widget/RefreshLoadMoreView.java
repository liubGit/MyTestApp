package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.xrefresh_library.R;
import com.xrefresh_library.Refresh.LoadingView;
import com.xrefresh_library.utils.RecyclerViewInitHelper;

/**
 * Created by liub on 2017/11/7 .
 */

public class RefreshLoadMoreView extends LoadingView {
    private SwipeRefreshView mRefresh;
    private LoadMoreRecyclerView mLoadMore;
    private boolean defaultDivider;

    public RefreshLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public RefreshLoadMoreView(Context context) {
        super(context);
        this.init(context, (AttributeSet) null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshLoadMoreView, 0, 0);
        this.defaultDivider = a.getBoolean(R.styleable.RefreshLoadMoreView_defaultDivider, false);
        a.recycle();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLoadMore = (LoadMoreRecyclerView) LayoutInflater.from(this.getContext()).inflate(R.layout.lib_load_more_recycler, this, false);
        this.setDefaultDivider(this.defaultDivider);
        this.setChildView(this.mLoadMore);
    }

    public void setDefaultDivider(boolean enable) {
        if (enable) {
            this.mLoadMore.addItemDecoration(new DividerItemDecoration(this.getContext(), 1));
        }

    }

    public void setGridLayoutManager(int span) {
        RecyclerViewInitHelper.initGridLayoutRecycler(this.mLoadMore, span);
    }

    public void onStartLoad() {
        this.onLoadStart();
    }

    protected void onLoadFailure(String msg) {
        super.onLoadFailure(msg);
        this.mLoadMore.onLoadFail();
    }

    public void onLoadComplete(int state, String msg) {
        super.onLoadComplete(state, msg);
        this.mLoadMore.stopLoadMore();
    }

    public void setHeaderView(View headerView) {
        this.mLoadMore.setHeaderView(headerView);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mLoadMore.setAdapter(adapter);
    }

    public void setNeedLoadMore(boolean needLoadMore) {
        this.mLoadMore.setNeedLoadMore(needLoadMore);
    }

    public void showFooterView(boolean show) {
        this.mLoadMore.showFooterView(show);
    }

    public SwipeRefreshLayout getSwipeRefreshView() {
        return this.mRefresh;
    }

    public void setMinLoadTime(int time) {
    }

    public void setOnLoadMoreListener(LoadMoreRecyclerView.OnLoadMoreListener onLoadMoreListener) {
        this.mLoadMore.setOnLoadMoreListener(onLoadMoreListener);
    }

    public LoadMoreRecyclerView getLoadMoreRecyclerView() {
        return this.mLoadMore;
    }

    public LoadingView getLoadingView() {
        return this;
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        this.mLoadMore.setHasFixedSize(hasFixedSize);
    }
}
