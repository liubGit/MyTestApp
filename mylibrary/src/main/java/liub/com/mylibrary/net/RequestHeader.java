package liub.com.mylibrary.net;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liub on 2017/10/26 .
 */
public class RequestHeader {
    public static final String APPID_APP = "101";

    public static Map<String, String> defaultHeader;
    private static Map<String, String> timeHeader;

    static {
        defaultHeader = new HashMap<>();
        timeHeader = new HashMap<>();
        staticInit(APPID_APP, "57", "2.3.0", "LRX21V");
    }

    private static void staticInit(String appid, String versionCode, String versionName, String deviceId) {
        defaultHeader.put("appId", appid);
        defaultHeader.put("versionCode", versionCode);
        defaultHeader.put("versionName", versionName);
        defaultHeader.put("deviceType", "1");
        defaultHeader.put("deviceId", deviceId);

        timeHeader.put("appId", appid);
        timeHeader.put("versionCode", versionCode);
        timeHeader.put("versionName", versionName);
        timeHeader.put("deviceType", "1");
        timeHeader.put("deviceId", deviceId);
        timeHeader.put("appId", appid);
    }

    public static void init(boolean fromApp, String versionCode, String versionName, String deviceId) {
        staticInit(APPID_APP , versionCode, versionName, deviceId);
    }

    public static Map<String, String> timeHeader(long timeStamp) {
        timeHeader.put("timeStamp", Long.toString(timeStamp));
        return timeHeader;
    }

    public static String get(String key) {
        String value = defaultHeader.get(key);
        if (TextUtils.isEmpty(value)) {
            return timeHeader.get(key);
        } else {
            return defaultHeader.get(key);
        }
    }
}
