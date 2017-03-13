package com.cn.smart.baselib.uiframework.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.RSRuntimeException;
import android.util.Log;

import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author：leo on 2016/11/25 10:16
 * email： leocheung4ever@gmail.com
 * description: 模糊图片处理辅助类
 * what & why is modified:
 */

public class StackBlurManager {

    static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);

    private final Bitmap mBitmap;
    private final JavaBlurProcess blurProcess;
    private Bitmap result;

    /**
     * Constructor method (basic initialization and construction of the pixel array)
     *
     * @param image The image that will be analysed
     */
    public StackBlurManager(Bitmap bitmap) {
        mBitmap = bitmap;
        blurProcess = new JavaBlurProcess();
    }

    public Bitmap process(int radius) {
        result = blurProcess.blur(mBitmap, radius);
        return result;
    }

    /**
     * Returns the blurred image as a bitmap
     *
     * @return blurred image
     */
    public Bitmap returnBlurredImage() {
        return mBitmap;
    }

    /**
     * Save the image into the file system
     *
     * @param path The path where to save the image
     */
    public void saveIntoFile(String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            result.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the original image as a bitmap
     *
     * @return the original bitmap image
     */
    public Bitmap getImage() {
        return this.mBitmap;
    }

}
