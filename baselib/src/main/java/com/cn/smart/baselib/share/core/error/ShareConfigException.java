
package com.cn.smart.baselib.share.core.error;

public class ShareConfigException extends ShareException {

    public ShareConfigException(String detailMessage) {
        super(detailMessage);
        setCode(CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_NOT_CONFIG);
    }
}
