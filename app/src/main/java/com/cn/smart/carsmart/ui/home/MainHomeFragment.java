package com.cn.smart.carsmart.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cn.smart.baselib.uiframework.banner.CustomPageTransformer;
import com.cn.smart.baselib.uiframework.banner.XBanner;
import com.cn.smart.baselib.uiframework.banner.holder.XBViewHolderCreator;
import com.cn.smart.baselib.uiframework.banner.listener.OnItemClickListener;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseFragment;
import com.cn.smart.carsmart.components.holder.LocalImageHolderView;
import com.cn.smart.carsmart.injector.HasComponent;
import com.cn.smart.carsmart.ui.main.MainComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：leo on 2017/3/3 11:24
 * email： leocheung4ever@gmail.com
 * description: 首页Fragment
 * what & why is modified:
 */

public class MainHomeFragment extends BaseFragment implements OnItemClickListener {

    @BindView(R.id.xbanner) XBanner xbanner;

    private ArrayList<Integer> localImages = new ArrayList<>();

    @Override
    protected void initInjector() {
        getComponent(MainHomeComponent.class).inject(this);
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    protected void initUI(View view) {
        ButterKnife.bind(this, view);
        initBanner();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int initContentView() {
        return R.layout.fragment_main_home;
    }

    @Override
    public void onResume() {
        super.onResume();
        xbanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        xbanner.stopTurning();
    }

    // 初始化Banner
    private void initBanner() {
        //todo for testing
        localImages.clear();
        for (int position = 0; position < 7; position++) {
            localImages.add(getResId("ic_test_" + position, R.drawable.class));
        }

        xbanner.setPages(new XBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, localImages)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this)
                .getViewPager()
                .setPageTransformer(true, CustomPageTransformer.setPageTransFormer(getResources().getInteger(R.integer.transformer_default), xbanner));
    }

    private Integer getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
    }
}
