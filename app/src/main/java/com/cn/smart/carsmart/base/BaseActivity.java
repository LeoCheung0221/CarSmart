package com.cn.smart.carsmart.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
 * description: app所有Activity基类
 * what & why is modified:
 */

public abstract class BaseActivity extends RootBaseActivity implements IContract.IIView {

    @Override
    public BaseActivity getContextForPresenter() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUiAndListener();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((Core) getApplication()).getApplicationComponent();
    }

    protected ApplicationModule getApplicationModule() {
        return new ApplicationModule(this);
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataSuccess(String tag, Object data) {
    }

    @Override
    public void onDataSuccess(Object data) {
    }

    @Override
    public void onDataFailed(String tag, String reason) {
    }

    @Override
    public void onDataFailed(String reason) {
    }

    @Override
    public void onNoNetwork(String tag) {
    }

    @Override
    public void onComplete(String tag) {
    }

    @Override
    public void onComplete() {
    }
}
