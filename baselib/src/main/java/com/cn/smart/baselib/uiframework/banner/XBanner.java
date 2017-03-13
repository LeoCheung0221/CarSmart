package com.cn.smart.baselib.uiframework.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.uiframework.banner.adapter.XBPageAdapter;
import com.cn.smart.baselib.uiframework.banner.holder.XBViewHolderCreator;
import com.cn.smart.baselib.uiframework.banner.listener.OnItemClickListener;
import com.cn.smart.baselib.uiframework.banner.listener.XBPageChangeListener;
import com.cn.smart.baselib.uiframework.banner.view.XBLoopViewPager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * author：leo on 2017/3/1 11:19
 * email： leocheung4ever@gmail.com
 * description: 页面翻转控件 方便的导航栏
 * 支持无限循环  自动翻页  翻页特效
 * what & why is modified:
 */

public class XBanner<T> extends LinearLayout {

    private XBLoopViewPager viewPager;
    private ViewGroup loPageTurningPoint;
    private ViewPagerScroller scroller;
    private XBPageChangeListener pageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private XBPageAdapter pageAdapter;

    private AdSwitchTask adSwitchTask;

    private ArrayList<ImageView> mPointViews = new ArrayList<>();
    private List<T> mDatas;
    private int[] page_indicatorId;
    private boolean canLoop = true;
    private boolean canTurn = false;
    private boolean manualPageable = true;
    private boolean turning;
    private long autoTurningTime;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }

    public XBanner(Context context) {
        super(context);
        init(context);
    }

    public XBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XBanner);
        canLoop = a.getBoolean(R.styleable.XBanner_canLoop, true);
        a.recycle();
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public XBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XBanner);
        canLoop = a.getBoolean(R.styleable.XBanner_canLoop, true);
        a.recycle();
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XBanner);
        canLoop = a.getBoolean(R.styleable.XBanner_canLoop, true);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        View xView = LayoutInflater.from(context).inflate(R.layout.include_viewpager, this, true);
        viewPager = (XBLoopViewPager) xView.findViewById(R.id.xbLoopViewPager);
        loPageTurningPoint = (ViewGroup) xView.findViewById(R.id.loPageTurningPoint);
        initViewPagerScroll();

        adSwitchTask = new AdSwitchTask(this);
    }

    /**
     * 初始化ViewPager  设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<XBanner> refBanner;

        AdSwitchTask(XBanner xBanner) {
            this.refBanner = new WeakReference<>(xBanner);
        }

        @Override
        public void run() {
            XBanner xBanner = refBanner.get();

            if (xBanner != null) {
                if (xBanner.viewPager != null && xBanner.turning) {
                    int page = xBanner.viewPager.getCurrentItem() + 1;
                    xBanner.viewPager.setCurrentItem(page);
                    xBanner.postDelayed(xBanner.adSwitchTask, xBanner.autoTurningTime);
                }
            }
        }
    }

    /**
     * 设置页面
     */
    public XBanner setPages(XBViewHolderCreator holderCreator, List<T> datas) {
        this.mDatas = datas;
        pageAdapter = new XBPageAdapter(holderCreator, mDatas);
        viewPager.setAdapter(pageAdapter, canLoop);

        if (page_indicatorId != null) {
            setPageIndicator(page_indicatorId);
        }
        return this;
    }

    public XBanner setPageIndicator(int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (mDatas == null) return this;
        for (int count = 0; count < mDatas.size(); count++) {
            //翻页指示点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (mPointViews.isEmpty()) {
                pointView.setImageResource(page_indicatorId[1]);
            } else {
                pointView.setImageResource(page_indicatorId[0]);
            }
            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView);
        }
        pageChangeListener = new XBPageChangeListener(mPointViews, page_indicatorId);
        viewPager.setOnPageChangeListener(pageChangeListener);
        if (onPageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);

        return this;
    }

    /**
     * 设置指示器所居位置
     */
    public XBanner setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);

        return this;
    }

    /**
     * 设置自定义翻页的动画效果
     */
    public XBanner setPageTransformer(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }

    /**
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间
     */
    public XBanner startTurning(long autoTurningTime) {
        //如果正在翻页 先暂停
        if (turning)
            stopTurning();
        //设置可以翻页并且开始翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);

        return this;
    }

    /**
     * 设置翻页监听器
     */
    public XBanner setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //如果有默认的监听器(即使用了默认的翻页指示器) 则把用户设置的依附到默认的指示器上面  否则就直接设置
        if (pageChangeListener != null)
            pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        else
            viewPager.setOnPageChangeListener(onPageChangeListener);

        return this;
    }

    /**
     * 监听Item点击事件
     */
    public XBanner setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            viewPager.setOnItemClickListener(null);

            return this;
        }
        viewPager.setOnItemClickListener(onItemClickListener);

        return this;
    }

    /**
     * 触碰控件之时  翻页应该停止 离开俄时候如果之前是开启了翻页  则重新启动翻页
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            //开始翻页
            if (canTurn)
                startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            //停止翻页
            if (canTurn)
                stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    /**
     * 设置是否翻页
     */
    public void setTurning(boolean turning) {
        this.turning = turning;
    }

    /**
     * 是否开启了翻页
     */
    public boolean isTurning() {
        return turning;
    }


    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        viewPager.setCanLoop(canLoop);
    }

    public boolean isManualPageable() {
        return viewPager.isCanScroll();
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //获取当前的页面index
    public int getCurrentItem() {
        if (viewPager != null) {
            return viewPager.getRealItem();
        }
        return -1;
    }

    //设置当前的页面index
    public void setcurrentitem(int index) {
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        }
    }

    //设置ViewPager的滚动速度
    public void setScrollDuration(int scrollDuration) {
        scroller.setScrollDuration(scrollDuration);
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }

    public XBLoopViewPager getViewPager() {
        return viewPager;
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }
}
