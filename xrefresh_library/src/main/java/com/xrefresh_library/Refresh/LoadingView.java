package com.xrefresh_library.Refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrefresh_library.R;
import com.xrefresh_library.base.NoDoubleClickListener;
import com.xrefresh_library.Refresh.widget.AlertToast;
import com.xrefresh_library.Refresh.widget.LoadMoreRecyclerView;

/**
 * Created by liub on 2017/11/6 .
 */
public class LoadingView extends PullToRefreshLayout {
    public static final String TAG_LOADING_VIEW = "loadingView";
    public static final int SUCCESS = 1;
    public static final int NOT_NET = -1;
    public static final int NOT_DATA = -2;
    public static final int FAILURE = -3;
    public static final int TIME_OUT = -4;
    private TextView mHrefButton;
    private AppCompatImageView mStatusImage;
    private TextView mStatusText;
    private FrameLayout mContainer;
    private LinearLayout mStatusLayout;
    private HMLoadingDrawable loadingDrawable;
    private boolean coverStyle = false;
    private int currStatus = 0;
    private String DefaultNoDataMessage;
    private String DefaultNoNetMessage;
    private String DefaultFailureMessage;
    private String DefaultTimeOutMessage;
    private int errorResourceId;
    private int nodataResourceId;
    private int showButton;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        errorResourceId = R.mipmap.ic_new_nodata;
        nodataResourceId = R.drawable.bg_default_no_data;
        showButton = -1;
        init(context, attrs);
    }

    public LoadingView(Context context) {
        super(context);
        errorResourceId = R.mipmap.ic_new_nodata;
        nodataResourceId = R.drawable.bg_default_no_data;
        showButton = -1;
        init(context, (AttributeSet) null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0);
        typedArray.recycle();
        DefaultNoDataMessage = context.getResources().getString(R.string.net_no_data);
        DefaultFailureMessage = context.getResources().getString(R.string.net_failure);
        DefaultNoNetMessage = context.getResources().getString(R.string.net_no_net);
        DefaultTimeOutMessage = context.getResources().getString(R.string.net_time_out);
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                LoadingView.this.setRefreshing(false);
            }
        });
        setTag("loadingView");
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setInTransparent(boolean transparent) {
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 2) {
            throw new IllegalArgumentException("LoadingView下只能有一个Layout");
        } else {
            mContainer = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_new, this, false);
            mStatusLayout = (LinearLayout) mContainer.findViewById(R.id.layout_status);
            mStatusImage = (AppCompatImageView) mContainer.findViewById(R.id.iv_loading_status);
            mStatusText = (TextView) mContainer.findViewById(R.id.tv_loading_status);
            mHrefButton = (TextView) mContainer.findViewById(R.id.btn_href);
            mHrefButton.setOnClickListener(new NoDoubleClickListener() {
                public void noDoubleClick(View v) {
                    LoadingView.this.onLoadStart(LoadingView.this.coverStyle);
                    if (LoadingView.this.onRefreshListener != null) {
                        LoadingView.this.onRefreshListener.onRefresh();
                    }

                }
            });
            if (childView != null) {
                removeView(childView);
                mContainer.addView(childView, 0);
            }

            addView(mContainer);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mContainer.layout(getPaddingLeft(), getPaddingTop(), mContainer.getMeasuredWidth() + getPaddingLeft(), getPaddingTop() + mContainer.getMeasuredHeight());
    }

    protected boolean shouldLayoutChildView() {
        return false;
    }

    public void setChildView(View childView) {
        if (mContainer != null) {
            if (childView != null) {
                mContainer.removeView(childView);
            }

            mContainer.addView(childView, 0);
            childView = childView;
        }

    }

    public void changeStyle(boolean hasData) {
        if (coverStyle) {
            coverStyle = !hasData;
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setNewStandard(boolean newStandard) {
    }

    public void setNoDataMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            DefaultNoDataMessage = msg;
        }
    }

    public void setNoNetMessage(String noNetMessage) {
        if (!TextUtils.isEmpty(noNetMessage)) {
            DefaultNoNetMessage = noNetMessage;
        }
    }

    public void setOnNoNetRetryClickListener(OnClickListener retryClickListener) {
        setHrefButton("点击重试", retryClickListener);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setNoNetImageResource(int resId) {
        setFailureBgIcon(resId);
    }

    public void setFailureMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            DefaultFailureMessage = msg;
        }
    }

    public void setFailureBgIcon(int resourceId) {
        errorResourceId = resourceId;
    }

    public void setNoDataIcon(int resourceId) {
        nodataResourceId = resourceId;
    }

    public void setHrefButton(CharSequence buttonString, OnClickListener listener) {
        boolean empty = TextUtils.isEmpty(buttonString);
        showButton = empty ? 0 : 1;
        if (!empty) {
            mHrefButton.setVisibility(VISIBLE);
            mHrefButton.setText(buttonString);
            mHrefButton.setOnClickListener(listener);
        }
    }

    public TextView getHrefButton() {
        return mHrefButton;
    }

    public void onLoadStart() {
        onLoadStart(false);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void onLoadStart(String loadingText) {
        onLoadStart();
    }

    public void onLoadStart(boolean coverStyle) {
        coverStyle = coverStyle;
        refreshing = true;
        if (!coverStyle) {
            setRefreshing(true);
        } else {
            mStatusLayout.setVisibility(VISIBLE);
            mStatusText.setVisibility(GONE);
            mHrefButton.setVisibility(View.GONE);
            loadingDrawable = new HMLoadingDrawable(getContext());
            mStatusImage.setImageDrawable(loadingDrawable);
            mStatusImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            loadingDrawable.start();
        }

    }

    public void onLoadComplete(int state, String msg) {
        if (msg != null && msg.equals(getResources().getString(R.string.net_error))) {
            currStatus = -1;
            msg = DefaultNoNetMessage;
        } else if (msg != null && msg.equals(getResources().getString(R.string.net_time_out))) {
            currStatus = -4;
            msg = DefaultTimeOutMessage;
        } else {
            currStatus = state;
        }

        dealComplete(msg);
    }

    private void dealComplete(String msg) {
        stopLoading();
        if (TextUtils.isEmpty(msg)) {
            msg = getDefaultMessage();
        }

        switch (currStatus) {
            case -3:
            case -1:
                onLoadFailure(msg);
                break;
            case -2:
                mStatusLayout.setVisibility(VISIBLE);
                mStatusText.setVisibility(VISIBLE);
                mStatusText.setText(msg);
                mStatusImage.setImageResource(nodataResourceId);
                mHrefButton.setVisibility(showButton == 1 ? VISIBLE : mHrefButton.getVisibility());
                break;
            case 0:
            case 1:
            default:
                mStatusLayout.setVisibility(GONE);
        }

    }

    protected void onLoadFailure(String msg) {
        if (coverStyle) {
            mStatusLayout.setVisibility(VISIBLE);
            mStatusText.setVisibility(VISIBLE);
            mStatusText.setText(msg);
            mStatusImage.setImageResource(currStatus == -1 ? R.mipmap.lib_ic_network : (currStatus == -4 ? R.drawable.ic_net_busy : errorResourceId));
            mHrefButton.setVisibility(VISIBLE);
        } else {
            AlertToast.make(this, msg).show();
        }

    }

    private void stopLoading() {
        refreshing = false;
        post(new Runnable() {
            public void run() {
                if (LoadingView.this.coverStyle) {
                    LoadingView.this.loadingDrawable.stop();
                }

                LoadingView.this.setRefreshing(false);
            }
        });
    }

    private String getDefaultMessage() {
        switch (currStatus) {
            case -2:
                return DefaultNoDataMessage;
            case -1:
                return DefaultNoNetMessage;
            default:
                return DefaultFailureMessage;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setMinLoadTime(int minLoadTime) {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean isLoading() {
        return refreshing || childView instanceof LoadMoreRecyclerView && ((LoadMoreRecyclerView) childView).isLoadingMore();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setFailureView(View failureView, TextView failureContentView) {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setFailureSrcIcon(int resoureId) {
        setFailureBgIcon(resoureId);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void resetFailureView() {
    }
}
