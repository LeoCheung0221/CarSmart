package com.cn.smart.baselib.uiframework.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * author：leo on 2017/3/1 14:16
 * email： leocheung4ever@gmail.com
 * description: ViewPager Scroller
 * 定义滑动速度参数等等
 * what & why is modified:
 */

public class ViewPagerScroller extends Scroller {

    private int mScrollDuration = 800; // 滑动速度 值越大滑动越慢  滑动过快会使3D效果不明显
    private boolean zero;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, zero ? 0 : mScrollDuration);
    }

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setScrollDuration(int mScrollDuration) {
        this.mScrollDuration = mScrollDuration;
    }

    public boolean isZero() {
        return zero;
    }

    public void setZero(boolean zero) {
        this.zero = zero;
    }
}
