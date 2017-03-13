package com.cn.smart.baselib.share.core.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.share.core.CarSmartShareConfiguration;
import com.cn.smart.baselib.share.core.error.ShareException;
import com.cn.smart.baselib.share.core.shareparam.BaseShareParam;
import com.cn.smart.baselib.share.core.shareparam.ShareImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamAudio;
import com.cn.smart.baselib.share.core.shareparam.ShareParamImage;
import com.cn.smart.baselib.share.core.shareparam.ShareParamText;
import com.cn.smart.baselib.share.core.shareparam.ShareParamVideo;
import com.cn.smart.baselib.share.core.shareparam.ShareParamWebPage;
import com.cn.smart.baselib.share.download.IImageDownloader;
import com.cn.smart.baselib.share.util.BitmapUtil;
import com.cn.smart.baselib.share.util.ShareFileUtil;

import java.io.File;
import java.io.IOException;

public class ShareImageHelper {
    private static final int THUMB_RESOLUTION_SIZE = 150;
    private static final int THUMB_MAX_SIZE = 30 * 1024;
    private static final int BITMAP_SAVE_THRESHOLD = 32 * 1024;

    private Context mContext;
    private CarSmartShareConfiguration mShareConfiguration;
    private Callback mCallback;

    public ShareImageHelper(Context context, CarSmartShareConfiguration shareConfiguration, Callback callback) {
        mContext = context.getApplicationContext();
        mShareConfiguration = shareConfiguration;
        mCallback = callback;
    }

    /**
     * 如果Bitmap/Res体积太大，保存到本地
     */
    public ShareImage saveBitmapToExternalIfNeed(BaseShareParam params) {
        return saveBitmapToExternalIfNeed(getShareImage(params));
    }

