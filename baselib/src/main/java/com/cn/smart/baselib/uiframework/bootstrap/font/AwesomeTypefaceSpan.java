package com.cn.smart.baselib.uiframework.bootstrap.font;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import com.cn.smart.baselib.uiframework.bootstrap.TypefaceProvider;

/**
 * author：leo on 2016/12/6 16:34
 * email： leocheung4ever@gmail.com
 * description: 一个自定义绘制文本风格的类,通过使用指定字体的图标集传递给构造函数
 * what & why is modified:
 */

@SuppressLint("ParcelCreator")
public class AwesomeTypefaceSpan extends TypefaceSpan {

    private final Context context;
    private final IconSet iconSet;

    public AwesomeTypefaceSpan(Context context, IconSet iconSet) {
        super(iconSet.fontPath().toString());
        this.context = context.getApplicationContext();
        this.iconSet = iconSet;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setTypeface(TypefaceProvider.getTypeface(context, iconSet));
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        paint.setTypeface(TypefaceProvider.getTypeface(context, iconSet));
    }
}
