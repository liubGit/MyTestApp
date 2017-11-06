package liub.com.mylibrary.Interface;

/**
 * 网络特殊处理
 * Created by liub on 2017/10/26 .
 */
public abstract class NetBaseDealListener {

    /**
     * 处理需要登录
     */
    public abstract void dealNeedLogin();

    /**
     * 处理登录过期
     */
    public abstract void dealLoginExpired();

    /**
     * 处理考试平台需要重新登录
     */
    public abstract void dealTestPlatformNeedLogin();

    /**
     * 503\504，服务器维护
     * @param url 请求的地址
     */
    public abstract void onServerNotAccess(String url);
}
