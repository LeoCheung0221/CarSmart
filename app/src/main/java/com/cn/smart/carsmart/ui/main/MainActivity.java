package com.cn.smart.carsmart.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cn.smart.baselib.uiframework.navigation.SmartNavigationBar;
import com.cn.smart.baselib.uiframework.navigation.SmartNavigationItem;
import com.cn.smart.baselib.util.StatusBarUtil;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseActivity;
import com.cn.smart.carsmart.injector.HasComponent;
import com.cn.smart.carsmart.ui.drive.MainDriveFragment;
import com.cn.smart.carsmart.ui.drive.MainDriveModule;
import com.cn.smart.carsmart.ui.home.MainHomeFragment;
import com.cn.smart.carsmart.ui.home.MainHomeModule;
import com.cn.smart.carsmart.ui.pcenter.MainPersonFragment;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.MainView, HasComponent<MainComponent>{

    @Inject MainPresenter mPresenter;

    @BindView(R.id.vp_main) ViewPager vp_main;
    @BindView(R.id.bottom_navigation_bar) SmartNavigationBar bottom_navigation_bar;

    private MainComponent mMainComponent;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected int initContentView() {
        return R.layout.content_main;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    protected void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule(this))
                .mainHomeModule(new MainHomeModule())
                .mainDriveModule(new MainDriveModule())
                .build();
        mMainComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        init();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, null);
    }

    private void init() {
        initNavigation();
    }

    //初始化底部导航栏 bottom_navigation_bar
    private void initNavigation() {
        bottom_navigation_bar
                .addItem(new SmartNavigationItem(R.drawable.ic_nav_gavel, "测试"))
                .addItem(new SmartNavigationItem(R.drawable.ic_nav_home, "首页"))
                .addItem(new SmartNavigationItem(R.drawable.ic_nav_money, "驾驶"))
                .addItem(new SmartNavigationItem(R.drawable.ic_nav_person, "我的"))
                .initialise();
        bottom_navigation_bar.setTabSelectedListener(new SmartNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                vp_main.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

        mFragmentList.add(new ImageFragment());
        mFragmentList.add(new MainHomeFragment());
        mFragmentList.add(new MainDriveFragment());
        mFragmentList.add(new MainPersonFragment());

        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottom_navigation_bar.selectTab(position);
                switch (position){
                    case 0:
                        break;
                    default:
                        Random random = new Random();
                        int color = 0xff000000 | random.nextInt(0xffffff);
                        if (mFragmentList.get(position) instanceof MainDriveFragment) {
                            ((MainDriveFragment) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vp_main.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

}
