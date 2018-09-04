package com.xj.marqueeview.sample.base.anim;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * Animation 工具类
 *
 * Created by xujun on 21/8/2018$ 12:18$.
 */
public class AnimationHelper {

    public static final int DURATION_MILLIS = 500;

    public static Animation newTranslateY(float fromYValue, float toYValue, Animation.AnimationListener animationListener){
        return newTranslate(0,0,fromYValue,toYValue,animationListener);
    }

    public static Animation newTranslateX(float fromXValue, float toXValue, Animation.AnimationListener animationListener){
        return newTranslate(fromXValue,toXValue,0,0,animationListener);
    }

    public static Animation newTranslate(float fromXValue, float toXValue, float fromYValue, float toYValue,
                                         Animation.AnimationListener animationListener) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation
                .RELATIVE_TO_SELF, fromXValue, Animation.RELATIVE_TO_SELF, toXValue, Animation.RELATIVE_TO_SELF,
                fromYValue, Animation.RELATIVE_TO_SELF, toYValue);
        translateAnimation.setDuration(DURATION_MILLIS);
        translateAnimation.setFillAfter(true);
        if (animationListener != null) {
            translateAnimation.setAnimationListener(animationListener);
        }
        return translateAnimation;
    }

    public static Animation newAlpha(float fromAlpha, float toAlpha, Animation.AnimationListener
            animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        if (animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener);
        }
        return alphaAnimation;
    }

    public static AnimationSet add(Animation... animations) {
        AnimationSet animationSet = new AnimationSet(true);
        for (Animation animation : animations) {
            if (animation == null) {
                continue;
            }
            animationSet.addAnimation(animation);
        }
        return animationSet;

    }
}
