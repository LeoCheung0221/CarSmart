package com.cn.smart.baselib.share.core.handler.wx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SharePlatformConfig;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.InvalidParamException;
import com.cn.smart.baselib.share.core.error.ShareConfigException;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.handler.BaseShareHandler;
import com.cn.smart.baselib.share.core.shareparam.ShareImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamText;
import com.cn.smart.baselib.share.core.shareparam.ShareParamVideo;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;
import com.cn.smart.baselib.share.core.shareparam.ShareVideo;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

public abstract class BaseWxShareHandler extends BaseShareHandler {
    public static final String ACTION_RESULT = "com.cn.aam.checaiduo.socialize.share.wx.result";
    public static final String BUNDLE_STATUS_CODE = "status_code";

    protected static final int IMAGE_MAX = 32 * 1024;
    protected static final int IMAGE_WIDTH = 600;
    protected static final int IMAGE_HEIGHT = 800;

    private static String mAppId;
    public static IWXAPI mIWXAPI;

    private ResultHolder mResultHolder = new ResultHolder();

    public BaseWxShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
        try {
            IntentFilter filter = new IntentFilter(ACTION_RESULT);
            context.registerReceiver(mResultReceiver, filter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> getAppConfig() {
        Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.WEIXIN);
        if (appConfig == null || appConfig.isEmpty()) {
            appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.WEIXIN_MONMENT);
        }
        return appConfig;
    }

    @Override
    protected void checkConfig() throws Exception {
        if (!TextUtils.isEmpty(mAppId)) {
            return;
        }

        Map<String, Object> appConfig = getAppConfig();
        if (appConfig == null || appConfig.isEmpty() ||
                TextUtils.isEmpty(mAppId = (String) appConfig.get(SharePlatformConfig.APP_ID))) {
            throw new ShareConfigException("Please set WeChat platform dev info.");
        }
    }

