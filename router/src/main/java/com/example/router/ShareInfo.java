package com.example.router;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liub on 2018/1/17.
 */

public class ShareInfo implements Parcelable{
    private String Title;
    private String Content;
    private String Link;
    private String Thumbnail;
    public static final Parcelable.Creator<ShareInfo> CREATOR = new Parcelable.Creator() {
        public ShareInfo createFromParcel(Parcel source) {
            return new ShareInfo(source);
        }

        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getContent() {
        return this.Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public String getLink() {
        return this.Link;
    }

    public void setLink(String link) {
        this.Link = link;
    }

    public String getThumbnail() {
        return this.Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.Thumbnail = thumbnail;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Content);
        dest.writeString(this.Link);
        dest.writeString(this.Thumbnail);
    }

    public ShareInfo() {
    }

    protected ShareInfo(Parcel in) {
        this.Title = in.readString();
        this.Content = in.readString();
        this.Link = in.readString();
        this.Thumbnail = in.readString();
    }
}
