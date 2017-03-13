package com.cn.smart.baselib.share.core;

import com.cn.smart.baselib.share.core.error.CarSmartShareStatusCode;

public abstract class SocializeListeners {

    private SocializeListeners() {
    }

    public interface ShareListener {

        void onStart(SocializeMedia type);

        void onProgress(SocializeMedia type, String progressDesc);

        void onSuccess(SocializeMedia type, int code);

        void onError(SocializeMedia type, int code, Throwable error);

        void onCancel(SocializeMedia type);

    }

    public static abstract class ShareListenerAdapter implements ShareListener {

        public void onStart(SocializeMedia type) {

        }

        protected abstract void onComplete(SocializeMedia type, int code, Throwable error);

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {

        }

        public final void onSuccess(SocializeMedia type, int code) {
            onComplete(type, CarSmartShareStatusCode.ST_CODE_SUCCESSED, null);
        }

        public final void onError(SocializeMedia type, int code, Throwable error) {
            onComplete(type, CarSmartShareStatusCode.ST_CODE_ERROR, error);
        }

        public final void onCancel(SocializeMedia type) {
            onComplete(type, CarSmartShareStatusCode.ST_CODE_ERROR_CANCEL, null);
        }

    }

}