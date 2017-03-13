package com.cn.smart.carsmart.ui.drive;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseFragment;
import com.cn.smart.carsmart.ui.main.MainComponent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：leo on 2017/3/3 12:38
 * email： leocheung4ever@gmail.com
 * description: 驾驶Fragment
 * what & why is modified:
 */

public class MainDriveFragment extends BaseFragment {

//    @Inject MainDrivePresenter mPresenter;
//    @Inject Activity mActivity;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.fake_status_bar)
    View mFakeStatusBar;

    private String uid;

    @Override
    protected void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    protected void getBundle(Bundle bundle) {
//        uid = bundle.getString("uid");
    }

    @Override
    protected void initUI(View view) {
        ButterKnife.bind(this, view);
//        mPresenter.attachView(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_main_drive;
    }

    public void setTvTitleBackgroundColor(@ColorInt int color) {
        mTvTitle.setBackgroundColor(color);
        mFakeStatusBar.setBackgroundColor(color);
    }

    public static MainDriveFragment newInstance() {
        return new MainDriveFragment();
    }
}
