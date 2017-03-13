package com.cn.smart.baselib.share.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.handler.AbsShareHandler;
import com.cn.smart.baselib.share.core.handler.IShareHandler;
import com.cn.smart.baselib.share.core.handler.ShareHandlerPool;
import com.cn.smart.baselib.share.core.handler.wx.BaseWxShareHandler;
import com.cn.smart.baselib.share.core.handler.wx.WxChatShareHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public abstract class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;
    private BaseWxShareHandler mShareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IShareHandler wxHandler = ShareHandlerPool.getCurrentHandler(SocializeMedia.WEIXIN);
        if (wxHandler == null) {
            wxHandler = ShareHandlerPool.getCurrentHandler(SocializeMedia.WEIXIN_MONMENT);
        }
        if (wxHandler == null) {
            wxHandler = new WxChatShareHandler(this, new CarSmartShareConfiguration.Builder(this).build());
            ((AbsShareHandler) wxHandler).setShareListener(mShareListener);
            initWXApi();
        }

        mShareHandler = (BaseWxShareHandler) wxHandler;

        if (isAutoCreateWXAPI() && mIWXAPI == null) {
            initWXApi();
        }
    }

    private void initWXApi() {
        mIWXAPI = WXAPIFactory.createWXAPI(getApplicationContext(), getAppId(), true);
        if (mIWXAPI.isWXAppInstalled()) {
            mIWXAPI.registerApp(getAppId());
        }
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if (mShareHandler != null) {
            mShareHandler.onReq(baseReq);
        }
        if (isAutoFinishAfterOnReq()) {
            finish();
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        if (mShareHandler != null) {
            mShareHandler.onResp(resp);
        }
        if (isAutoFinishAfterOnResp()) {
            finish();
        }
    }

    private SocializeListeners.ShareListener mShareListener = new SocializeListeners.ShareListener() {
        @Override
        public void onStart(SocializeMedia type) {

        }

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {

        }

        @Override
        public void onSuccess(SocializeMedia type, int code) {
            sendResult(CarSmartShareStatusCode.ST_CODE_SUCCESSED);
        }

        @Override
        public void onError(SocializeMedia type, int code, Throwable error) {
            sendResult(CarSmartShareStatusCode.ST_CODE_ERROR);
        }

        @Override
        public void onCancel(SocializeMedia type) {
            sendResult(CarSmartShareStatusCode.ST_CODE_ERROR_CANCEL);
        }

        private void sendResult(int statusCode) {
            if (mShareHandler != null) {
                mShareHandler.release();
            }
            Intent intent = new Intent(BaseWxShareHandler.ACTION_RESULT);
            intent.putExtra(BaseWxShareHandler.BUNDLE_STATUS_CODE, statusCode);
            sendBroadcast(intent);
        }
    };

    protected boolean isAutoFinishAfterOnReq() {
        return true;
    }

    protected boolean isAutoFinishAfterOnResp() {
        return true;
    }

    protected boolean isAutoCreateWXAPI() {
        return true;
    }

    protected abstract String getAppId();

}
