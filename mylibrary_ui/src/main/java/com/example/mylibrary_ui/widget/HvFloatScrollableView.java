package com.example.mylibrary_ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mylibrary_ui.R;

import java.util.List;

/**
 * Created by liub on 2017/11/19.
 *Description: 实现ViewPager+ Fragment + 上滑可悬浮置顶
 * 使用此布局 必须 设置  app:hvp_headerLayout="@layout/layout_mewcun_main_header"  头部布局
 *                       app:hvp_floatLayout="@layout/layout_mewcun_main_float"    置顶悬浮布局
 *
 *  初始化  fragment 必须implements HeaderScrollHelper.ScrollableContainer  ,使用LoadMoreRecyclerView可以加载更多
 */

public class HvFloatScrollableView  extends LinearLayout {

    private View view;
    private View headerView;
    private View floatView;
//    private LoadingView mLoadView;//刷新的组件
    private HeaderViewPager mScrollableLayout;

    private LinearLayout mHeaderViewContainerLay;
    private LinearLayout mFoatViewContainerLay;
    private ViewPager mViewPager;

    private HMhvScrollableViewListener hMhvScrollableViewListener;

    public HMhvScrollableViewListener getHvScrollableViewListener() {
        return hMhvScrollableViewListener;
    }

    public void setHvScrollableViewListener(HMhvScrollableViewListener hMhvScrollableViewListener) {
        this.hMhvScrollableViewListener = hMhvScrollableViewListener;
    }

    public interface HMhvScrollableViewListener{
        void toViewPagerOnPageChangeListener(int position);
        void toHeaderView(View headerView);
        void toFloatView(View floatView);
    }

    public HvFloatScrollableView(Context context) {
        this(context, null);
    }

    public HvFloatScrollableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HvFloatScrollableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeaderViewPager);
        int headerViewResId = a.getResourceId(R.styleable.HeaderViewPager_hvp_headerLayout,-1);
        if(headerViewResId != -1){
            headerView = LayoutInflater.from(context).inflate(headerViewResId, null);
        }else{
            throw new NullPointerException("you need to increase hvp_headerLayout in XML");
        }
        int floatViewResId = a.getResourceId(R.styleable.HeaderViewPager_hvp_floatLayout,-1);
        if(floatViewResId != -1){
            floatView = LayoutInflater.from(context).inflate(floatViewResId, null);
        }else{
            throw new NullPointerException("you need to increase hvp_floatLayout in XML");
        }
        a.recycle();

    }


    public void initHMhvScrollableView(Activity activity, final List<Fragment> fragments){
        initHMhvScrollableView(activity,fragments,null);
    }
    /**
     * 初始化
     * @param activity
     * @param fragments fragment数组
     * @param scrollableViewListener 监听viewpager 滑动事件 获取头部rootView ( headerView )  置顶悬浮rootView ( floatView )
     */
    public void initHMhvScrollableView(Activity activity, final List<Fragment> fragments, final HMhvScrollableViewListener scrollableViewListener){
        if(null == view){
            view = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_hv_scroll_view,null);

//            mLoadView = (LoadingView) view.findViewById(R.id.loading_view);
            mScrollableLayout = (HeaderViewPager) view.findViewById(R.id.scrollableLayout);
            mHeaderViewContainerLay = (LinearLayout) view.findViewById(R.id.lly_header_container);
            if(headerView !=null){
                mHeaderViewContainerLay.removeAllViews();
                mHeaderViewContainerLay.addView(headerView);
                if(null !=scrollableViewListener){
                    scrollableViewListener.toHeaderView(headerView);
                }
            }else{
                throw new NullPointerException("you need to increase hvp_headerLayout in XML");
            }

            mFoatViewContainerLay = (LinearLayout) view.findViewById(R.id.lly_float_container);
            if(floatView !=null){
                mFoatViewContainerLay.removeAllViews();
                mFoatViewContainerLay.addView(floatView);
                if(null !=scrollableViewListener){
                    scrollableViewListener.toFloatView(floatView);
                }
            }else{
                throw new NullPointerException("you need to increase hvp_floatLayout in XML");
            }

            mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
            mViewPager.setAdapter(new ContentAdapter(((FragmentActivity)activity).getSupportFragmentManager(),fragments));
            try{
                mScrollableLayout.setCurrentScrollableContainer((HeaderScrollHelper.ScrollableContainer)fragments.get(0));
            }catch (Exception e){
                throw new NullPointerException("fragment want to implements HeaderScrollHelper.ScrollableContainer");
            }
            mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    mScrollableLayout.setCurrentScrollableContainer((HeaderScrollHelper.ScrollableContainer)fragments.get(position));
                    if(null !=scrollableViewListener){
                        scrollableViewListener.toViewPagerOnPageChangeListener(position);
                    }
                }
            });
            mViewPager.setCurrentItem(0);
            mViewPager.setOffscreenPageLimit(fragments.size());

//            mScrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
//                @Override
//                public void onScroll(int currentY, int maxY) {
//                    if(mScrollableLayout.canPtr()){
//                        mLoadView.setEnabled(true);
//                    }else{
//                        mLoadView.setEnabled(false);
//                    }
//                }
//            });
        }
        this.addView(view);
    }


    /**
     * 获取顶部布局
     * @return
     */
    public View getHeaderView() {
        return headerView;
    }

    /**
     * 获取悬浮布局
     * @return
     */
    public View getFloatView() {
        return floatView;
    }

    /**
     * 获取滑动的ViewPager
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }


    public HeaderViewPager getScrollableLayout() {
        return mScrollableLayout;
    }

//    public LoadingView getLoadView() {
//        return mLoadView;
//    }

    /**
     * 内容页的适配器
     */
    private class ContentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public ContentAdapter(FragmentManager fm, List<Fragment> fragment) {
            super(fm);
            this.fragments = fragment;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
