package liub.com.mylibrary.net;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import liub.com.mylibrary.utils.JsonUtils;

/**
 * Created by liub on 2017/10/26 .
 */

public class NetResultAdapter<T> implements ResultAdapter<T> {
    private JSONObject mResultJson;
    private Type mType;
    private Boolean success;
    private String mResultString;
    private Integer mErrorCode;
    private String mTime;
    private String message;
    private int mExtra;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

    @Override
    public boolean isSuccess() {
        return success == null ? (mResultJson != null) : success;
    }

    @Override
    public String getMessage(Context context) {
        if (TextUtils.isEmpty(message) || message.equals("null"))
            message = NetHandler.getErrorMessage(context, mErrorCode);
        return message;
    }

    @Override
    public long getTime() {
        try {
            return dateFormat.parse(mTime).getTime();
        } catch (Exception e) {
            //再尝试一次，是否是时间戳（晋段后的修改）
            try {
                return Long.valueOf(mTime);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return 0;
        }
    }

    @Override
    public int getErrorCode() {
        return mErrorCode == null ? 0 : mErrorCode;
    }

    @Override
    public int getValuesEx() {
        return mExtra;
    }

    @Override
    public void parseResult(String resultJson, Type type) {
        try {
            mResultJson = new JSONObject(resultJson);
            mType = type;
            if (mResultJson.has("valuse"))
                mResultString = mResultJson.getString("valuse");
            if (mResultJson.has("succeed"))
                success = mResultJson.getBoolean("succeed");
            if (mResultJson.has("errmsg"))
                mErrorCode = mResultJson.getInt("errmsg");
            if (mResultJson.has("MsgTime"))
                mTime = mResultJson.getString("MsgTime");
            if (mResultJson.has("msg"))
                message = mResultJson.getString("msg");
            if (mResultJson.has("valuseex")) {
                String extraString = mResultJson.getString("valuseex");
                if (!TextUtils.isEmpty(extraString) && !extraString.equals("null")) {
                    mExtra = Integer.parseInt(extraString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
