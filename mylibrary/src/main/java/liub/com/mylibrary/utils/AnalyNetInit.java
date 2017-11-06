package liub.com.mylibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import liub.com.mylibrary.Interface.NetBaseDealListener;
import liub.com.mylibrary.net.NetDealUtil;
import liub.com.mylibrary.net.RequestHeader;

/**
 * Created by liub on 2017/10/26 .
 */

public class AnalyNetInit {

    private static volatile AnalyNetInit netInit;
    private Gson gson;
    private Gson noteGson;

    public final static int URL_TYPE_REAL = 0, URL_TYPE_TEST = 1, URL_TYPE_LOCAL = 2;

    public AnalyNetInit() {
        gson = new Gson();
        initNote();
    }

    protected static class AnalyNetInitHolder {
        static final AnalyNetInit NET_INIT = new AnalyNetInit();
    }

    protected static AnalyNetInit getInstance() {
        return AnalyNetInitHolder.NET_INIT;
    }

    //.excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
    //.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
    private void initNote() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
                .serializeNulls().setDateFormat(JsonUtils.DEFAULT_DATE_PATTERN)//时间转化为特定格式
                .setPrettyPrinting() //对json结果格式化.
                .setVersion(JsonUtils.SINCE_VERSION_10);   //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
        noteGson = gsonBuilder.create();
    }

    public Gson getGson() {
        return gson;
    }

    public Gson getNoteGson() {
        return noteGson;
    }

    public static void setNetBaseDealListener(NetBaseDealListener baseDealListener) {
        NetDealUtil.getInstance().setNetBaseDealListener(baseDealListener);
    }

    public static NetBaseDealListener getNetBaseDealListener() {
        return NetDealUtil.getInstance().getNetBaseDealListener();
    }

    public static void initRequestHeader(String versionCode, String versionName, String deviceId) {
        RequestHeader.init(true, versionCode, versionName, deviceId);
    }
}
