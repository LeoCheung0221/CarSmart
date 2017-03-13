package com.cn.smart.baselib.uiframework.swipeback;

/**
 * author：leo on 2016/11/23 20:45
 * email： leocheung4ever@gmail.com
 * description: 左滑返回接口类
 * what & why is modified:
 */

public interface ISwipeBack {

    /**
     * 获得左滑布局
     */
    SwipeBackLayout getSwipeBackLayout();

    /**
     * 设置是否可以左滑退出
     */
    void setSwipeBackEnable(boolean enable);

    /**
     * 滑出内容视图 结束Activity
     */
    void scrollToFinishActivity();
}
