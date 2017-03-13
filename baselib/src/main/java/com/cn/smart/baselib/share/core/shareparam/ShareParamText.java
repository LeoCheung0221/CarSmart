package com.cn.smart.baselib.share.core.shareparam;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareParamText extends BaseShareParam {

    public ShareParamText(String title, String content) {
        super(title, content);
    }

    public ShareParamText(String title, String content, String targetUrl) {
        super(title, content, targetUrl);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ShareParamText(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ShareParamText> CREATOR = new Parcelable.Creator<ShareParamText>() {
        public ShareParamText createFromParcel(Parcel source) {
            return new ShareParamText(source);
        }

        public ShareParamText[] newArray(int size) {
            return new ShareParamText[size];
        }
    };
}
