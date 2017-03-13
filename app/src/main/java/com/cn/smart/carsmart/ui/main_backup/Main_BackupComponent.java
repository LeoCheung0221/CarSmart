package com.cn.smart.carsmart.ui.main_backup;

import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;

import dagger.Component;

/**
 * author：leo on 2017/2/8 15:59
 * email： leocheung4ever@gmail.com
 * description: main组件注入器
 * what & why is modified:
 */

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                Main_BackupModule.class
        }
)
public interface Main_BackupComponent {
    void inject(Main_BackupActivity activity);
}
