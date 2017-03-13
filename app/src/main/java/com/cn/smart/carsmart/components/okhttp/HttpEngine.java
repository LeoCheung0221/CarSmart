package com.cn.smart.carsmart.components.okhttp;

import com.cn.smart.carsmart.Core;
import com.cn.smart.carsmart.config.AppCookieManager;
import com.cn.smart.carsmart.components.okhttp.interceptor.CacheControlInterceptor;
import com.cn.smart.carsmart.components.okhttp.interceptor.HeaderInterceptor;
import com.cn.smart.carsmart.components.okhttp.interceptor.QueryParamsInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author：leo on 2017/2/20 11:44
 * email： leocheung4ever@gmail.com
 * description: 对外Http封装类
 * what & why is modified:
 */

public class HttpEngine {

    private static OkHttpClient client;
    private static ApiCarSmart httpService;
    private static Retrofit retrofit;

    private static AppCookieManager cookieManager = new AppCookieManager();

    /**
     * retrofit的底层利用反射的方式 获取所有的api接口
     */
    public static ApiCarSmart getHttpService() {
        if (httpService == null) {
            httpService = getRetrofit().create(ApiCarSmart.class);
        }
        return httpService;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (HttpEngine.class) {
                if (retrofit == null) {
                    HttpLoggingInterceptor addHttpLoggingInterceptor = new HttpLoggingInterceptor(); //添加log拦截器 打印所有响应拦截日志
                    addHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); //设置请求过滤的层级, body basic headers

                    QueryParamsInterceptor addQueryParamsInterceptor = new QueryParamsInterceptor(); //添加设置公共参数拦截器
                    HeaderInterceptor addHeaderInterceptor = new HeaderInterceptor(); //添加设置头部拦截器
                    CacheControlInterceptor addCacheInterceptor = new CacheControlInterceptor(); //添加设置头部拦截器
                    ChuckInterceptor addChuckInterceptor = new ChuckInterceptor(Core.getInstance()); //添加请求响应结果拦截器

                    File cacheFile = new File(Core.getInstance().getCacheDir(), "cache"); //设置 请求的缓存大小和位置
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50MB 缓存的大小

                    client = new OkHttpClient
                            .Builder()
                            .addInterceptor(addQueryParamsInterceptor) //参数添加
                            .addInterceptor(addHeaderInterceptor) //token过滤
                            .addInterceptor(addCacheInterceptor) //添加缓存拦截器
                            .addInterceptor(addHttpLoggingInterceptor) //响应请求日志拦截 控制台打印
                            .addInterceptor(addChuckInterceptor) //响应请求输出拦截  手机观测
                            .cache(cache) //添加缓存
                            .cookieJar(cookieManager)
                            .connectTimeout(60L, TimeUnit.SECONDS)
                            .readTimeout(60L, TimeUnit.SECONDS)
                            .writeTimeout(60L, TimeUnit.SECONDS)
                            .build();

                    Gson gson = new GsonBuilder()
                            .create();

                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(ApiCarSmart.BASE_URL) //手动配置
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
