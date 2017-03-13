package com.cn.smart.baselib.share.core.handler.sina;

import android.app.Activity;
import android.content.Intent;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SharePlatformConfig;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.handler.AbsShareHandler;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.core.ui.SinaAssistActivity;

import java.util.Map;

public class SinaShareTransitHandler extends AbsShareHandler {

    public static final int REQ_CODE = 10233;

    public SinaShareTransitHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener) {
        super.onActivityResult(activity, requestCode, resultCode, data, listener);
        if (data == null || getShareListener() == null) {
            return;
        }

        int statusCode = data.getIntExtra(SinaAssistActivity.KEY_CODE, -1);
        if (statusCode == CarSmartShareStatusCode.ST_CODE_SUCCESSED) {
            getShareListener().onSuccess(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SUCCESSED);
        } else if (statusCode == CarSmartShareStatusCode.ST_CODE_ERROR) {
            getShareListener().onError(SocializeMedia.SINA, CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_SHARE_FAILED, new Exception());
        } else if (statusCode == CarSmartShareStatusCode.ST_CODE_ERROR_CANCEL) {
            getShareListener().onCancel(SocializeMedia.SINA);
        }

    }

    @Override
    public void share(final BaseShareParam params, final SocializeListeners.ShareListener listener) throws Exception {
        super.share(params, listener);
        mImageHelper.saveBitmapToExternalIfNeed(params);
        mImageHelper.copyImageToCacheFileDirIfNeed(params);
        mImageHelper.downloadImageIfNeed(params, new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), SinaAssistActivity.class);
                intent.putExtra(SinaAssistActivity.KEY_PARAM, params);
                Map<String, Object> appConfig = SharePlatformConfig.getPlatformDevInfo(SocializeMedia.SINA);
                if (appConfig != null) {
                    intent.putExtra(SinaAssistActivity.KEY_APPKEY, (String) appConfig.get(SharePlatformConfig.APP_KEY));
                }
                intent.putExtra(SinaAssistActivity.KEY_CONFIG, mShareConfiguration);
                ((Activity) getContext()).startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.SINA;
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

}
