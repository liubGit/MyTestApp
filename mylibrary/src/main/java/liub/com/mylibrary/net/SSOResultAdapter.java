package liub.com.mylibrary.net;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import liub.com.mylibrary.utils.JsonUtils;

/**
 * Created by liub on 2017/10/26 .
 */
public class SSOResultAdapter<T> implements ResultAdapter<T> {
    /**
     * 成功码
     */
    private static final int CODE_SUCCESS = 2000;

    private String mErrorMessage = "";
    private String mResultString;
    private Type mType;
    private boolean success;
    private int mResultCode;

    @Override
    public T getResult() {
        if (!TextUtils.isEmpty(mResultString)) {
            if (mType == String.class) {
                return (T) mResultString;
            }
            return JsonUtils.jsonResolve(mResultString, mType);
        }
        return null;
    }

    public SSOResultAdapter() {
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage(Context context) {
        return mErrorMessage;
    }

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }

    @Override
    public int getErrorCode() {
        return mResultCode;
    }

    @Override
    public int getValuesEx() {
        return 0;
    }

    @Override
    public void parseResult(String resultJson, Type type) {
        try {
            JSONObject mResultJson = new JSONObject(resultJson);
            mType = type;
            mErrorMessage = mResultJson.getString("msg");
            mResultCode = Integer.parseInt(mResultJson.getString("code"));
            success = mResultCode == CODE_SUCCESS;
            mResultString = mResultJson.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
