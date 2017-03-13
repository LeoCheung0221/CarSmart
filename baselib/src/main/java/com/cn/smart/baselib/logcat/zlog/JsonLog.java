package com.cn.smart.baselib.logcat.zlog;

import android.util.Log;

import com.cn.smart.baselib.logcat.ZLog;
import com.cn.smart.baselib.logcat.ZLogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author：leo on 2016/11/23 10:12
 * email： leocheung4ever@gmail.com
 * description: Json日志打印类
 * what & why is modified:
 */

public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(ZLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(ZLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        ZLogUtil.printLine(tag, true);
        message = headString + ZLog.LINE_SEPARATOR + message;
        String[] lines = message.split(ZLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        ZLogUtil.printLine(tag, false);
    }
}
