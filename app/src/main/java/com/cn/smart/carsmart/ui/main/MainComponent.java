package com.cn.smart.carsmart.ui.main;

import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;
import com.cn.smart.carsmart.ui.drive.MainDriveFragment;
import com.cn.smart.carsmart.ui.drive.MainDriveModule;
import com.cn.smart.carsmart.ui.home.MainHomeComponent;
import com.cn.smart.carsmart.ui.home.MainHomeFragment;
import com.cn.smart.carsmart.ui.home.MainHomeModule;

import dagger.Component;

/**
 * author：leo on 2017/2/8 15:59
 * email： leocheung4ever@gmail.com
 * description: main组件注入器
 * what & why is modified:
 */

@PerActivity
@Component(
        dependencies = {
                ApplicationComponent.class,
        },
        modules = {
                ActivityModule.class,
                MainModule.class,
                MainHomeModule.class,
                MainDriveModule.class
        }
)
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(MainDriveFragment fragment);

    void inject(MainHomeFragment fragment);
}
