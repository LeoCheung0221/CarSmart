package com.cn.smart.baselib.logcat;

import android.text.TextUtils;
import android.util.Log;

/**
 * author：leo on 2016/11/23 10:07
 * email： leocheung4ever@gmail.com
 * description: 日志工具处理类
 * what & why is modified:
 */

public class ZLogUtil {

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}
