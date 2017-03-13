package com.cn.smart.baselib.share.core.handler;

import android.app.Activity;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.handler.generic.CopyShareHandler;
import com.cn.smart.baselib.share.core.handler.generic.GenericShareHandler;
import com.cn.smart.baselib.share.core.handler.qq.QQChatShareHandler;
import com.cn.smart.baselib.share.core.handler.qq.QQZoneShareHandler;
import com.cn.smart.baselib.share.core.handler.sina.SinaShareTransitHandler;
import com.cn.smart.baselib.share.core.handler.wx.WxChatShareHandler;
import com.cn.smart.baselib.share.core.handler.wx.WxMomentShareHandler;

import java.util.HashMap;
import java.util.Map;

import static com.cn.smart.baselib.share.core.SocializeMedia.WEIXIN;

public class ShareHandlerPool {

    private static ShareHandlerPool ourInstance = new ShareHandlerPool();
    private Map<SocializeMedia, IShareHandler> mHandlerMap = new HashMap<>();

    private ShareHandlerPool() {
    }

    public static IShareHandler newHandler(Activity context, SocializeMedia type, CarSmartShareConfiguration shareConfiguration) {
        IShareHandler handler;
        switch (type) {
            case WEIXIN: //发送到聊天界面
                handler = new WxChatShareHandler(context, shareConfiguration);
                break;

            case WEIXIN_MONMENT: //发送到朋友圈
                handler = new WxMomentShareHandler(context, shareConfiguration);
                break;

            case QQ:
                handler = new QQChatShareHandler(context, shareConfiguration);
                break;

            case QZONE:
                handler = new QQZoneShareHandler(context, shareConfiguration);
                break;

            case SINA:
                handler = new SinaShareTransitHandler(context, shareConfiguration);
                break;

            case COPY:
                handler = new CopyShareHandler(context, shareConfiguration);
                break;

            default:
                handler = new GenericShareHandler(context, shareConfiguration);
        }

        ourInstance.mHandlerMap.put(type, handler);

        return handler;
    }

    public static IShareHandler getCurrentHandler(SocializeMedia type) {
        return ourInstance.mHandlerMap.get(type);
    }

    public static void remove(SocializeMedia type) {
        ourInstance.mHandlerMap.remove(type);
    }

}
