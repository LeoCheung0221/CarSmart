
package com.cn.smart.baselib.share.core.error;

public class InvalidParamException extends ShareException{

    public InvalidParamException(String detailMessage) {
        super(detailMessage);
        setCode(CarSmartShareStatusCode.ST_CODE_SHARE_ERROR_PARAM_INVALID);
    }
}
