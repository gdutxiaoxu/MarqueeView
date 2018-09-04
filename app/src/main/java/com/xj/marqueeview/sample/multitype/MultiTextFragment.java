package com.xj.marqueeview.sample.multitype;

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
public class MultiTextFragment extends BaseFragment {

    private static final String TAG = MultiTextFragment.class.getSimpleName();
    private View view;
    private MarqueeView mMvMultiText;
    private MarqueeView mMvMultiText2;
    private MarqueeView mMvMultiText3;
    private MarqueeView mMvMultiText4;
    private MarqueeView mMvMultiText5;

    @Override
    protected void initView(View view) {
        mMvMultiText = (MarqueeView) view.findViewById(R.id.mv_multi_text);
        mMvMultiText2 = (MarqueeView) view.findViewById(R.id.mv_multi_text2);
        mMvMultiText3 = (MarqueeView) view.findViewById(R.id.mv_multi_text3);
        mMvMultiText4 = (MarqueeView) view.findViewById(R.id.mv_multi_text4);
        mMvMultiText5 = (MarqueeView) view.findViewById(R.id.mv_multi_text5);
    }

    @Override
    protected void handleUserVisible(boolean isVisibleToUser) {
        super.handleUserVisible(isVisibleToUser);
        handleMarqueeView(mMvMultiText, isVisibleToUser);
        handleMarqueeView(mMvMultiText2, isVisibleToUser);
        handleMarqueeView(mMvMultiText3, isVisibleToUser);
        handleMarqueeView(mMvMultiText4, isVisibleToUser);
        handleMarqueeView(mMvMultiText5, isVisibleToUser);
    }

    private void handleMarqueeView(MarqueeView marqueeView, boolean isVisibleToUser) {
        if (marqueeView == null) {
            return;
        }
        if(isVisibleToUser){
            marqueeView.startFlip();
        }else{
            marqueeView.stopFilp();
        }
    }

    @Override
    public int getContentViewLayoutID() {
        return R.layout.fragment_multi_text;
    }

    @Override
    protected void initData() {
        super.initData();
        ArrayList<MultiTypeBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String title = DataUtils.produceTitle(i);
            int imageResId = DataUtils.produceImageResId(i);
            String content = DataUtils.produceContent(i);
            MultiTypeBean multiTypeBean = new MultiTypeBean();
            multiTypeBean.title = title;
            multiTypeBean.resImageId = imageResId;
            multiTypeBean.content = content;
            int result = i % 3;
            if (result == 0) {
                multiTypeBean.mItemViewType = MultiTypeBean.ItemViewType.text;
            } else if (result == 1) {
                multiTypeBean.mItemViewType = MultiTypeBean.ItemViewType.imageText;
            } else {
                multiTypeBean.mItemViewType = MultiTypeBean.ItemViewType.multiTextAndImage;
            }
            datas.add(multiTypeBean);

        }
        setAdapter(mMvMultiText, datas);
        setAdapter(mMvMultiText2, datas);
        setAdapter(mMvMultiText3, datas);
        setAdapter(mMvMultiText4, datas);


        Animation translateYInt = AnimationHelper.newTranslateY(-1, 0, null);
        Animation alphaInt = AnimationHelper.newAlpha(0, 1, null);
        AnimationSet inAnimaionSet = AnimationHelper.add(translateYInt, alphaInt);

        Animation translateYOut = AnimationHelper.newTranslateY(0, 1, null);
        Animation alphaOut = AnimationHelper.newAlpha(1, 0, null);
        AnimationSet outAnimaionSet = AnimationHelper.add(translateYOut, alphaOut);
        mMvMultiText5.setInAndOutAnimation(inAnimaionSet, outAnimaionSet);
        mMvMultiText5.setIFlipListener(new MarqueeView.IFlipListener() {
            @Override
            public void onFilpStart(int position, View view) {
                Log.i(TAG, "onFilpStart: position = " + position);
            }

            @Override
            public void onFilpSelect(int position, View view) {
                Log.i(TAG, "onFilpSelect: position = " + position);
            }
        });
        setAdapter(mMvMultiText5, datas);
        mMvMultiText5.startFlip();

    }

    private void setAdapter(final MarqueeView marqueeView, ArrayList<MultiTypeBean> datas) {

        MultiItemTypeAdapter<MultiTypeBean> multiItemTypeAdapter = new MultiItemTypeAdapter<MultiTypeBean>(mContext, datas);
        multiItemTypeAdapter.addItemViewDelegate(new TextItemViewDelegate());
        multiItemTypeAdapter.addItemViewDelegate(new ImageTextItemViewDelegate());
        multiItemTypeAdapter.addItemViewDelegate(new MultiTextItemViewDelegate());
        multiItemTypeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Log.i(TAG, "onItemClick: position = " + position);
                if (marqueeView.isStart()) {
                    marqueeView.stopFilp();
                } else {
                    marqueeView.startFlip();
                }
            }
        });
        marqueeView.setAdapter(multiItemTypeAdapter);
    }


}
