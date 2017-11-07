package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrefresh_library.R;
import com.xrefresh_library.base.NoDoubleClickListener;
import com.xrefresh_library.Refresh.HMLoadingDrawable;

/**
 * Created by liub on 2017/11/6 .
 */

public class  FooterView extends LinearLayout {
    private TextView loadingStatusText;
    private ImageView loadingImage;
    private View footerPaddingView;
    private HMLoadingDrawable loadingDrawable;
    private boolean firstShow;

    public FooterView(Context context) {
        super(context);
        init(context, (AttributeSet)null);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.lib_layout_footer, this);
        loadingStatusText = (TextView)findViewById(R.id.tv_footer_status);
        loadingImage = (ImageView)findViewById(R.id.iv_footer_loading);
        footerPaddingView = findViewById(R.id.footer_padding);
        loadingDrawable = new HMLoadingDrawable(getContext());
        loadingImage.setImageDrawable(loadingDrawable);
        setVisibility(GONE);
        setAlpha(0.0F);
    }

    public void setLoadComplete() {
        setVisible();
        footerPaddingView.setVisibility(VISIBLE);
        loadingStatusText.setVisibility(VISIBLE);
        loadingStatusText.setText("已加载全部内容");
        loadingStatusText.setOnClickListener((View.OnClickListener)null);
        post(new Runnable() {
            public void run() {
                FooterView.this.loadingDrawable.stop();
            }
        });
    }

    private void setVisible() {
        if(!firstShow) {
            setVisibility(VISIBLE);
            post(new Runnable() {
                public void run() {
                    ViewCompat.animate(FooterView.this).alpha(1.0F).setDuration(580L).start();
                }
            });
            firstShow = true;
        }

    }

    public void setLoading() {
        setVisible();
        post(new Runnable() {
            public void run() {
                FooterView.this.setVisibility(VISIBLE);
                FooterView.this.footerPaddingView.setVisibility(GONE);
                FooterView.this.loadingStatusText.setVisibility(GONE);
                FooterView.this.loadingDrawable.stop();
            }
        });
    }

    public void startLoading() {
        setVisible();
        footerPaddingView.setVisibility(GONE);
        loadingStatusText.setVisibility(GONE);
        loadingDrawable.start();
    }

    public void setLoadFail(final LoadMoreRecyclerView.OnLoadMoreListener onLoadMoreListener) {
        setVisible();
        footerPaddingView.setVisibility(GONE);
        loadingStatusText.setVisibility(VISIBLE);
        loadingStatusText.setText("加载失败，请点击重试");
        loadingStatusText.setOnClickListener(new NoDoubleClickListener() {
            public void noDoubleClick(View v) {
                if(onLoadMoreListener != null) {
                    FooterView.this.startLoading();
                    onLoadMoreListener.onLoadMore();
                }

            }
        });
        post(new Runnable() {
            public void run() {
                FooterView.this.loadingDrawable.stop();
            }
        });
    }
}
