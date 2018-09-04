package com.xj.marqueeview.sample.base;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    public static void removeParent(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }

    }

}
