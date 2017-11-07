package com.xrefresh_library.Refresh.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xrefresh_library.R;

/**
 * Created by liub on 2017/11/7 .
 */

class VerticalItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpacing;
    private final Paint mPaint;

    public VerticalItemDecoration(@Px int verticalSpace, int color) {
        this.mSpacing = verticalSpace;
        this.mPaint = new Paint();
        this.mPaint.setColor(color);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = this.mSpacing;
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for(int i = 0; i < childCount - 1; ++i) {
            View view = parent.getChildAt(i);
            float top = (float)view.getBottom();
            float bottom = (float)(view.getBottom() + this.mSpacing);
            c.drawRect((float)(left + parent.getContext().getResources().getDimensionPixelSize(R.dimen.lib_ui_w_16)), top, (float)right, bottom, this.mPaint);
        }

    }
}
