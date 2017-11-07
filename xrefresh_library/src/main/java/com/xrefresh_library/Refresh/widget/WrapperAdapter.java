package com.xrefresh_library.Refresh.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.xrefresh_library.Refresh.base.BaseLoadMoreViewAdapter;
import com.xrefresh_library.Refresh.LoadingView;
import com.xrefresh_library.Refresh.base.BaseRecViewHolder;

/**
 * Created by liub on 2017/11/6 .
 */

public class WrapperAdapter extends Adapter {
    static final int TYPE_HEADER = -999;
    static final int TYPE_FOOTER = -1000;
    private View headerContainer;
    private FooterView footerContainer;
    private LoadingView statusView;
    private LoadMoreRecyclerView loadMoreRecyclerView;
    private RecyclerView.Adapter targetAdapter;
    private int onePageCount = 20;
    private boolean hasMoreData = true;
    private boolean hasHeader = false;
    private AdapterDataObserver observer = new AdapterDataObserver() {
        public void onChanged() {
            if(WrapperAdapter.this.targetAdapter instanceof BaseLoadMoreViewAdapter) {
                WrapperAdapter.this.onePageCount = ((BaseLoadMoreViewAdapter)WrapperAdapter.this.targetAdapter).getOnePageNum();
            }

            WrapperAdapter.this.hasMoreData = WrapperAdapter.this.targetAdapter.getItemCount() >= WrapperAdapter.this.onePageCount;
            WrapperAdapter.this.changeFooterStatus();
            WrapperAdapter.this.changeLoadStatus(WrapperAdapter.this.targetAdapter.getItemCount());
            WrapperAdapter.this.notifyDataSetChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            WrapperAdapter.this.changeFooterStatus();
            WrapperAdapter.this.changeLoadStatus(WrapperAdapter.this.targetAdapter.getItemCount());
            WrapperAdapter.this.notifyItemRangeChanged(WrapperAdapter.this.hasHeader?positionStart + 1:positionStart, itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            WrapperAdapter.this.changeFooterStatus();
            WrapperAdapter.this.changeLoadStatus(WrapperAdapter.this.targetAdapter.getItemCount());
            WrapperAdapter.this.notifyItemRangeChanged(WrapperAdapter.this.hasHeader?positionStart + 1:positionStart, itemCount, payload);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            if(WrapperAdapter.this.targetAdapter instanceof BaseLoadMoreViewAdapter) {
                WrapperAdapter.this.onePageCount = ((BaseLoadMoreViewAdapter)WrapperAdapter.this.targetAdapter).getOnePageNum();
            }

            WrapperAdapter.this.hasMoreData = itemCount >= WrapperAdapter.this.onePageCount;
            WrapperAdapter.this.changeFooterStatus();
            WrapperAdapter.this.changeLoadStatus(WrapperAdapter.this.targetAdapter.getItemCount());
            WrapperAdapter.this.notifyItemRangeInserted(WrapperAdapter.this.hasHeader?positionStart + 1:positionStart, itemCount);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            WrapperAdapter.this.changeFooterStatus();
            WrapperAdapter.this.changeLoadStatus(WrapperAdapter.this.targetAdapter.getItemCount());
            WrapperAdapter.this.notifyItemRangeRemoved(WrapperAdapter.this.hasHeader?positionStart + 1:positionStart, itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            WrapperAdapter.this.changeFooterStatus();
            WrapperAdapter.this.changeLoadStatus(WrapperAdapter.this.targetAdapter.getItemCount());
            WrapperAdapter.this.notifyDataSetChanged();
        }
    };

    WrapperAdapter(View headerContainer, FooterView footerView, RecyclerView.Adapter targetAdapter, int onePageCount) {
        this.onePageCount = onePageCount;
        this.headerContainer = headerContainer;
        this.targetAdapter = targetAdapter;
        this.footerContainer = footerView;
        this.hasHeader = headerContainer != null;
        targetAdapter.registerAdapterDataObserver(this.observer);
        this.setHasStableIds(true);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (ViewHolder)(viewType == -999?new BaseRecViewHolder(this.headerContainer):(viewType == -1000?new BaseRecViewHolder(this.footerContainer):this.targetAdapter.onCreateViewHolder(parent, viewType)));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = this.getItemViewType(position);
        Log.e("type:", position + "-" + viewType);
        if(viewType != -999 && viewType != -1000) {
            this.targetAdapter.onBindViewHolder(holder, this.getPosition(position));
        }

    }

    public long getItemId(int position) {
        return this.getItemViewType(position) == -1000?-1000L:(long)this.getPosition(position);
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.loadMoreRecyclerView = (LoadMoreRecyclerView)recyclerView;

        for(ViewParent targetView = recyclerView.getParent(); targetView != null; targetView = targetView.getParent()) {
            if(targetView instanceof LoadingView) {
                this.statusView = (LoadingView)targetView;
                break;
            }
        }

    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.statusView = null;
    }

    public int getItemViewType(int position) {
        return position == 0 && this.hasHeader?-999:((position != this.targetAdapter.getItemCount() + 1 || !this.hasHeader) && (position != this.targetAdapter.getItemCount() || this.hasHeader)?this.targetAdapter.getItemViewType(this.getPosition(position)):-1000);
    }

    private int getPosition(int position) {
        return this.hasHeader?position - 1:position;
    }

    public int getItemCount() {
        return this.headerContainer == null?this.targetAdapter.getItemCount() + 1:this.targetAdapter.getItemCount() + 2;
    }

    boolean isHasMoreData() {
        return this.hasMoreData;
    }

    private void changeFooterStatus() {
        if(this.hasMoreData) {
            this.footerContainer.setLoading();
        } else {
            this.footerContainer.setLoadComplete();
        }

    }

    private void changeLoadStatus(int itemCount) {
        this.loadMoreRecyclerView.setLoading(false);
        if(this.statusView != null) {
            if(itemCount == 0) {
                this.statusView.onLoadComplete(-2, (String)null);
            } else {
                this.statusView.onLoadComplete(1, (String)null);
                this.statusView.changeStyle(itemCount > 0);
            }
        }
    }
}