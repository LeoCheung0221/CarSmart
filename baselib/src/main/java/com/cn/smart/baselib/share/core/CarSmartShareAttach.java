package com.cn.smart.baselib.share.core;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.handler.IShareHandler;
import com.cn.smart.baselib.share.core.handler.ShareHandlerPool;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;

class CarSmartShareAttach {

    private IShareHandler mCurrentHandler;

    private CarSmartShareConfiguration mShareConfiguration;

    private SocializeListeners.ShareListener mOuterShareListener;

    public void init(CarSmartShareConfiguration configuration) {
        mShareConfiguration = configuration;
    }

    public void share(Activity activity, SocializeMedia type, BaseShareParam params, SocializeListeners.ShareListener listener) {

        if (mShareConfiguration == null) {
            throw new IllegalArgumentException("CarSmartShareConfiguration must be initialized before share");
        }

        if (mCurrentHandler != null) {
            release(mCurrentHandler.getShareMedia());
        }

        mCurrentHandler = ShareHandlerPool.newHandler(activity, type, mShareConfiguration);

        if (mCurrentHandler != null) {
            try {
                mOuterShareListener = listener;

                if (params == null) {
                    throw new IllegalArgumentException(("Share param cannot be null"));
                }

                mInnerProxyListener.onStart(type);
                mCurrentHandler.share(params, mInnerProxyListener);

                if (mCurrentHandler.isDisposable()) {
                    release(mCurrentHandler.getShareMedia());
                }

            } catch (ShareException e) {
                e.printStackTrace();
                mInnerProxyListener.onError(type, e.getCode(), e);
            } catch (Exception e) {
                mInnerProxyListener.onError(type, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_EXCEPTION, e);
                e.printStackTrace();
            }
        } else {
            mInnerProxyListener.onError(type, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_UNEXPLAINED, new Exception("Unknown share type"));
        }
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (mCurrentHandler != null) {
            mCurrentHandler.onActivityResult(activity, requestCode, resultCode, data, mInnerProxyListener);
        }
    }

    private void release(SocializeMedia type) {
        mOuterShareListener = null;
        if (mCurrentHandler != null) {
            mCurrentHandler.release();
        }
        mCurrentHandler = null;
        ShareHandlerPool.remove(type);
    }

    public CarSmartShareConfiguration getShareConfiguration() {
        return mShareConfiguration;
    }

    private IShareHandler.InnerShareListener mInnerProxyListener = new IShareHandler.InnerShareListener() {

        @Override
        public void onStart(SocializeMedia type) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onStart(type);
            }
        }

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {
            if (mCurrentHandler != null && mCurrentHandler.getContext() != null) {
                Toast.makeText(mCurrentHandler.getContext(), progressDesc, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onSuccess(SocializeMedia type, int code) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onSuccess(type, code);
            }
            release(type);
        }

        @Override
        public void onError(SocializeMedia type, int code, Throwable error) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onError(type, code, error);
            }
            release(type);
        }

        @Override
        public void onCancel(SocializeMedia type) {
            if (mOuterShareListener != null) {
                mOuterShareListener.onCancel(type);
            }
            release(type);
        }
    };
}