package com.cn.smart.baselib.uiframework.bootstrap.api.defaults;

import android.content.Context;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.uiframework.bootstrap.api.attributes.BootstrapBrand;

import static com.cn.smart.baselib.util.ColorUtils.ACTIVE_OPACITY_FACTOR_EDGE;
import static com.cn.smart.baselib.util.ColorUtils.ACTIVE_OPACITY_FACTOR_FILL;
import static com.cn.smart.baselib.util.ColorUtils.DISABLED_ALPHA_EDGE;
import static com.cn.smart.baselib.util.ColorUtils.DISABLED_ALPHA_FILL;
import static com.cn.smart.baselib.util.ColorUtils.decreaseRgbChannels;
import static com.cn.smart.baselib.util.ColorUtils.increaseOpacity;
import static com.cn.smart.baselib.util.ColorUtils.resolveColor;

/**
 * author：leo on 2016/12/6 17:33
 * email： leocheung4ever@gmail.com
 * description: Bootstrap提供6 种默认的Brand - 基本(Primary), 成功(Success), 信息(Info), 警告(Warning), 危险(Danger), 以及默认(Default)
 * Brands常被用来提供指定view的颜色, 它不是全局使用的
 * what & why is modified:
 */

public enum DefaultBootstrapBrand implements BootstrapBrand {

    PRIMARY(R.color.bootstrap_brand_primary),
    SUCCESS(R.color.bootstrap_brand_success),
    INFO(R.color.bootstrap_brand_info),
    WARNING(R.color.bootstrap_brand_warning),
    DANGER(R.color.bootstrap_brand_danger),
    SECONDARY(R.color.bootstrap_brand_secondary_fill, R.color.bootstrap_brand_secondary_text),
    REGULAR(R.color.bootstrap_gray_light);

    private final int color;
    private final int textColor;

    DefaultBootstrapBrand(int color) {
        this.color = color;
        this.textColor = android.R.color.white;
    }

    DefaultBootstrapBrand(int color, int textColor) {
        this.color = color;
        this.textColor = textColor;
    }

    public static DefaultBootstrapBrand fromAttributeValue(int attrValue) {
        switch (attrValue) {
            case 0:
                return PRIMARY;
            case 1:
                return SUCCESS;
            case 2:
                return INFO;
            case 3:
                return WARNING;
            case 4:
                return DANGER;
            case 5:
                return REGULAR;
            case 6:
                return SECONDARY;
            default:
                return REGULAR;
        }
    }

    @Override
    public int defaultFill(Context context) {
        return resolveColor(color, context);
    }

    @Override
    public int defaultEdge(Context context) {
        return decreaseRgbChannels(context, color, ACTIVE_OPACITY_FACTOR_EDGE);
    }

    @Override
    public int defaultTextColor(Context context) {
        return resolveColor(textColor, context);
    }

    @Override
    public int activeFill(Context context) {
        return decreaseRgbChannels(context, color, ACTIVE_OPACITY_FACTOR_FILL);
    }

    @Override
    public int activeEdge(Context context) {
        return decreaseRgbChannels(context, color, ACTIVE_OPACITY_FACTOR_FILL + ACTIVE_OPACITY_FACTOR_EDGE);
    }

    @Override
    public int activeTextColor(Context context) {
        return resolveColor(textColor, context);
    }

    @Override
    public int disabledFill(Context context) {
        return increaseOpacity(context, color, DISABLED_ALPHA_FILL);
    }

    @Override
    public int disabledEdge(Context context) {
        return increaseOpacity(context, color, DISABLED_ALPHA_FILL - DISABLED_ALPHA_EDGE);
    }

    @Override
    public int disabledTextColor(Context context) {
        return resolveColor(textColor, context);
    }

    @Override
    public int getColor() {
        return color;
    }
}
