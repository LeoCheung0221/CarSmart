package com.cn.smart.baselib.uiframework.sweetalert;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.cn.smart.baselib.R;

/**
 * author：leo on 2016/11/17 12:55
 * email： leocheung4ever@gmail.com
 * description: 加载进度条辅助类
 * what & why is modified:
 */
public class ProgressHelper {

    private int mCircleRadius; //进度条半径大小
    private float mProgressVal; //进度值
    private boolean mIsInstantProgress; //是否即刻进行
    private int mRimColor; //边缘颜色
    private int mRimWidth; //边缘宽度
    private int mBarColor; //进度条颜色
    private int mBarWidth; //进度条宽度
    private boolean mToSpin; //是否旋转
    private float mSpinSpeed; //旋转速度

    private ProgressWheel mProgressWheel;

    public ProgressHelper(Context context) {
        mToSpin = true;
        mSpinSpeed = 0.75f;
        mBarWidth = context.getResources().getDimensionPixelSize(R.dimen.common_circle_width) + 1;
        mBarColor = ContextCompat.getColor(context, R.color.success_stroke_color);
        mRimWidth = 0;
        mRimColor = 0x00000000;
        mIsInstantProgress = false;
        mProgressVal = -1;
        mCircleRadius = context.getResources().getDimensionPixelSize(R.dimen.progress_circle_radius);
    }

    public ProgressWheel getProgressWheel() {
        return mProgressWheel;
    }

    public void setProgressWheel(ProgressWheel mProgressWheel) {
        this.mProgressWheel = mProgressWheel;
        updatePropsIfNeed();
    }

    private void updatePropsIfNeed() {
        if (mProgressWheel != null) {
            if (!mToSpin && mProgressWheel.isSpinning()) {
                mProgressWheel.stopSpinning();
            } else if (mToSpin && !mProgressWheel.isSpinning()) {
                mProgressWheel.spin();
            }
            if (mSpinSpeed != mProgressWheel.getSpinSpeed()) {
                mProgressWheel.setSpinSpeed(mSpinSpeed);
            }
            if (mBarWidth != mProgressWheel.getBarWidth()) {
                mProgressWheel.setBarWidth(mBarWidth);
            }
            if (mBarColor != mProgressWheel.getBarColor()) {
                mProgressWheel.setBarColor(mBarColor);
            }
            if (mRimWidth != mProgressWheel.getRimWidth()) {
                mProgressWheel.setRimWidth(mRimWidth);
            }
            if (mRimColor != mProgressWheel.getRimColor()) {
                mProgressWheel.setRimColor(mRimColor);
            }
            if (mProgressVal != mProgressWheel.getProgress()) {
                if (mIsInstantProgress) {
                    mProgressWheel.setInstantProgress(mProgressVal);
                } else {
                    mProgressWheel.setProgress(mProgressVal);
                }
            }
            if (mCircleRadius != mProgressWheel.getCircleRadius()) {
                mProgressWheel.setCircleRadius(mCircleRadius);
            }
        }
    }

    public void resetCount() {
        if (mProgressWheel != null) {
            mProgressWheel.resetCount();
        }
    }

    public boolean isSpinning() {
        return mToSpin;
    }

    public void spin() {
        mToSpin = true;
        updatePropsIfNeed();
    }

    public void stopSpinning() {
        mToSpin = false;
        updatePropsIfNeed();
    }

    public float getmProgress() {
        return mProgressVal;
    }

    public void setmProgress(float progress) {
        this.mProgressVal = progress;
        mIsInstantProgress = true;
        updatePropsIfNeed();
    }

    public void setInstantProgress(float progress) {
        mProgressVal = progress;
        mIsInstantProgress = true;
        updatePropsIfNeed();
    }

    public int getCircleRadius() {
        return mCircleRadius;
    }

    /**
     * @param circleRadius units using pixel
     **/
    public void setCircleRadius(int circleRadius) {
        mCircleRadius = circleRadius;
        updatePropsIfNeed();
    }

    public int getBarWidth() {
        return mBarWidth;
    }

    public void setBarWidth(int barWidth) {
        mBarWidth = barWidth;
        updatePropsIfNeed();
    }

    public int getBarColor() {
        return mBarColor;
    }

    public void setBarColor(int barColor) {
        mBarColor = barColor;
        updatePropsIfNeed();
    }

    public int getRimWidth() {
        return mRimWidth;
    }

    public void setRimWidth(int rimWidth) {
        mRimWidth = rimWidth;
        updatePropsIfNeed();
    }

    public int getRimColor() {
        return mRimColor;
    }

    public void setRimColor(int rimColor) {
        mRimColor = rimColor;
        updatePropsIfNeed();
    }

    public float getSpinSpeed() {
        return mSpinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        mSpinSpeed = spinSpeed;
        updatePropsIfNeed();
    }
}
