package com.cn.smart.baselib.uiframework.bootstrap;

import android.content.Context;
import android.graphics.Typeface;

import com.cn.smart.baselib.uiframework.bootstrap.font.FontAwesome;
import com.cn.smart.baselib.uiframework.bootstrap.font.IconSet;
import com.cn.smart.baselib.uiframework.bootstrap.font.MaterialIcons;
import com.cn.smart.baselib.uiframework.bootstrap.font.Typicon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * author：leo on 2016/12/6 15:43
 * email： leocheung4ever@gmail.com
 * description:
 * 支持静态字体实例, 图标集, 允许视图在应用程序中使用,而不需昂贵的初始化开销
 * what & why is modified:
 */

public class TypefaceProvider {

    private final static Map<CharSequence, Typeface> TYPEFACE_MAP = new HashMap<>();
    private final static Map<CharSequence, IconSet> REGISTERED_ICON_SETS = new HashMap<>();


    /**
     * 注册默认的图标集实例, 以便它们可以在整个应用程序中使用
     * 目前默认的图标集合包括FontAwesome 和 Typicon
     */
    public static void registerDefaultIconSets() {
        final FontAwesome fontAwesome = new FontAwesome();
        final Typicon typicon = new Typicon();
        final MaterialIcons materialIcons = new MaterialIcons();

        REGISTERED_ICON_SETS.put(fontAwesome.fontPath(), fontAwesome);
        REGISTERED_ICON_SETS.put(typicon.fontPath(), typicon);
        REGISTERED_ICON_SETS.put(materialIcons.fontPath(), materialIcons);
    }

    /**
     * 返回一个请求过字体的引用, 如果没有存在则创建一个新的实例
     *
     * @param context 上下文环境
     * @param iconSet 图标字体
     * @return 一个字体实例的引用
     */
    public static Typeface getTypeface(Context context, IconSet iconSet) {
        String path = iconSet.fontPath().toString();

        if (TYPEFACE_MAP.get(path) == null) {
            final Typeface font = Typeface.createFromAsset(context.getAssets(), path);
            TYPEFACE_MAP.put(path, font);
        }
        return TYPEFACE_MAP.get(path);
    }

    /**
     * 注册自定义图标集, 以便它可以在整个应用程序中使用
     *
     * @param iconSet 一个自定义图标集
     */
    public static void registerCustomIconSet(IconSet iconSet) {
        REGISTERED_ICON_SETS.put(iconSet.fontPath(), iconSet);
    }

    /**
     * 找回应用程序中全部注册过的图标集的集合
     *
     * @return 一个已注册过的集合
     */
    public static Collection<IconSet> getRegisteredIconSets() {
        return REGISTERED_ICON_SETS.values();
    }

    /**
     * 找回已注册的自定义图标集, 这些图标集的字体可以在给定路径的资源文件目录下找到
     *
     * @param path 给定的路径
     * @param mode 视图请求图标集是否显示在预览编辑器里
     * @return 已注册过的图标集实例
     */
    public static IconSet retrieveRegisteredIconSet(String fontPath, boolean editMode) {
        final IconSet iconSet = REGISTERED_ICON_SETS.get(fontPath);

        if (iconSet == null && !editMode) {
            throw new RuntimeException(String.format("Font '%s' not properly registered, please" +
                    " see the README at https://github.com/Bearded-Hen/Android-Bootstrap", fontPath));
        }
        return iconSet;
    }
}
