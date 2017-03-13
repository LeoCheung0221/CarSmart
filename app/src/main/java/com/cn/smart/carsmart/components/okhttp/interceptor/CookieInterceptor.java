package com.cn.smart.carsmart.components.okhttp.interceptor;

import android.text.TextUtils;

import com.cn.smart.carsmart.config.Constants;
import com.cn.smart.carsmart.components.storage.UserStorage;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：leo on 2017/2/7 10:49
 * email： leocheung4ever@gmail.com
 * description: 自定义Cookie拦截器
 * what & why is modified:
 */

public class CookieInterceptor implements Interceptor {

    private UserStorage mUserStorage;

    public CookieInterceptor(UserStorage userStorage) {
        this.mUserStorage = userStorage;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        if (!TextUtils.isEmpty(mUserStorage.getCookie())
                && !original.url().toString().contains("loginUsernameEmail") //todo 原始请求中是否包含字符换再作判断
                ) {
            Request request = original.newBuilder()
                    .addHeader("Cookie", "u=" + URLEncoder.encode(mUserStorage.getCookie()) + ":")
                    .build();

            return chain.proceed(request);
        } else {
            for (String header : chain.proceed(original).headers("Set-Cookie")) {
                if (header.startsWith("u=")) {
                    String cookie = header.split(":")[0].substring(2);
                    if (!TextUtils.isEmpty(cookie)) {
                        Constants.Cookie = cookie;
                    }
                }
            }
        }
        return chain.proceed(original);
    }
}