    public ShareImage saveBitmapToExternalIfNeed(ShareImage image) {
        if (image == null) {
            return null;
        } else if (image.isBitmapImage()) {
            if (image.getBitmap().getByteCount() > BITMAP_SAVE_THRESHOLD) {
                if (checkImageCachePath()) {
                    File file = BitmapUtil.saveBitmapToExternal(image.getBitmap(), mShareConfiguration.getImageCachePath(mContext));
                    if (file != null && file.exists()) {
                        image.setLocalFile(file);
                    }
                }
            }
        } else if (image.isResImage()) {
            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), image.getResId());
            if (bmp.getByteCount() > BITMAP_SAVE_THRESHOLD) {
                if (checkImageCachePath()) {
                    File file = BitmapUtil.saveBitmapToExternal(bmp, mShareConfiguration.getImageCachePath(mContext));
                    if (file != null && file.exists()) {
                        image.setLocalFile(file);
                        bmp.recycle();
                    }
                }
            }
        }
        return image;
    }

    public void copyImageToCacheFileDirIfNeed(BaseShareParam params) {
        copyImageToCacheFileDirIfNeed(getShareImage(params));
    }

    public void copyImageToCacheFileDirIfNeed(ShareImage shareImage) {
        if (shareImage == null) {
            return;
        }

        File localFile = shareImage.getLocalFile();
        if (localFile == null || !localFile.exists()) {
            return;
        }

        if (!checkImageCachePath()) {
            return;
        }

        String localFilePath = localFile.getAbsolutePath();
        if (!localFilePath.startsWith(mContext.getCacheDir().getParentFile().getAbsolutePath())
                && localFilePath.startsWith(mShareConfiguration.getImageCachePath(mContext))) {
            return;
        }

        File targetFile = copyFile(localFile, mShareConfiguration.getImageCachePath(mContext));
        if (targetFile != null && targetFile.exists()) {
            shareImage.setLocalFile(targetFile);
        }
    }

    private File copyFile(File srcFile, String targetCacheDirPath) {
        if (srcFile == null || !srcFile.exists()) {
            return null;
        }

        File targetFileDir = new File(targetCacheDirPath);
        File targetFile = new File(targetFileDir, srcFile.getName());

        if (!targetFileDir.exists() && !targetFileDir.mkdirs()) {
            return null;
        }

        try {
            ShareFileUtil.copyFile(srcFile, targetFile);
            return targetFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 缩略图。32kb限制。
     * 注意：在工作线程调用。
     */
    public byte[] buildThumbData(final ShareImage image) {
        return buildThumbData(image, THUMB_MAX_SIZE, THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE, false);
    }

    public byte[] buildThumbData(final ShareImage image, int maxSize, int widthMax, int heightMax, boolean isFixSize) {
        if (image == null) {
            return new byte[0];
        }

        boolean isRecycleSrcBitmap = true;
        Bitmap bmp = null;

        if (image.isNetImage()) {
            if (mCallback != null) {
                mCallback.onProgress(R.string.carsmart_share_sdk_progress_compress_image);
            }
            bmp = BitmapUtil.decodeUrl(image.getNetImageUrl());
        } else if (image.isLocalImage()) {
            bmp = BitmapUtil.decodeFile(image.getLocalPath(), THUMB_RESOLUTION_SIZE, THUMB_RESOLUTION_SIZE);
        } else if (image.isResImage()) {
            bmp = BitmapFactory.decodeResource(mContext.getResources(), image.getResId());
        } else if (image.isBitmapImage()) {
            if (mCallback != null) {
                mCallback.onProgress(R.string.carsmart_share_sdk_progress_compress_image);
            }
            isRecycleSrcBitmap = false;
            bmp = image.getBitmap();
        }

        if (bmp != null && !bmp.isRecycled()) {
            if (!isFixSize) {
                int bmpWidth = bmp.getWidth();
                int bmpHeight = bmp.getHeight();
                double scale = BitmapUtil.getScale(widthMax, heightMax, bmpWidth, bmpHeight);
                widthMax = (int) (bmpWidth / scale);
                heightMax = (int) (bmpHeight / scale);
            }

            final Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, widthMax, heightMax, true);
            if (isRecycleSrcBitmap && thumbBmp != bmp) {
                bmp.recycle();
            }
            byte[] tempArr = BitmapUtil.bmpToByteArray(thumbBmp, maxSize, true);
            return tempArr == null ? new byte[0] : tempArr;
        }

        return new byte[0];
    }

    public void downloadImageIfNeed(final BaseShareParam params, final Runnable task) throws ShareException {
        downloadImageIfNeed(getShareImage(params), task);
    }

    /**
     * @param image 分享图片
     * @param task  图片下载完成后待执行的任务
     * @throws ShareException
     */
    public void downloadImageIfNeed(final ShareImage image, final Runnable task) throws ShareException {
        if (image == null || !image.isNetImage()) {
            task.run();
        } else {
            if (!checkImageCachePath()) {
                if (mCallback != null) {
                    mCallback.onImageDownloadFailed();
                }
                return;
            }

            mShareConfiguration.getImageDownloader().download(mContext, image.getNetImageUrl(), mShareConfiguration.getImageCachePath(mContext),
                    new IImageDownloader.OnImageDownloadListener() {
                        @Override
                        public void onStart() {
                            if (mCallback != null) {
                                mCallback.onProgress(R.string.carsmart_share_sdk_progress_compress_image);
                            }
                        }

                        @Override
                        public void onSuccess(String filePath) {
                            image.setLocalFile(new File(filePath));
                            copyImageToCacheFileDirIfNeed(image);
                            task.run();
                        }

                        @Override
                        public void onFailed(String url) {
                            if (mCallback != null) {
                                mCallback.onProgress(R.string.carsmart_share_sdk_compress_image_failed);
                                mCallback.onImageDownloadFailed();
                            }
                        }
                    });
        }
    }

    private boolean checkImageCachePath() {
        if (TextUtils.isEmpty(mShareConfiguration.getImageCachePath(mContext))) {
            Toast.makeText(mContext.getApplicationContext(), "存储设备不可用", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    protected ShareImage getShareImage(BaseShareParam params) {
        ShareImage image = null;
        if (params == null || params instanceof ShareParamText) {
            return null;
        } else if (params instanceof ShareParamImage) {
            image = ((ShareParamImage) params).getImage();
        } else if (params instanceof ShareParamWebPage) {
            image = ((ShareParamWebPage) params).getThumb();
        } else if (params instanceof ShareParamAudio) {
            image = ((ShareParamAudio) params).getThumb();
        } else if (params instanceof ShareParamVideo) {
            image = ((ShareParamVideo) params).getThumb();
        }
        return image;
    }

    public interface Callback {
        void onProgress(int msgId);

        void onImageDownloadFailed();
    }

}
