package com.xrefresh_library.Refresh.base;

import android.view.View;

/**
 * holderView点击事件
 * Created by zeda on 16/4/8.
 */
public abstract class OnHolderViewClickListener implements View.OnClickListener {

    private BaseRecViewHolder holder;

    public OnHolderViewClickListener(BaseRecViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void onClick(View v) {
        onClick(holder, v);
    }

    public abstract void onClick(BaseRecViewHolder holder, View v);
}
