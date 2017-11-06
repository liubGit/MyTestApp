package liub.com.mylibrary.net;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import liub.com.mylibrary.callback.NetActionApi;
import liub.com.mylibrary.model.request.SSOEncryptData;
import liub.com.mylibrary.utils.AESUtil;
import liub.com.mylibrary.utils.Hex;
import liub.com.mylibrary.utils.JsonUtils;
import liub.com.mylibrary.utils.Lmsg;
import liub.com.mylibrary.utils.RSAUtil;
import liub.com.mylibrary.utils.RandomUtils;
import liub.com.mylibrary.utils.SecurityUtil;

/**
 * Created by liub on 2017/10/26 .
 */
public class RequestEncryptUtil {
    private static volatile String accessToken;
    private static volatile String aesKey;
    private static volatile String token;

    public static void init(String accessToken, String aesKey, String token) {
        RequestEncryptUtil.accessToken = accessToken;
        RequestEncryptUtil.aesKey = aesKey;
        RequestEncryptUtil.token = token;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getAesKey() {
        return aesKey;
    }

    public static String getToken() {
        return token;
    }

    /**
     * 生成RSA加密的请求体
     *
     * @param requestParam
     * @return
     * @throws Exception
     */
    public static Map<String, String> genRSAParam(Object requestParam) throws Exception {

        Map<String, String> param = new HashMap<>();
        String content = JsonUtils.toJson(requestParam);
        Lmsg.e(content);
        if (NetActionApi.ENCRYPTSWITCHER) {
            param.put("data", content);
        } else {
            String value = RSAUtil.getInstance().encryptByPrivateKey(content, RSAUtil.getSSOPrivateKey());
            param.put("data", Hex.encodeHexStr(value.getBytes("UTF-8")));
        }
        return param;
    }

    /**
     * 生成AES加密的请求体
     *
     * @param dataObj
     * @return
     * @throws Exception
     */
    public static Map<String, String> genAESParam(Object dataObj) throws Exception {
        checkTokenNotNull();

        Map<String, String> param = new HashMap<>();
        param.put("accessToken", accessToken);
        if (NetActionApi.ENCRYPTSWITCHER) {
            param.put("data", dataObj instanceof String ? (String) dataObj : JsonUtils.toJson(dataObj));
        } else {
            param.put("data", encryptSSORequestData(dataObj));
        }
        return param;
    }

    public static Map<String, String> genAccessTokenParam() throws Exception {
        checkTokenNotNull();

        Map<String, String> param = new HashMap<>();
        param.put("accessToken", accessToken);
        return param;
    }

    /**
     * 生成晋段data参数请求表
     *
     * @param dataObj
     * @return
     * @throws Exception
     */
    public static Map<String, String> genAdvanceDataParam(Object dataObj) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("data", dataObj instanceof String ? (String) dataObj : JsonUtils.toJson(dataObj));
        return param;
    }

    /**
     * 测试环境的请求体
     *
     * @param userId
     * @param param
     * @return
     */
    public static Map<String, String> genTestModeParam(String userId, Object param) throws Exception {
        Lmsg.i("request data :" + param.toString());

//        if(NetActionApi.ENCRYPTSWITCHER) {
//            Map<String, String> requestParam = new HashMap<>();
//             if (!TextUtils.isEmpty(userId)) {
//                requestParam.put("userId", "test2");
//            }
//            if (param != null) {
//                requestParam.put("data", JsonUtils.toJson(param));
//            }
//            return requestParam ;
//        }
        return genAESParam(param);
    }

    public static Map<String, String> genRequestData(String[] keys, String[] values) {
        checkKeyValueSize(keys, values);

        Map<String, String> param = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            if (TextUtils.isEmpty(values[i])) {
                continue;
            }
            param.put(keys[i], values[i]);
        }
        return param;
    }

    public static Map<String, Object> genRequestData(String[] keys, Object[] values) {
        checkKeyValueSize(keys, values);

        Map<String, Object> param = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            if (values[i] == null) {
                continue;
            }
            param.put(keys[i], values[i]);
        }
        return param;
    }

    private static <T> void checkKeyValueSize(String[] keys, T[] values) {
        if (keys == null || values == null) {
            throw new NullPointerException("请求的key或者value不能为空");
        }

        if (keys.length != values.length) {
            throw new NullPointerException("请求的key或者value长度不同");
        }
    }

    /**
     * 加密方法
     *
     * @param requestParam
     * @return
     */
    public static String encryptSSORequestData(Object requestParam) throws Exception {
        if (NetActionApi.ENCRYPTSWITCHER) {
            return requestParam instanceof String ? (String) requestParam : JsonUtils.toJson(requestParam);
        }

        checkTokenNotNull();

        String content = requestParam instanceof String ? (String) requestParam : JsonUtils.toJson(requestParam);
        Lmsg.e(content);
        String encrypt = AESUtil.getInstance().encrypt(content, aesKey);
        String nonce = RandomUtils.getRandomNumbersAndLetters(8);
        String timeStamp = Long.toString(System.currentTimeMillis());
        String sortSignature = getSortSignature(token, encrypt, nonce, timeStamp);

        String signature = SecurityUtil.encryptToSHA(sortSignature);

        SSOEncryptData encryptData = new SSOEncryptData();
        encryptData.setEncrypt(encrypt);
        encryptData.setMsgSignature(signature);
        encryptData.setNonce(nonce);
        encryptData.setTimeStamp(timeStamp);

        return Hex.encodeHexStr(JsonUtils.toJson(encryptData).getBytes("UTF-8"));
    }

    private static void checkTokenNotNull() {
        if (TextUtils.isEmpty(accessToken) && TextUtils.isEmpty(aesKey) && TextUtils.isEmpty(token)) {
            throw new NullPointerException("请先调用NetActionApi的init方法！");
        }
    }

    public static String getSortSignature(String token, String encrypt, String nonce, String timeStamp) {
        String[] strings = {token, timeStamp, nonce, encrypt};
        Arrays.sort(strings);
        StringBuilder sortSignature = new StringBuilder();
        for (String field : strings) {
            sortSignature.append(field);
        }
        return sortSignature.toString();
    }
}
