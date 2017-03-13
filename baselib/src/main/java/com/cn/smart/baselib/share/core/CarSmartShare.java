package com.cn.smart.baselib.share.core;

import android.app.Activity;
import android.content.Intent;

import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;

/**
 * author：leo on 2017/1/6 09:54
 * email： leocheung4ever@gmail.com
 * description: 分享类
 * what & why is modified:
 */

public class CarSmartShare {

    private static final CarSmartShareAttach sShareAttach = new CarSmartShareAttach();

    private CarSmartShare() {
    }

    /**
     * 分享前必须先初始化
     */
    public static void init(CarSmartShareConfiguration configuration) {
        sShareAttach.init(configuration);
    }

    /**
     * 分享的入口
     */
    public static void share(Activity activity, SocializeMedia type, BaseShareParam params, SocializeListeners.ShareListener listener) {
        sShareAttach.share(activity, type, params, listener);
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        sShareAttach.onActivityResult(activity, requestCode, resultCode, data);
    }

    public static CarSmartShareConfiguration getShareConfiguration() {
        return sShareAttach.getShareConfiguration();
    }
}
