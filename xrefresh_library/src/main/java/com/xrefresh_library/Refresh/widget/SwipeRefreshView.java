package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.xrefresh_library.R;
import com.xrefresh_library.Refresh.base.BaseLoadMoreViewAdapter;

/**
 * Created by liub on 2017/11/6 .
 */

public class SwipeRefreshView extends SwipeRefreshLayout {
    private LoadMoreRecyclerView mLoadMoreListView;
    private View mScrollableChild;
    private int mScrollableChildId;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(new int[]{context.getResources().getColor(R.color.colorPrimary)});
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefreshView);
        mScrollableChildId = a.getResourceId(R.styleable.SwipeRefreshView_scrollableChildId, 0);
        if(mScrollableChildId != 0) {
            mScrollableChild = findViewById(mScrollableChildId);
        }

        a.recycle();
    }

    public void setLoadMoreListView(LoadMoreRecyclerView mLoadMoreListView) {
        mLoadMoreListView = mLoadMoreListView;
    }

    public void setPaddingOnTransparentMode() {
        setProgressViewOffset(false, getResources().getDimensionPixelOffset(R.dimen.h_25), getResources().getDimensionPixelOffset(R.dimen.h_88));
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(mLoadMoreListView != null) {
            RecyclerView.Adapter adapter = mLoadMoreListView.getAdapter();
            if(adapter != null && adapter instanceof BaseLoadMoreViewAdapter && ((BaseLoadMoreViewAdapter)adapter).isLoading()) {
                return false;
            }
        }

        return super.onTouchEvent(event);
    }

    public boolean canChildScrollUp() {
        if(mScrollableChild == null) {
            if(mLoadMoreListView != null) {
                mScrollableChild = mLoadMoreListView;
            } else if(mScrollableChildId != 0) {
                mScrollableChild = findViewById(mScrollableChildId);
            }
        }

        if(mScrollableChild == null) {
            Log.i("refresh", "canscrollup: true, child is null");
            return super.canChildScrollUp();
        } else if(mScrollableChild instanceof RecyclerView) {
            RecyclerView.Adapter layoutManager1 = ((RecyclerView)mScrollableChild).getAdapter();
            if(layoutManager1 == null) {
                return false;
            }
            /*else if(layoutManager1 instanceof BaseHeaderAdapter) {
                return ((BaseHeaderAdapter)layoutManager1).getHeaderView() != null && ((BaseHeaderAdapter)layoutManager1).getHeaderView().getTop() < mScrollableChild.getPaddingTop();
            } */
            else {
                LayoutManager canScrollUp1 = ((RecyclerView)mScrollableChild).getLayoutManager();
                return canScrollUp1 instanceof GridLayoutManager ?canScrollUp1.getItemCount() > 0 && ((GridLayoutManager)canScrollUp1).findFirstVisibleItemPosition() > 0:(canScrollUp1 instanceof LinearLayoutManager ?canScrollUp1.getItemCount() > 0 && ((LinearLayoutManager)canScrollUp1).findFirstVisibleItemPosition() > 0:canScrollUp1.getItemCount() > 0 && canScrollUp1.getChildAt(0).getTop() < mScrollableChild.getPaddingTop());
            }
        } else if(mScrollableChild instanceof ViewGroup && ((ViewGroup)mScrollableChild).getChildAt(0) instanceof RecyclerView) {
            LayoutManager layoutManager = ((RecyclerView)((ViewGroup)mScrollableChild).getChildAt(0)).getLayoutManager();
            if(layoutManager instanceof GridLayoutManager) {
                return layoutManager.getItemCount() > 0 && ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition() > 0;
            } else if(layoutManager instanceof LinearLayoutManager) {
                boolean canScrollUp = layoutManager.getItemCount() > 0 && ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() > 0;
                Log.i("refresh", "canScrollUp: " + canScrollUp);
                return canScrollUp;
            } else {
                return layoutManager.getItemCount() > 0 && layoutManager.getChildAt(0).getTop() < mScrollableChild.getPaddingTop();
            }
        } else {
            return super.canChildScrollUp();
        }
    }
}
