package com.cn.smart.baselib.uiframework.crouton;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * author：leo on 2016/11/23 14:12
 * email： leocheung4ever@gmail.com
 * description: 默认动画构建器
 * what & why is modified:
 */

final class DefaultAnimationsBuilder {

    private static final long DURATION = 400;
    private static Animation slideInDownAnimation, slideOutUpAnimation;
    private static int lastInAnimationHeight, lastOutAnimationHeight;

    private DefaultAnimationsBuilder() {
    /* no-op */
    }

    /**
     * @param croutonView The croutonView which gets animated.
     * @return The default Animation for a showing {@link Crouton}.
     */
    static Animation buildDefaultSlideInDownAnimation(View croutonView) {
        if (!areLastMeasuredInAnimationHeightAndCurrentEqual(croutonView) || (null == slideInDownAnimation)) {
            slideInDownAnimation = new TranslateAnimation(
                    0, 0,                               // X: from, to
                    -croutonView.getMeasuredHeight(), 0 // Y: from, to
            );
            slideInDownAnimation.setDuration(DURATION);
            setLastInAnimationHeight(croutonView.getMeasuredHeight());
        }
        return slideInDownAnimation;
    }

    /**
     * @param croutonView The croutonView which gets animated.
     * @return The default Animation for a hiding {@link Crouton}.
     */
    static Animation buildDefaultSlideOutUpAnimation(View croutonView) {
        if (!areLastMeasuredOutAnimationHeightAndCurrentEqual(croutonView) || (null == slideOutUpAnimation)) {
            slideOutUpAnimation = new TranslateAnimation(
                    0, 0,                               // X: from, to
                    0, -croutonView.getMeasuredHeight() // Y: from, to
            );
            slideOutUpAnimation.setDuration(DURATION);
            setLastOutAnimationHeight(croutonView.getMeasuredHeight());
        }
        return slideOutUpAnimation;
    }

    private static boolean areLastMeasuredInAnimationHeightAndCurrentEqual(View croutonView) {
        return areLastMeasuredAnimationHeightAndCurrentEqual(lastInAnimationHeight, croutonView);
    }

    private static boolean areLastMeasuredOutAnimationHeightAndCurrentEqual(View croutonView) {
        return areLastMeasuredAnimationHeightAndCurrentEqual(lastOutAnimationHeight, croutonView);
    }

    private static boolean areLastMeasuredAnimationHeightAndCurrentEqual(int lastHeight, View croutonView) {
        return lastHeight == croutonView.getMeasuredHeight();
    }

    private static void setLastInAnimationHeight(int lastInAnimationHeight) {
        DefaultAnimationsBuilder.lastInAnimationHeight = lastInAnimationHeight;
    }

    private static void setLastOutAnimationHeight(int lastOutAnimationHeight) {
        DefaultAnimationsBuilder.lastOutAnimationHeight = lastOutAnimationHeight;
    }
}
