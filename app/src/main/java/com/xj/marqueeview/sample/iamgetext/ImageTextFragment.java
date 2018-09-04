package com.xj.marqueeview.sample.iamgetext;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.MultiItemTypeAdapter;
import com.xj.marqueeview.sample.DataUtils;
import com.xj.marqueeview.sample.R;
import com.xj.marqueeview.sample.base.BaseFragment;
import com.xj.marqueeview.sample.base.anim.AnimationHelper;

import java.util.ArrayList;

/**
 * Created by xujun on 31/8/2018$ 18:16$.
 */
public class ImageTextFragment extends BaseFragment {

    private static final String TAG = ImageTextFragment.class.getSimpleName();
    private View view;
    private MarqueeView mMvSimpleText;
    private MarqueeView mMvSimpleText2;
    private MarqueeView mMvSimpleText3;
    private MarqueeView mMvSimpleText4;
    private MarqueeView mMvSimpleText5;

    @Override
    protected void initView(View view) {
        mMvSimpleText = (MarqueeView) view.findViewById(R.id.mv_image_text);
        mMvSimpleText2 = (MarqueeView) view.findViewById(R.id.mv_image_text2);
        mMvSimpleText3 = (MarqueeView) view.findViewById(R.id.mv_image_text3);
        mMvSimpleText4 = (MarqueeView) view.findViewById(R.id.mv_image_text4);
        mMvSimpleText5 = (MarqueeView) view.findViewById(R.id.mv_image_text5);
    }

    @Override
    public int getContentViewLayoutID() {
        return R.layout.fragment_iamge_text;
    }

    @Override
    protected void initData() {
        super.initData();
        ArrayList<ImageTextBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String content = DataUtils.produceTitle(i);
            int imageResId = DataUtils.produceImageResId(i);
            ImageTextBean imageTextBean = new ImageTextBean();
            imageTextBean.title = content;
            imageTextBean.resImageId = imageResId;
            datas.add(imageTextBean);

        }
        setAdapter(mMvSimpleText, datas);
        setAdapter(mMvSimpleText2, datas);
        setAdapter(mMvSimpleText3, datas);
        setAdapter(mMvSimpleText4, datas);


        Animation translateYInt = AnimationHelper.newTranslateY(-1, 0, null);
        Animation alphaInt = AnimationHelper.newAlpha(0, 1, null);
        AnimationSet inAnimaionSet = AnimationHelper.add(translateYInt, alphaInt);

        Animation translateYOut= AnimationHelper.newTranslateY(0, 1, null);
        Animation alphaOut = AnimationHelper.newAlpha(1, 0, null);
        AnimationSet outAnimaionSet = AnimationHelper.add(translateYOut, alphaOut);
        mMvSimpleText5.setInAndOutAnimation(inAnimaionSet,outAnimaionSet);
        mMvSimpleText5.setIFlipListener(new MarqueeView.IFlipListener() {
            @Override
            public void onFilpStart(int position, View view) {
                Log.i(TAG, "onFilpStart: position = " + position);
            }

            @Override
            public void onFilpSelect(int position, View view) {
                Log.i(TAG, "onFilpSelect: position = " + position);
            }
        });
        setAdapter(mMvSimpleText5, datas);
        mMvSimpleText5.startFlip();

    }

    private void setAdapter(final MarqueeView marqueeView, ArrayList<ImageTextBean> datas) {

        ImageTextAdapter imageTextAdapter = new ImageTextAdapter(mContext, datas);
        imageTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.i(TAG, "onItemClick: position = " +position);
                if(marqueeView.isStart()){
                    marqueeView.stopFilp();
                }else{
                    marqueeView.startFlip();
                }
            }
        });
        marqueeView.setAdapter(imageTextAdapter);
    }
}
