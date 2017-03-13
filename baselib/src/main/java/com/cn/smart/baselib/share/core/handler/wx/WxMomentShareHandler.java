
package com.cn.smart.baselib.share.core.handler.wx;

import android.app.Activity;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

public class WxMomentShareHandler extends BaseWxShareHandler {

    public WxMomentShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        if (params.getImage() != null && (!params.getImage().isUnknowImage())) {
            super.shareImage(params);
        } else {
            ShareParamWebPage webpage = new ShareParamWebPage(params.getTitle(), params.getContent(), params.getTargetUrl());
            webpage.setThumb(params.getImage());
            shareWebPage(webpage);
        }
    }

    @Override
    int getShareType() {
        return SendMessageToWX.Req.WXSceneTimeline;
    }

    @Override
    protected SocializeMedia getSocializeType() {
        return SocializeMedia.WEIXIN_MONMENT;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.WEIXIN_MONMENT;
    }
}

