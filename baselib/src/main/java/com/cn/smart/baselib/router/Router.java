package com.cn.smart.baselib.router;

import com.cn.smart.baselib.router.module.RouteCreator;

/**
 * author：leo on 2016/11/18 15:19
 * email： leocheung4ever@gmail.com
 * description: 路由类 用来实现跳转规则
 * what & why is modified:
 */

public class Router {

    /**
     * 设置创建路由跳转规则
     */
    public static void addRouteCreator(RouteCreator creator) {
        RouteManager.INSTANCE.addCreator(creator);
    }
}
