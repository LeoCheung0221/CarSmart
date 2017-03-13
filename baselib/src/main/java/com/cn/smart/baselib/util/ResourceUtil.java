package com.cn.smart.baselib.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

import com.cn.smart.baselib.R;

/**
 * author：leo on 2016/11/21 19:13
 * email： leocheung4ever@gmail.com
 * description: 资源相关工具类
 * what & why is modified:
 */
public class ResourceUtil {

    /**
     * 获取主题颜色
     */
    public static int getThemeColor(@NonNull Context context) {
        return getThemeAttrColor(context, R.attr.colorPrimary);
    }

    /**
     * 获取主题属性颜色
     *
     * @param context 上下文
     * @param attr    属性
     * @return 颜色值
     */
    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
}
