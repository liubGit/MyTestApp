package com.xrefresh_library.Refresh.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xrefresh_library.Refresh.LoadingView;
import com.xrefresh_library.Refresh.widget.AlertToast;
import com.xrefresh_library.base.ConstantValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基础的加载更多adapter
 * Created by zeda on 15/12/5.
 */
public abstract class BaseLoadMoreViewAdapter<T> extends Adapter<BaseRecViewHolder> {
    View footerView;
    protected LoadingView loadingView;
    public static final int FOOTER_VIEW_TYPE = -2000;
    protected boolean isHaveMoreData = true;
    private View lineView;
    ProgressBar progressBar;
    TextView tipContent;
    protected boolean isLoading = true;
    protected int onePageNum = 20;
    protected List<T> list;
    protected Context mContext;
    protected BaseLoadMoreViewAdapter.OnItemClickListener<T> onItemClickListener;
    private BaseLoadMoreViewAdapter.OnItemLongClickListener<T> onItemLongClickListener;
    private int mItemLayoutRes;
    private boolean footerViewVisible = true;
    private boolean footerDividerVisible = true;
    String tipByNotHaveMore = "只有这么多啦";
    String tipByLoading = "正在加载...";
    String tipByNotData = "暂无数据";
    String tipByLoadFailure = "加载失败";
    private ViewGroup parent;

    public BaseLoadMoreViewAdapter(Context context, int itemLayout, List<T> list) {
        this.mContext = context;
        this.list = list;
        this.mItemLayoutRes = itemLayout;
        if (this.list == null) {
            this.list = new ArrayList();
        }

        this.setHasStableIds(true);
    }

    public BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(this.mContext).inflate(this.mItemLayoutRes, parent, false);
        this.changeViewSize(view);
        final BaseRecViewHolder holder = new BaseRecViewHolder(view);
        view.setOnClickListener(new OnHolderViewClickListener(holder) {
            public void onClick(BaseRecViewHolder holder, View v) {
                int adapterPosition = BaseLoadMoreViewAdapter.this.getAdapterPosition(holder);
                if (adapterPosition >= 0 && adapterPosition < BaseLoadMoreViewAdapter.this.getListSize()) {
                    if (BaseLoadMoreViewAdapter.this.onItemClickListener != null) {
                        BaseLoadMoreViewAdapter.this.onItemClickListener.onItemClickDeal(holder, BaseLoadMoreViewAdapter.this.get(adapterPosition), adapterPosition);
                    }

                    BaseLoadMoreViewAdapter.this.onClickHook(holder, adapterPosition);
                }
            }
        });
        view.setOnLongClickListener(new OnHolderViewLongClickListener(holder) {
            public boolean onLongClick(BaseRecViewHolder holder, View v) {
                int adapterPosition = BaseLoadMoreViewAdapter.this.getAdapterPosition(holder);
                if (adapterPosition >= 0 && adapterPosition < BaseLoadMoreViewAdapter.this.getListSize()) {
                    if (BaseLoadMoreViewAdapter.this.onItemLongClickListener != null) {
                        BaseLoadMoreViewAdapter.this.onItemLongClickListener.onItemLongClick(holder, BaseLoadMoreViewAdapter.this.get(adapterPosition), adapterPosition);
                    }

                    return true;
                } else {
                    return true;
                }
            }
        });
        return holder;
    }

    protected int getAdapterPosition(BaseRecViewHolder holder) {
        return (int) holder.getItemId();
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    protected void changeViewSize(View rootView) {
    }

    protected void onClickHook(BaseRecViewHolder holder, int adapterPosition) {
    }

    public void onBindViewHolder(BaseRecViewHolder holder, int position) {
        this.onBindViewHolder(holder, this.get(position), position);
    }

    public abstract void onBindViewHolder(BaseRecViewHolder var1, T var2, int var3);

    public int getItemCount() {
        return this.getListSize();
    }

    public View getFooter() {
        return this.footerView;
    }

    public void setFooterVisibility(boolean shouldShow) {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void onLoadSuccess(int count) {
        if (count == 0) {
            this.addAll((List) null);
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public void onLoadFailure(String msg) {
        if (this.parent != null) {
            AlertToast.make(this.parent, msg).show();
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean isHaveMoreData() {
        return this.isHaveMoreData;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setLoadingView(LoadingView loadingView) {
        this.loadingView = loadingView;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean isLoading() {
        return this.isLoading;
    }

    public void setOnePageNum(int onePageNum) {
        this.onePageNum = onePageNum;
    }

    public int getOnePageNum() {
        return this.onePageNum;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getTipByNotHaveMore() {
        return this.tipByNotHaveMore;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setTipByNotHaveMore(String tipByNotHaveMore) {
        this.tipByNotHaveMore = tipByNotHaveMore;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getTipByLoading() {
        return this.tipByLoading;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setTipByLoading(String tipByLoading) {
        this.tipByLoading = tipByLoading;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getTipByNotData() {
        return this.tipByNotData;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setTipByNotData(String tipByNotData) {
        this.tipByNotData = tipByNotData;
        if (this.loadingView != null) {
            this.loadingView.setNoDataMessage(tipByNotData);
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public String getTipByLoadFailure() {
        return this.tipByLoadFailure;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setTipByLoadFailure(String tipByLoadFailure) {
        this.tipByLoadFailure = tipByLoadFailure;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void showLineView() {
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void hiddenLineView() {
    }

    public T get(int position) {
        return position >= this.getListSize() ? null : this.list.get(position);
    }

    public List<T> getList() {
        return this.list;
    }

    public int getListSize() {
        return this.list.size();
    }

    public void add(T data) {
        int position = this.getListSize();
        this.list.add(data);
        if (position == 0) {
            this.notifyDataSetChanged();
        } else {
            this.notifyItemInserted(this.getListSize() - 1);
        }

    }

    public void addAll(List<T> addDatas) {
        if (addDatas == null) {
            addDatas = new ArrayList();
        }

        int position = this.getListSize();
        this.list.addAll((Collection) addDatas);
        if (position == 0) {
            this.notifyDataSetChanged();
        } else {
            this.notifyItemRangeInserted(position, ((List) addDatas).size());
        }

    }

    public void addAll(int position, List<T> addList) {
        if (addList != null) {
            this.list.addAll(position, addList);
            this.notifyDataSetChanged();
        }
    }

    public void remove(T t) {
        int position = this.list.indexOf(t);
        if (position != -1) {
            this.list.remove(t);
            this.notifyItemRemoved(position);
        }

    }

    public void reset() {
        this.list.clear();
        this.notifyDataSetChanged();
    }

    public void replace(List<T> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList();
        }

        this.list.clear();
        this.list.addAll((Collection) newDatas);
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(BaseLoadMoreViewAdapter.OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(BaseLoadMoreViewAdapter.OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(BaseRecViewHolder var1, T var2, int var3);
    }

    public abstract static class OnItemClickListener<T> {
        private long lastClickTime = 0L;

        public OnItemClickListener() {
        }

        public void onItemClickDeal(BaseRecViewHolder holder, T data, int position) {
            long time = System.currentTimeMillis();
            if (time - this.lastClickTime >= ConstantValue.CLICK_SPACE) {
                this.lastClickTime = time;
                this.onItemClick(holder, data, position);
            }
        }

        public abstract void onItemClick(BaseRecViewHolder var1, T var2, int var3);
    }
}

