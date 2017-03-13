package com.cn.smart.baselib.share.core.handler;

import android.content.Context;

import com.cn.smart.baselib.share.core.IActivityLifecycleMirror;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;

public interface IShareHandler extends IActivityLifecycleMirror {

    void share(BaseShareParam params, SocializeListeners.ShareListener listener) throws Exception;

    void release();

    Context getContext();

    SocializeMedia getShareMedia();

    /**
     * 是否是一次性的ShareHandler.
     * GENERIC/COPY这种分享方式，不需要或者无法得知第三方app的分享结果，用此方法来标记。
     *
     * @return 如果为true, 则调用share()后就release()b;
     */
    boolean isDisposable();

    interface InnerShareListener extends SocializeListeners.ShareListener {

        void onProgress(SocializeMedia type, String progressDesc);

    }
}
