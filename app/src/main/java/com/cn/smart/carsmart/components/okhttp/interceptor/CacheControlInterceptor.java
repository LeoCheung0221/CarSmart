package com.cn.smart.carsmart.components.okhttp.interceptor;

import com.cn.smart.carsmart.Core;
import com.cn.smart.carsmart.util.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：leo on 2017/2/7 10:49
 * email： leocheung4ever@gmail.com
 * description: 自定义缓存设置拦截器
 * what & why is modified:
 */

public class CacheControlInterceptor implements Interceptor {

    public CacheControlInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtils.isNetworkAvailable(Core.getInstance())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (NetworkUtils.isNetworkAvailable(Core.getInstance())) {
            int maxAge = 0;
            //有网络时  设置缓存超时时间为0个小时 即不读取缓存数据  只对get有用  post没有缓存
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Retrofit") //清除头部信息 因为服务器如果不支持的话  会返回一些干扰信息  不清除下面无法生效
                    .build();
        } else {
            //无网络时  设置超时4周 只对get有用 post没有缓冲
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" +
                            maxStale)
                    .removeHeader("nyn")
                    .build();
        }
        return response;
    }
}
