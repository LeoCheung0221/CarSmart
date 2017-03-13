package com.cn.smart.baselib.uiframework.bootstrap.api.view;

import android.support.annotation.NonNull;

import com.cn.smart.baselib.uiframework.bootstrap.api.defaults.ButtonMode;

/**
 * Views which implement this interface allow the selection mode of their buttons to be set
 */
public interface ButtonModeView {

    /**
     * @return the selection mode currently used by the button
     */
    @NonNull
    ButtonMode getButtonMode();

    /**
     * Sets the selection mode the button should use
     *
     * @param buttonMode the selection mode
     */
    void setButtonMode(@NonNull ButtonMode buttonMode);

}
