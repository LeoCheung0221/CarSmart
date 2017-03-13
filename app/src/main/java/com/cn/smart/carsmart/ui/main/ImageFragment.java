package com.cn.smart.carsmart.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.smart.baselib.uiframework.banner.CustomPageTransformer;
import com.cn.smart.baselib.uiframework.banner.XBanner;
import com.cn.smart.baselib.uiframework.banner.holder.XBViewHolderCreator;
import com.cn.smart.carsmart.R;
import com.cn.smart.carsmart.components.holder.LocalImageHolderView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFragment extends Fragment {

    @BindView(R.id.xbanner)
    XBanner xbanner;

    private ArrayList<Integer> localImages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        xbanner.startTurning(5000);
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
