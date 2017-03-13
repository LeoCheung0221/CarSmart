package com.cn.smart.carsmart.components.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.cn.smart.baselib.uiframework.banner.holder.BaseHolder;

/**
 * author：leo on 2017/3/1 17:19
 * email： leocheung4ever@gmail.com
 * description: 本地图片Holder例子
 * what & why is modified:
 */

public class LocalImageHolderView implements BaseHolder<Integer>{

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
