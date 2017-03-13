package com.cn.smart.baselib.share.core;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.share.core.handler.sina.SinaShareHandler;
import com.cn.smart.baselib.share.download.DefaultImageDownloader;
import com.cn.smart.baselib.share.download.IImageDownloader;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author：leo on 2017/1/6 10:01
 * email： leocheung4ever@gmail.com
 * description: 初始化分享类 初始化配置参数
 * what & why is modified:
 */

public class CarSmartShareConfiguration implements Parcelable {

    String mImageCachePath; //图片缓存路径
    final int mDefaultShareImage; //默认分享的图片

    private String mSinaRedirectUrl; // sina 重定向url
    private String mSinaScope; // sina's scope

    final IImageDownloader mImageDownloader;
    final Executor mTaskExecutor;

    private CarSmartShareConfiguration(Builder builder) {
        mImageCachePath = builder.mImageCachePath;
        mDefaultShareImage = builder.mDefaultShareImage;
        mSinaRedirectUrl = builder.mSinaRedirectUrl;
        mSinaScope = builder.mSinaScope;
        mImageDownloader = builder.mImageLoader;
        mTaskExecutor = Executors.newCachedThreadPool();
    }

    public String getImageCachePath(Context context) {
        if (TextUtils.isEmpty(mImageCachePath)) {
            mImageCachePath = Builder.getDefaultImageCacheFile(context.getApplicationContext());
        }
        return mImageCachePath;
    }

    public int getDefaultShareImage() {
        return mDefaultShareImage;
    }

    public String getSinaRedirectUrl() {
        return mSinaRedirectUrl;
    }

    public String getSinaScope() {
        return mSinaScope;
    }

    public IImageDownloader getImageDownloader() {
        return mImageDownloader;
    }

    public Executor getTaskExecutor() {
        return mTaskExecutor;
    }

    public static class Builder {
        public static final String IMAGE_CACHE_FILE_NAME = "shareImage";

        private Context mContext;
        private String mImageCachePath;
        private int mDefaultShareImage = -1;

        private String mSinaRedirectUrl;
        private String mSinaScope;

        private IImageDownloader mImageLoader;

        public Builder(Context context) {
            mContext = context.getApplicationContext();
        }

        public Builder imageCachePath(String path) {
            mImageCachePath = path;
            return this;
        }

        public Builder defaultShareImage(int defaultImage) {
            mDefaultShareImage = defaultImage;
            return this;
        }

        public Builder sina(String redirectUrl, String mScope) {
            mSinaRedirectUrl = redirectUrl;
            mSinaScope = mScope;
            return this;
        }

        public Builder imageDownloader(IImageDownloader loader) {
            mImageLoader = loader;
            return this;
        }

        public CarSmartShareConfiguration build() {
            checkFields();
            return new CarSmartShareConfiguration(this);
        }

        private void checkFields() {
            File imageCacheFile = null;
            if (!TextUtils.isEmpty(mImageCachePath)) {
                imageCacheFile = new File(mImageCachePath);
                if (!imageCacheFile.isDirectory()) {
                    imageCacheFile = null;
                } else if (!imageCacheFile.exists() && !imageCacheFile.mkdirs()) {
                    imageCacheFile = null;
                }
            }
            if (imageCacheFile == null) {
                mImageCachePath = getDefaultImageCacheFile(mContext);
            }

            if (mImageLoader == null) {
                mImageLoader = new DefaultImageDownloader();
            }

            if (mDefaultShareImage == -1) {
                mDefaultShareImage = R.drawable.default_share_image;
            }

            if (TextUtils.isEmpty(mSinaRedirectUrl)) {
                mSinaRedirectUrl = SinaShareHandler.DEFAULT_REDIRECT_URL;
            }

            if (TextUtils.isEmpty(mSinaScope)) {
                mSinaScope = SinaShareHandler.DEFAULT_SCOPE;
            }
        }

        private static String getDefaultImageCacheFile(Context context) {
            String imageCachePath = null;
            File extCacheFile = context.getExternalCacheDir();
            if (extCacheFile == null) {
                extCacheFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
            if (extCacheFile != null) {
                imageCachePath = extCacheFile.getAbsolutePath() + File.separator + IMAGE_CACHE_FILE_NAME + File.separator;
                File imageCacheFile = new File(imageCachePath);
                imageCacheFile.mkdirs();
            }
            return imageCachePath;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageCachePath);
        dest.writeInt(this.mDefaultShareImage);
        dest.writeString(this.mSinaRedirectUrl);
        dest.writeString(this.mSinaScope);
    }

    protected CarSmartShareConfiguration(Parcel in) {
        this.mImageCachePath = in.readString();
        this.mDefaultShareImage = in.readInt();
        this.mSinaRedirectUrl = in.readString();
        this.mSinaScope = in.readString();
        this.mImageDownloader = new DefaultImageDownloader();
        this.mTaskExecutor = Executors.newCachedThreadPool();
    }

    public static final Parcelable.Creator<CarSmartShareConfiguration> CREATOR = new Parcelable.Creator<CarSmartShareConfiguration>() {
        public CarSmartShareConfiguration createFromParcel(Parcel source) {
            return new CarSmartShareConfiguration(source);
        }

        public CarSmartShareConfiguration[] newArray(int size) {
            return new CarSmartShareConfiguration[size];
        }
    };
}
