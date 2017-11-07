package com.xrefresh_library.Refresh.widget;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xrefresh_library.R;

/**
 * Created by liub on 2017/11/6 .
 */

public class AlertToast {
    public static final int DEFAULT_DURATION = 3000;
    private View mView;
    private TextView toastText;
    private ImageView toastImage;
    private int duration = 3000;
    private ViewGroup parent;
    private boolean showText = true;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            AlertToast.this.animateOut();
        }
    };

    private AlertToast(ViewGroup parent) {
        this.parent = parent;
    }

    public static AlertToast make(@NonNull View view, CharSequence message) {
        AlertToast alertToast = new AlertToast(findSuitableParent(view));
        alertToast.setText(message);
        return alertToast;
    }

    public static AlertToast make(@NonNull View view, @NonNull View contentView) {
        AlertToast alertToast = new AlertToast(findSuitableParent(view));
        alertToast.setShowText(false);
        alertToast.setContentView(contentView);
        return alertToast;
    }

    public AlertToast setText(CharSequence message) {
        if(this.showText) {
            if(this.mView == null) {
                this.mView = LayoutInflater.from(this.parent.getContext()).inflate(R.layout.lib_ui_toast_view, this.parent, false);
                this.toastText = (TextView)this.mView.findViewById(R.id.tv_toast);
                this.toastImage = (ImageView)this.mView.findViewById(R.id.iv_toast);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
                params.gravity = 17;
                params.leftMargin = this.parent.getContext().getResources().getDimensionPixelSize(R.dimen.w_50);
                params.rightMargin = this.parent.getContext().getResources().getDimensionPixelSize(R.dimen.w_50);
                this.mView.setLayoutParams(params);
            }

            this.toastText.setText(message);
        }

        return this;
    }

    public AlertToast setContentView(@NonNull View view) {
        this.mView = view;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = 17;
        params.leftMargin = this.parent.getContext().getResources().getDimensionPixelSize(R.dimen.w_50);
        params.rightMargin = this.parent.getContext().getResources().getDimensionPixelSize(R.dimen.w_50);
        this.mView.setLayoutParams(params);
        return this;
    }

    public AlertToast setDrawable(Drawable drawable) {
        if(!this.showText) {
            return this;
        } else if(drawable == null) {
            return this;
        } else {
            this.toastImage.setVisibility(View.VISIBLE);
            this.toastImage.setImageDrawable(drawable);
            return this;
        }
    }

    public AlertToast setDuration(int duration) {
        if(duration <= 0) {
            return this;
        } else {
            this.duration = duration;
            return this;
        }
    }

    public void show() {
        this.parent.post(new Runnable() {
            public void run() {
                if(AlertToast.this.mView.getParent() != null) {
                    ((ViewGroup)AlertToast.this.mView.getParent()).removeView(AlertToast.this.mView);
                }

                AlertToast.this.mView.setVisibility(View.VISIBLE);
                AlertToast.this.parent.addView(AlertToast.this.mView);
                AlertToast.this.animateIn();
            }
        });
        this.mHandler.sendEmptyMessageDelayed(0, (long)this.duration);
    }

    private void setShowText(boolean showText) {
        this.showText = showText;
    }

    private void animateIn() {
        ViewCompat.setAlpha(this.mView, 0.0F);
        ViewCompat.animate(this.mView).alpha(1.0F).setDuration(180L).start();
        ViewCompat.setScaleX(this.mView, 0.8F);
        ViewCompat.animate(this.mView).scaleX(1.0F).setDuration(180L).start();
        ViewCompat.setScaleY(this.mView, 0.8F);
        ViewCompat.animate(this.mView).scaleY(1.0F).setDuration(180L).start();
    }

    private void animateOut() {
        ViewCompat.animate(this.mView).alpha(0.0F).setDuration(180L).setListener(new ViewPropertyAnimatorListener() {
            public void onAnimationStart(View view) {
            }

            public void onAnimationEnd(View view) {
                ViewCompat.animate(AlertToast.this.mView).setListener((ViewPropertyAnimatorListener)null);
                AlertToast.this.mView.setVisibility(View.GONE);
            }

            public void onAnimationCancel(View view) {
            }
        }).start();
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;

        do {
            if(view instanceof FrameLayout) {
                if(view.getId() == R.id.layout_status) {
                    return (ViewGroup)view;
                }

                fallback = (ViewGroup)view;
            }

            if(view != null) {
                ViewParent parent = view.getParent();
                view = parent instanceof View?(View)parent:null;
            }
        } while(view != null);

        return fallback;
    }
}