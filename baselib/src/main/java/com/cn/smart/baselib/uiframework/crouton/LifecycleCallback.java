package com.cn.smart.baselib.uiframework.crouton;

/**
 * author：leo on 2016/11/23 14:09
 * email： leocheung4ever@gmail.com
 * description: 生命周期回调接口
 * what & why is modified:
 */

public interface LifecycleCallback {

    /**
     * Will be called when your Crouton has been displayed.
     */
    void onDisplayed();

    /**
     * Will be called when your {@link Crouton} has been removed.
     */
    void onRemoved();

    //void onCeasarDressing();

}
