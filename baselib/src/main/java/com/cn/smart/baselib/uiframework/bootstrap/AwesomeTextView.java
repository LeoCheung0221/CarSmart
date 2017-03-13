package com.cn.smart.baselib.uiframework.bootstrap;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.uiframework.bootstrap.api.attributes.BootstrapBrand;
import com.cn.smart.baselib.uiframework.bootstrap.api.defaults.DefaultBootstrapBrand;
import com.cn.smart.baselib.uiframework.bootstrap.api.view.BootstrapBrandView;
import com.cn.smart.baselib.uiframework.bootstrap.api.view.BootstrapTextView;
import com.cn.smart.baselib.uiframework.bootstrap.font.FontAwesome;
import com.cn.smart.baselib.uiframework.bootstrap.font.IconSet;
import com.cn.smart.baselib.uiframework.bootstrap.font.MaterialIcons;
import com.cn.smart.baselib.uiframework.bootstrap.font.Typicon;

import java.io.Serializable;

/**
 * author：leo on 2016/12/6 14:40
 * email： leocheung4ever@gmail.com
 * description: 这个类继承了Android 默认的TextView, 来提供Bootstrap的接口行为
 * 文本颜色可以通过改变BootstrapBrand设置, 以及可扩展字体图标可以使用风格化的Bootstrap Text点缀普通文本,
 * what & why is modified:
 */

public class AwesomeTextView extends TextView implements BootstrapTextView, BootstrapBrandView {

    private static final String TAG = "com.cn.tufusi.formylove.widget.bootstrap.AwesomeTextView";

    private BootstrapText bootstrapText;
    private BootstrapBrand bootstrapBrand;

    public AwesomeTextView(Context context) {
        super(context);
        initialise(null);
    }

    public AwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(attrs);
    }

    public AwesomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(attrs);
    }

    @Override
    public void setBootstrapText(@Nullable BootstrapText bootstrapText) {
        this.bootstrapText = bootstrapText;
        updateBootstrapState();
    }

    @Nullable
    @Override
    public BootstrapText getBootstrapText() {
        return bootstrapText;
    }

    @Override
    public void setMarkdownText(@Nullable String text) {
        setBootstrapText(IconResolver.resolveMarkdown(getContext(), text, isInEditMode()));
    }

    @Override
    public void setBootstrapBrand(@NonNull BootstrapBrand bootstrapBrand) {
        this.bootstrapBrand = bootstrapBrand;
        updateBootstrapState();
    }

    @NonNull
    @Override
    public BootstrapBrand getBootstrapBrand() {
        return bootstrapBrand;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        bootstrapText = null;
    }

    private void initialise(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.AwesomeTextView);
        String markdownText;

        try {
            int typeOrdinal = ta.getInt(R.styleable.AwesomeTextView_bootstrapBrand, -1);
            int faIconOrdinal = ta.getInt(R.styleable.AwesomeTextView_fontAwesomeIcon, -1);
            int typiconOrdinal = ta.getInt(R.styleable.AwesomeTextView_typicon, -1);
            int materialIconOrdinal = ta.getInt(R.styleable.AwesomeTextView_materialIcon, -1);
            boolean clickable = ta.getBoolean(R.styleable.AwesomeTextView_android_clickable, true);

            this.bootstrapBrand = DefaultBootstrapBrand.fromAttributeValue(typeOrdinal);
            boolean editMode = isInEditMode();

            if (typiconOrdinal != -1) {
                final IconSet typicon = TypefaceProvider.retrieveRegisteredIconSet(Typicon.FONT_PATH, editMode);

                if (!editMode) {
                    setIcon(typicon.iconCodeForAttrIndex(typiconOrdinal), typicon);
                }
            }
            if (faIconOrdinal != -1) {
                final IconSet fontAwesome = TypefaceProvider.retrieveRegisteredIconSet(FontAwesome.FONT_PATH, editMode);

                if (!editMode) {
                    setIcon(fontAwesome.iconCodeForAttrIndex(faIconOrdinal), fontAwesome);
                }
            }
            if (materialIconOrdinal != -1) {
                final IconSet materialIcons = TypefaceProvider.retrieveRegisteredIconSet(MaterialIcons.FONT_PATH, editMode);

                if (!editMode) {
                    setIcon(materialIcons.iconCodeForAttrIndex(materialIconOrdinal), materialIcons);
                }
            }
            markdownText = ta.getString(R.styleable.AwesomeTextView_bootstrapText);
            setClickable(clickable); // allows view to reach android:state_pressed
        } finally {
            ta.recycle();
        }
