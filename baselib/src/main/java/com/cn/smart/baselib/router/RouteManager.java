package com.cn.smart.baselib.router;


import com.cn.smart.baselib.router.module.RouteCreator;
import com.cn.smart.baselib.router.module.RouteMap;
import com.cn.smart.baselib.router.route.RouteCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author：leo on 2016/11/18 16:03
 * email： leocheung4ever@gmail.com
 * description: 路由管理枚举类
 * what & why is modified:
 */
public enum RouteManager {

    INSTANCE;

    /**
     * 全局路由回调
     */
    RouteCallback GlobalCallback;

    /**
     * 是否需要重载路由映射
     */
    boolean shouldReload;

    /**
     * 装载所有路由规则创建器的列表容器，兼容一些复杂场景
     */
    List<RouteCreator> creatorList = new ArrayList<>();

    /**
     * 装载所有由创建器列表创建的路由规则的映射
     */
    Map<String, RouteMap> routeMap = new HashMap<>();

    public void setCallback(RouteCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback should not be null");
        }
        this.GlobalCallback = callback;
    }

    public RouteCallback getCallback() {
        return GlobalCallback;
    }

    public Map<String, RouteMap> getRouteMap() {
        if (shouldReload) {
            routeMap.clear();
            int count = creatorList == null ? 1 : creatorList.size();
            for (int i = 0; i < count; i++) {
                routeMap.putAll(creatorList.get(i).createRouteRules());
            }
            shouldReload = false;
        }
        return routeMap;
    }

    public void addCreator(RouteCreator creator) {
        if (creator == null) {
            throw new IllegalArgumentException("Route creator should not be null");
        }
        this.creatorList.add(creator);
        shouldReload = true;
    }
}
