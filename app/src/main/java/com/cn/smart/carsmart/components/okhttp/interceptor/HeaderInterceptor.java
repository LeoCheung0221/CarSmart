package com.cn.smart.carsmart.components.okhttp.interceptor;

import com.cn.smart.carsmart.util.SpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：leo on 2017/2/7 10:49
 * email： leocheung4ever@gmail.com
 * description: 自定义头部信息拦截器
 * what & why is modified:
 */

public class HeaderInterceptor implements Interceptor {

    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
                //Provide your custom header here
                .header("token", (String) SpUtils.get("token", ""))
                .method(originalRequest.method(), originalRequest.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
