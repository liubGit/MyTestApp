package liub.com.mylibrary.net;

import liub.com.mylibrary.Interface.NetBaseDealListener;

/**
 * 网络特殊处理
 * Created by liub on 2017/10/26 .
 */

public class NetDealUtil {
    private static volatile NetDealUtil netDealUtil;

    private NetBaseDealListener baseDealListener;

    public static NetDealUtil getInstance() {
        if (netDealUtil == null)
            synchronized (NetDealUtil.class) {
                if (netDealUtil == null)
                    netDealUtil = new NetDealUtil();
            }
        return netDealUtil;
    }

    private NetDealUtil() {
    }

    public void dealNetStatus(int errorCode, String url) {
        if (baseDealListener == null) return;
        switch (errorCode) {
            case 10263://游客无操作权限
                baseDealListener.dealNeedLogin();
                break;
            case 10003://令牌错误
            case 10007://该用户长时间没有登录让用户重新登录（令牌过期）
            case 3425: //生活馆token失效
            case 40019://token不存在或已经失效,请重新登录!
                baseDealListener.dealLoginExpired();
                break;
            case 20406://签名时间戳失效
            case 20403://令牌错误
                baseDealListener.dealTestPlatformNeedLogin();
                break;
            case 500:
            case 501:
            case 502:
            case 403:
            case 404:
            case 405:
            case 503:   // 对应平台无法访问
            case 504:   // 对应平台访问超时
            case 4400:  // SSO平台访问失败
                baseDealListener.onServerNotAccess(url);
                break;
        }
    }

    public void setNetBaseDealListener(NetBaseDealListener baseDealListener) {
        this.baseDealListener = baseDealListener;
    }

    public NetBaseDealListener getNetBaseDealListener() {
        return baseDealListener;
    }
}
