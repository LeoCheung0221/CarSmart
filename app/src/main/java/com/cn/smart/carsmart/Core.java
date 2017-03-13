package com.cn.smart.carsmart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.cn.smart.baselib.router.Router;
import com.cn.smart.baselib.router.module.RouteCreator;
import com.cn.smart.baselib.router.module.RouteMap;
import com.cn.smart.carsmart.injector.component.ApplicationComponent;
import com.cn.smart.carsmart.injector.component.DaggerApplicationComponent;
import com.cn.smart.carsmart.injector.module.ApplicationModule;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * author：leo on 2017/2/7 09:35
 * email： leocheung4ever@gmail.com
 * description: 应用程序入口
 * what & why is modified:
 */

public class Core extends MultiDexApplication {

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;
    private static final long MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;

    private ApplicationComponent mApplicationComponent;
    @SuppressLint("StaticFieldLeak")
    private static ApplicationModule mApplicationModule;

    @Inject
    OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void init() {
        //初始化组件
        initComponent();
        //初始化路由
//        initRouter();
        //初始化文件下载器
//        initFileDownloader();
        //初始化Fresco配置 可以减轻Core初始化任务 将其放在需要的入口进行初始化
//        initFrescoConfig();
        //初始化OkHttpClient及拦截器
        initOkHttp();
        //初始化LeakCanary
        initLeakCanary();
    }

    private void initComponent() {
        mApplicationModule = new ApplicationModule(this);
        mApplicationComponent =
                DaggerApplicationComponent.builder().applicationModule(mApplicationModule).build();
        mApplicationComponent.inject(this);
    }

    private void initRouter() {
        Router.addRouteCreator(new RouterRule());
    }

    private void initFileDownloader() {
        FileDownloader.init(this, new FileDownloadHelper.OkHttpClientCustomMaker() {
            @Override
            public OkHttpClient customMake() {
                return mOkHttpClient;
            }
        });
    }

    private void initFrescoConfig() {
        final MemoryCacheParams bitmapCacheParams =
                new MemoryCacheParams(MAX_MEMORY_CACHE_SIZE, // 缓存中元素最大数
                        Integer.MAX_VALUE, // 最大缓存条目
                        MAX_MEMORY_CACHE_SIZE, // 驱逐队列里元素最大数
                        Integer.MAX_VALUE, //驱逐队列中长度最大值
                        Integer.MAX_VALUE);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this, mOkHttpClient)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapMemoryCacheParamsSupplier(new Supplier<MemoryCacheParams>() {
                    @Override
                    public MemoryCacheParams get() {
                        return bitmapCacheParams;
                    }
                })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(this).setMaxCacheSize(MAX_DISK_CACHE_SIZE).build()
                )
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
    }

    private void initOkHttp() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ChuckInterceptor(getApplicationContext()))
                .build();
    }

    private void initLeakCanary() {
        LeakCanary.install(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getInstance() {
        return mApplicationModule.provideApplicationContext();
    }

    private class RouterRule implements RouteCreator {

        @Override
        public Map<String, RouteMap> createRouteRules() {
            Map<String, RouteMap> rule = new HashMap<>();
            rule.put("CarSmart://login", new RouteMap("com.cn.smart.carsmart.ui.login.LoginActivity_Backup"));
            return rule;
        }
    }
}
