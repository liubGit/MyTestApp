package com.example.mylibrary_ui.base;

import android.provider.Settings;
import android.view.View;

/**
 * Created by liub on 2018/1/2.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long time= System.currentTimeMillis();
        if (time-lastClickTime<500)
            return;
        lastClickTime=time;
        noDoubleClick(v);
    }
    public abstract void noDoubleClick(View view);
}
