package com.cn.smart.baselib.share.core;

import java.util.HashMap;
import java.util.Map;

/**
 * author：leo on 2017/2/6 12:23
 * email： leocheung4ever@gmail.com
 * description: TODO
 * what & why is modified:
 */

public class SharePlatformConfig {

    public static final String APP_ID = "appId";
    public static final String APP_KEY = "appKey";
    public static final String APP_SECRET = "AppSecret";

    private static HashMap<SocializeMedia, Map<String, Object>> CONFIG = new HashMap<>();

    public static boolean hasAlreadyConfig() {
        return !CONFIG.isEmpty();
    }

    public static void addPlatformDevInfo(SocializeMedia media, HashMap<String, Object> value) {
        CONFIG.put(media, value);
    }

    public static void addPlatformDevInfo(SocializeMedia media, String... appInfo) {
        if (appInfo == null || appInfo.length % 2 != 0) {
            throw new RuntimeException("Please check your share app config info");
        }

        HashMap<String, Object> infoMap = new HashMap<>();
        int length = appInfo.length / 2;
        for (int i = 0; i < length; i++) {
            infoMap.put(appInfo[i * 2], appInfo[i * 2 + 1]);
        }
        addPlatformDevInfo(media, infoMap);
    }

    public static Map<String, Object> getPlatformDevInfo(SocializeMedia media) {
        return CONFIG.get(media);
    }
}
