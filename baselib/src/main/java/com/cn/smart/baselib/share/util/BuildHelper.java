package com.cn.smart.baselib.share.util;

import android.os.Build;

public class BuildHelper {
    public static int HONEYCOMB = 11;

    public static boolean isApi11_HoneyCombOrLater() {
        return getSDKVersion() >= HONEYCOMB;
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

}
