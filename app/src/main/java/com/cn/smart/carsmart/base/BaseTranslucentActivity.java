package com.cn.smart.carsmart.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.cn.smart.baselib.uiframework.base.RootBaseActivity;
import com.cn.smart.carsmart.Core;
import com.cn.smart.carsmart.base.mvp.IContract;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.module.ActivityModule;
import com.cn.smart.carsmart.injector.module.ApplicationModule;

import butterknife.ButterKnife;

/**
 * author：leo on 2017/2/8 11:02
 * email： leocheung4ever@gmail.com
 * description: app所有透明状态栏  Activity基类
 * what & why is modified:
 */

public class BaseTranslucentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
