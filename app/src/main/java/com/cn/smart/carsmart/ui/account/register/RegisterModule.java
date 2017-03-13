package com.cn.smart.carsmart.ui.account.register;

import com.cn.smart.carsmart.ui.account.login.LoginActivity;

import dagger.Module;

/**
 * author：leo on 2017/2/28 17:24
 * email： leocheung4ever@gmail.com
 * description: 注册模块方法提供
 * what & why is modified:
 */

@Module
public class RegisterModule {

    private RegisterActivity mActivity;

    RegisterModule(RegisterActivity mActivity) {
        this.mActivity = mActivity;
    }
}
