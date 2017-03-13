package com.cn.smart.carsmart.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * author：leo on 2016/11/25 21:12
 * email： leocheung4ever@gmail.com
 * description: 懒加载Fragment基类
 * what & why is modified:
 */

public abstract class BaseLazyLoadFragment extends BaseFragment {

    /**
     * 是否是第一次加载
     */
    private boolean isFirstLoad = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInjector();
        getBundle(getArguments());
        initUI(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        }
    }

    private void onVisible() {
        if (isFirstLoad && isPrepare) {
            initData();
            isFirstLoad = false;
        }
    }
}
