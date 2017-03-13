package com.cn.smart.baselib.share.core.error;

public class ShareException extends Exception {
    private int mCode = -1;

    public ShareException(String detailMessage) {
        super(detailMessage);
    }

    public ShareException(String detailMessage, int code) {
        super(detailMessage);
        mCode = code;
    }

    public ShareException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ShareException(Throwable throwable) {
        super(throwable);
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }
}
