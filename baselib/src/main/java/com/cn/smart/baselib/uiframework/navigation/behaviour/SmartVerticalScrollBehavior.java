package com.cn.smart.baselib.uiframework.navigation.behaviour;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Interpolator;

import com.cn.smart.baselib.uiframework.navigation.SmartNavigationBar;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author：leo on 2017/3/3 14:43
 * email： leocheung4ever@gmail.com
 * description: 垂直滚动行为 -- 下滑出现 上滑滚出
 * what & why is modified:
 */

public class SmartVerticalScrollBehavior<V extends View> extends VerticalScrollingBehavior<V> {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private int mBottomNavHeight;
    private WeakReference<SmartNavigationBar> mViewRef;

    ///////////////////////////////////////////////////////////////////////////
    // onBottomBar changes
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, final V child, int layoutDirection) {
        //First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection);
        if (child instanceof SmartNavigationBar) { //如果子控件是导航栏
            mViewRef = new WeakReference<>((SmartNavigationBar) child);
        }

        child.post(new Runnable() {
            @Override
            public void run() {
                mBottomNavHeight = child.getHeight();
            }
        });
        updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child));
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SnackBar Handling
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        return isDependent(dependency) || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        if (isDependent(dependency)) {
            updateSnackBarPosition(parent, child, dependency);
            return false;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Auto Hide Handling
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onNestedVerticalScrollUnconsumed(CoordinatorLayout coordinatorLayout, V child, @ScrollDirection int scrollDirection, int currentOverScroll, int totalScroll) {
        //Empty body
    }

    @Override
    public void onNestedVerticalScrollConsumed(CoordinatorLayout coordinatorLayout, V child, @ScrollDirection int scrollDirection, int currentOverScroll, int totalConsumedScroll) {
        handleDirection(coordinatorLayout, child, scrollDirection);
    }

    @Override
    public void onNestedVerticalPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {
//        handleDirection(child, scrollDirection);
    }

    @Override
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY, boolean consumed, @ScrollDirection int scrollDirection) {
//        if (consumed){
//            handleDirection(child, scrollDirection);
//        }
        return consumed;
    }


    ///////////////////////////////////////////////////////////////////////////
    // ＰＲＩＶＡＴＥ
    ///////////////////////////////////////////////////////////////////////////
    // 更新SnackBar位置
    private void updateSnackBarPosition(CoordinatorLayout parent, V child, View dependency) {
        updateSnackBarPosition(parent, child, dependency, ViewCompat.getTranslationY(child) - child.getHeight());
    }

    private void updateSnackBarPosition(CoordinatorLayout parent, V child, View dependency, float translationY) {
        if (dependency != null && dependency instanceof Snackbar.SnackbarLayout) {
            ViewCompat.animate(dependency).setInterpolator(INTERPOLATOR).setDuration(80).setStartDelay(0).translationY(translationY).start();
        }
    }

    // 获得当前SnackBar实例对象
    private View getSnackBarInstance(CoordinatorLayout parent, V child) {
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout) {
                return view;
            }
        }
        return null;
    }

    //是否是依赖View
    private boolean isDependent(View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    //处理上下滑动隐藏
    private void handleDirection(CoordinatorLayout parent, V child, int scrollDirection) {
        SmartNavigationBar smartNavigationBar = mViewRef.get();
        if (smartNavigationBar != null && smartNavigationBar.isAutoHideEnabled()) {
            if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && smartNavigationBar.isHidden()) {
                updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child), -mBottomNavHeight);
                smartNavigationBar.show();
            } else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !smartNavigationBar.isHidden()) {
                updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child), 0);
                smartNavigationBar.hide();
            }
        }
    }
}
