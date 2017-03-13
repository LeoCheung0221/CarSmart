package com.cn.smart.carsmart.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * author：leo on 2017/2/28 14:10
 * email： leocheung4ever@gmail.com
 * description: 定义生命周期和Service相同
 * what & why is modified:
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
