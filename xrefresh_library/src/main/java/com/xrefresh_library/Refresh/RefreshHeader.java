package com.xrefresh_library.Refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by liub on 2017/11/6 .
 */

public abstract class RefreshHeader extends FrameLayout {
    public RefreshHeader(@NonNull Context context) {
        super(context);
    }

    public RefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void onUnderState(boolean var1);

    public abstract void onOverState(boolean var1);

    public abstract void onRefreshingState(boolean var1);

    public abstract void onClosingState();
}
