package com.cn.smart.baselib.uiframework.crouton;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.LruCache;

/**
 * author：leo on 2016/11/23 14:16
 * email： leocheung4ever@gmail.com
 * description: 字体间距
 * what & why is modified:
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class TypefaceSpan extends MetricAffectingSpan {

    /**
     * An <code>LruCache</code> for previously loaded typefaces.
     */
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(5);

    private Typeface mTypeface;

    /**
     * Load the {@link Typeface} and apply to a spannable.
     */
    public TypefaceSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext()
                    .getAssets(), String.format("%s", typefaceName));

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);
    }
}
