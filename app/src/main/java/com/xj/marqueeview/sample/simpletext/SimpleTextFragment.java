package com.xj.marqueeview.sample.simpletext;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.MultiItemTypeAdapter;
import com.xj.marqueeview.sample.R;
import com.xj.marqueeview.sample.base.BaseFragment;
import com.xj.marqueeview.sample.base.anim.AnimationHelper;

import java.util.ArrayList;

/**
 * Created by xujun on 31/8/2018$ 18:16$.
 */
public class SimpleTextFragment extends BaseFragment {

    private static final String TAG = SimpleTextFragment.class.getSimpleName();
    private View view;
    private MarqueeView mMvSimpleText;
    private MarqueeView mMvSimpleText2;
    private MarqueeView mMvSimpleText3;
    private MarqueeView mMvSimpleText4;
    private MarqueeView mMvSimpleText5;

    @Override
    protected void initView(View view) {
        mMvSimpleText = (MarqueeView) view.findViewById(R.id.mv_simple_text);
        mMvSimpleText2 = (MarqueeView) view.findViewById(R.id.mv_simple_text2);
        mMvSimpleText3 = (MarqueeView) view.findViewById(R.id.mv_simple_text3);
        mMvSimpleText4 = (MarqueeView) view.findViewById(R.id.mv_simple_text4);
        mMvSimpleText5 = (MarqueeView) view.findViewById(R.id.mv_simple_text5);
    }

    @Override
    public int getContentViewLayoutID() {
        return R.layout.fragment_simple_text;
    }

    @Override
    protected void initData() {
        super.initData();
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String content = String.format("I am %s handsome boy", i);
            datas.add(content);
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

    private void setAdapter(final MarqueeView marqueeView, ArrayList<String> datas) {
        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(mContext, datas);
        simpleTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.i(TAG, "onItemClick: position = " + position);
                if(marqueeView.isStart()){
                    marqueeView.stopFilp();
                }else{
                    marqueeView.startFlip();
                }
            }
        });
        marqueeView.setAdapter(simpleTextAdapter);
//        mvSimpleText.stopFilp();
    }
}
