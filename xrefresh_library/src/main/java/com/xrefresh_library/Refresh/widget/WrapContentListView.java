package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Created by liub on 2017/11/7 .
 */

class WrapContentListView extends ListView {
    public WrapContentListView(Context context) {
        super(context);
    }

    public WrapContentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(536870911, AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}