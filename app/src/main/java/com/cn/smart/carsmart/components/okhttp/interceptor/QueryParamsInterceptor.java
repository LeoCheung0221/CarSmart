package com.cn.smart.carsmart.components.okhttp.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：leo on 2017/2/7 10:49
 * email： leocheung4ever@gmail.com
 * description: 自定义公共参数拦截器
 * what & why is modified:
 */

public class QueryParamsInterceptor implements Interceptor {

    public QueryParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request;
        HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                //Provide your custom parameter here
                .addQueryParameter("phoneSystem", "")
                .addQueryParameter("phoneModel", "")
                .build();
        request = originalRequest.newBuilder().url(modifiedUrl).build();
        return chain.proceed(request);
    }
}
