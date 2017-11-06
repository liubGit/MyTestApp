package liub.com.mylibrary.Interface;

import okhttp3.Request;

/**
 * Created by liub on 2017/10/26 .
 */

public abstract  class NetCallExtraListener<T> extends NetCallBackBaseListener<T> {
    public abstract void onSuccess(int extra, String msg, T result);

    public abstract void onFailure(String msg);

    @Override
    public void onStart(Request request) {
    }

    @Override
    public void onFinish(Object requestTag, long currTime, int errorCode, String msg) {
    }

    @Override
    public void onSuccess(long currTime, int extra, String msg, T result) {
        onSuccess(extra,msg,result);
    }

    @Override
    public void onFailure(long currTime, int errorCode, String msg) {
        onFailure(msg);
    }

    @Override
    public void onRepeat(boolean isSuccess, long currTime, int errorCode, String msg, T result) {
    }
}
