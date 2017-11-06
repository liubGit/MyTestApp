package liub.com.mylibrary.net;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import liub.com.mylibrary.Interface.NetCallBackBaseListener;
import liub.com.mylibrary.model.response.NetBaseInfo;
import liub.com.mylibrary.utils.FileUtils;
import liub.com.mylibrary.utils.JsonUtils;
import liub.com.mylibrary.utils.Lmsg;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by liub on 2017/10/26 .
 */

public class OkClientCallback<T> implements Callback {

    private ResultAdapter<T> mResultAdapter;
    private Context context;
    private NetHandler<T> netHandler;

    private Request request;

    private Class<T> clazz;
    private Type type;

    //是否直接返回
    private boolean isDirectReturn = false;

    public OkClientCallback(Context context) {
        this(context, null);
    }

    public OkClientCallback(Context context, ResultAdapter<T> resultAdapter) {
        this.context = context;
        netHandler = new NetHandler<>(context);
        if (resultAdapter == null)
            mResultAdapter = new NetResultAdapter<>();
        else
            mResultAdapter = resultAdapter;
    }

    @Override
    public void onFailure(Call request, IOException e) {
        if (e instanceof SocketTimeoutException) {
            Lmsg.syso("请求超时");
            netHandler.sendMessage(netHandler.obtainMessage(NetHandler.TIMEOUT));
        } else {
            Lmsg.syso("请求失败");
            netHandler.sendMessage(netHandler.obtainMessage(NetHandler.NETERROR));
        }
        if (request != null) {
            printResponseInfoToLocal(request.request(), "请求失败");
        }
        if (e != null)
            e.printStackTrace();
    }

    public void onNotNet() {
        netHandler.sendMessage(netHandler.obtainMessage(NetHandler.NOT_NET));
        Lmsg.syso("网络不佳");
        printResponseInfoToLocal(this.request, "网络不佳");
    }

    public void onStart(Request request) {
        netHandler.setRequest(request);
        netHandler.sendMessage(netHandler.obtainMessage(NetHandler.START, 0, 0, request));
        if (Lmsg.isDebug) this.request = request;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            if (response.isSuccessful()) {
//                String value = IOUtil.getValues(response.body().byteStream());
                String value = response.body().string();
                Lmsg.syso(value);
                printResponseInfoToLocal(response.request(), value);
                if (isDirectReturn) {//直接返回
                    T result = getResult(value);
                    netHandler.sendMessage(netHandler.obtainMessage(NetHandler.SUCCESS, 0, 0, new NetBaseInfo<>(System.currentTimeMillis(), result)));
                    return;
                }
                mResultAdapter.parseResult(value, clazz == null ? type : clazz);
                if (mResultAdapter.isSuccess()) {
                    netHandler.sendMessage(netHandler.obtainMessage(NetHandler.SUCCESS, mResultAdapter.getErrorCode(), mResultAdapter.getValuesEx(), new NetBaseInfo<>(mResultAdapter.getMessage(context), mResultAdapter.getResult(), mResultAdapter.getTime())));
                } else {
                    netHandler.sendMessage(netHandler.obtainMessage(NetHandler.FAILURE, mResultAdapter.getErrorCode(), mResultAdapter.getValuesEx(), new NetBaseInfo<>(mResultAdapter.getMessage(context), null, mResultAdapter.getTime())));
                }
            } else {
                Lmsg.syso("请求不成功-->>" + response.code());
                printResponseInfoToLocal(response.request(), String.valueOf(response.code()));
                // 把请求地址带过去
//                netHandler.sendMessage(netHandler.obtainMessage(NetHandler.NETERROR, response.code(), 0, new NetBaseInfo<T>(call.request().url().toString(), null, System.currentTimeMillis())));
                netHandler.sendMessage(netHandler.obtainMessage(NetHandler.NETERROR, response.code(), 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Lmsg.syso("请求异常");
            printResponseInfoToLocal(response.request(), "请求异常");
            netHandler.sendMessage(netHandler.obtainMessage(NetHandler.NETERROR));
        }
    }

    private T getResult(String value) throws Exception {
        T result = null;
        if (clazz != null) {
            if (clazz == String.class)
                result = (T) value;
            else
                result = JsonUtils.jsonResolve(value, clazz);
        } else if (type != null)
            result = JsonUtils.jsonResolve(value, type);
        return result;
    }

    public void setIsDealNetStatus(boolean isDealNetStatus) {
        netHandler.setIsDealNetStatus(isDealNetStatus);
    }

    public void setNetCallBackBaseListener(NetCallBackBaseListener<T> backListener, Class<T> clazz) {
        netHandler.setNetCallBackBaseListener(backListener);
        this.clazz = clazz;
    }

    public void setNetCallBackBaseListener(NetCallBackBaseListener<T> backListener, TypeToken<T> token) {
        netHandler.setNetCallBackBaseListener(backListener);
        this.type = token.getType();
    }

    public void setNetCallBackBaseListener(NetCallBackBaseListener<T> backListener, Type type) {
        netHandler.setNetCallBackBaseListener(backListener);
        this.type = type;
    }

    public void isDirectReturn(boolean isDirectReturn) {
        this.isDirectReturn = isDirectReturn;
    }

    /**
     * 输出请求结果到本地
     */
    public void printResponseInfoToLocal(Request request, String result) {
        if (!Lmsg.isDebug) return;
        try {
            String logContent = "";
            if (request != null) {
                logContent = "url-->>" + request.url() + "\r\n\r\n"
                        + "method-->>" + request.method() + "\r\n\r\n"
                        + "requestHeaders-->>" + request.headers().toString() + "\r\n\r\n"
                        + "requestBody-->>" + bodyToString(request.body()) + "\r\n\r\n";
            }
            logContent += "result-->>" + result;
            FileUtils.printLogToLocal(logContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RequestBody转String
     */
    private String bodyToString(final RequestBody requestBody) {
        Buffer buffer = null;
        try {
            buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        } finally {
            if (buffer != null) {
                buffer.close();
            }
        }

    }
}
