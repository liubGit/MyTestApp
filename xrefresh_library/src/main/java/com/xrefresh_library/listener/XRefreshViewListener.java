package com.xrefresh_library.listener;

/**
 * Created by liub on 2017/11/1 .
 */

public interface XRefreshViewListener {
    /**
     * use {@link #onRefresh(boolean)} instead.
     */
    @Deprecated
    void onRefresh();

    /**
     * @param isPullDown 是不是由下拉手势引起的刷新，是则返回true，反之则是自动刷新或者是调用{@link # startRefresh()}引起的刷新
     */
    void onRefresh(boolean isPullDown);

    /**
     * @param isSilence 是不是静默加载，静默加载即不显示footerview，自动监听滚动到底部并触发此回调
     */
    void onLoadMore(boolean isSilence);

    /**
     * 用户手指释放的监听回调
     *
     * @param direction >0: 下拉释放，<0:上拉释放 注：暂时没有使用这个方法
     */
    void onRelease(float direction);

    /**
     * 获取headerview显示的高度与headerview高度的比例
     *
     * @param headerMovePercent 移动距离和headerview高度的比例
     * @param offsetY           headerview移动的距离
     */
    void onHeaderMove(double headerMovePercent, int offsetY);
}
