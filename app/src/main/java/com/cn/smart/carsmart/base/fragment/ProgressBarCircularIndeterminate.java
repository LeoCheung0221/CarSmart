package com.cn.smart.carsmart.base.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cn.smart.carsmart.util.DensityUtils;

/**
 * author：leo on 2016/11/25 17:42
 * email： leocheung4ever@gmail.com
 * description: 圆形缩放渐变的加载控件
 * what & why is modified:
 */

public class ProgressBarCircularIndeterminate extends RelativeLayout {

    private static final String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    final int disableBackgroundColor = Color.parseColor("#E2E2E2");
    int beforeBackground;
    boolean isAnimation = false;
    boolean firstAnimation = false; //是否是第一次动画
    int count = 0;
    float radius1 = 0;
    float radius2 = 0;
    int backgroundColor = Color.parseColor("#1E88E5");

    int arc0 = 0;
    int arcD = 1;
    int limited = 0;
    int rotateAngle = 0;

    public ProgressBarCircularIndeterminate(Context context) {
        this(context, null);
    }

    public ProgressBarCircularIndeterminate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarCircularIndeterminate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackgroundColor(beforeBackground);
        } else {
            setBackgroundColor(disableBackgroundColor);
        }
        invalidate();
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        isAnimation = true;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        isAnimation = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isAnimation) invalidate();
        if (firstAnimation == false) drawFirstAnimation(canvas);
        if (count > 0) drawSecondAnimation(canvas);
        invalidate();
    }

    /**
     * 画view的第二次动画
     */
    private void drawSecondAnimation(Canvas canvas) {
        if (arc0 == limited) arcD += 6;
        if (arcD > 290 || arc0 > limited) {
            arc0 += 6;
            arcD -= 6;
        }
        if (arc0 > limited + 290) {
            limited = arc0;
            arc0 = limited;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arc0, arcD, true, paint);
        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2) - DensityUtils.dip2px(4, getResources()), transparentPaint);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    /**
     * 画view的第一次动画
     */
    private void drawFirstAnimation(Canvas canvas) {
        if (radius1 < getWidth() / 2) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            radius1 = (radius1 >= getWidth() / 2) ? (float) getWidth() / 2 : radius1 + 1;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius1, paint);
        } else {
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            temp.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);
            Paint transparentPaint = new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            if (count >= 50) {
                radius2 = (radius2 >= getWidth() / 2) ? (float) getWidth() / 2 : radius2 + 1;
            } else {
                radius2 = (radius2 >= getWidth() / 2 - DensityUtils.dip2px(4, getResources())) ?
                        (float) getWidth() / 2 - DensityUtils.dip2px(4, getResources()) : radius2 + 1;
            }
            temp.drawCircle(getWidth() / 2, getHeight() / 2, radius2, transparentPaint);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            if (radius2 >= getWidth() / 2 - DensityUtils.dip2px(4, getResources())) count++;
            if (radius2 >= getWidth() / 2) firstAnimation = true;
        }
    }

    /**
     * 画笔按下颜色
     */
    private int makePressColor() {
        int r = (this.backgroundColor >> 16) & 0xFF;
        int g = (this.backgroundColor >> 8) & 0xFF;
        int b = (this.backgroundColor) & 0xFF;
        return Color.argb(128, r, g, b);
    }

    /**
     * 设置View 的xml属性
     */
    private void setAttributes(AttributeSet attrs) {
        setMinimumWidth(DensityUtils.dip2px(32, getResources()));
        setMinimumHeight(DensityUtils.dip2px(32, getResources()));

        //设置背景颜色
        int backgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (backgroundColor != -1) {
            setBackgroundColor(ContextCompat.getColor(getContext(), backgroundColor));
        } else {
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1) {
                setBackgroundColor(backgroundColor);
            } else {
                setBackgroundColor(Color.parseColor("#1E88E5"));
            }
        }
        setMinimumHeight(DensityUtils.dip2px(3, getResources()));
    }

    public void setBackgroundColor(int backgroundColor) {
        super.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        if (isEnabled()) beforeBackground = backgroundColor;
        this.backgroundColor = backgroundColor;
    }
}
