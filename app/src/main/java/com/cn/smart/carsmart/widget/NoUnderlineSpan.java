package com.cn.smart.carsmart.widget;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * author：leo on 2017/3/8 11:44
 * email： leocheung4ever@gmail.com
 * description: 自定义无下划线的Spannable对象
 * what & why is modified:
 */

public abstract class NoUnderlineSpan extends ClickableSpan{

    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
