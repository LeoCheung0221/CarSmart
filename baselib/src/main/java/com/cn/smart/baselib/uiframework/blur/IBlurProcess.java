package com.cn.smart.baselib.uiframework.blur;

import android.graphics.Bitmap;

/**
 * author：leo on 2016/11/25 09:24
 * email： leocheung4ever@gmail.com
 * description: 模糊处理接口
 * what & why is modified:
 */

public interface IBlurProcess {

    /**
     * 处理给定图片,通过提供的模糊半径进行模糊化
     * 如果半径是0,则会返回原始图像
     *
     * @param original 即将模糊化的bitmap
     * @param radius   像素模糊图片的半径
     * @return 模糊化后的图片版本
     */
    Bitmap blur(Bitmap original, float radius);
}
