package com.cn.smart.carsmart.ui.home;

import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;
import com.cn.smart.carsmart.ui.main.MainComponent;

import dagger.Component;

/**
 * author：leo on 2017/3/3 19:50
 * email： leocheung4ever@gmail.com
 * description: main - 首页模块 组件注入器
 * what & why is modified:
 */

@PerActivity
@Component(
        dependencies = {
                ApplicationComponent.class,
        },
        modules = {
                ActivityModule.class,
                MainHomeModule.class,
        }
)
public interface MainHomeComponent {

    void inject(MainHomeFragment fragment);

}
