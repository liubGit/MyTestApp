package liub.com.mylibrary.net;

import android.content.Context;

import java.util.Map;

import liub.com.mylibrary.utils.Lmsg;
import liub.com.mylibrary.utils.NetworkUtil;
import okhttp3.Response;

import static liub.com.mylibrary.net.RequestHeader.defaultHeader;

/**
 * 网络管理
 * Created by liub on 2017/10/26 .
 */

public class NetWorkManage {
    private Context context;

    public NetWorkManage(Context context) {
        this.context = context;
    }

    /**
     * post Json数据
     *
     * @param writeTimeout 写入超时时间(秒)
     * @param readTimeout  读取超时时间(秒)
     */
    public void postJson(String strUrl, String strJson, int connTimeout, int writeTimeout, int readTimeout, OkClientCallback callback) {
        Lmsg.syso(strJson + "");
        Lmsg.syso(strUrl + "");
        try {
            if (!NetworkUtil.hasNetWorkConnect(context)) {
                callback.onNotNet();
                return;
            }
            HttpClientUtils.getInstance().sendPost(strUrl, strJson, connTimeout, writeTimeout, readTimeout, callback);
        } catch (Exception e) {
            callback.onFailure(null, null);
            e.printStackTrace();
        }
    }

    /**
     * post Json数据 简单同步
     *
     * @param writeTimeout 写入超时时间(秒)
     * @param readTimeout  读取超时时间(秒)
     */
    public Response postJsonSyn(String strUrl, String strJson, int connTimeout, int writeTimeout, int readTimeout) {
        Lmsg.syso(strJson + "");
        Lmsg.syso(strUrl + "");
        try {
            if (NetworkUtil.hasNetWorkConnect(context))
                return HttpClientUtils.getInstance().postRequestSyn(strUrl, strJson, connTimeout, writeTimeout, readTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get 异步
     */
    public void getData(String strUrl, OkClientCallback callback) {
        try {
            Lmsg.syso(strUrl + "");
            if (!NetworkUtil.hasNetWorkConnect(context)) {
                callback.onNotNet();
                return;
            }
            HttpClientUtils.getInstance().sendGet(strUrl, callback);
        } catch (Exception e) {
            callback.onFailure(null, null);
            e.printStackTrace();
        }
    }

    public void postNameValuePair(Map<String, String> param, String url, OkClientCallback callBack) {
        if (Lmsg.isDebug) {
            StringBuilder builder = new StringBuilder();
            builder.append("请求参数： ");
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue());
                    builder.append("&");
                }
            }
            Lmsg.syso(builder.substring(0, builder.length() - 1));
        }
        Lmsg.syso("请求地址：" + url);
        Lmsg.syso(defaultHeader + "");
        try {
            if (!NetworkUtil.hasNetWorkConnect(context)) {
                callBack.onNotNet();
                return;
            }
            HttpClientUtils.getInstance().sendPostNameValuePairs(url, param, callBack, defaultHeader);
        } catch (Exception e) {
            callBack.onFailure(null, null);
            e.printStackTrace();
        }
    }

    public void postNameValuePairWithHeader(Map<String, String> param, Map<String, String> headers, String url, OkClientCallback callBack) {
        if (Lmsg.isDebug) {
            StringBuilder builder = new StringBuilder();
            builder.append("请求参数： ");
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue());
                    builder.append("&");
                }
            }
            Lmsg.syso(builder.substring(0, builder.length() - 1));
        }
//        Lmsg.syso(param + "");
        Lmsg.syso("请求地址：" + url);
        Lmsg.syso(headers + "");
        try {
            if (!NetworkUtil.hasNetWorkConnect(context)) {
                callBack.onNotNet();
                return;
            }
            HttpClientUtils.getInstance().sendPostNameValuePairs(url, param, callBack, headers);
        } catch (Exception e) {
            callBack.onFailure(null, null);
            e.printStackTrace();
        }
    }

    public void postNameValueLongOverTime(Map<String, String> param, Map<String, String> headers, String url, OkClientCallback callBack) {
        postNameValueSpecifyOverTime(param, headers, url, callBack, 120, 40, 20);
    }

    public void postNameValueSpecifyOverTime(Map<String, String> param, Map<String, String> headers, String url, OkClientCallback callBack, int write, int read, int conn) {
        if (Lmsg.isDebug) {
            StringBuilder builder = new StringBuilder();
            builder.append("请求参数： ");
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue());
                    builder.append("&");
                }
            }
            Lmsg.syso(builder.substring(0, builder.length() - 1));
        }
        Lmsg.syso("请求地址：" + url);
        Lmsg.syso(headers + "");
        try {
            if (!NetworkUtil.hasNetWorkConnect(context)) {
                callBack.onNotNet();
                return;
            }
            HttpClientUtils.getInstance().sendPostNameValuePairs(url, param, callBack, headers, write, read, conn);
        } catch (Exception e) {
            callBack.onFailure(null, null);
            e.printStackTrace();
        }
    }

}
