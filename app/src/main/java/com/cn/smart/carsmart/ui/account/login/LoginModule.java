package com.cn.smart.carsmart.ui.account.login;

import dagger.Module;

/**
 * author：leo on 2017/2/28 17:24
 * email： leocheung4ever@gmail.com
 * description: 登录模块方法提供
 * what & why is modified:
 */

@Module
public class LoginModule {

    private LoginActivity mActivity;

    LoginModule(LoginActivity mActivity) {
        this.mActivity = mActivity;
    }
}
