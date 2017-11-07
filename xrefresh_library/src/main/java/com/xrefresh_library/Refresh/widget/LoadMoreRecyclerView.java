package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xrefresh_library.R;
import com.xrefresh_library.Refresh.base.BaseHeaderAdapter;
import com.xrefresh_library.Refresh.base.BaseLoadMoreViewAdapter;
import com.xrefresh_library.Refresh.LoadingView;
import com.xrefresh_library.Refresh.PullToRefreshLayout;
import com.xrefresh_library.utils.RecyclerViewInitHelper;

/**
 * Created by liub on 2017/11/6 .
 */

public class LoadMoreRecyclerView extends RecyclerView {
    private SwipeRefreshView mRefreshView;
    private LoadMoreRecyclerView.OnLoadMoreListener mOnLoadMoreListener;
    private FooterView footerView;
    private View headerView;
    private boolean loadingMore = false;
    private FrameLayout headerContainer;
    private boolean needLoadMore = true;
    private WrapperAdapter adapter;
    private PullToRefreshLayout pullToRefreshLayout;
    private AppToolbar toolbar;
    private LoadMoreRecyclerView.OnToolbarStyleChangeListener onToolbarStyleChangeListener;
    private int triggerDistance;

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.LoadMoreRecyclerView, 0, 0);
        int layoutManager = a.getInt(R.styleable.LoadMoreRecyclerView_recLayoutManager, 0);
        int span = a.getInt(R.styleable.LoadMoreRecyclerView_recSpan, 3);
        a.recycle();
        if(layoutManager == 0) {
            NoAnimLinearLayoutManager manager = new NoAnimLinearLayoutManager(context);
            manager.setOrientation(1);
            this.setLayoutManager(manager);
        } else {
            RecyclerViewInitHelper.initGridLayoutRecycler(this, span);
        }

        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager)LoadMoreRecyclerView.this.getLayoutManager();
                if(newState == 0) {
                    Adapter adapter = LoadMoreRecyclerView.this.getAdapter();
                    if(adapter == null) {
                        return;
                    }

                    WrapperAdapter wrapperAdapter = (WrapperAdapter)adapter;
                    int last = manager.findLastCompletelyVisibleItemPosition();
                    int total = manager.getItemCount();
                    if(last == total - 1 && !LoadMoreRecyclerView.this.loadingMore && LoadMoreRecyclerView.this.mOnLoadMoreListener != null && wrapperAdapter.isHasMoreData() && LoadMoreRecyclerView.this.needLoadMore && (LoadMoreRecyclerView.this.pullToRefreshLayout == null || !LoadMoreRecyclerView.this.pullToRefreshLayout.getRefreshing())) {
                        LoadMoreRecyclerView.this.loadingMore = true;
                        LoadMoreRecyclerView.this.footerView.startLoading();
                        LoadMoreRecyclerView.this.mOnLoadMoreListener.onLoadMore();
                    }
                }

            }
        });
        this.addOnScrollListener(new OnScrollListener() {
            int scrollY = 0;
            boolean toolbarTransparent = true;

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                this.scrollY += dy;
                if(LoadMoreRecyclerView.this.toolbar != null && LoadMoreRecyclerView.this.onToolbarStyleChangeListener != null) {
                    if(this.scrollY > LoadMoreRecyclerView.this.triggerDistance && dy > 0 && this.toolbarTransparent) {
                        this.toolbarTransparent = false;
                        LoadMoreRecyclerView.this.toolbar.setTintOnScroll(ContextCompat.getColor(LoadMoreRecyclerView.this.getContext(), R.color.text_title));
                        LoadMoreRecyclerView.this.toolbar.setBackGroundAlpha(-1);
                        LoadMoreRecyclerView.this.toolbar.setAlpha(0.4F);
                        ViewCompat.animate(LoadMoreRecyclerView.this.toolbar).alpha(LoadMoreRecyclerView.this.toolbar.getAlpha()).alpha(1.0F).setDuration(200L).start();
                        LoadMoreRecyclerView.this.onToolbarStyleChangeListener.onChangeTransparent(LoadMoreRecyclerView.this.toolbar, this.toolbarTransparent);
                    } else if(this.scrollY <= LoadMoreRecyclerView.this.triggerDistance && dy < 0) {
                        this.toolbarTransparent = true;
                        LoadMoreRecyclerView.this.toolbar.setBackGroundTransparent();
                        ViewCompat.animate(LoadMoreRecyclerView.this.toolbar).alpha(LoadMoreRecyclerView.this.toolbar.getAlpha()).alpha(0.4F).withEndAction(new Runnable() {
                            public void run() {
                                LoadMoreRecyclerView.this.toolbar.setAlpha(1.0F);
                            }
                        }).setDuration(200L).start();
                        LoadMoreRecyclerView.this.onToolbarStyleChangeListener.onChangeTransparent(LoadMoreRecyclerView.this.toolbar, this.toolbarTransparent);
                    }

                }
            }
        });
    }

    public void setToolbar(AppToolbar toolbar, int triggerDistance, LoadMoreRecyclerView.OnToolbarStyleChangeListener listener) {
        this.toolbar = toolbar;
        this.triggerDistance = triggerDistance;
        this.onToolbarStyleChangeListener = listener;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(this.getParent() instanceof LoadingView) {
            this.pullToRefreshLayout = (LoadingView)this.getParent();
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.pullToRefreshLayout = null;
    }

    public void setAdapter(Adapter adapter) {
        int onePageCount;
        if(adapter instanceof BaseLoadMoreViewAdapter) {
            onePageCount = ((BaseLoadMoreViewAdapter)adapter).getOnePageNum();
        } else {
            onePageCount = 20;
        }

        this.footerView = new FooterView(this.getContext());
        this.footerView.setLayoutParams(new LayoutParams(-1, this.needLoadMore?-2:0));
        if(adapter instanceof BaseHeaderAdapter) {
            this.headerView = ((BaseHeaderAdapter)adapter).getHeaderView();
        }

        if(this.headerView != null) {
            this.headerContainer = new FrameLayout(this.getContext());
            this.headerContainer.setLayoutParams(new LayoutParams(-1, -2));
            if(this.headerView.getParent() != null) {
                ((ViewGroup)this.headerView.getParent()).removeView(this.headerView);
            }

            this.headerContainer.addView(this.headerView);
        }

        this.adapter = new WrapperAdapter(this.headerContainer, this.footerView, adapter, onePageCount);
        super.setAdapter(this.adapter);
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        Adapter adapter;
        if(this.headerContainer != null) {
            this.headerContainer.removeAllViews();
            this.headerContainer.addView(headerView);
            adapter = this.getAdapter();
            if(adapter != null) {
                adapter.notifyItemChanged(0);
            }
        } else {
            this.headerContainer = new FrameLayout(this.getContext());
            this.headerContainer.setLayoutParams(new LayoutParams(-1, -2));
            if(headerView.getParent() != null) {
                ((ViewGroup)headerView.getParent()).removeView(headerView);
            }

            this.headerContainer.addView(headerView);
            adapter = this.getAdapter();
            if(adapter != null) {
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            }
        }

    }

    public void setNeedLoadMore(boolean needLoadMore) {
        this.needLoadMore = needLoadMore;
        if(!needLoadMore && this.footerView != null) {
            this.footerView.getLayoutParams().height = 0;
            this.footerView.requestLayout();
        } else if(needLoadMore && this.footerView != null) {
            this.footerView.getLayoutParams().height = -2;
            this.footerView.requestLayout();
        }

    }

    public View getHeaderView() {
        return this.headerView;
    }

    public void setGridLayoutManager(int span) {
        RecyclerViewInitHelper.initGridLayoutRecycler(this, span);
    }

    public void showFooterView(boolean show) {
        if(this.footerView != null && this.footerView.getLayoutParams() != null) {
            this.footerView.getLayoutParams().height = show?-2:0;
            this.footerView.requestLayout();
        }
    }

    public void setFooterHeight(int height) {
        this.footerView.getLayoutParams().height = height;
        this.footerView.requestLayout();
        this.requestLayout();
    }

    /** @deprecated */
    @Deprecated
    public void setRefreshView(SwipeRefreshView mRefreshMoreView) {
        this.mRefreshView = mRefreshMoreView;
    }

    void setLoading(boolean loading) {
        this.loadingMore = loading;
    }

    public boolean isLoadingMore() {
        return this.loadingMore;
    }

    public void setOnLoadMoreListener(LoadMoreRecyclerView.OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    void stopLoadMore() {
        this.loadingMore = false;
        this.footerView.setLoadComplete();
    }

    void onLoadFail() {
        this.stopLoadMore();
        this.footerView.setLoadFail(new LoadMoreRecyclerView.OnLoadMoreListener() {
            public void onLoadMore() {
                if(LoadMoreRecyclerView.this.mOnLoadMoreListener != null) {
                    LoadMoreRecyclerView.this.loadingMore = true;
                    LoadMoreRecyclerView.this.footerView.startLoading();
                    LoadMoreRecyclerView.this.mOnLoadMoreListener.onLoadMore();
                }

            }
        });
    }

    public interface OnToolbarStyleChangeListener {
        void onChangeTransparent(AppToolbar var1, boolean var2);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}