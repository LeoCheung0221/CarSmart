package com.cn.smart.baselib.share.core.shareparam;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareParamWebPage extends BaseShareParam {
    protected ShareImage mThumb;

    public ShareParamWebPage() {
    }

    public ShareParamWebPage(String title, String content) {
        super(title, content);
    }

    public ShareParamWebPage(String title, String content, String targetUrl) {
        super(title, content, targetUrl);
    }

    public ShareImage getThumb() {
        return mThumb;
    }

    public void setThumb(ShareImage thumb) {
        mThumb = thumb;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mThumb, 0);
    }

    protected ShareParamWebPage(Parcel in) {
        super(in);
        mThumb = in.readParcelable(ShareImage.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShareParamWebPage> CREATOR = new Parcelable.Creator<ShareParamWebPage>() {
        public ShareParamWebPage createFromParcel(Parcel source) {
            return new ShareParamWebPage(source);
        }

        public ShareParamWebPage[] newArray(int size) {
            return new ShareParamWebPage[size];
        }
    };
}
