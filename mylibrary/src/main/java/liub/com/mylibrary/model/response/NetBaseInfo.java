package liub.com.mylibrary.model.response;

import java.io.Serializable;

/**
 * 基础网络返回
 * Created by liub on 2017/10/26 .
 */

public class NetBaseInfo<T> implements Serializable {

    private long time;
    private T result;
    private String msgCode;

    public NetBaseInfo() {

    }

    public NetBaseInfo(String msgCode, T result, long time) {
        this.msgCode = msgCode;
        this.result = result;
        this.time = time;
    }

    public NetBaseInfo(long time, T result) {
        setTime(time);

        setResult(result);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
