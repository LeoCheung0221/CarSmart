package com.cn.smart.baselib.uiframework.bootstrap.api.attributes;

import android.content.Context;
import android.support.annotation.ColorInt;

import java.io.Serializable;

/**
 * author：leo on 2016/12/6 16:54
 * email： leocheung4ever@gmail.com
 * description: 一个Bootstrap Brand 就是一个颜色, 这个Brand 被普遍使用在许多Bootstrap 视图上.
 * 一个例子就是 "Info" Brand , 给views 涂上的就是淡蓝色
 * what & why is modified:
 */

public interface BootstrapBrand extends Serializable {

    String KEY = "com.cn.smart.baselib.uiframework.bootstrap.api.attributes.BootstrapBrand";

    /**
     * 检索到的颜色应该被用于默认填充状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int defaultFill(Context context);

    /**
     * 检索到的颜色应该被用于默认边界状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int defaultEdge(Context context);

    /**
     * 检索到的文本颜色应该被用于默认状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int defaultTextColor(Context context);

    /**
     * 检索到的颜色应该被用于激活填充状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int activeFill(Context context);

    /**
     * 检索到的颜色应该被用于激活边界状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int activeEdge(Context context);

    /**
     * 检索到的文本颜色应该被用于激活状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int activeTextColor(Context context);

    /**
     * 检索到的颜色应该被用于禁用填充状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int disabledFill(Context context);

    /**
     * 检索到的颜色应该被用于禁用边界状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int disabledEdge(Context context);

    /**
     * 检索到的颜色应该被用于禁用状态
     *
     * @return 当前brand 的颜色
     */
    @ColorInt
    int disabledTextColor(Context context);

    @ColorInt
    int getColor();
}
