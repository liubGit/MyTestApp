package com.xrefresh_library.Refresh.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xrefresh_library.Refresh.LoadingView;

import java.util.List;

import static android.view.View.MeasureSpec;
import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Created by liub on 2017/11/7 .
 */

public abstract class BaseHeaderAdapter <T> extends BaseLoadMoreViewAdapter<T> {
    private static final int TYPE_HEADER = 1;
    private View mHeaderView;
    private boolean showNodataView;

    public BaseHeaderAdapter(Context context, int itemLayout, List<T> list) {
        super(context, itemLayout, list);
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public void showNoDataView(boolean show) {
        this.showNodataView = show;
        if(show) {
            this.resizeLoadingView(this.loadingView);
        }

    }

    private void resizeLoadingView(LoadingView loadingView) {
        if(loadingView != null) {
            int height;
            label18: {
                ViewGroup.LayoutParams params = this.mHeaderView.getLayoutParams();
                if(params != null) {
                    height = params.height;
                    if(params.height > 0) {
                        break label18;
                    }
                }

                int layoutParams = MeasureSpec.makeMeasureSpec(1073741823, AT_MOST);
                int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1073741823, AT_MOST);
                this.mHeaderView.measure(layoutParams, heightMeasureSpec);
                height = this.mHeaderView.getMeasuredHeight();
            }

            ViewGroup.LayoutParams layoutParams1 = loadingView.getChildAt(1).getLayoutParams();
            if(layoutParams1 instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams)layoutParams1).topMargin = height;
                loadingView.getChildAt(1).requestLayout();
            }

        }
    }
}
