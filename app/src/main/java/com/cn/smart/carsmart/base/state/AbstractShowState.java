package com.cn.smart.carsmart.base.state;

import android.view.View;
import android.view.animation.Animation;

/**
 * author：leo on 2016/11/25 16:19
 * email： leocheung4ever@gmail.com
 * description: 显示状态的抽象
 * what & why is modified:
 */

public abstract class AbstractShowState implements ShowState {

    protected View mFragmentView;
    protected Animation mAnimationIn;
    protected Animation mAnimationOut;

    /**
     * 显示相应ID的View
     *
     * @param viewId  view id
     * @param animate 显示动画
     */
    protected void showViewById(int viewId, boolean animate) {
        View content = mFragmentView.findViewById(viewId);
        if (animate) {
            mAnimationIn.reset();
            content.startAnimation(mAnimationIn);
        } else {
            content.clearAnimation();
        }
        content.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏相应ID的View
     *
     * @param viewId  view id
     * @param animate 隐藏动画
     */
    protected void dismissViewById(int viewId, boolean animate) {
        View content = mFragmentView.findViewById(viewId);
        if (animate) {
            mAnimationOut.reset();
            content.startAnimation(mAnimationOut);
        } else {
            content.clearAnimation();
        }
        content.setVisibility(View.GONE);
    }

    @Override
    public void setFragmentView(View fragmentView) {
        mFragmentView = fragmentView;
    }

    @Override
    public void setAnimIn(Animation in) {
        mAnimationIn = in;
    }

    @Override
    public void setAnimOut(Animation out) {
        mAnimationOut = out;
    }
}
