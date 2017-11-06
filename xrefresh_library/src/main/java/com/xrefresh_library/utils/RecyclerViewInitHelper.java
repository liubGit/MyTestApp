package com.xrefresh_library.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xrefresh_library.R;
import com.xrefresh_library.widget.GridSpacingItemDecoration;
import com.xrefresh_library.widget.LoadMoreGridLayoutManager;


/**
 * 初始化recyclerview 帮助类
 * Created by liub on 2017/11/2 .
 */
public class RecyclerViewInitHelper {

    /**
     * 初始化水平方向的Recycler
     *
     * @param recyclerView
     */
    public static void initWithHorizontalLayout(RecyclerView recyclerView) {
        initLinearRecyclerView(recyclerView, LinearLayoutManager.HORIZONTAL, false);
    }

    /**
     * 初始化垂直方向的Recycler
     *
     * @param recyclerView
     */
    public static void initWithVerticalLayout(RecyclerView recyclerView) {
        initLinearRecyclerView(recyclerView, LinearLayoutManager.VERTICAL, false);
    }

    /**
     * 初始化水平方向的包裹内容的Recycler
     *
     * @param recyclerView
     */
    public static void initWrapContentHorizontalLayout(RecyclerView recyclerView) {
        initLinearRecyclerView(recyclerView, LinearLayoutManager.HORIZONTAL, true);
    }

    /**
     * 初始化垂直方向的包裹内容的Recycler
     *
     * @param recyclerView
     */
    public static void initWrapContentVerticalLayout(RecyclerView recyclerView) {
        initLinearRecyclerView(recyclerView, LinearLayoutManager.VERTICAL, true);
    }

    /**
     * 初始化网格布局的RecyclerView
     *
     * @param span 一行几个
     */
    public static void initGridLayoutRecycler(RecyclerView recyclerView, int span) {
        GridLayoutManager manager = new LoadMoreGridLayoutManager(recyclerView.getContext(), span, recyclerView);
        setLayoutManager(recyclerView, manager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(span, (int) recyclerView.getContext().getResources().getDimension(R.dimen.global_padding), false));
    }

    private static void initLinearRecyclerView(RecyclerView recyclerView, int orientation, boolean needWrapContent) {
        if (needWrapContent) {
            LinearLayoutManager wrapContentManager = new LinearLayoutManager(recyclerView.getContext(), orientation, false);
            setLayoutManager(recyclerView, wrapContentManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
            layoutManager.setOrientation(orientation);
            setLayoutManager(recyclerView, layoutManager);
        }
    }

    private static void setLayoutManager(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }
}
