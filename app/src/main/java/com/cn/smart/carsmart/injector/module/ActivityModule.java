package com.cn.smart.carsmart.injector.module;

import android.app.Activity;

import com.cn.smart.carsmart.injector.PerActivity;

import javax.annotation.security.PermitAll;

import dagger.Module;
import dagger.Provides;

/**
 * author：leo on 2017/2/8 14:28
 * email： leocheung4ever@gmail.com
 * description: Activity模块内需要注解方法的父类集合 --> 提供需注入的Activity对象
 * what & why is modified:
 */

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

}
