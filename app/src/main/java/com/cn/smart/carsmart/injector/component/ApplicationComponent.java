package com.cn.smart.carsmart.injector.component;

import com.cn.smart.baselib.uiframework.base.RootBaseActivity;
import com.cn.smart.carsmart.Core;
import com.cn.smart.carsmart.injector.module.ApiModule;
import com.cn.smart.carsmart.injector.module.ApplicationModule;
import com.cn.smart.carsmart.injector.module.DbModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author：leo on 2017/2/7 13:46
 * email： leocheung4ever@gmail.com
 * description: 组件注入器  是@Inject 和 @Module 桥梁
 * 提供了所有定义了的类型的实例
 * what & why is modified:
 */

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ApiModule.class,
                DbModule.class
        }
)
public interface ApplicationComponent {

    void inject(Core application);

    void inject(RootBaseActivity activity);

}
