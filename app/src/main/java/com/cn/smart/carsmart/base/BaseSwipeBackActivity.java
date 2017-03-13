package com.cn.smart.carsmart.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Dimension;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.cn.smart.baselib.uiframework.base.manager.AppManager;
import com.cn.smart.baselib.uiframework.bootstrap.AwesomeTextView;
import com.cn.smart.baselib.uiframework.swipeback.ISwipeBack;
import com.cn.smart.baselib.uiframework.swipeback.SwipeBackHelper;
import com.cn.smart.baselib.uiframework.swipeback.SwipeBackLayout;
import com.cn.smart.baselib.uiframework.swipeback.SwipeUtils;
import com.cn.smart.baselib.util.DimenUtils;
import com.cn.smart.baselib.util.SettingPrefUtil;
import com.cn.smart.carsmart.R;

import butterknife.BindView;

/**
 * author：leo on 2016/11/23 20:42
 * email： leocheung4ever@gmail.com
 * description: 左滑返回基类
 * what & why is modified:
 */

public abstract class BaseSwipeBackActivity extends BaseActivity implements ISwipeBack {

    @BindView(R.id.ivNavBack)
    ImageView ivNavBack;
    @BindView(R.id.atvToolBarMainTitle)
    AwesomeTextView atvToolBarMainTitle;

    private static final int VIBRATE_DURATION = 20;

    private SwipeBackHelper mHelper;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackHelper(this);
        mHelper.onActivityCreate();

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                if (isVibrate())
                    vibrate(VIBRATE_DURATION);
            }

            @Override
            public void onScrollOverThreshold() {
                if (isVibrate())
                    vibrate(VIBRATE_DURATION);
            }
        });

        setToolBar(isShowNavBack(), isShowHeaderTitle());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(@IdRes int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        SwipeUtils.convertActivityFromTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int mode = SettingPrefUtil.getSwipeBackEdgeMode(this);
        SwipeBackLayout mSwipeBackLayout = mHelper.getSwipeBackLayout();
        switch (mode) {
            case 0:
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
                break;
            case 1:
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
                break;
            case 2:
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTTOM);
                break;
            case 3:
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 是否左滑退出震动提示
     * 默认提示
     */
    protected boolean isVibrate(){
        return true;
    }

    /**
     * 是否显示返回按钮
     * 默认显示
     */
    protected boolean isShowNavBack() {
        return true;
    }

    /**
     * 设置toolbar显示标题
     * 默认不显示标题
     */
    protected String isShowHeaderTitle() {
        return "";
    }

    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

    private void setToolBar(boolean isShow, String headerTitle) {
        if (!isShow) {
            ivNavBack.setVisibility(View.INVISIBLE);
            ivNavBack.setClickable(false);
            ivNavBack.setEnabled(false);
            ivNavBack.setOnClickListener(null);

            if (TextUtils.isEmpty(headerTitle)) {
                atvToolBarMainTitle.setVisibility(View.INVISIBLE);
            } else {
                atvToolBarMainTitle.setVisibility(View.VISIBLE);
                atvToolBarMainTitle.setText(headerTitle);
                atvToolBarMainTitle.setTextSize(Dimension.SP, 17);
                atvToolBarMainTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        } else {
            ivNavBack.setVisibility(View.VISIBLE);
            ivNavBack.setClickable(true);
            ivNavBack.setEnabled(true);
            ivNavBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            if (TextUtils.isEmpty(headerTitle)) {
                atvToolBarMainTitle.setVisibility(View.INVISIBLE);
            } else {
                atvToolBarMainTitle.setVisibility(View.VISIBLE);
                atvToolBarMainTitle.setText(headerTitle);
                atvToolBarMainTitle.setTextSize(Dimension.SP, 17);
                atvToolBarMainTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }
    }
}
