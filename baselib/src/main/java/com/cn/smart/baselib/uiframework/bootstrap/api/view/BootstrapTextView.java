package com.cn.smart.baselib.uiframework.bootstrap.api.view;

import android.support.annotation.Nullable;

import com.cn.smart.baselib.uiframework.bootstrap.BootstrapText;

/**
 * author：leo on 2016/12/6 14:46
 * email： leocheung4ever@gmail.com
 * description: 实现这个接口的Views 可以使用BootstrapText设置它们的文本
 * what & why is modified:
 */

public interface BootstrapTextView {

    String KEY = "com.cn.smart.baselib.uiframework.bootstrap.api.view.BootstrapTextView";

    /**
     * 设置视图展示给定的BootstrapText
     *
     * @param bootstrapText BootstrapText(文本)
     */
    void setBootstrapText(@Nullable BootstrapText bootstrapText);

    /**
     * @return 目前BootstrapText文本, 或者如果不存在就返回null
     */
    @Nullable
    BootstrapText getBootstrapText();

    /**
     * 通过构造一个BootstrapText 设置view展示给定的markdown文本
     * e.g. "This is a {fa-stop} button"
     *
     * @param text markdown 文本
     */
    void setMarkdownText(@Nullable String text);

}
