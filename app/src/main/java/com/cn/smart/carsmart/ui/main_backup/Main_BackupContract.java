package com.cn.smart.carsmart.ui.main_backup;

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

public interface Main_BackupContract {

    interface Main_BackupView extends IView {

        void setTitle(CharSequence title);

        void reload();

    }

    abstract class Presenter extends BasePresenter<Main_BackupView> {

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
