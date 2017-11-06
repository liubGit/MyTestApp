package liub.com.mylibrary.Interface;

import okhttp3.Request;

/**
 * 基础回调
 * Created by liub on 2017/10/26 .
 */

public abstract class NetCallBackBaseListener<T> {
    /**
     * 开始
     *
     * @param request 当前请求体
     */
    public abstract void onStart(Request request);

    /**
     * 请求完成
     *
     * @param requestTag 当前请求的requestId
     * @param currTime   服务器返回的当前时间
     * @param errorCode  错误码
     * @param msg        提示信息
     */
    public abstract void onFinish(Object requestTag, long currTime, int errorCode, String msg);

    /**
     * 成功
     *
     * @param currTime  服务器返回的当前时间
     * @param errorCode 错误码
     * @param msg       成功提示信息
     * @param result    成功返回结果
     */
    public abstract void onSuccess(long currTime, int errorCode, String msg, T result);

    /**
     * 失败
     *
     * @param currTime  服务器返回的当前时间
     * @param errorCode 错误码
     * @param msg       失败提示信息
     */
    public abstract void onFailure(long currTime, int errorCode, String msg);

    /**
     * 重复返回
     *
     * @param isSuccess 是否成功
     * @param currTime  服务器返回的当前时间
     * @param errorCode 错误码
     * @param msg       提示信息
     * @param result    成功返回的结果
     */
    public abstract void onRepeat(boolean isSuccess, long currTime, int errorCode, String msg, T result);
}
