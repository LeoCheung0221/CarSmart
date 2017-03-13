package com.cn.smart.baselib.share.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 分享回调
 */
public interface IActivityLifecycleMirror {

    void onActivityCreated(Activity activity, Bundle savedInstanceState, SocializeListeners.ShareListener listener);

    void onActivityNewIntent(Activity activity, Intent intent);

    void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data, SocializeListeners.ShareListener listener);

    void onActivityDestroy();

}