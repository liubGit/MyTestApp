package com.example.router;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liub on 2018/1/17.
 */

public class RequestStartViewInfo implements Parcelable {
    private String URL;
    private boolean IsNotifyResult;
    private int NavigationBarStyle;
    private boolean IsLoading;
    private boolean HideCloseButton;
    private boolean HideScrollbar;
    private int TitleStyle;
    private boolean ShareEnable;
    private ShareInfo Share;
    private boolean Token;
    public static final Parcelable.Creator<RequestStartViewInfo> CREATOR = new Parcelable.Creator() {
        public RequestStartViewInfo createFromParcel(Parcel source) {
            return new RequestStartViewInfo(source);
        }

        public RequestStartViewInfo[] newArray(int size) {
            return new RequestStartViewInfo[size];
        }
    };

    public RequestStartViewInfo(String url, boolean isNotifyResult, int style, boolean isLoading) {
        this(url, isNotifyResult, style, isLoading, false, 1);
    }

    public RequestStartViewInfo(String url, boolean isNotifyResult, int style, boolean isLoading, boolean hideCloseButton) {
        this(url, isNotifyResult, style, isLoading, hideCloseButton, 1);
    }

    public RequestStartViewInfo(String url, boolean isNotifyResult, int style, boolean isLoading, boolean hideCloseButton, int titleStyle) {
        this.setUrl(url);
        this.setNotifyResult(isNotifyResult);
        this.setStyle(style);
        this.setLoading(isLoading);
        this.setHideCloseButton(hideCloseButton);
        this.setTitleStyle(titleStyle);
    }

    public boolean isHideScrollbar() {
        return this.HideScrollbar;
    }

    public void setHideScrollbar(boolean hideScrollbar) {
        this.HideScrollbar = hideScrollbar;
    }

    public boolean isHideCloseButton() {
        return this.HideCloseButton;
    }

    public void setHideCloseButton(boolean hideCloseButton) {
        this.HideCloseButton = hideCloseButton;
    }

    public void setUrl(String URL) {
        this.URL = URL;
    }

    public void setNotifyResult(boolean notifyResult) {
        this.IsNotifyResult = notifyResult;
    }

    public void setStyle(int style) {
        this.NavigationBarStyle = style;
    }

    public void setLoading(boolean isLoading) {
        this.IsLoading = isLoading;
    }

    public String getUrl() {
        return this.URL;
    }

    public boolean isNotifyResult() {
        return this.IsNotifyResult;
    }

    public int getStyle() {
        return this.NavigationBarStyle;
    }

    public boolean isLoading() {
        return this.IsLoading;
    }

    public int getTitleStyle() {
        return this.TitleStyle;
    }

    public void setTitleStyle(int TitleStyle) {
        this.TitleStyle = TitleStyle;
    }

    public boolean isShareEnable() {
        return this.ShareEnable;
    }

    public void setShareEnable(boolean shareEnable) {
        this.ShareEnable = shareEnable;
    }

    public boolean isToken() {
        return this.Token;
    }

    public void setToken(boolean token) {
        this.Token = token;
    }

    public RequestStartViewInfo() {
    }

    public ShareInfo getShare() {
        return this.Share;
    }

    public void setShare(ShareInfo share) {
        this.Share = share;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.URL);
        dest.writeByte((byte)(this.IsNotifyResult?1:0));
        dest.writeInt(this.NavigationBarStyle);
        dest.writeByte((byte)(this.IsLoading?1:0));
        dest.writeByte((byte)(this.HideCloseButton?1:0));
        dest.writeByte((byte)(this.HideScrollbar?1:0));
        dest.writeInt(this.TitleStyle);
        dest.writeByte((byte)(this.ShareEnable?1:0));
        dest.writeParcelable(this.Share, flags);
        dest.writeByte((byte)(this.Token?1:0));
    }

    protected RequestStartViewInfo(Parcel in) {
        this.URL = in.readString();
        this.IsNotifyResult = in.readByte() != 0;
        this.NavigationBarStyle = in.readInt();
        this.IsLoading = in.readByte() != 0;
        this.HideCloseButton = in.readByte() != 0;
        this.HideScrollbar = in.readByte() != 0;
        this.TitleStyle = in.readInt();
        this.ShareEnable = in.readByte() != 0;
        this.Share = (ShareInfo)in.readParcelable(ShareInfo.class.getClassLoader());
        this.Token = in.readByte() != 0;
    }
}
