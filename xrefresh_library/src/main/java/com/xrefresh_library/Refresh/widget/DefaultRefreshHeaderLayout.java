package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.xrefresh_library.R;
import com.xrefresh_library.Refresh.HMLoadingDrawable;
import com.xrefresh_library.Refresh.RefreshHeader;

/**
 * Created by liub on 2017/11/7 .
 */

public class DefaultRefreshHeaderLayout extends RefreshHeader {
    private static final String TAG = "Refresh";
    private TextView state;
    private ImageView imageView;
    private HMLoadingDrawable drawable;

    public DefaultRefreshHeaderLayout(@NonNull Context context) {
        super(context);
        this.init(context, (AttributeSet)null);
    }

    public DefaultRefreshHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_default_refresh_header, this);
        this.state = (TextView)this.findViewById(R.id.tv_state);
        this.imageView = (ImageView)this.findViewById(R.id.image);
        this.drawable = new HMLoadingDrawable(context);
        this.imageView.setImageDrawable(this.drawable);
    }

    public void onUnderState(boolean change) {
    }

    public void onOverState(boolean change) {
    }

    public void onRefreshingState(boolean change) {
        if(change) {
            this.drawable.start();
        }

    }

    public void onClosingState() {
        this.post(new Runnable() {
            public void run() {
                DefaultRefreshHeaderLayout.this.drawable.stop();
            }
        });
    }
}