//        setGravity(Gravity.CENTER_HORIZONTAL|Gravity.END);

        if (markdownText != null) {
            setMarkdownText(markdownText);
        }
        updateBootstrapState();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, super.onSaveInstanceState());
        bundle.putSerializable(BootstrapTextView.KEY, bootstrapText);
        bundle.putSerializable(BootstrapBrand.KEY, bootstrapBrand);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            Serializable text = bundle.getSerializable(BootstrapTextView.KEY);
            Serializable brand = bundle.getSerializable(BootstrapBrand.KEY);

            if (brand instanceof BootstrapBrand) {
                bootstrapBrand = (BootstrapBrand) brand;
            }
            if (text instanceof BootstrapText) {
                bootstrapText = (BootstrapText) text;
            }
            state = bundle.getParcelable(TAG);
        }
        super.onRestoreInstanceState(state);
        updateBootstrapState();
    }

    protected void updateBootstrapState() {
        if (bootstrapText != null) {
            setText(bootstrapText);
        }
        if (bootstrapBrand != null) {
            setTextColor(bootstrapBrand.defaultFill(getContext()));
        }
    }

    /**
     * Starts a Flashing Animation on the AwesomeTextView
     *
     * @param forever whether the animation should be infinite or play once
     * @param speed   how fast the item should flash
     */
    public void startFlashing(boolean forever, AnimationSpeed speed) {
        Animation fadeIn = new AlphaAnimation(0, 1);

        //set up extra variables
        fadeIn.setDuration(50);
        fadeIn.setRepeatMode(Animation.REVERSE);

        //default repeat count is 0, however if user wants, set it up to be infinite
        fadeIn.setRepeatCount(0);
        if (forever) {
            fadeIn.setRepeatCount(Animation.INFINITE);
        }

        fadeIn.setStartOffset(speed.getFlashDuration());
        startAnimation(fadeIn);
    }

    /**
     * Starts a rotating animation on the AwesomeTextView
     *
     * @param clockwise true for clockwise, false for anti clockwise spinning
     * @param speed     how fast the item should rotate
     */
    public void startRotate(boolean clockwise, AnimationSpeed speed) {
        Animation rotate;

        //set up the rotation animation
        if (clockwise) {
            rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotate = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }

        //set up some extra variables
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setStartOffset(0);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setDuration(speed.getRotateDuration());
        startAnimation(rotate);
    }

    /**
     * Sets the text to display a FontIcon, replacing whatever text is already present.
     * Used to set the text to display a FontAwesome Icon.
     *
     * @param iconSet - An implementation of FontIcon
     */
    public void setIcon(CharSequence iconCode, IconSet iconSet) {
        setBootstrapText(new BootstrapText.Builder(getContext(), isInEditMode()).addIcon(iconCode, iconSet).build());
    }

    /**
     * Sets the text to display a FontIcon, replacing whatever text is already present.
     * Used to set the text to display a FontAwesome Icon.
     *
     * @param iconCode the fontawesome icon code e.g. "fa_play"
     */
    public void setFontAwesomeIcon(@FontAwesome.Icon CharSequence iconCode) {
        setBootstrapText(new BootstrapText.Builder(getContext(), isInEditMode()).addFontAwesomeIcon(iconCode).build());
    }

    /**
     * Sets the text to display a MaterialIcon, replacing whatever text is already present.
     * Used to set the text to display a MaterialIcon Icon.
     *
     * @param iconCode the fontawesome icon code e.g. "md_share"
     */
    public void setMaterialIcon(@FontAwesome.Icon CharSequence iconCode) {
        setBootstrapText(new BootstrapText.Builder(getContext(), isInEditMode()).addMaterialIcon(iconCode).build());
    }

    /**
     * Sets the text to display a FontIcon, replacing whatever text is already present.
     * Used to set the text to display a Typicon.
     *
     * @param iconCode the typicon icon code e.g. "ty_adjust_brightness"
     */
    public void setTypicon(@Typicon.Icon CharSequence iconCode) {
        setBootstrapText(new BootstrapText.Builder(getContext(), isInEditMode()).addTypicon(iconCode).build());
    }

    public enum AnimationSpeed {
        FAST(500, 200),
        MEDIUM(1000, 500),
        SLOW(2000, 1000);

        private final long rotateDuration;
        private final long flashDuration;

        AnimationSpeed(long rotateDuration, long flashDuration) {
            this.rotateDuration = rotateDuration;
            this.flashDuration = flashDuration;
        }

        public long getRotateDuration() {
            return rotateDuration;
        }

        public long getFlashDuration() {
            return flashDuration;
        }
    }
}
