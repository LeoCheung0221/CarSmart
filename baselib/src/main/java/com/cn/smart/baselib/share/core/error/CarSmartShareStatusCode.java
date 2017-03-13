
package com.cn.smart.baselib.share.core.error;

public class CarSmartShareStatusCode {

    public static final int ST_CODE_SUCCESSED = 200;
    public static final int ST_CODE_ERROR_CANCEL = 201;
    public static final int ST_CODE_ERROR = 202;

    public static final int ST_CODE_SHARE_ERROR_NOT_CONFIG = -233;//没有配置appkey, appId
    public static final int ST_CODE_SHARE_ERROR_NOT_INSTALL = -234;//第三方软件未安装
    public static final int ST_CODE_SHARE_ERROR_PARAM_INVALID = -235;//ShareParam参数不正确
    public static final int ST_CODE_SHARE_ERROR_EXCEPTION = -236;//异常
    public static final int ST_CODE_SHARE_ERROR_UNEXPLAINED = -237;
    public static final int ST_CODE_SHARE_ERROR_SHARE_FAILED = -238;
    public static final int ST_CODE_SHARE_ERROR_AUTH_FAILED = -239;//认证失败
    public static final int ST_CODE_SHARE_ERROR_CONTEXT_TYPE = -240;//上下文类型和需求不负
    public static final int ST_CODE_SHARE_ERROR_PARAM_UNSUPPORTED = -241;//上下文类型和需求不负
    public static final int ST_CODE_SHARE_ERROR_IMAGE = -242;//图片处理失败

}