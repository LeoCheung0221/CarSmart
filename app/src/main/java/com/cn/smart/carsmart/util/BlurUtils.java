package com.cn.smart.carsmart.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cn.smart.baselib.uiframework.blur.StackBlurManager;
import com.cn.smart.baselib.util.BitmapUtil;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：leo on 2016/11/25 11:34
 * email： leocheung4ever@gmail.com
 * description: 针对StackBlurManager封装的工具类
 * what & why is modified:
 */

public class BlurUtils {

    private static StackBlurManager stackBlurManager;

    /**
     * 模糊网络图片 - eg. avatar, bg etc.
     */
    public static void setBlurWithUrl(final Activity context, final ImageView imageView, final String url) {
        final Bitmap[] mBitmap = {null};
        Observable.just(url)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        stackBlurManager = new StackBlurManager(mBitmap[0]);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(stackBlurManager.process(15));
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        mBitmap[0] = BitmapUtil.getBitmapFromUrl(url);
                    }
                });
    }

    /**
     * 设置本地图片模糊化
     */
    public static void setBlurWithLocal(Context context, ImageView imageView, String name) {
        Bitmap mBitmap = getBitmapFromAsset(context, name);
        stackBlurManager = new StackBlurManager(mBitmap);
        imageView.setImageBitmap(stackBlurManager.process(15));
    }

    /**
     * 设置本地图片模糊化
     */
    public static void setBlurWithLocal(Context context, ViewGroup viewGroup, int drawableId) {
        Bitmap mBitmap = getBitmapFromDrawable(context, drawableId);
        stackBlurManager = new StackBlurManager(mBitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewGroup.setBackground(new BitmapDrawable(context.getResources(), stackBlurManager.process(15)));
        }
    }

    /**
     * 模糊本地图片 asset
     */
    private static Bitmap getBitmapFromAsset(Context context, String name) {
        AssetManager assetManager = context.getAssets();
        InputStream is;
        Bitmap bitmap;
        try {
            is = assetManager.open(name);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        }
        return bitmap;
    }

    /**
     * 模糊本地图片 drawable
     */
    private static Bitmap getBitmapFromDrawable(Context context, int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }
}
