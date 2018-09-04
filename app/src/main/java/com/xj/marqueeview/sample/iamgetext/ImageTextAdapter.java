package com.xj.marqueeview.sample.iamgetext;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.marqueeview.base.CommonAdapter;
import com.xj.marqueeview.base.ViewHolder;
import com.xj.marqueeview.sample.R;

import java.util.List;

/**
 * Created by xujun on 1/9/2018$ 10:41$.
 */
public class ImageTextAdapter extends CommonAdapter<ImageTextBean> {

    public ImageTextAdapter(Context context, List<ImageTextBean> datas) {
        super(context, R.layout.item_image_text, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ImageTextBean item, int position) {
        TextView tv = viewHolder.getView(R.id.tv);
        tv.setText(item.title);

        ImageView iv = viewHolder.getView(R.id.iv);
        iv.setImageResource(item.resImageId);

    }
}
