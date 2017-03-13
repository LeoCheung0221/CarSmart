/*
 * Copyright (C) 2015 Bilibili <jungly.ik@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cn.smart.baselib.share.core.ui;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.common.AssistActivity;

/**
 * 修复分享完成后通过非正常方式返回app后界面卡死的bug。
 */
public class QQAssistAdapterActivity extends AssistActivity {
    private boolean mIsRestartFromQQSDK;
    private boolean mHasActivityResultCalled;
    private boolean mHasOnIntentCalled;

    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            if (bundle != null) {
                mIsRestartFromQQSDK = bundle.getBoolean("RESTART_FLAG");
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasOnIntentCalled || mHasActivityResultCalled) {
            return;
        }

        if (mIsRestartFromQQSDK && !isFinishing()) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mHasOnIntentCalled = true;
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int i, int i1, Intent intent) {
        mHasActivityResultCalled = true;
        super.onActivityResult(i, i1, intent);
    }
}
