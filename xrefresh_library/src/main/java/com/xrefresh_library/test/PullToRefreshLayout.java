//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.xrefresh_library.test;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class PullToRefreshLayout extends FrameLayout {
    private static final String TAG = "pullToRefresh";
    private static final int STATE_UNDER_DRAG = 0;
    private static final int STATE_REFRESHING = 1;
    private static final int STATE_OVER_DRAG = 2;
    private static final int STATE_CLOSE_DRAG = 3;
    private static final float DRAG_RATE = 0.5F;
    private RefreshHeader refreshHead;
    private int headerHeight;
    protected View childView;
    protected OnRefreshListener onRefreshListener;
    private int triggerOffset;
    private float downX;
    private float downY;
    private Scroller mScroller;
    protected boolean refreshing = false;
    private boolean isBeingDragged = false;
    private int dragStatus = 3;
    private PullToRefreshLayout.OnChildScrollUpCallback mChildScrollUpCallback;
    private PullToRefreshLayout.OnHeaderShouldMoveListener onHeaderShouldMoveListener;

    public PullToRefreshLayout(Context context) {
        super(context);
        this.init(context, (AttributeSet) null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mScroller = new Scroller(context);
        this.triggerOffset = context.getResources().getDimensionPixelOffset(dimen.refresh_trigger_offset);
    }

    @CallSuper
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (this.getChildCount() > 1) {
            throw new IllegalArgumentException("PullToRefreshLayout的子View只能有一个");
        } else {
            if (this.getChildCount() != 0) {
                this.childView = this.getChildAt(0);
            }

            this.refreshHead = (RefreshHeader) LayoutInflater.from(this.getContext()).inflate(layout.default_header, this, false);
            this.refreshHead.setLayoutParams(new LayoutParams(-1, this.headerHeight = this.getHeaderHeight()));
            this.addView(this.refreshHead);
            ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        if (this.refreshHead != null) {
            this.refreshHead.layout(this.getPaddingLeft(), -this.headerHeight, width - this.getPaddingRight(), 0);
        }

        if (this.childView != null && this.shouldLayoutChildView()) {
            int childLeft = this.getPaddingLeft();
            int childTop = this.getPaddingTop();
            int childWidth = width - this.getPaddingLeft() - this.getPaddingRight();
            int childHeight = height - this.getPaddingTop() - this.getPaddingBottom();
            this.childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }

    }

    protected boolean shouldLayoutChildView() {
        return true;
    }

    public void setRefreshing(boolean refreshing) {
        this.setRefreshing(refreshing, false);
    }

    public boolean getRefreshing() {
        return this.refreshing;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnChildScrollUpCallback(@Nullable PullToRefreshLayout.OnChildScrollUpCallback callback) {
        this.mChildScrollUpCallback = callback;
    }

    public void setOnHeaderShouldMoveListener(PullToRefreshLayout.OnHeaderShouldMoveListener onHeaderShouldMoveListener) {
        this.onHeaderShouldMoveListener = onHeaderShouldMoveListener;
    }

    public void setTriggerOffset(int offset) {
        this.triggerOffset = offset;
    }

    public void setRefreshHeader(RefreshHeader header) {
        this.removeView(this.refreshHead);
        this.refreshHead = header;
        this.addView(this.refreshHead);
    }

    public void setRefreshable(boolean refreshable) {
        this.setEnabled(refreshable);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isEnabled() && !this.canChildScrollUp() && !this.refreshing) {
            switch (event.getAction()) {
                case 0:
                    this.isBeingDragged = false;
                    this.downX = event.getX();
                    this.downY = event.getY();
                    break;
                case 1:
                case 3:
                    this.isBeingDragged = false;
                    break;
                case 2:
                    this.isBeingDragged = this.moveVertically(event);
            }

            return this.isBeingDragged;
        } else {
            return false;
        }
    }

    protected boolean canChildScrollUp() {
        return this.mChildScrollUpCallback != null ? this.mChildScrollUpCallback.canChildScrollUp(this, this.childView) : (this.childView != null && this.childView instanceof RecyclerView ? ((RecyclerView) this.childView).getChildCount() != 0 && ((RecyclerView) this.childView).getChildAt(0).getTop() != 0 : this.childView != null && ViewCompat.canScrollVertically(this.childView, -1));
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                return true;
            case 1:
            case 3:
                if (this.dragStatus == 2) {
                    this.triggerRefreshing();
                } else {
                    this.setRefreshing(false);
                }

                return true;
            case 2:
                float offsetY = event.getY() - this.downY;
                this.startDragging(offsetY);
                this.downX = event.getX();
                this.downY = event.getY();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private int getHeaderHeight() {
        return (int) ((float) this.getResources().getDisplayMetrics().heightPixels * 0.5F);
    }

    private void triggerRefreshing() {
        this.refreshHead.onRefreshingState(this.dragStatus != 1);
        this.dragStatus = 1;
        this.setRefreshing(true, true);
    }

    private void setRefreshing(boolean refreshing, boolean notify) {
        this.refreshing = refreshing;
        if (refreshing) {
            this.refreshHead.onRefreshingState(this.dragStatus != 1);
            this.dragStatus = 1;
            this.smoothScrollTo(0, -this.triggerOffset);
            if (this.onRefreshListener != null && notify) {
                this.onRefreshListener.onRefresh();
            }
        } else {
            this.smoothScrollTo(0, 0);
            this.dragStatus = 3;
            this.refreshHead.onClosingState();
        }

    }

    private void startDragging(float offsetY) {
        if (this.getScrollY() > 0) {
            this.smoothScrollBy(0, 0);
        } else {
            this.smoothScrollBy(0, (int) (-offsetY * 0.5F));
        }

        if (-this.getScrollY() > this.triggerOffset) {
            this.refreshHead.onOverState(this.dragStatus != 2);
            this.dragStatus = 2;
        } else {
            this.refreshHead.onUnderState(this.dragStatus != 0);
            this.dragStatus = 0;
        }

    }

    private boolean moveVertically(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return this.isBeingDragged || Math.abs(x - this.downX) < y - this.downY && y - this.downY > 0.0F;
    }

    private void smoothScrollTo(int fx, int fy) {
        int dx = fx - this.mScroller.getFinalX();
        int dy = fy - this.mScroller.getFinalY();
        this.smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), dx, dy);
        if (this.onHeaderShouldMoveListener != null) {
            this.onHeaderShouldMoveListener.onMove((float) dy);
        }

        Log.i("pullToRefresh", "smoothScrollBy: " + dy);
        this.invalidate();
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            this.scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            this.postInvalidate();
        }

    }

    public interface OnHeaderShouldMoveListener {
        void onMove(float var1);
    }

    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(PullToRefreshLayout var1, @Nullable View var2);
    }
}
