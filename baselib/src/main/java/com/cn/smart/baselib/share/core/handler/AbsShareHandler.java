
package com.cn.smart.baselib.share.core.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.helper.ShareImageHelper;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.tencent.open.utils.ThreadManager;

public abstract class AbsShareHandler implements IShareHandler {

    protected Context mContext;

    protected CarSmartShareConfiguration mShareConfiguration;

    private SocializeListeners.ShareListener mShareListener;

    protected ShareImageHelper mImageHelper;

    public AbsShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        initContext(context);
        mShareConfiguration = configuration;
        mImageHelper = new ShareImageHelper(mContext, configuration, mImageCallback);
    }

    private void initContext(Activity context) {
        if (isNeedActivityContext()) {
            mContext = context;
        } else {
            mContext = context.getApplicationContext();
        }
    }

    /**
     * 该平台是否必须用Activity类型的Context来调起分享
     */
    protected boolean isNeedActivityContext() {
        return false;
    }

    @Override
    public boolean isDisposable() {
        return false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState, SocializeListeners.ShareListener listener) {
        initContext(activity);
        mShareListener = listener;
    }

    @Override
    public void onActivityNewIntent(Activity activity, Intent intent) {
        initContext(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener) {
        initContext(activity);
        mShareListener = listener;
    }

    @Override
    public void onActivityDestroy() {
        release();
    }

    @Override
    public void release() {
        mShareListener = null;
        mContext = null;
    }

    @Override
    public void share(BaseShareParam params, SocializeListeners.ShareListener listener) throws Exception {
        mShareListener = listener;
    }

    protected void doOnWorkThread(final Runnable runnable) {
        mShareConfiguration.getTaskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();

                    if (getShareListener() != null) {
                        doOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getShareListener().onError(getShareMedia(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_IMAGE, new ShareException("Share failed"));
                            }
                        });
                    }
                }
            }
        });
    }

    protected void doOnMainThread(Runnable runnable) {
        ThreadManager.getMainHandler().post(runnable);
    }

    protected void postProgressStart() {
        //postProgress(R.string.bili_share_sdk_share_start);
    }

    protected void postProgress(final int msgRes) {
        if (getContext() != null) {
            postProgress(getContext().getString(msgRes));
        }
    }

    protected void postProgress(final String msg) {
        doOnMainThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (getShareListener() != null) {
                            getShareListener().onProgress(getShareMedia(), msg);
                        }
                    }
                }
        );
    }

    protected SocializeListeners.ShareListener getShareListener() {
        return mShareListener;
    }

    public void setShareListener(SocializeListeners.ShareListener shareListener) {
        mShareListener = shareListener;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private ShareImageHelper.Callback mImageCallback = new ShareImageHelper.Callback() {
        @Override
        public void onProgress(int msgId) {
            postProgress(msgId);
        }

        @Override
        public void onImageDownloadFailed() {
            if (getShareListener() != null) {
                getShareListener().onError(getShareMedia(), CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_IMAGE, new ShareException("Image compress failed"));
            }
        }
    };
}
