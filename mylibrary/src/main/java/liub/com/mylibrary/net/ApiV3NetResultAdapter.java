package liub.com.mylibrary.net;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import liub.com.mylibrary.utils.JsonUtils;

/**
 * Created by liub on 2017/10/26 .
 */
public class ApiV3NetResultAdapter<T> implements ResultAdapter<T> {
    /**
     * 成功码
     */
    private static final int CODE_SUCCESS = 10000;

    private String mErrorMessage = "";
    private String mResultString;
    private Type mType;
    private boolean success;
    private int mResultCode;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private JSONObject mResultJson;
    private String mTime;

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

    public ApiV3NetResultAdapter() {
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
        try {
            return TextUtils.isEmpty(mTime) ? System.currentTimeMillis() : dateFormat.parse(mTime).getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
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
            mResultJson = new JSONObject(resultJson);
            mType = type;
            if (mResultJson.has("data"))
                mResultString = mResultJson.getString("data");
            if (mResultJson.has("succeed"))
                success = mResultJson.getBoolean("succeed");
            if (mResultJson.has("responseCode"))
                mResultCode = mResultJson.getInt("responseCode");
            if (mResultJson.has("MsgTime"))
                mTime = mResultJson.getString("msgTime");
            if (mResultJson.has("msg"))
                mErrorMessage = mResultJson.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
