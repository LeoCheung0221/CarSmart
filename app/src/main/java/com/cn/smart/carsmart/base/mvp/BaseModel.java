package com.cn.smart.carsmart.base.mvp;

import com.cn.smart.carsmart.components.okhttp.ApiCarSmart;
import com.cn.smart.carsmart.components.okhttp.HttpEngine;

/**
 * author：leo on 2017/2/28 13:15
 * email： leocheung4ever@gmail.com
 * description: 基类Model
 * what & why is modified:
 */

public class BaseModel implements IModel {

    protected static ApiCarSmart httpService;

    //初始化httpService
    static {
        httpService = HttpEngine.getHttpService();
    }
}
