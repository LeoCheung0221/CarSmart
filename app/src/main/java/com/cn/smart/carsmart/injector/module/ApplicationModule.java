package com.cn.smart.carsmart.injector.module;

import android.content.Context;

import com.cn.smart.carsmart.components.okhttp.interceptor.CookieInterceptor;
import com.cn.smart.carsmart.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author：leo on 2017/2/7 09:58
 * email： leocheung4ever@gmail.com
 * description: 应用程序模块 -> 提供有关application对外注入的一些方法集合
 * what & why is modified:
 */

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    @Provides
    @Singleton
    Bus provideBusEvent() {
        return new Bus();
    }

    @Provides
    @Singleton
    @Named("api")
    OkHttpClient provideApiOkHttpClient(CookieInterceptor cookieInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        builder.addInterceptor(cookieInterceptor);
        return builder.build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(@Named("api") OkHttpClient mOkHttpClient) {
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        builder.interceptors().clear();
        return builder.build();
    }

    @Provides
    @Singleton
    CookieInterceptor provideCookieInterceptor(UserStorage userStorage) {
        return new CookieInterceptor(userStorage);
    }

    @Provides
    @Singleton
    UserStorage provideUserStorage(Context context) {
        return new UserStorage(context);
    }
}
