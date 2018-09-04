package com.xj.marqueeview.sample.multitype;

import android.widget.ImageView;
import android.widget.TextView;

import com.xj.marqueeview.base.ItemViewDelegate;
import com.xj.marqueeview.base.ViewHolder;
import com.xj.marqueeview.sample.R;

/**
 * Created by xujun on 1/9/2018$ 18:25$.
 */
public class ImageTextItemViewDelegate implements ItemViewDelegate<MultiTypeBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_image_text;
    }

    @Override
    public boolean isForViewType(MultiTypeBean item, int position) {
        return item.mItemViewType == MultiTypeBean.ItemViewType.imageText;
    }

    @Override
    public void convert(ViewHolder holder, MultiTypeBean multiTypeBean, int position) {
        TextView tv = holder.getView(R.id.tv);
        tv.setText(multiTypeBean.title);

        ImageView iv = holder.getView(R.id.iv);
        iv.setImageResource(multiTypeBean.resImageId);
    }



}
