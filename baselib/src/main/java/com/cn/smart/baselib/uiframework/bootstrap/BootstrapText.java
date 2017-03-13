package com.cn.smart.baselib.uiframework.bootstrap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;

import com.cn.smart.baselib.uiframework.bootstrap.font.AwesomeTypefaceSpan;
import com.cn.smart.baselib.uiframework.bootstrap.font.FontAwesome;
import com.cn.smart.baselib.uiframework.bootstrap.font.IconSet;
import com.cn.smart.baselib.uiframework.bootstrap.font.MaterialIcons;
import com.cn.smart.baselib.uiframework.bootstrap.font.Typicon;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * author：leo on 2016/12/6 14:48
 * email： leocheung4ever@gmail.com
 * description: Bootstrap 文本提供一个构建者类, 它允许方便的构造SpannableStrings(复合文本)
 * 目前常规的文本, FontAwesome 图标, 以及Typicons的添加
 * what & why is modified:
 */

public class BootstrapText extends SpannableString implements Serializable {

    private BootstrapText(CharSequence source) {
        super(source);
    }

    /**
     * 这个类被用来构造BootstrapText实例的. 按顺序添加文本附加到自身
     */
    public static class Builder {

        private final StringBuilder sb;
        private final Context context;
        private final boolean editMode;
        private final Map<Integer, IconSet> fontIndicesMap;

        @SuppressLint("UseSparseArrays")
        public Builder(Context context) {
            fontIndicesMap = new HashMap<>();
            sb = new StringBuilder();
            this.context = context.getApplicationContext();
            this.editMode = false;
        }

        @SuppressLint("UseSparseArrays")
        public Builder(Context context, boolean editMode) {
            fontIndicesMap = new HashMap<>();
            sb = new StringBuilder();
            this.context = context.getApplicationContext();
            this.editMode = editMode;
        }

        /**
         * 附加常规文本到BootstrapText构造器下
         *
         * @param text 常规文本
         * @return 更新后的构建实例
         */
        public Builder addText(CharSequence text) {
            sb.append(text);
            return this;
        }

        /**
         * 附加FontAwesomeIcon到BootstrapText构造器下
         *
         * @return 更新后的构建实例
         */
        public Builder addFontAwesomeIcon(@FontAwesome.Icon CharSequence iconCode) {
            IconSet iconSet = TypefaceProvider.retrieveRegisteredIconSet(FontAwesome.FONT_PATH, editMode);
            sb.append(iconSet.unicodeForKey(iconCode.toString().replaceAll("\\-", "_")));
            fontIndicesMap.put(sb.length(), iconSet);
            return this;
        }

        /**
         * 附加Typicon到BootstrapText构造器下
         *
         * @return 更新后的构建实例
         */
        public Builder addTypicon(@Typicon.Icon CharSequence iconCode) {
            IconSet iconSet = TypefaceProvider.retrieveRegisteredIconSet(Typicon.FONT_PATH, editMode);
            sb.append(iconSet.unicodeForKey(iconCode.toString().replaceAll("\\-", "_")));
            fontIndicesMap.put(sb.length(), iconSet);
            return this;
        }

        /**
         * 附加MaterialIcons到BootstrapText构造器下
         *
         * @return 更新后的构建实例
         */
        public Builder addMaterialIcon(CharSequence iconCode) {
            IconSet iconSet = TypefaceProvider.retrieveRegisteredIconSet(MaterialIcons.FONT_PATH, editMode);
            sb.append(iconSet.unicodeForKey(iconCode.toString().replaceAll("\\-", "_")));
            fontIndicesMap.put(sb.length(), iconSet);
            return this;
        }

        /**
         * 附加字体图标到BootstrapText构造器下
         *
         * @param iconSet a font icon 一个字体图标
         * @return 更新后的构建实例
         */
        public Builder addIcon(CharSequence iconCode, IconSet iconSet) {
            sb.append(iconSet.unicodeForKey(iconCode.toString().replaceAll("\\-", "_")));
            fontIndicesMap.put(sb.length(), iconSet);
            return this;
        }

        /**
         * @return 一个新的通过构建器参数构造的BootstrapText实例
         */
        public BootstrapText build() {
            BootstrapText bootstrapText = new BootstrapText(sb.toString());

            for (Map.Entry<Integer, IconSet> entry : fontIndicesMap.entrySet()) {
                int index = entry.getKey();

                AwesomeTypefaceSpan span = new AwesomeTypefaceSpan(context, entry.getValue());
                bootstrapText.setSpan(span, index - 1, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            return bootstrapText;
        }
    }
}
