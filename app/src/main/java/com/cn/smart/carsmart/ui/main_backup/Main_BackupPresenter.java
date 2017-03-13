package com.cn.smart.carsmart.ui.main_backup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.cn.smart.baselib.util.SettingPrefUtil;
import com.cn.smart.carsmart.base.mvp.IView;
import com.cn.smart.carsmart.injector.PerActivity;
import com.cn.smart.carsmart.model.local.User;

import java.util.List;

import javax.inject.Inject;


/**
 * author：leo on 2016/11/23 11:15
 * email： leocheung4ever@gmail.com
 * description: for testing
 * what & why is modified:
 */
@PerActivity
public class Main_BackupPresenter extends Main_BackupContract.Presenter {

    private Main_BackupContract.Main_BackupView mMainView;
    private Context mContext;

    @Inject
    public Main_BackupPresenter(Context mContext) {
        this.mContext = mContext;
    }

//    @Override
//    public void attachView(@NonNull MainContract.MainView view) {
//        mMainView = view;
//    }

    @Override
    public void attachView(@NonNull IView iView) {
        super.attachView(iView);
    }

    @Override
    public void detachView() {
        mMainView = null;
    }

    @Override
    void onNightModelClick() {
        SettingPrefUtil.setNightModel(mContext, !SettingPrefUtil.getNightModel(mContext));
//        mMainView.reload();
    }

    @Override
    void onNotificationClick() {

    }

    @Override
    void onCoverClick() {

    }

    @Override
    void onNavigationClick(MenuItem item) {

    }

    @Override
    void showAccountMenu() {

    }

    @Override
    void onAccountItemClick(int position, List<User> users, String[] items) {

    }

    @Override
    void exist() {

    }

    @Override
    boolean isLogin() {
        return false;
    }
}
