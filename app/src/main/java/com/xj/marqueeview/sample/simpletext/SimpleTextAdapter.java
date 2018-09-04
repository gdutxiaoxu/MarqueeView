package com.xj.marqueeview.sample.simpletext;

import android.content.Context;
import android.widget.TextView;

import com.xj.marqueeview.base.CommonAdapter;
import com.xj.marqueeview.base.ViewHolder;
import com.xj.marqueeview.sample.R;

import java.util.List;

/**
 * Created by xujun on 1/9/2018$ 10:41$.
 */
public class SimpleTextAdapter extends CommonAdapter<String> {

    public SimpleTextAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_simple_text, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, String item, int position) {
        TextView tv = viewHolder.getView(R.id.tv);
        tv.setText(item);
    }

}
