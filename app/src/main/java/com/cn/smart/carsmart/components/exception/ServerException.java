package com.cn.smart.carsmart.components.exception;

/**
 * author：leo on 2017/2/28 10:37
 * email： leocheung4ever@gmail.com
 * description: server exception class
 * what & why is modified:
 */

public class ServerException extends RuntimeException {

    public int code;
    public String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
