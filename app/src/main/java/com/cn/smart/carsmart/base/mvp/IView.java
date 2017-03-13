package com.cn.smart.carsmart.base.mvp;

import com.cn.smart.carsmart.base.BaseActivity;
import com.cn.smart.carsmart.base.BaseSwipeBackActivity;

/**
 * author：leo on 2017/2/8 11:14
 * email： leocheung4ever@gmail.com
 * description: View基类
 * what & why is modified:
 */

public interface IView<M> {

    /**
     * request network data success
     *
     * @param tag  加载成功的标识
     * @param data 加载成功的数据
     */
    void onDataSuccess(String tag, M data);

    /**
     * request network data success
     *
     * @param data 加载成功数据
     */
    void onDataSuccess(M data);

    /**
     * request network data fail [note: 401 or 404 and so on]
     *
     * @param tag    加载失败标识
     * @param reason 加载失败原因
     */
    void onDataFailed(String tag, String reason);

    /**
     * request network data fail [note: 401 or 404 and so on]
     *
     * @param reason 失败原因
     */
    void onDataFailed(String reason);

    /**
     * there is no network
     *
     * @param tag 无网络标识
     */
    void onNoNetwork(String tag);

    /**
     * finish
     *
     * @param tag 完成标识
     */
    void onComplete(String tag);

    /**
     * finish
     */
    void onComplete();

    /**
     * 获得上下文
     */
    BaseActivity getContextForPresenter();
}
