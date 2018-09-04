package com.xj.marqueeview.sample.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author xujun  on 2016/12/28.
 * @email gdutxiaoxu@163.com
 */

public abstract class BaseFragment extends Fragment {

    public static final String DEFAULT_PARCEABLE_NAME = "default_parceable_name";

    protected Context mContext;
    private static final String TAG = "BaseFragment";
    protected boolean mIsVisiableToUser = false;
    protected boolean mIsViewInitiated = false;
    protected boolean mIsDataInitiated = false;
    protected View mView;
    private String mSimpleName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mSimpleName = this.getClass().getSimpleName();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAru();
    }

    protected void initAru() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: mSimpleName = " + getSimpleName());
        if(mView == null){
            mView = View.inflate(mContext, getContentViewLayoutID(), null);
            initView(mView);
        }else{
            ViewUtils.removeParent(mView);
        }

        mIsViewInitiated = true;
        mIsDataInitiated = false;
        return mView;
    }

    private String getSimpleName() {
        if (TextUtils.isEmpty(mSimpleName)) {
            mSimpleName = this.getClass().getSimpleName();
        }
        return mSimpleName;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated:  mSimpleName = " + getSimpleName());
        super.onActivityCreated(savedInstanceState);
        initData();
        mIsDataInitiated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: isVisibleToUser = " + isVisibleToUser + " mSimpleName = "
                + getSimpleName());
        if (!mIsViewInitiated) {
            return;
        }
        handleUserVisible(isVisibleToUser);
    }

    protected void handleUserVisible(boolean isVisibleToUser) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewInitiated = false;
        Log.i(TAG, "onDestroyView: mSimpleName = " + getSimpleName());
    }

    protected void initData() {
    }

    protected abstract void initView(View view);

    public abstract @LayoutRes
    int getContentViewLayoutID();

    public void readyGo(Class<?> clazz) {
        this.readyGo(clazz, null, "");
    }

    public void readyGo(Class<?> clazz, Parcelable parcelable) {
        this.readyGo(clazz, DEFAULT_PARCEABLE_NAME, parcelable);
    }

    public void readyGo(Class<?> clazz, String name, Parcelable parcelable) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != parcelable) {
            intent = intent.putExtra(name, parcelable);
        }
        startActivity(intent);
    }

    public void readyGo(Class<?> clazz, String name, String value) {

        Intent intent = new Intent(getActivity(), clazz);
        if (null != value) {
            intent = intent.putExtra(name, value);
        }
        startActivity(intent);
    }

    protected <T> T checkNotNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

}
