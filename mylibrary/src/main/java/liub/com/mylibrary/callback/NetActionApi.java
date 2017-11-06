package liub.com.mylibrary.callback;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import liub.com.mylibrary.Interface.NetBaseDealListener;
import liub.com.mylibrary.Interface.NetCallBackBaseListener;
import liub.com.mylibrary.model.request.BaseRequestTypeInfo;
import liub.com.mylibrary.net.ApiV3NetResultAdapter;
import liub.com.mylibrary.net.NetDealUtil;
import liub.com.mylibrary.net.NetWorkManage;
import liub.com.mylibrary.net.OkClientCallback;
import liub.com.mylibrary.net.RequestEncryptUtil;
import liub.com.mylibrary.net.RequestHeader;
import liub.com.mylibrary.net.SSOResultAdapter;
import liub.com.mylibrary.utils.JsonUtils;
import liub.com.mylibrary.utils.Lmsg;

/**
 * 接口api
 * Created by liub on 2017/10/26 .
 */

public class NetActionApi {


    public static boolean SSOSWITCHER = false;  // false 为开启sso
    public static boolean ENCRYPTSWITCHER = false; // true 为不开启加密
    private NetWorkManage workManage;

    private Context context;

    public NetActionApi(Context context) {
        workManage = new NetWorkManage(context);
        this.context = context;
    }


    /**
     * 请求验证码
     */
//    public void sendVerifyCode(String telePhone, String sendType, String sendScenario, NetCallBackBaseListener<T> callback) {
//        //组织请求参数
//        String[] keys = new String[]{"whichFunc", "telePhone", "sendType", "sendScenario"};
//        Object[] values = new Object[]{"post.VerificationCode.SendVerifycode", telePhone, sendType, sendScenario};
//
//        //请求接口
//        postToApiV3(getAccessToken(), NetActionUrl.getApiV3Url("Post"), keys, values, new TypeToken<T>() {
//        }, callback);
//    }

//    public void getRelativeAll(NetCallBackListener<MyRelativeDetailInfo> callBackListener) {
//        String[] keys = new String[]{};
//        String[] values = new String[]{};
//        String url = getUrl(GET_RELATIVE_ALL);
//        api.postToJava(getAccessToken(), keys, values, url, new TypeToken<MyRelativeDetailInfo>() {
//        }, callBackListener);
//    }
    public <T, V> void postToJava(String accessToken, String[] keys, V[] values, String url, TypeToken<T> token, NetCallBackBaseListener<T> callback) {
        try {
            Map<String, Object> data = RequestEncryptUtil.genRequestData(keys, values);
            Map<String, String> param = genPostRequestParam(accessToken, data, true);
            OkClientCallback<T> clientCallback = new OkClientCallback<>(context, new SSOResultAdapter<T>());
            clientCallback.setNetCallBackBaseListener(callback, token);
            workManage.postNameValuePair(param, url, clientCallback);
        } catch (Exception e) {
            e.printStackTrace();
            handleExceptionCallBack(callback, url);
        }
    }

    public <T, V> void postToApiV3(String accessToken, String url, String[] keys, V[] values, TypeToken<T> token, NetCallBackBaseListener<T> callback) {
        try {
            Map<String, Object> data = RequestEncryptUtil.genRequestData(keys, values);
            Map<String, String> param = genPostRequestParam(accessToken, data, true);
            OkClientCallback<T> clientCallback = new OkClientCallback<>(context, new ApiV3NetResultAdapter<T>());
            clientCallback.setNetCallBackBaseListener(callback, token);
            workManage.postNameValuePair(param, url, clientCallback);
        } catch (Exception e) {
            e.printStackTrace();
            handleExceptionCallBack(callback, url);
        }
    }

    private <T> void handleExceptionCallBack(NetCallBackBaseListener<T> callBack, String url) {
        if (callBack == null) {
            return;
        }
        callBack.onFinish(null, System.currentTimeMillis(), 0, "请求加密失败");
        callBack.onFailure(System.currentTimeMillis(), 0, "网络繁忙");
        NetBaseDealListener netBaseDealListener = NetDealUtil.getInstance().getNetBaseDealListener();
        if (netBaseDealListener != null) {
            netBaseDealListener.onServerNotAccess(url);
        }
    }


    /**
     * 生成请求参数
     *
     * @param data    data
     * @param key     key
     * @param encrypt 是否加密
     * @return
     * @throws Exception
     */
    public static Map<String, String> genPostRequestParam(Map<String, String> data, String key, boolean encrypt) throws Exception {
        Map<String, String> param = new HashMap<>();
        if (!TextUtils.isEmpty(key) && encrypt) {    // 不需要加密的接口不要传key
            param.put("accessToken", key);
        }
        if (TextUtils.isEmpty(key) || !encrypt || disableEncrypt(RequestHeader.get("versionName"))) {
            param.put("data", JsonUtils.toJson(data));
        } else {
            param.put("data", RequestEncryptUtil.encryptSSORequestData(data));
        }
        return param;
    }

    /**
     * 生成请求参数
     *
     * @param data    data
     * @param key     key
     * @param encrypt 是否加密
     * @return
     * @throws Exception
     */
    public static Map<String, String> genPostRequestParam(String key, Map<String, Object> data, boolean encrypt) throws Exception {
        Map<String, String> param = new HashMap<>();
        if (!TextUtils.isEmpty(key) && encrypt) {    // 不需要加密的接口不要传key
            param.put("accessToken", key);
        }
        if (TextUtils.isEmpty(key) || !encrypt || disableEncrypt(RequestHeader.get("versionName"))) {
            param.put("data", JsonUtils.toJson(data));
        } else {
            param.put("data", RequestEncryptUtil.encryptSSORequestData(data));
        }
        return param;
    }

    /**
     * 根据版本号判断是否开启加密
     *
     * @param versionName
     * @return true 为未开启，false为开启
     */
    public static boolean disableEncrypt(String versionName) {
//        return versionName.startsWith("2.3");
        return SSOSWITCHER;
    }

    private Map<String, String> genPostRequestParam(BaseRequestTypeInfo info, String tokenKey, boolean isEntry) throws Exception {
        Map<String, String> requestParam = new HashMap<>();
        String data = JsonUtils.toJson(info);
        Lmsg.e(data + "");
        if (isEntry) {
            data = RequestEncryptUtil.encryptSSORequestData(data);
        }
        requestParam.put("data", data);
        requestParam.put("accessToken", tokenKey);
        return requestParam;
    }
}
