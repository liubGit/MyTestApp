package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by liub on 2017/11/7 .
 */

class SimpleSquareDraweeView extends SimpleDraweeView {
    public SimpleSquareDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
