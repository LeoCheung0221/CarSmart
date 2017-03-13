package com.cn.smart.baselib.share.core.handler.qq;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SharePlatformConfig;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.ShareConfigException;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.handler.BaseShareHandler;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.Map;

public abstract class BaseQQShareHandler extends BaseShareHandler {

    private static String mAppId;
    protected static Tencent mTencent;

    public BaseQQShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    private static Map<String, Object> getAppConfig() {
        Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.QQ);
        if (appConfig == null || appConfig.isEmpty()) {
            appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.QZONE);
        }

        return appConfig;
    }

    @Override
    protected void checkConfig() throws Exception {
        if (!TextUtils.isEmpty(mAppId)) {
            return;
        }

        Map<String, Object> appConfig = getAppConfig();
        if (appConfig == null || appConfig.isEmpty()
                || TextUtils.isEmpty(mAppId = (String) appConfig.get(SharePlatformConfig.APP_ID))) {
            throw new ShareConfigException("Please set QQ platform dev info.");
        }
    }

    @Override
    protected void init() throws Exception {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(mAppId, getContext().getApplicationContext());
        }
    }

    /**
     * 必须在主线程分享
     */
    protected void doShareToQQ(final Activity activity, final Bundle params) {
        doOnMainThread(new Runnable() {
            @Override
            public void run() {
                postProgressStart();
                onShare(activity, mTencent, params, mUiListener);
                if (!Util.isMobileQQSupportShare(getContext())) {
                    String msg = getContext().getString(R.string.carsmart_share_sdk_not_install_qq);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    if (getShareListener() != null) {
                        getShareListener().onError(getShareMedia(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_NOT_INSTALL, new ShareException(msg));
                    }
                }
            }
        });
    }

    protected abstract void onShare(Activity activity, Tencent tencent, Bundle params, IUiListener iUiListener);

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    protected final IUiListener mUiListener = new IUiListener() {
        @Override
        public void onCancel() {
            if (getShareListener() != null) {
                getShareListener().onCancel(getShareMedia());
            }
        }

        @Override
        public void onComplete(Object response) {
            if (getShareListener() != null) {
                getShareListener().onSuccess(getShareMedia(), CarSmartShareStatusCode.ST_CODE_SUCCESSED);
            }
        }

        @Override
        public void onError(UiError e) {
            if (getShareListener() != null) {
                getShareListener().onError(getShareMedia(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION, new ShareException(e.errorMessage));
            }
        }
    };
}
