package com.xj.marqueeview.sample.multitype;

import android.support.annotation.DrawableRes;

/**
 * Created by xujun on 1/9/2018$ 18:29$.
 */
public class MultiTypeBean {

    public @DrawableRes
    int resImageId;
    public String title;
    public String content;
    public ItemViewType mItemViewType = ItemViewType.text;

    public enum ItemViewType{
        text,
        imageText,
        multiTextAndImage;

    }
}
