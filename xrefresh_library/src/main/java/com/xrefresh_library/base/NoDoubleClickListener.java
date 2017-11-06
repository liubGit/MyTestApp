package com.xrefresh_library.base;

import android.view.View;

/**
 * 防止过快多次点击
 * Created by zeda on 16/3/2.
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    private long lastClickTime = 0;

    @Override
    public synchronized void onClick(View v) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < ConstantValue.CLICK_SPACE)
            return;
        lastClickTime = time;
        noDoubleClick(v);
    }

    public abstract void noDoubleClick(View v);
}
