package com.cn.smart.carsmart.ui.account.retrieve;

import dagger.Module;

/**
 * author：leo on 2017/3/9 16:55
 * email： leocheung4ever@gmail.com
 * description: 找回密码 方法提供模块
 * what & why is modified:
 */

@Module
public class FindMeModule {

    private FindMeActivity mActivity;

    FindMeModule(FindMeActivity mActivity) {
        this.mActivity = mActivity;
    }
}
