package com.cn.smart.carsmart.ui.main_backup;

import android.os.Bundle;
import android.view.Window;

import com.cn.smart.baselib.uiframework.banner.CustomPageTransformer;
import com.cn.smart.baselib.uiframework.banner.XBanner;
import com.cn.smart.baselib.uiframework.banner.holder.XBViewHolderCreator;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.base.BaseTranslucentActivity;
import com.cn.smart.carsmart.components.holder.LocalImageHolderView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Main_BackupActivity extends BaseTranslucentActivity {

    private ArrayList<Integer> localImages = new ArrayList<>();

    @BindView(R.id.xbanner)
    XBanner xbanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_backup);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        xbanner.startTurning(3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        xbanner.stopTurning();
    }

    private void init() {
        initBanner();
    }

    // 初始化Banner
    private void initBanner() {
        //todo for testing
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
}
