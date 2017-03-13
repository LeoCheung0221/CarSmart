package com.cn.smart.baselib.uiframework.banner.holder;

import android.content.Context;
import android.view.View;

/**
 * author：leo on 2017/3/1 11:35
 * email： leocheung4ever@gmail.com
 * description: <T> 任何指定的对象
 * what & why is modified:
 */

public interface BaseHolder<T> {

    View createView(Context context);

    void UpdateUI(Context context, int position, T data);

}