    @Override
    protected void init() throws Exception {
        if (mIWXAPI == null) {
            mIWXAPI = WXAPIFactory.createWXAPI(getContext().getApplicationContext(), mAppId, true);
            if (mIWXAPI.isWXAppInstalled()) {
                mIWXAPI.registerApp(mAppId);
            }
        }

        if (!mIWXAPI.isWXAppInstalled()) {
            String msg = getContext().getString(R.string.carsmart_share_sdk_not_install_wechat);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            throw new ShareException(msg, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_NOT_INSTALL);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState, SocializeListeners.ShareListener listener) {
        super.onActivityCreated(activity, savedInstanceState, listener);
        if (mResultHolder.mResp != null) {
            parseResult(mResultHolder.mResp, listener);
            mResultHolder.mResp = null;
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener) {
        super.onActivityResult(activity, requestCode, resultCode, data, listener);
        if (mResultHolder.mResp != null) {
            parseResult(mResultHolder.mResp, listener);
            mResultHolder.mResp = null;
        }
    }

    @Override
    protected void shareText(final ShareParamText params) throws ShareException {
        String text = params.getContent();
        if (TextUtils.isEmpty(text)) {
            throw new InvalidParamException("Content is empty or illegal");
        }

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        req.scene = getShareType();
        shareOnMainThread(req);
    }

    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                WXImageObject imgObj = buildWXImageObject(params.getImage());

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;
                msg.thumbData = mImageHelper.buildThumbData(params.getImage());

                final SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("imgshareappdata");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    protected WXImageObject buildWXImageObject(final ShareImage image) {
        WXImageObject imgObj = new WXImageObject();

        if (image == null) {
            return imgObj;
        }

        if (image.isLocalImage()) {
            imgObj.setImagePath(image.getLocalPath());
        } else if (!image.isUnknowImage()) {
            imgObj.imageData = mImageHelper.buildThumbData(image, IMAGE_MAX, IMAGE_WIDTH, IMAGE_HEIGHT, false);
        }

        return imgObj;
    }

    @Override
    protected void shareWebPage(final ShareParamWebPage params) throws ShareException {
        if (TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Target url is empty or illegal");
        }

        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {

                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = params.getTargetUrl();

                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = params.getTitle();
                msg.description = params.getContent();
                msg.thumbData = mImageHelper.buildThumbData(params.getThumb());

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    @Override
    protected void shareAudio(final ShareParamAudio params) throws ShareException {
        if (TextUtils.isEmpty(params.getTargetUrl()) && TextUtils.isEmpty(params.getAudioUrl())) {
            throw new InvalidParamException("Target url or audio url is empty or illegal");
        }

        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                WXMusicObject audio = new WXMusicObject();

                if (!TextUtils.isEmpty(params.getAudioUrl())) {
                    audio.musicUrl = params.getAudioUrl();
                } else {
                    audio.musicUrl = params.getTargetUrl();
                }

                WXMediaMessage msg = new WXMediaMessage(audio);
                msg.title = params.getTitle();
                msg.description = params.getContent();
                msg.thumbData = mImageHelper.buildThumbData(params.getThumb());

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("music");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    @Override
    protected void shareVideo(final ShareParamVideo params) throws ShareException {
        if (TextUtils.isEmpty(params.getTargetUrl()) && (params.getVideo() == null || TextUtils.isEmpty(params.getVideo().getVideoH5Url()))) {
            throw new InvalidParamException("Target url or video url is empty or illegal");
        }

        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                WXVideoObject video = new WXVideoObject();
                ShareVideo sv = params.getVideo();
                if (!TextUtils.isEmpty(sv.getVideoH5Url())) {
                    video.videoUrl = sv.getVideoH5Url();
                } else {
                    video.videoUrl = params.getTargetUrl();
                }

                WXMediaMessage msg = new WXMediaMessage(video);
                msg.title = params.getTitle();
                msg.description = params.getContent();
                msg.thumbData = mImageHelper.buildThumbData(params.getThumb());

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("video");
                req.message = msg;
                req.scene = getShareType();
                shareOnMainThread(req);
            }
        });
    }

    protected String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void shareOnMainThread(final SendMessageToWX.Req req) {
        doOnMainThread(new Runnable() {
            @Override
            public void run() {
                postProgressStart();
                boolean result = mIWXAPI.sendReq(req);
                if (!result && getShareListener() != null) {
                    getShareListener().onError(getShareMedia(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED, new ShareException("sendReq failed"));
                }
            }
        });
    }

    public void onReq(BaseReq baseReq) {
    }

    public void onResp(BaseResp resp) {
        SocializeListeners.ShareListener listener = getShareListener();
        if (listener == null) {
            mResultHolder.mResp = resp;
            return;
        }

        parseResult(resp, listener);
    }

    private void parseResult(BaseResp resp, SocializeListeners.ShareListener listener) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                listener.onSuccess(getSocializeType(), CarSmartShareStatusCode.ST_CODE_SUCCESSED);
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                listener.onCancel(getSocializeType());
                break;

            case BaseResp.ErrCode.ERR_SENT_FAILED:
                listener.onError(getSocializeType(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED, new ShareException(resp.errStr));
                break;
        }
    }

    @Override
    public void release() {
        try {
            if (getContext() != null) {
                getContext().unregisterReceiver(mResultReceiver);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.release();
    }

    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SocializeListeners.ShareListener listener = getShareListener();
            if (intent == null || listener == null) {
                return;
            }

            int code = intent.getIntExtra(BUNDLE_STATUS_CODE, -1);
            if (code == CarSmartShareStatusCode.ST_CODE_SUCCESSED) {
                listener.onSuccess(getSocializeType(), CarSmartShareStatusCode.ST_CODE_SUCCESSED);
            } else if (code == CarSmartShareStatusCode.ST_CODE_ERROR) {
                listener.onError(getSocializeType(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED, new ShareException("unknown"));
            } else if (code == CarSmartShareStatusCode.ST_CODE_ERROR_CANCEL) {
                listener.onCancel(getSocializeType());
            }
        }
    };

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    abstract int getShareType();

    protected abstract SocializeMedia getSocializeType();

    private static class ResultHolder {
        BaseResp mResp;
    }

}
