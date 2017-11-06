package liub.com.mylibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * Created by liub on 2017/10/26 .
 */
public class NetworkUtil {

    /**
     * 判断当前是否有网络连接
     *
     * @return 有连接返回true
     */
    public static boolean hasNetWorkConnect(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }
}
