package com.cn.smart.carsmart.components.exception;

/**
 * author：leo on 2017/2/28 10:37
 * email： leocheung4ever@gmail.com
 * description: error type
 * what & why is modified:
 */

public interface ErrorType {

    /**
     * 请求成功
     */
    int SUCCESS = 1;

    /**
     * 未知错误
     */
    int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    int NETWORK_ERROR = 1002;

    /**
     * 解析对象为空
     */
    int EMPTY_BEAN = 1004;

    /**
     * HTTP错误
     */
    int HTTP_ERROR = 1003;
}
