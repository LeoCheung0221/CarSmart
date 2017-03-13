package com.cn.smart.baselib.uiframework.bootstrap.api.view;

import android.support.annotation.NonNull;

import com.cn.smart.baselib.uiframework.bootstrap.api.attributes.BootstrapBrand;

/**
 * author：leo on 2016/12/6 15:41
 * email： leocheung4ever@gmail.com
 * description: 实现这个接口的view, 通过给定的Bootstrap Brand 改变它们的颜色
 * what & why is modified:
 */

public interface BootstrapBrandView {

    String KEY = "com.cn.smart.baselib.uiframework.bootstrap.api.view.BootstrapBrandView";

    /**
     * 改变view颜色来匹配给定的Bootstrap Brand
     *
     * @param bootstrapBrand 给定的Bootstrap Brand
     */
    void setBootstrapBrand(@NonNull BootstrapBrand bootstrapBrand);

    /**
     * @return 当前的Bootstrap Brand
     */
    @NonNull
    BootstrapBrand getBootstrapBrand();

}
