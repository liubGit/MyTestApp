package liub.com.mylibrary.model.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liub on 2017/10/26 .
 */
public class SSOEncryptData implements Parcelable {
    private String encrypt;
    private String msgSignature;
    private String nonce;
    private String timeStamp;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getMsgSignature() {
        return msgSignature;
    }

    public void setMsgSignature(String msgSignature) {
        this.msgSignature = msgSignature;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.encrypt);
        dest.writeString(this.msgSignature);
        dest.writeString(this.nonce);
        dest.writeString(this.timeStamp);
    }

    public SSOEncryptData() {
    }

    protected SSOEncryptData(Parcel in) {
        this.encrypt = in.readString();
        this.msgSignature = in.readString();
        this.nonce = in.readString();
        this.timeStamp = in.readString();
    }

    public static final Creator<SSOEncryptData> CREATOR = new Creator<SSOEncryptData>() {
        @Override
        public SSOEncryptData createFromParcel(Parcel source) {
            return new SSOEncryptData(source);
        }

        @Override
        public SSOEncryptData[] newArray(int size) {
            return new SSOEncryptData[size];
        }
    };
}
