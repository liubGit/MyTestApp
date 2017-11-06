package liub.com.mylibrary.model.request;

/**
 * Created by liub on 2017/10/26 .
 */
public class BaseRequestTypeInfo<T> {
    private String accessToken;
    private T model;
    private String type;
    private String whichFunc ;

    public BaseRequestTypeInfo(String key, T model, String type, String whichFunc) {
//        this.accessToken = key;
        this.model = model;
        this.type = type;
        this.whichFunc = whichFunc;
    }
}
