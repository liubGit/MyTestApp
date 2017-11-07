package com.xrefresh_library.Refresh.base;

import android.view.View;

/**
 * holderView长按事件
 * Created by zeda on 16/4/8.
 */
public abstract class OnHolderViewLongClickListener implements View.OnLongClickListener {

    private BaseRecViewHolder holder;

    public OnHolderViewLongClickListener(BaseRecViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public boolean onLongClick(View v) {
        return onLongClick(holder, v);
    }

    public abstract boolean onLongClick(BaseRecViewHolder holder, View v);
}
