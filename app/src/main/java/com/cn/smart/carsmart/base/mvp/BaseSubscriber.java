package com.cn.smart.carsmart.base.mvp;

import com.cn.smart.baselib.logcat.ZLog;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * author：leo on 2017/2/8 12:46
 * email： leocheung4ever@gmail.com
 * description: 订阅者基类
 * what & why is modified:
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    protected String msg;
    protected int httpCode;

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            /**
             * DEBUG
             */
            try {
                ZLog.d(httpException.response().errorBody().string());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            httpCode = httpException.code();
            msg = httpException.getMessage();
            if (httpCode == 504) {
                msg = "网络不给力！";
            } else if (httpCode == 502 || httpCode == 404 || httpCode == 500) {
                msg = "服务器异常，请稍后再试！";
            }
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时，请检查您的网络状态";
        } else if (e instanceof ConnectException) {
            msg = "网络中断，请检查您的网络状态";
        } else {
            msg = e.getMessage();
        }
        ZLog.d(msg);
    }
}
