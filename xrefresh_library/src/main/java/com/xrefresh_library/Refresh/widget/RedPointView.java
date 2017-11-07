package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.xrefresh_library.R;

/**
 * Created by liub on 2017/11/7 .
 */

public class RedPointView extends AppCompatTextView {
    public RedPointView(Context context) {
        super(context);
        this.init();
    }

    public RedPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        this.setBackgroundResource(R.drawable.lib_red_point);
        this.setPadding(this.getSize(3), 0, this.getSize(3), 0);
        this.setTextSize(2, 8.0F);
        this.setMinWidth((int)(this.getResources().getDisplayMetrics().density * 15.0F));
        this.setTextColor(-1);
        this.setGravity(17);
    }

    public RedPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private int getSize(int size) {
        return (int)(this.getResources().getDisplayMetrics().density * (float)size);
    }
}

