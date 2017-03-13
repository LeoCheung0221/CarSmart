package com.cn.smart.baselib.router.module;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * author：leo on 2016/11/18 15:50
 * email： leocheung4ever@gmail.com
 * description: 包含数据的路由实体对象
 * what & why is modified:
 */
public class RouteMap {

    //数据类型包
    public static final int STRING = -1;
    public static final int BYTE = 0;
    public static final int SHORT = 1;
    public static final int INT = 2;
    public static final int LONG = 3;
    public static final int FLOAT = 4;
    public static final int DOUBLE = 5;
    public static final int BOOLEAN = 6;
    public static final int CHAR = 7;

    private String clzName;
    private Map<String, Integer> params = new HashMap<>();

    /**
     * 构造器
     *
     * @param clzName 类名必须是Activity全名
     */
    public RouteMap(String clzName) {
        this.clzName = clzName;
    }

    public <T extends Activity> RouteMap(Class<T> clz) {
        this.clzName = clz.getCanonicalName();
    }

    public String getClzName() {
        return clzName;
    }

    public Map<String, Integer> getParams() {
        return params;
    }

    public RouteMap addParams(String key, int type) {
        params.put(key, type);
        return this;
    }
}
