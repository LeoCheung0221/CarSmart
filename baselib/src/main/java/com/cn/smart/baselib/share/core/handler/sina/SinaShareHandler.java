package com.cn.smart.baselib.share.core.handler.sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SharePlatformConfig;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.InvalidParamException;
import com.cn.smart.baselib.share.core.error.ShareConfigException;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.error.UnSupportedException;
import com.cn.smart.baselib.share.core.handler.BaseShareHandler;
import com.cn.smart.baselib.share.core.helper.AccessTokenKeeper;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.core.shareparam.ShareAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamText;
import com.cn.smart.baselib.share.core.shareparam.ShareParamVideo;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;
import com.cn.smart.baselib.share.core.shareparam.ShareVideo;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;
import java.util.Map;

/**
 * 支持所有的类型。
 */
public class SinaShareHandler extends BaseShareHandler {

    private static String mAppKey;
    public static final String DEFAULT_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String DEFAULT_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    public static IWeiboShareAPI mWeiboShareAPI = null;
    private static SsoHandler mSsoHandler;
    private static WeiboMultiMessage mWeiboMessage;

    public SinaShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    public void checkConfig() throws Exception {
        if (!TextUtils.isEmpty(mAppKey)) {
            return;
        }

        Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.SINA);
        if (appConfig == null || appConfig.isEmpty() || TextUtils.isEmpty(mAppKey = (String) appConfig.get(SharePlatformConfig.APP_KEY))) {
            throw new ShareConfigException("Please set Sina platform dev info.");
        }
    }

    @Override
    public void init() throws Exception {
        //安装客户端后要重新创建实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getContext().getApplicationContext(), mAppKey);
        mWeiboShareAPI.registerApp();
    }

    /**
     * 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
     * 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
     * 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
     * 失败返回 false，不调用上述回调
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState, SocializeListeners.ShareListener listener) {
        super.onActivityCreated(activity, savedInstanceState, listener);
        if (savedInstanceState != null && mWeiboShareAPI != null) {
            mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), (IWeiboHandler.Response) activity);
        }
    }

    /**
     * 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
     * 来接收微博客户端返回的数据；执行成功，返回 true，并调用
     * {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
     */
    @Override
    public void onActivityNewIntent(Activity activity, Intent intent) {
        super.onActivityNewIntent(activity, intent);
        if (mWeiboShareAPI != null)
            try {
                mWeiboShareAPI.handleWeiboResponse(intent, (IWeiboHandler.Response) activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener) {
        super.onActivityResult(activity, requestCode, resultCode, data, listener);
        if (mSsoHandler != null && TextUtils.isEmpty(getToken())) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * Content cannot be empty
     */
    @Override
    protected void shareText(final ShareParamText params) throws ShareException {
        checkContent(params);

        final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj(params);

        allInOneShare(weiboMessage);
    }

    /**
     * Content and image cannot be empty or null
     */
    @Override
    protected void shareImage(final ShareParamImage params) throws ShareException {
        checkContent(params);
        checkImage(params.getImage());

        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

                weiboMessage.textObject = getTextObj(params);
                weiboMessage.imageObject = getImageObj(params.getImage());

                allInOneShare(weiboMessage);
            }
        });
    }

    /**
     * Content and image cannot be empty or null
     */
    @Override
    protected void shareWebPage(final ShareParamWebPage params) throws ShareException {
        checkContent(params);
        if (TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Target url is empty or illegal");
        }

        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

                if (!isSinaClientInstalled()) {
                    weiboMessage.textObject = getTextObj(params);
                }

                try {
                    checkImage(params.getThumb());
                    weiboMessage.imageObject = getImageObj(params.getThumb());
                } catch (Exception e) {
                    weiboMessage.textObject = getTextObj(params);
                }

                weiboMessage.mediaObject = getWebPageObj(params);

                allInOneShare(weiboMessage);
            }
        });
    }

    @Override
    protected void shareAudio(final ShareParamAudio params) throws ShareException {
        checkContent(params);
        if (TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Target url is empty or illegal");
        }
        if (params.getAudio() == null) {
            throw new InvalidParamException("Audio is empty or illegal");
        }

        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

                if (!isSinaClientInstalled()) {
                    weiboMessage.textObject = getTextObj(params);
                }

                try {
                    checkImage(params.getThumb());
                    weiboMessage.imageObject = getImageObj(params.getThumb());
                } catch (Exception e) {
                    weiboMessage.textObject = getTextObj(params);
                }

                weiboMessage.mediaObject = getAudioObj(params);

                allInOneShare(weiboMessage);
            }
        });
    }

    @Override
    protected void shareVideo(final ShareParamVideo params) throws ShareException {
        checkContent(params);
        if (TextUtils.isEmpty(params.getTargetUrl())) {
            throw new InvalidParamException("Target url is empty or illegal");
        }
        if (params.getVideo() == null) {
            throw new InvalidParamException("Video is empty or illegal");
        }

        doOnWorkThread(new Runnable() {
            @Override
            public void run() {
                final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

                if (!isSinaClientInstalled()) {
                    weiboMessage.textObject = getTextObj(params);
                }

                try {
                    checkImage(params.getThumb());
                    weiboMessage.imageObject = getImageObj(params.getThumb());
                } catch (Exception e) {
                    weiboMessage.textObject = getTextObj(params);
                }

                weiboMessage.mediaObject = getVideoObj(params);

                allInOneShare(weiboMessage);
            }
        });
    }

    private void checkContent(BaseShareParam params) throws ShareException {
        if (TextUtils.isEmpty(params.getContent())) {
            throw new InvalidParamException("Content is empty or illegal");
        }
    }

    private void checkImage(ShareImage image) throws ShareException {
        if (image == null) {
            throw new InvalidParamException("Image cannot be null");
        }

        if (image.isLocalImage()) {
            if (TextUtils.isEmpty(image.getLocalPath()) || !new File(image.getLocalPath()).exists()) {
                throw new InvalidParamException("Image path is empty or illegal");
            }
        } else if (image.isNetImage()) {
            if (TextUtils.isEmpty(image.getNetImageUrl())) {
                throw new InvalidParamException("Image url is empty or illegal");
            }
        } else if (image.isResImage())
            throw new UnSupportedException("Unsupport image type");
        else if (image.isBitmapImage()) {
            if (image.getBitmap().isRecycled()) {
                throw new InvalidParamException("Cannot share recycled bitmap.");
            }
        } else
            throw new UnSupportedException("Invaild image");
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(BaseShareParam params) {
        TextObject textObject = new TextObject();

        if (params != null) {
            textObject.text = params.getContent();
        }

        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(ShareImage image) {
        ImageObject imageObject = new ImageObject();

        if (image == null) {
            return imageObject;
        }

        if (image.isLocalImage()) {
            imageObject.imagePath = image.getLocalPath();
        } else {
            imageObject.imageData = mImageHelper.buildThumbData(image);
        }
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebPageObj(ShareParamWebPage params) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = params.getContent();
        mediaObject.description = params.getTitle();

        byte[] thumbData = mImageHelper.buildThumbData(params.getThumb());
        if (thumbData == null || thumbData.length == 0) {
            mediaObject.thumbData = mImageHelper.buildThumbData(new ShareImage(mShareConfiguration.getDefaultShareImage()));
        } else
            mediaObject.thumbData = thumbData;

        mediaObject.actionUrl = params.getTargetUrl();
        mediaObject.defaultText = "车财多媒体消息对象";
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getAudioObj(ShareParamAudio params) {
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = params.getContent();
        musicObject.description = params.getTitle();

        byte[] thumbData = mImageHelper.buildThumbData(params.getThumb());
        if (thumbData == null || thumbData.length == 0) {
            musicObject.thumbData = mImageHelper.buildThumbData(new ShareImage(mShareConfiguration.getDefaultShareImage()));
        } else {
            musicObject.thumbData = thumbData;
        }

        musicObject.actionUrl = params.getTargetUrl();

        ShareAudio audio = params.getAudio();
        if (audio != null) {
            musicObject.dataUrl = audio.getAudioSrcUrl();
            musicObject.dataHdUrl = audio.getAudioSrcUrl();
            musicObject.h5Url = audio.getAudioH5Url();
            musicObject.duration = 10;
            musicObject.defaultText = audio.getDescription();
        }
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj(ShareParamVideo param) {
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = param.getContent();
        videoObject.description = param.getTitle();

        byte[] thumbData = mImageHelper.buildThumbData(param.getThumb());
        if (thumbData == null || thumbData.length == 0) {
            videoObject.thumbData = mImageHelper.buildThumbData(new ShareImage(mShareConfiguration.getDefaultShareImage()));
        } else {
            videoObject.thumbData = thumbData;
        }

        videoObject.actionUrl = param.getTargetUrl();

        ShareVideo video = param.getVideo();
        if (video != null) {
            videoObject.dataUrl = video.getVideoSrcUrl();
            videoObject.dataHdUrl = video.getVideoSrcUrl();
            videoObject.h5Url = video.getVideoH5Url();
            videoObject.duration = 10;
            videoObject.defaultText = video.getDescription();
        }
        return videoObject;
    }

    private void allInOneShare(WeiboMultiMessage weiboMessage) {
        final SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        final String token = getToken();
        final AuthInfo mAuthInfo = new AuthInfo(getContext(), mAppKey, mShareConfiguration.getSinaRedirectUrl(), mShareConfiguration.getSinaScope());
        if (TextUtils.isEmpty(token)) {
            mSsoHandler = new SsoHandler((Activity) getContext(), mAuthInfo);
            mSsoHandler.authorize(mAuthListener);
            mWeiboMessage = weiboMessage;
        } else {
            mWeiboMessage = null;
            mSsoHandler = null;
            doOnMainThread(new Runnable() {
                @Override
                public void run() {
                    postProgressStart();
                    boolean result = mWeiboShareAPI.sendRequest((Activity) getContext(), request, mAuthInfo, token, mAuthListener);
                    if (!result) {
                        if (getShareListener() != null) {
                            getShareListener().onError(getShareMedia(), 0, null);
                        }
                    }
                }
            });
        }

    }

    private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

        @Override
        public void onWeiboException(WeiboException arg0) {
            if (getShareListener() != null) {
                getShareListener().onError(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_AUTH_FAILED, arg0);
            }
        }

        @Override
        public void onComplete(Bundle bundle) {
            Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (newToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(getContext(), newToken);
                if (mWeiboMessage != null) {
                    allInOneShare(mWeiboMessage);
                }
                return;
            }

            SocializeListeners.ShareListener listener = getShareListener();
            if (listener == null) {
                return;
            }

            listener.onError(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_AUTH_FAILED, new ShareException("无效的token"));
        }

        @Override
        public void onCancel() {
            if (getShareListener() != null) {
                getShareListener().onCancel(SocializeMedia.SINA);
            }
        }
    };

    public void onResponse(BaseResponse baseResp) {
        SocializeListeners.ShareListener listener = getShareListener();
        if (listener == null) {
            return;
        }

        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                listener.onSuccess(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SUCCESSED);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                listener.onCancel(SocializeMedia.SINA);
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                listener.onError(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED, new ShareException(baseResp.errMsg));
                break;
        }
    }

    private String getToken() {
        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
        String token = "";
        if (mAccessToken != null) {
            token = mAccessToken.getToken();
        }
        return token;
    }

    private boolean isSinaClientInstalled() {
        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.SINA;
    }

}
