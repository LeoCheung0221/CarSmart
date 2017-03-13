package com.cn.smart.carsmart.ui.account.register;

import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;
import com.cn.smart.carsmart.ui.account.login.LoginActivity;

import dagger.Component;

/**
 * author：leo on 2017/2/28 17:23
 * email： leocheung4ever@gmail.com
 * description: 注册模块组件注入器
 * what & why is modified:
 */

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                RegisterModule.class
        }
)
public interface RegisterComponent {

    void inject(RegisterActivity activity);
}
