package com.cn.smart.baselib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * author：leo on 2016/11/21 18:26
 * email： leocheung4ever@gmail.com
 * description: 设置偏好工具类
 * what & why is modified:
 */

public class SettingPrefUtil {

    /**
     * 获得主题索引值
     *
     * @param context 上下文
     * @return 主题索引
     */
    public static int getThemeIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("ThemeIndex", 9);
    }

    /**
     * 设置夜间模式
     */
    public static void setNightModel(Context context, boolean nightModel) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("pNightMode", nightModel).apply();
    }

    /**
     * 获得夜间模式
     */
    public static boolean getNightModel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNightMode", false);
    }

    /**
     * 获得登录后Uid
     */
    public static String getLoginUid(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("loginUid", "");
    }

    /**
     * 设置登录唯一标志码
     */
    public static void setLoginUid(Context context, String uid) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("loginUid", uid).apply();
    }

    /**
     * 获得左滑返回侧移模式
     */
    public static int getSwipeBackEdgeMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString("pSwipeBackEdgeMode", "0"));
    }
}
