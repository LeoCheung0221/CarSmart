package com.cn.smart.carsmart.base.mvp;

import android.support.annotation.NonNull;

/**
 * author：leo on 2017/2/28 13:20
 * email： leocheung4ever@gmail.com
 * description: 基类Presenter
 * what & why is modified:
 */

public interface IPresenter<V extends IView> {

    /**
     * attach view
     */
    void attachView(@NonNull V view);

    /**
     * 防止内存泄露 清除presenter与activity之间的绑定
     */
    void detachView();

    /**
     * 获取view
     */
    IView getIView();

}
