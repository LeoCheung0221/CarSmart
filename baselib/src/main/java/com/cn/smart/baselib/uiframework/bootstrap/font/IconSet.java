package com.cn.smart.baselib.uiframework.bootstrap.font;

/**
 * author：leo on 2016/12/6 15:17
 * email： leocheung4ever@gmail.com
 * description: 指定的图标代码字体, 以及提供字体的文件名以便可以初始化
 * what & why is modified:
 */

public interface IconSet {

    /**
     * 返回当前字体图标的unicode字符
     */
    CharSequence unicodeForKey(CharSequence key);

    /**
     * 返回当前字体图标的图标代码
     */
    CharSequence iconCodeForAttrIndex(int index);

    /**
     * 指定字体文件所在位置, 从资源目录开始
     */
    CharSequence fontPath();

}
