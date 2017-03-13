package com.cn.smart.baselib.share.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cn.smart.baselib.share.core.CarSmartShare;
import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SharePlatformConfig;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.handler.sina.SinaShareHandler;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.util.SharePlatformConfigHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

import java.util.Map;

/**
 * 处理微博分享，相当于QQ的{@link com.tencent.connect.common.AssistActivity}
 */
public class SinaAssistActivity extends Activity implements IWeiboHandler.Response {

    private static final String TAG = SinaAssistActivity.class.getSimpleName();

    public static final String KEY_CONFIG = "sina_share_config";
    public static final String KEY_APPKEY = "sina_share_appkey";
    public static final String KEY_CODE = "sina_share_result_code";
    public static final String KEY_PARAM = "sina_share_param";

    private SinaShareHandler mShareHandler;

    private boolean mIsActivityResultCanceled;
    private boolean mHasOnNewIntentCalled;
    private boolean mHasResponseCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //解决多进程问题
        CarSmartShareConfiguration shareConfig = CarSmartShare.getShareConfiguration();
        if (shareConfig == null) {
            shareConfig = getIntent().getParcelableExtra(KEY_CONFIG);
        }
        if (shareConfig == null) {
            finishWithFailResult();
            return;
        }

        Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.SINA);
        if (appConfig == null || appConfig.isEmpty()
                || TextUtils.isEmpty((String) appConfig.get(SharePlatformConfig.APP_KEY))) {
            String appKey = getIntent().getStringExtra(KEY_APPKEY);
            if (TextUtils.isEmpty(appKey)) {
                finishWithFailResult();
                return;
            } else {
                SharePlatformConfigHelper.configSina(appKey);
            }
        }

        mShareHandler = new SinaShareHandler(this, shareConfig);
        try {
            mShareHandler.checkConfig();
            mShareHandler.init();
        } catch (Exception e) {
            e.printStackTrace();
            finishWithFailResult();
            return;
        }

        mShareHandler.onActivityCreated(this, savedInstanceState, mInnerListener);
        try {
            if (savedInstanceState == null) {
                BaseShareParam param = getShareParam();
                if (param == null) {
                    mInnerListener.onError(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION,
                            new ShareException("sina share param error"));
                    finishWithCancelResult();
                } else {
                    mShareHandler.share(getShareParam(), mInnerListener);
                }
            }
        } catch (Exception e) {
            mInnerListener.onError(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION, e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasOnNewIntentCalled || mHasResponseCalled) {
            return;
        }

        if (SinaShareHandler.mWeiboShareAPI != null &&
                SinaShareHandler.mWeiboShareAPI.isWeiboAppInstalled() &&
                mIsActivityResultCanceled && !isFinishing()) {
            finishWithCancelResult();
        }
    }

    private BaseShareParam getShareParam() {
        return getIntent().getParcelableExtra(KEY_PARAM);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mHasOnNewIntentCalled = true;
        mShareHandler.onActivityNewIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsActivityResultCanceled = resultCode == Activity.RESULT_CANCELED;
        mShareHandler.onActivityResult(this, requestCode, resultCode, data, mInnerListener);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        mHasResponseCalled = true;
        if (mShareHandler != null) {
            mShareHandler.onResponse(baseResponse);
        }
    }

    private void finishWithCancelResult() {
        finishWithResult(CarSmartShareStatusCode.ST_CODE_ERROR_CANCEL);
    }

    private void finishWithFailResult() {
        finishWithResult(CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED);
    }

    protected SocializeListeners.ShareListenerAdapter mInnerListener = new SocializeListeners.ShareListenerAdapter() {

        @Override
        public void onStart(SocializeMedia type) {
            super.onStart(type);
        }

        @Override
        public void onComplete(SocializeMedia type, int code, Throwable error) {
            finishWithResult(code);
        }

    };

    private void finishWithResult(int code) {

        if (mShareHandler != null) {
            mShareHandler.onActivityDestroy();
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_CODE, code);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
