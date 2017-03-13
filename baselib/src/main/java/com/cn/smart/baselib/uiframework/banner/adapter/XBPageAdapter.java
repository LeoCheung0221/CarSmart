package com.cn.smart.baselib.uiframework.banner.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.uiframework.banner.holder.BaseHolder;
import com.cn.smart.baselib.uiframework.banner.holder.XBViewHolderCreator;
import com.cn.smart.baselib.uiframework.banner.view.XBLoopViewPager;

import java.util.List;

/**
 * author：leo on 2017/3/1 11:31
 * email： leocheung4ever@gmail.com
 * description: ViewPager适配器
 * what & why is modified:
 */

public class XBPageAdapter<T> extends PagerAdapter {

    protected List<T> mDatas;
    protected XBViewHolderCreator holderCreator;
    private boolean canLoop = true;
    private XBLoopViewPager viewPager;
    private final int MULTIPLE_COUNT = 300;

    public XBPageAdapter(XBViewHolderCreator holderCreator, List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    /**
     * 返回当前有效视图的个数
     */
    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    /**
     * 该函数用来判断instantiateItem(ViewGroup, int)函数所返回的Key与一个页面视图是否视图是否代表代表的同一个视图(即他俩是否是对应的,对应的表示同一个View)
     *
     * @param view   Page View to check for association with object
     * @param object Object to check for association with view
     * @return true if view is associated with the key object
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 创建指定位置的页面视图  适配器有责任的增加即将创建的View视图到这里给定的container中  这是为了确保在finishUpdate(viewGroup)返回时this is be done!
     *
     * @param container the containing View in which the page will be shown
     * @param position  the page position to be instantiated
     * @return an Object representing the new page. this does not need to be a View, but can be some other container of the page
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = toRealPosition(position);

        View view = getView(realPosition, null, container);
        container.addView(view);
        return view;
    }

    /**
     * 移除一个给定位置的页面  适配器有责任从容器中删除这个视图  为了确保在finishUpdate(ViewGroup)返回时视图能够被移除
     *
     * @param container the containing View from which the page will be removed
     * @param position  the page position to be removed
     * @param object    the same object that was returned by instantiateItem(View, int).
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFirstItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0) {
            return 0;
        }
        return position % realCount;
    }

    public View getView(int position, View view, ViewGroup container) {
        BaseHolder holder;
        if (view == null) {
            holder = (BaseHolder) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(R.id.xb_item_tag, holder);
        } else {
            holder = (BaseHolder<T>) view.getTag(R.id.xb_item_tag);
        }
        if (mDatas != null && !mDatas.isEmpty()) {
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        }
        return view;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(XBLoopViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
