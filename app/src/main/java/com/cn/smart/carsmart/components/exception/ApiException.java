package com.cn.smart.carsmart.components.exception;

/**
 * author：leo on 2017/2/28 10:47
 * email： leocheung4ever@gmail.com
 * description: api exception class to handle error of parsing internet
 * what & why is modified:
 */

public class ApiException extends RuntimeException {

    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
