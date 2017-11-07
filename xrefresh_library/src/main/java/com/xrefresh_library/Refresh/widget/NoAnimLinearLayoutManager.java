package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by liub on 2017/11/7 .
 */

class NoAnimLinearLayoutManager extends LinearLayoutManager {
    public NoAnimLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoAnimLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NoAnimLinearLayoutManager(Context context) {
        super(context);
    }

    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}

