package com.cn.smart.baselib.share.core.handler.generic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeListeners;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.handler.BaseShareHandler;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.core.shareparam.ShareParamAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamText;
import com.cn.smart.baselib.share.core.shareparam.ShareParamVideo;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;

/**
 * 只读title和content
 */
public class GenericShareHandler extends BaseShareHandler {

    public GenericShareHandler(Activity context, CarSmartShareConfiguration configuration) {
        super(context, configuration);
    }

    @Override
    protected void checkConfig() throws Exception {

    }

    @Override
    protected void init() throws Exception {

    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareImage(ShareParamImage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        share(params);
    }

    private void share(BaseShareParam param) {
        SocializeListeners.ShareListener shareListener = getShareListener();
        Intent shareIntent = createIntent(param.getTitle(), param.getContent());
        Intent chooser = Intent.createChooser(shareIntent, "分享到：");
        try {
            getContext().startActivity(chooser);
        } catch (ActivityNotFoundException ignored) {
            if (shareListener != null) {
                shareListener.onError(getShareMedia(), CarSmartShareStatusCode.ST_CODE_ERROR, new ShareException("activity not found"));
            }
        }
    }

    private Intent createIntent(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }

    @Override
    public boolean isDisposable() {
        return true;
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.GENERIC;
    }
}
