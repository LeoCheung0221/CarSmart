package com.cn.smart.baselib.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

/**
 * author：leo on 2016/12/6 17:52
 * email： leocheung4ever@gmail.com
 * description: 操作Bootstrap 颜色的工具类, 以及解决资源值的颜色
 * what & why is modified:
 */

public class ColorUtils {

    public static final int DISABLED_ALPHA_FILL = 165;
    public static final int DISABLED_ALPHA_EDGE = 190;
    public static final float ACTIVE_OPACITY_FACTOR_FILL = 0.125f;
    public static final float ACTIVE_OPACITY_FACTOR_EDGE = 0.025f;

    /**
     * 解决一个颜色资源.
     *
     * @param color   the color resource
     * @param context the current context
     * @return a color int
     */
    @SuppressWarnings("deprecation")
    public static
    @ColorInt
    int resolveColor(@ColorRes int color, Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(color, context.getTheme());
        } else {
            return context.getResources().getColor(color);
        }
    }

    /**
     * 通过减少RGB频道值加深一个颜色
     *
     * @param context the current context
     * @param res     the color resource
     * @param percent the percent to decrease
     * @return a color int
     */
    @ColorInt
    public static int decreaseRgbChannels(Context context,
                                          @ColorRes int res, float percent) {
        int c = resolveColor(res, context);

        // reduce rgb channel values to produce box shadow effect
        int red = (Color.red(c));
        red -= (red * percent);
        red = red > 0 ? red : 0;

        int green = (Color.green(c));
        green -= (green * percent);
        green = green > 0 ? green : 0;

        int blue = (Color.blue(c));
        blue -= (blue * percent);
        blue = blue > 0 ? blue : 0;

        return Color.argb(Color.alpha(c), red, green, blue);
    }

    /**
     * 通过增加透明度频道值来浅化一个颜色
     *
     * @param context the current context
     * @param res     the color resource
     * @param alpha   the alpha to set
     * @return a color int
     */
    @ColorInt
    public static int increaseOpacity(Context context, @ColorRes int res, int alpha) {
        int c = resolveColor(res, context);
        return increaseOpacityFromInt(context, resolveColor(res, context), alpha);
    }

    @ColorInt
    public static int increaseOpacityFromInt(Context context, @ColorInt int c, int
            alpha) {
        return Color.argb(alpha, Color.red(c), Color.green(c), Color.blue(c));
    }
}
