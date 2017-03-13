package com.cn.smart.baselib.share.util;

import com.cn.smart.baselib.share.core.SharePlatformConfig;
import com.cn.smart.baselib.share.core.SocializeMedia;

public class SharePlatformConfigHelper {

    private SharePlatformConfigHelper() {

    }

    public static void configQQPlatform(String appId, String appKey) {
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.QQ, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_KEY, appKey);
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.QZONE, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_KEY, appKey);
    }

    public static void configWeixinPlatform(String appId, String appSecret) {
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.WEIXIN, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_SECRET, appSecret);
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.WEIXIN_MONMENT, SharePlatformConfig.APP_ID, appId, SharePlatformConfig.APP_SECRET, appSecret);
    }

    public static void configSina(String appKey) {
        SharePlatformConfig.addPlatformDevInfo(SocializeMedia.SINA, SharePlatformConfig.APP_KEY, appKey);
    }

}
