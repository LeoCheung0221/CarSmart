package com.cn.smart.carsmart.injector;

/**
 * author：leo on 2017/2/8 15:58
 * email： leocheung4ever@gmail.com
 * description: 获得组件接口
 * what & why is modified:
 */

public interface HasComponent<C> {
    C getComponent();
}
