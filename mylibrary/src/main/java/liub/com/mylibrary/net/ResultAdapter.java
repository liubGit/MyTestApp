package liub.com.mylibrary.net;

import android.content.Context;

import java.lang.reflect.Type;


/**
 * Created by liub on 2017/10/26 .
 */

public interface ResultAdapter<T> {
    T getResult();
    boolean isSuccess();

    String getMessage(Context context);

    long getTime();

    int getErrorCode();

    int getValuesEx();

    void parseResult(String resultJson, Type type);
}
