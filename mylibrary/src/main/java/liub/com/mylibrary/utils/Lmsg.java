package liub.com.mylibrary.utils;


import android.text.TextUtils;
import android.util.Log;

/**
 * 日志
 * Created by liub on 2017/10/26 .
 */
public class Lmsg {
    public static boolean isDebug = false;
    public static String TAG = "MyLibrary";

    public static void syso(String strMsg) {
        if (isDebug)
            System.out.println(strMsg + "");
    }

    public static void syso(String strTitle, String strMsg) {
        if (isDebug)
            System.out.println(strTitle + "--->" + strMsg);
    }

    public static void i(String strTag, String strMsg) {
        if (checkPrint(strMsg)) {
            if (TextUtils.isEmpty(strTag)) {
                strTag = TAG;
            }
            Log.i(strTag, strMsg);
        }
    }

    public static void i(String strMsg) {
        i(TAG, strMsg);
    }

    public static void d(String strTag, String strMsg) {
        Log.d(strTag, strMsg);
    }

    public static void d(String strMsg) {
        d(TAG, strMsg);
    }

    public static void e(String strTag, String strMsg) {
        if (checkPrint(strMsg)) {
            if (TextUtils.isEmpty(strTag)) {
                strTag = TAG;
            }
            Log.e(strTag, strMsg);
        }
    }

    public static void e(String strMsg) {
        e(TAG, strMsg);
    }

    public static void v(String strTag, String strMsg) {
        if (checkPrint(strMsg)) {
            if (TextUtils.isEmpty(strTag)) {
                strTag = TAG;
            }
            Log.v(strTag, strMsg);
        }
    }

    public static void v(String strMsg) {
        v(TAG, strMsg);
    }

    public static void w(String strTag, String strMsg) {
        if (checkPrint(strMsg)) {
            if (TextUtils.isEmpty(strTag)) {
                strTag = TAG;
            }
            Log.w(strTag, strMsg);
        }
    }

    public static void w(String strMsg) {
        w(TAG, strMsg);
    }

    public static void wtf(String strTag, String strMsg) {
        if (checkPrint(strMsg)) {
            if (TextUtils.isEmpty(strTag)) {
                strTag = TAG;
            }
            Log.wtf(strTag, strMsg);
        }
    }

    public static void wtf(String strMsg) {
        wtf(TAG, strMsg);
    }

    private static boolean checkPrint(String msg) {
        return isDebug && !TextUtils.isEmpty(msg);
    }

    /**
     * 打印超过3500以上的长日志。
     *
     * @param tag
     * @param content
     */
    public static void longLog(String tag, String content) {
        //限制长度
        int limitLength = 2000;
        while (content.length() > limitLength) {
            String logContent = content.substring(0, limitLength);
            content = content.replace(logContent, "");
            Log.e(tag, logContent);
        }
        Log.e(tag, content);
    }

}
