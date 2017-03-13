package com.cn.smart.carsmart.ui.account.retrieve;

import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;

import dagger.Component;

/**
 * author：leo on 2017/3/9 16:55
 * email： leocheung4ever@gmail.com
 * description: 找回密码 组件注入器
 * what & why is modified:
 */

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                FindMeModule.class
        }
)
public interface FindMeComponent {

    void inject(FindMeActivity activity);
}
