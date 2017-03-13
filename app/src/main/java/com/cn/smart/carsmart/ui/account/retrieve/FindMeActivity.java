package com.cn.smart.carsmart.ui.account.retrieve;

import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseSwipeBackActivity;

/**
 * author：leo on 2017/3/9 16:40
 * email： leocheung4ever@gmail.com
 * description: 找回密码界面
 * what & why is modified:
 */

public class FindMeActivity extends BaseSwipeBackActivity {

    private FindMeComponent mFindMeComponent;

    @Override
    protected int initContentView() {
        return R.layout.activity_retrieve;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    protected void initInjector() {
        mFindMeComponent = DaggerFindMeComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .findMeModule(new FindMeModule(this))
                .build();
        mFindMeComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {

    }
}
