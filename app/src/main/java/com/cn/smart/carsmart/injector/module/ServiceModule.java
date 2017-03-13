package com.cn.smart.carsmart.injector.module;

import android.app.Service;

import com.cn.smart.carsmart.injector.PerService;

import dagger.Module;
import dagger.Provides;

/**
 * author：leo on 2017/2/28 14:08
 * email： leocheung4ever@gmail.com
 * description: 对外提供依赖Service的方法注入
 * what & why is modified:
 */

@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service){
        this.mService = service;
    }

    @Provides
    @PerService
    public Service provieService(){
        return mService;
    }
}
