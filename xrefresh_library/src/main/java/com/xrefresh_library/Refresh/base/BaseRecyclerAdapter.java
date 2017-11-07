package com.xrefresh_library.Refresh.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xrefresh_library.base.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liub on 2017/11/6 .
 */

public abstract class BaseRecyclerAdapter <T> extends Adapter<BaseRecViewHolder> {
    public  List<T> list;
    protected Context mContext;
    protected BaseRecyclerAdapter.OnItemClickListener onItemClickListener;
    protected BaseRecyclerAdapter.OnItemLongClickListener<T> onItemLongClickListener;
    private int mItemLayoutRes;

    public void setOnItemLongClickListener(BaseRecyclerAdapter.OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void addAll(int position, List<T> addList) {
        if(addList != null) {
            this.list.addAll(position, addList);
            this.notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public BaseRecyclerAdapter(Context context, int itemLayout, List<T> list) {
        this.mContext = context;
        this.list = list;
        this.mItemLayoutRes = itemLayout;
        if(this.list == null) {
            this.list = new ArrayList();
        }

        this.setHasStableIds(true);
    }

    public void add(T t) {
        int position = this.list.size();
        this.list.add(t);
        this.notifyItemInserted(this.list.size() - 1);
        if(position == 0) {
            this.notifyDataSetChanged();
        } else {
            this.notifyItemInserted(this.list.size() - 1);
        }

    }

    public void addAll(List<T> addDatas) {
        if(addDatas != null) {
            int position = this.list.size();
            this.list.addAll(addDatas);
            if(position == 0) {
                this.notifyDataSetChanged();
            } else {
                this.notifyItemRangeInserted(position, addDatas.size());
            }

        }
    }

    public void replace(List<T> newDatas) {
        this.list.clear();
        this.list.addAll(newDatas);
        this.notifyDataSetChanged();
    }

    public void remove(T t) {
        int position = this.list.indexOf(t);
        if(position != -1) {
            this.list.remove(t);
            this.notifyItemRemoved(position);
        } else {
            Log.w("baseRecyclerAdapter", "移除的对象不存在");
        }

    }

    public BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(this.mItemLayoutRes, parent, false);
        this.changeViewSize(view);
        final BaseRecViewHolder holder = new BaseRecViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int adapterPosition = (int)holder.getItemId();
                BaseRecyclerAdapter.this.onClickHook(holder, adapterPosition);
                if(BaseRecyclerAdapter.this.onItemClickListener != null) {
                    BaseRecyclerAdapter.this.onItemClickListener.onItemClickDeal(holder, adapterPosition);
                }

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                int adapterPosition = (int)holder.getItemId();
                if(BaseRecyclerAdapter.this.onItemLongClickListener != null) {
                    Object data = null;
                    if(adapterPosition < BaseRecyclerAdapter.this.list.size()) {
                        data = BaseRecyclerAdapter.this.list.get(adapterPosition);
                    }

                    BaseRecyclerAdapter.this.onItemLongClickListener.onItemLongClick(holder, (T) data, adapterPosition);
                }

                return true;
            }
        });
        return holder;
    }

    protected void changeViewSize(View view) {
    }

    public long getItemId(int position) {
        return (long)position;
    }

    protected void onClickHook(BaseRecViewHolder holder, int adapterPosition) {
    }

    public int getItemCount() {
        return this.list == null?0:this.list.size();
    }

    public T getItem(int position) {
        return this.list == null?null:this.list.get(position);
    }

    public List<T> getList() {
        return this.list;
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(BaseRecViewHolder var1, T var2, int var3);
    }

    public abstract static class OnItemClickListener {
        private long lastClickTime = 0L;

        public OnItemClickListener() {
        }

        public void onItemClickDeal(BaseRecViewHolder holder, int position) {
            long time = System.currentTimeMillis();
            if(time - this.lastClickTime >= ConstantValue.CLICK_SPACE) {
                this.lastClickTime = time;
                this.onItemClick(holder, position);
            }
        }

        public abstract void onItemClick(BaseRecViewHolder var1, int var2);
    }
}