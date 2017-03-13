package com.cn.smart.baselib.uiframework.bootstrap.api.view;

/**
 * Views which implement this interface allow the border to be dynamically displayed
 */
public interface BorderView {

    String KEY_DISPLAYED = "com.cn.smart.baselib.uiframework.bootstrap.api.view.KEY_DISPLAYED";

    /**
     * Sets whether a border should be displayed or not
     *
     * @param displayed whether a border should be displayed or not
     */
    void setBorderDisplayed(boolean displayed);

    /**
     * @return whether the border is displayed or not
     */
    boolean isBorderDisplayed();

}
