package com.cn.smart.baselib.logcat.zlog;

import android.util.Log;

import com.cn.smart.baselib.logcat.ZLog;

/**
 * author：leo on 2016/11/23 10:08
 * email： leocheung4ever@gmail.com
 * description: 日志打印基类
 * what & why is modified:
 */

public class BaseLog {

    private static final int MAX_LENGTH = 4000;

    public static void printDefault(int type, String tag, String msg) {

        int index = 0;
        int length = msg.length();
        int countOfSub = length / MAX_LENGTH;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + MAX_LENGTH);
                printSub(type, tag, sub);
                index += MAX_LENGTH;
            }
            printSub(type, tag, msg.substring(index, length));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case ZLog.V:
                Log.v(tag, sub);
                break;
            case ZLog.D:
                Log.d(tag, sub);
                break;
            case ZLog.I:
                Log.i(tag, sub);
                break;
            case ZLog.W:
                Log.w(tag, sub);
                break;
            case ZLog.E:
                Log.e(tag, sub);
                break;
            case ZLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }
}
