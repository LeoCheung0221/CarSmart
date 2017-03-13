package com.cn.smart.baselib.uiframework.banner.listener;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * author：leo on 2017/3/1 14:36
 * email： leocheung4ever@gmail.com
 * description: 页面变化事件监听器 - 翻页指示器适配器
 * what & why is modified:
 */

public class XBPageChangeListener implements ViewPager.OnPageChangeListener {

    private ArrayList<ImageView> pointViews;
    private int[] page_indicatorId;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public XBPageChangeListener(ArrayList<ImageView> pointViews, int page_indicatorId[]) {
        this.pointViews = pointViews;
        this.page_indicatorId = page_indicatorId;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null)
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(position).setImageResource(page_indicatorId[1]);
            if (position != i) {
                pointViews.get(i).setImageResource(page_indicatorId[0]);
            }
        }
        if (onPageChangeListener != null)
            onPageChangeListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null)
            onPageChangeListener.onPageScrollStateChanged(state);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
