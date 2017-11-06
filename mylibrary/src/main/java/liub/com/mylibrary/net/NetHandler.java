package liub.com.mylibrary.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import liub.com.mylibrary.Interface.NetCallBackBaseListener;
import liub.com.mylibrary.Interface.NetCallExtraListener;
import liub.com.mylibrary.callback.NetConstant;
import liub.com.mylibrary.model.response.NetBaseInfo;
import liub.com.mylibrary.utils.IOUtil;
import liub.com.mylibrary.utils.JsonUtils;
import okhttp3.Request;

/**
 * 网络请求返回结果处理
 * Created by liub on 2017/10/26 .
 */

public class NetHandler<T> extends Handler {

    public static final int NETERROR = -1, NOT_NET = -2, TIMEOUT = -3, SUCCESS = 0, FAILURE = 1, START = 2;

    //已经完成
    private boolean isCompleted = false;
    //是否网络特殊处理
    private boolean isDealNetStatus = true;

    private Context context;
    private NetCallBackBaseListener<T> backListener;

    private static Map<String, String> messages = null;
    private Request request;

    public NetHandler(Context context) {
        super(Looper.getMainLooper());
        this.context = context;
        if (messages == null) {
            try {
                messages = JsonUtils.jsonResolveForNote(IOUtil.readTextFile(context.getAssets().open("errorMsg.txt")), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (backListener == null) {
            return;
        }

        //开始请求
        if (msg.what == START) {
            Request request = (Request) msg.obj;
            backListener.onStart(request);
            return;
        }

        //处理完成结果
        int errmsg = msg.arg1;
        NetBaseInfo<T> netBaseInfo = new NetBaseInfo<>();
        T result = null;
        if (msg.obj instanceof NetBaseInfo) {
            netBaseInfo = (NetBaseInfo<T>) msg.obj;
        } else {
            result = (T) msg.obj;
        }

        String errorMessage = getErrorMessage(msg, errmsg, netBaseInfo);
        long currTime = getCurrTime(netBaseInfo);
        //完成
        if (!isCompleted)
            backListener.onFinish(msg.arg2, currTime, errmsg, errorMessage);
        switch (msg.what) {
            case SUCCESS:
                if (netBaseInfo != null)
                    result = netBaseInfo.getResult();
                if (!isCompleted) {
                    if (backListener instanceof NetCallExtraListener) {
                        backListener.onSuccess(currTime, msg.arg2, errorMessage, result);
                    } else {
                        backListener.onSuccess(currTime, errmsg, errorMessage, result);
                    }
                } else
                    backListener.onRepeat(true, currTime, errmsg, errorMessage, result);
                break;
            case FAILURE:
            case NOT_NET:
            case NETERROR:
            case TIMEOUT:
                if (!isCompleted)
                    backListener.onFailure(currTime, errmsg, errorMessage);
                else
                    backListener.onRepeat(false, currTime, errmsg, errorMessage, null);
                break;
        }
        if (!isCompleted && isDealNetStatus)
            NetDealUtil.getInstance().dealNetStatus(errmsg, request != null ? request.url().toString() : "");
        isCompleted = true;
    }

    private long getCurrTime(NetBaseInfo<T> netBaseInfo) {
        long currTime;
        if (netBaseInfo == null)
            currTime = System.currentTimeMillis();
        else
            currTime = netBaseInfo.getTime();
        return currTime;
    }

    private String getErrorMessage(Message msg, int errmsg, NetBaseInfo<T> netBaseInfo) {
        String errorMessage = netBaseInfo == null ? "" : netBaseInfo.getMsgCode();
        try {
            if (messages == null)
                messages = JsonUtils.jsonResolveForNote(IOUtil.readTextFile(context.getAssets().open("errorMsg.txt")), Map.class);

            if (TextUtils.isEmpty(errorMessage)) {
                if (msg.what == NETERROR && messages != null)
                    errorMessage = messages.get("error_network");
                else if (msg.what == NOT_NET)
                    errorMessage = NetConstant.NOT_NET_MSG;
                else if (msg.what == TIMEOUT)
                    errorMessage = NetConstant.TIME_OUT_MSG;
                else if (messages != null)
                    errorMessage = messages.get(String.valueOf(errmsg));//"error_" +
            }
        } catch (Exception e) {
            errorMessage = "";
            e.printStackTrace();
        }
        return errorMessage;
    }

    public void setNetCallBackBaseListener(NetCallBackBaseListener<T> backListener) {
        WeakReference<NetCallBackBaseListener<T>> reference = new WeakReference<>(backListener);
        this.backListener = reference.get();
    }

    public void setIsDealNetStatus(boolean isDealNetStatus) {
        this.isDealNetStatus = isDealNetStatus;
    }


    public static String getErrorMessage(Context context, int key) {
        if (messages == null) {
            try {
                messages = JsonUtils.jsonResolveForNote(IOUtil.readTextFile(context.getAssets().open("errorMsg.txt")), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return messages.get(String.valueOf(key));
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
