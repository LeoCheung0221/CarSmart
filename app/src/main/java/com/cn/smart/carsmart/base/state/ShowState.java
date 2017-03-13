package com.cn.smart.carsmart.base.state;

import android.view.View;
import android.view.animation.Animation;

/**
 * author：leo on 2016/11/25 16:20
 * email： leocheung4ever@gmail.com
 * description: 显示状态行为接口
 * what & why is modified:
 */

public interface ShowState {

    /**
     * 显示状态
     *
     * @param animate 是否动画
     */
    void show(boolean animate);

    /**
     * 隐藏状态
     *
     * @param animate 是否动画
     */
    void dismiss(boolean animate);

    /**
     * 设置碎片视图
     */
    void setFragmentView(View fragmentView);

    /**
     * 设置进入动画
     */
    void setAnimIn(Animation in);

    /**
     * 设置退出动画
     */
    void setAnimOut(Animation out);

}
