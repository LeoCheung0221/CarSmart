package com.cn.smart.baselib.share.core.handler.wx;

import android.app.Activity;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

public class WxChatShareHandler extends BaseWxShareHandler {

    public WxChatShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    int getShareType() {
        return SendMessageToWX.Req.WXSceneSession;
    }

    @Override
    protected SocializeMedia getSocializeType() {
        return SocializeMedia.WEIXIN;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.WEIXIN;
    }
}
