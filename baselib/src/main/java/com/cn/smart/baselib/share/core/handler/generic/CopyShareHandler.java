package com.cn.smart.baselib.share.core.handler.generic;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.SocializeMedia;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.handler.BaseShareHandler;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.core.shareparam.ShareParamAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamText;
import com.cn.smart.baselib.share.core.shareparam.ShareParamVideo;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;
import com.cn.smart.baselib.share.util.BuildHelper;


/**
 * ShareParam，只读取content的内容。
 */
public class CopyShareHandler extends BaseShareHandler {

    public CopyShareHandler(Activity context, CarSmartShareConfiguration configuration) {
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
        Context context = getContext();
        if (context == null) {
            return;
        }

        String content = param.getContent();
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (BuildHelper.isApi11_HoneyCombOrLater()) {
            clip.setPrimaryClip(ClipData.newPlainText(null, content));
        } else {
            ((android.text.ClipboardManager) clip).setText(content);
        }
        Toast.makeText(context, R.string.carsmart_share_sdk_share_copy, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isDisposable() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.COPY;
    }

}
