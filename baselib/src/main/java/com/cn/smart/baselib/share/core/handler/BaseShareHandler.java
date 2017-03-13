
package com.cn.smart.baselib.share.core.handler;

import android.app.Activity;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.core.shareparam.ShareParamAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamText;
import com.cn.smart.baselib.share.core.shareparam.ShareParamVideo;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;

/**
 * 定义模板步骤
 */
public abstract class BaseShareHandler extends AbsShareHandler {

    public BaseShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    public void share(BaseShareParam params, SocializeListeners.ShareListener listener) throws Exception {
        super.share(params, listener);
        checkConfig();
        init();

        mImageHelper.saveBitmapToExternalIfNeed(params);
        mImageHelper.copyImageToCacheFileDirIfNeed(params);

        if (params instanceof ShareParamText) {
            shareText((ShareParamText) params);
        } else if (params instanceof ShareParamImage) {
            shareImage((ShareParamImage) params);
        } else if (params instanceof ShareParamWebPage) {
            shareWebPage((ShareParamWebPage) params);
        } else if (params instanceof ShareParamAudio) {
            shareAudio((ShareParamAudio) params);
        } else if (params instanceof ShareParamVideo) {
            shareVideo((ShareParamVideo) params);
        }
    }

    /**
     * 检查配置，比如appKey，appSecret
     */
    protected abstract void checkConfig() throws Exception;

    protected abstract void init() throws Exception;

    protected abstract void shareText(ShareParamText params) throws ShareException;

    protected abstract void shareImage(ShareParamImage params) throws ShareException;

    protected abstract void shareWebPage(ShareParamWebPage params) throws ShareException;

    protected abstract void shareAudio(ShareParamAudio params) throws ShareException;

    protected abstract void shareVideo(ShareParamVideo params) throws ShareException;

}
