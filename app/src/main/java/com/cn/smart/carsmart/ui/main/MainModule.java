package com.cn.smart.carsmart.ui.main;

import android.content.Context;

import com.cn.smart.carsmart.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * author：leo on 2017/2/8 16:07
 * email： leocheung4ever@gmail.com
 * description: main模块中所有对外提供的方法
 * what & why is modified:
 */

@Module
public class MainModule {

    private MainActivity mActivity;

    MainModule(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @PerActivity
    Context provideContext() {
        return mActivity;
    }
}
