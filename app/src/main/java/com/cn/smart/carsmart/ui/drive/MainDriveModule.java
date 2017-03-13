package com.cn.smart.carsmart.ui.drive;

import dagger.Module;
import dagger.Provides;

/**
 * author：leo on 2017/3/3 13:09
 * email： leocheung4ever@gmail.com
 * description: main - 驾驶 模块所有对外提供的方法
 * what & why is modified:
 */

@Module
public class MainDriveModule {

    private String uid;

    public MainDriveModule() {
    }

    public MainDriveModule(String uid) {
        this.uid = uid;
    }

    @Provides
    String provideUid() {
        return uid;
    }
}
