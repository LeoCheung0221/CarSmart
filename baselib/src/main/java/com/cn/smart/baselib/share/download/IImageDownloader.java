package com.cn.smart.baselib.share.download;

import android.content.Context;

import com.cn.smart.baselib.share.core.error.ShareException;

/**
 * author：leo on 2017/1/6 10:08
 * email： leocheung4ever@gmail.com
 * description: 图片下载对外提供行为接口
 * what & why is modified:
 */

public interface IImageDownloader {

    /**
     * download image
     *
     * @param context           上下文环境
     * @param imageUrl          下载图片url
     * @param targetFileDirPath 目标文件
     * @param listener
     * @throws ShareException
     */
    void download(Context context, String imageUrl, String targetFileDirPath, OnImageDownloadListener listener) throws ShareException;

    /**
     * picture download listener
     */
    interface OnImageDownloadListener {

        void onStart();

        void onSuccess(String filePath);

        void onFailed(String url);
    }
}
