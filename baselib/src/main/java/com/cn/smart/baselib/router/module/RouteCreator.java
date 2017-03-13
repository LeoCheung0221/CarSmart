package com.cn.smart.baselib.router.module;

import java.util.Map;

/**
 * author：leo on 2016/11/18 15:48
 * email： leocheung4ever@gmail.com
 * description: 定义路由规则接口
 * what & why is modified:
 */
public interface RouteCreator {

    /**
     * 为ActivityRoute创建路由规则
     *
     * @return 含所有规则的一个键值对映射
     */
    Map<String, RouteMap> createRouteRules();
}
