package com.cn.smart.baselib.router.route;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * author：leo on 2016/11/18 16:11
 * email： leocheung4ever@gmail.com
 * description: 一个用来承载附加数据Intent的容器
 * what & why is modified:
 */

public class ActivityRouteBundleExtras implements Parcelable {

    int flags;
    int outAnimation = -1;
    int inAnimation = -1;
    int requestCode = -1;
    Bundle extras = new Bundle();

    ActivityRouteBundleExtras() {
    }

    protected ActivityRouteBundleExtras(Parcel in) {
        extras = in.readBundle(getClass().getClassLoader());
        requestCode = in.readInt();
        inAnimation = in.readInt();
        outAnimation = in.readInt();
        flags = in.readInt();
    }

    public static final Creator<ActivityRouteBundleExtras> CREATOR = new Creator<ActivityRouteBundleExtras>() {
        @Override
        public ActivityRouteBundleExtras createFromParcel(Parcel in) {
            return new ActivityRouteBundleExtras(in);
        }

        @Override
        public ActivityRouteBundleExtras[] newArray(int size) {
            return new ActivityRouteBundleExtras[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(extras);
        dest.writeInt(requestCode);
        dest.writeInt(inAnimation);
        dest.writeInt(outAnimation);
        dest.writeInt(flags);
    }
}
