package com.cn.smart.carsmart.ui.main;

import android.view.MenuItem;

import com.cn.smart.carsmart.base.mvp.BasePresenter;
import com.cn.smart.carsmart.base.mvp.IView;
import com.cn.smart.carsmart.model.local.User;

import java.util.List;

/**
 * author：leo on 2017/2/8 15:28
 * email： leocheung4ever@gmail.com
 * description: main模块 协议接口 实现各种业务逻辑接口
 * what & why is modified:
 */

public interface MainContract {

    interface MainView extends IView {

        void setTitle(CharSequence title);

    }

    abstract class Presenter extends BasePresenter<MainView> {

        abstract void onNightModelClick();

        abstract void onNotificationClick();

        abstract void onCoverClick();

        abstract void onNavigationClick(MenuItem item);

        abstract void showAccountMenu();

        abstract void onAccountItemClick(int position, List<User> users, final String[] items);

        abstract void exist();

        abstract boolean isLogin();
    }

}
