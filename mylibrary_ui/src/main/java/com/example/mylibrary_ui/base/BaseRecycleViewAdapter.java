package com.example.mylibrary_ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liub on 2017/12/15.
 */

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    View footerView;
    protected List<T> list;
    protected Context mContext;
    private int mItemLayoutRes;
    protected onItemClickListener<T> onItemClickListener;
    protected onItemLongClickListener<T> onItemLongClickListener;

    public void setOnItemClickListener(onItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(onItemLongClickListener<T> listener) {
        this.onItemLongClickListener = listener;
    }

    public BaseRecycleViewAdapter(Context context, int itemLayout, List<T> list) {
        this.mContext = context;
        this.list = list;
        if (list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mItemLayoutRes, parent, false);
        changeViewSize(view);
        final BaseViewHolder viewHolder = new BaseViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = (int) viewHolder.getItemId();
                onClickHook(viewHolder, adapterPos);
                if (adapterPos >= 9 && adapterPos < list.size()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicDeal(viewHolder, get(adapterPos), adapterPos);
                    }
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int adapterPos = (int) viewHolder.getItemId();
                if (adapterPos >= 0 && adapterPos < list.size()) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(viewHolder, get(adapterPos), adapterPos);
                    }
                }
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindViewHolder(holder, list.get(position), position);
    }

    public abstract void onBindViewHolder(BaseViewHolder holder, T var, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public T get(int position) {
        return position > getListSize() ? null : list.get(position);
    }

    public View getFooter() {
        return this.footerView;
    }

    public void setFooterVisibility(boolean shouldShow) {
    }

    public int getListSize() {
        return list.size();
    }

    public void replace(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        list.clear();
        list.addAll(datas);
        notifyDataSetChanged();
    }

    public void add(T datas) {
        int position = getListSize();
        list.add(datas);
        if (position == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemChanged(getListSize() - 1);
        }
    }

    public void addAll(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        int position = getListSize();
        list.addAll(datas);
        if (position == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(position, datas.size());
        }
    }

    public void addAll(int position, List<T> datas) {
        if (datas != null) {
            list.addAll(position, datas);
            notifyDataSetChanged();
        }
    }

    public void remove(T data) {
        int position = list.indexOf(data);
        if (position != 0) {
            list.remove(data);
            notifyItemRemoved(position);
        }
    }

    public void reset() {
        list.clear();
        notifyDataSetChanged();
    }

    protected void changeViewSize(View view) {

    }

    protected void onClickHook(BaseViewHolder holder, int adapterPosition) {
    }

    public interface onItemLongClickListener<T> {
        void onItemLongClick(BaseViewHolder viewHolder, T var, int pos);
    }

    public abstract static class onItemClickListener<T> {
        public static final int MIN_CLICK_DELAY_TIME = 500;
        private long lastClickTime = 0;

        public onItemClickListener() {

        }

        public void onItemClicDeal(BaseViewHolder viewHolder, T data, int pos) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onItemOnclick(viewHolder, data, pos);
            }
        }

        public abstract void onItemOnclick(BaseViewHolder viewHolder, T var, int pos);
    }
}
