package com.xj.marqueeview.sample;

import android.support.annotation.DrawableRes;

/**
 * Created by xujun on 1/9/2018$ 18:10$.
 */
public class DataUtils {

    public static String produceTitle(int position) {
        String content = String.format("I am %s handsome boy", position);
        return content;
    }

    public static String produceContent(int position) {
        String content = String.format("为我点赞 + %s", position);
        return content;
    }

    public static @DrawableRes
    int produceImageResId(int position) {
        if ((position & 1) == 0) {
            return R.mipmap.notice;
        }
        return R.mipmap.doubt;
    }
}
