package com.xrefresh_library.Refresh.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xrefresh_library.base.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liub on 2017/11/7 .
 */

public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> list;
    protected Context mContext;
    private int mItemLayoutRes;
    protected BaseListAdapter.OnItemClickListener<T> mOnItemClickListener;

    public BaseListAdapter(Context context, int layoutRes, List<T> list) {
        this.mContext = context;
        this.list = list;
        this.mItemLayoutRes = layoutRes;
        if(this.list == null) {
            this.list = new ArrayList();
        }

    }

    public void add(T t) {
        this.list.add(t);
        this.notifyDataSetChanged();
    }

    public void addAll(List<T> addDatas) {
        if(addDatas != null) {
            this.list.addAll(addDatas);
            this.notifyDataSetChanged();
        }

    }

    public void remove(T t) {
        int position = this.list.indexOf(t);
        if(position != -1) {
            this.list.remove(t);
            this.notifyDataSetChanged();
        } else {
            Log.w("baseRecyclerAdapter", "移除的对象不存在");
        }

    }

    public void replace(List<T> newList) {
        if(newList == null) {
            newList = new ArrayList();
        }

        this.list = (List)newList;
        this.notifyDataSetChanged();
    }

    public abstract void onBindViewHolder(BaseListHolder var1, int var2);

    public int getCount() {
        return this.list.size();
    }

    public T getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final BaseListHolder holder = BaseListHolder.get(this.mContext, convertView, parent, this.mItemLayoutRes, position);
        View view = holder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(BaseListAdapter.this.mOnItemClickListener != null) {
                    BaseListAdapter.this.mOnItemClickListener.onItemClickDeal(holder, BaseListAdapter.this.getItem(position), position);
                }

            }
        });
        this.onBindViewHolder(holder, position);
        return view;
    }

    public void setOnItemClickListener(BaseListAdapter.OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public abstract static class OnItemClickListener<T> {
        private long lastClickTime = 0L;

        public OnItemClickListener() {
        }

        public void onItemClickDeal(BaseListHolder holder, T data, int position) {
            long time = System.currentTimeMillis();
            if(time - this.lastClickTime >= ConstantValue.CLICK_SPACE) {
                this.lastClickTime = time;
                this.onItemClick(holder, data, position);
            }
        }

        public abstract void onItemClick(BaseListHolder var1, T var2, int var3);
    }
}

