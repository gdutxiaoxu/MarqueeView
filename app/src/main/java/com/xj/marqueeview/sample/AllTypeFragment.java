package com.xj.marqueeview.sample;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.MultiItemTypeAdapter;
import com.xj.marqueeview.sample.base.BaseFragment;
import com.xj.marqueeview.sample.base.anim.AnimationHelper;
import com.xj.marqueeview.sample.iamgetext.ImageTextAdapter;
import com.xj.marqueeview.sample.iamgetext.ImageTextBean;
import com.xj.marqueeview.sample.multitype.ImageTextItemViewDelegate;
import com.xj.marqueeview.sample.multitype.MultiTextItemViewDelegate;
import com.xj.marqueeview.sample.multitype.MultiTypeBean;
import com.xj.marqueeview.sample.multitype.TextItemViewDelegate;
import com.xj.marqueeview.sample.simpletext.SimpleTextAdapter;

import java.util.ArrayList;

/**
 * Created by xujun on 4/9/2018$ 11:06$.
 */
public class AllTypeFragment extends BaseFragment {

    private static final String TAG = AllTypeFragment.class.getSimpleName();
    private View view;
    private MarqueeView mMvAllType;
    private MarqueeView mMvAllType2;
    private MarqueeView mMvAllType3;
    private MarqueeView mMvAllType4;


    @Override
    protected void initView(View view) {
        mMvAllType = (MarqueeView) view.findViewById(R.id.mv_all_type);
        mMvAllType2 = (MarqueeView) view.findViewById(R.id.mv_all_type2);
        mMvAllType3 = (MarqueeView) view.findViewById(R.id.mv_all_type3);
        mMvAllType4 = (MarqueeView) view.findViewById(R.id.mv_all_type4);

    }

    @Override
    public int getContentViewLayoutID() {
        return R.layout.fragment_all_type;
    }

    @Override
    protected void initData() {
        super.initData();

        initSimpleText();
        initImageText();
        ArrayList<MultiTypeBean> datas = getMultiTypeDatas();
        setMultiTextAdapter(mMvAllType3, datas);

        Animation translateYInt = AnimationHelper.newTranslateY(-1, 0, null);
        Animation alphaInt = AnimationHelper.newAlpha(0, 1, null);
        AnimationSet inAnimaionSet = AnimationHelper.add(translateYInt, alphaInt);

        Animation translateYOut = AnimationHelper.newTranslateY(0, 1, null);
        Animation alphaOut = AnimationHelper.newAlpha(1, 0, null);
        AnimationSet outAnimaionSet = AnimationHelper.add(translateYOut, alphaOut);
        mMvAllType4.setInAndOutAnimation(inAnimaionSet, outAnimaionSet);
        mMvAllType4.setIFlipListener(new MarqueeView.IFlipListener() {
            @Override
            public void onFilpStart(int position, View view) {
                Log.i(TAG, "onFilpStart: position = " + position);
            }

            @Override
            public void onFilpSelect(int position, View view) {
                Log.i(TAG, "onFilpSelect: position = " + position);
            }
        });
        setMultiTextAdapter(mMvAllType4, datas);
    }

    @Override
    protected void handleUserVisible(boolean isVisibleToUser) {
        super.handleUserVisible(isVisibleToUser);
        handleMarqueeView(mMvAllType,isVisibleToUser);
        handleMarqueeView(mMvAllType2,isVisibleToUser);
        handleMarqueeView(mMvAllType3,isVisibleToUser);
        handleMarqueeView(mMvAllType4,isVisibleToUser);
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

    @NonNull
    private ArrayList<MultiTypeBean> getMultiTypeDatas() {
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
        return datas;
    }

    private void initSimpleText() {
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String content = String.format("I am %s handsome boy", i);
            datas.add(content);
        }
        setSimpleAdapter(mMvAllType, datas);
    }

    private void initImageText() {
        ArrayList<ImageTextBean> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String content = DataUtils.produceTitle(i);
            int imageResId = DataUtils.produceImageResId(i);
            ImageTextBean imageTextBean = new ImageTextBean();
            imageTextBean.title = content;
            imageTextBean.resImageId = imageResId;
            datas.add(imageTextBean);

        }
        setImageTextAdapter(mMvAllType2, datas);
    }

    private void setSimpleAdapter(final MarqueeView marqueeView, ArrayList<String> datas) {
        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(mContext, datas);
        simpleTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
        marqueeView.setAdapter(simpleTextAdapter);
    }

    private void setImageTextAdapter(final MarqueeView marqueeView, ArrayList<ImageTextBean>
            datas) {

        ImageTextAdapter imageTextAdapter = new ImageTextAdapter(mContext, datas);
        imageTextAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
        marqueeView.setAdapter(imageTextAdapter);
    }


    private void setMultiTextAdapter(final MarqueeView marqueeView, ArrayList<MultiTypeBean> datas) {

        MultiItemTypeAdapter<MultiTypeBean> multiItemTypeAdapter = new
                MultiItemTypeAdapter<MultiTypeBean>(mContext, datas);
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
