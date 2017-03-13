package com.cn.smart.baselib.uiframework.bootstrap.api.view;

import android.support.annotation.NonNull;

import com.cn.smart.baselib.uiframework.bootstrap.api.attributes.BootstrapHeading;

/**
 * Views which implement this interface change their text size and padding according to the
 * given Bootstrap Heading
 */
public interface BootstrapHeadingView {

    String KEY = "com.cn.smart.baselib.uiframework.bootstrap.api.view.BootstrapHeading";

    /**
     * Sets this view to use the given Bootstrap Heading, changing its text size and padding
     *
     * @param bootstrapHeading the Bootstrap Heading
     */
    void setBootstrapHeading(@NonNull BootstrapHeading bootstrapHeading);

    /**
     * @return the Bootstrap Heading for the view
     */
    @NonNull
    BootstrapHeading getBootstrapHeading();

}
