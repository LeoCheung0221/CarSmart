package com.cn.smart.carsmart.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * author：leo on 2017/2/7 09:58
 * email： leocheung4ever@gmail.com
 * description: 定义生命周期和Activity相同
 * what & why is modified:
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
