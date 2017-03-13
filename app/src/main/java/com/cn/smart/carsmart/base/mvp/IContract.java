package com.cn.smart.carsmart.base.mvp;

/**
 * author：leo on 2017/2/8 11:04
 * email： leocheung4ever@gmail.com
 * description: 基类协议
 * what & why is modified:
 */

public interface IContract {

    interface IIView<M> extends IView<M> {

    }

    interface IIPresenter extends IPresenter<IIView>{

    }
}
