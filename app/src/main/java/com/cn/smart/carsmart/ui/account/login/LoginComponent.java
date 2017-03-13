package com.cn.smart.carsmart.ui.account.login;

import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;

import dagger.Component;

/**
 * author：leo on 2017/2/28 17:23
 * email： leocheung4ever@gmail.com
 * description: 登录模块组件注入器
 * what & why is modified:
 */

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                LoginModule.class
        }
)
public interface LoginComponent {

    void inject(LoginActivity activity);
}
