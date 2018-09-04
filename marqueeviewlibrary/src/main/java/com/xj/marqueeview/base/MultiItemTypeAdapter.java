package com.xj.marqueeview.base;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xujun on 28/8/2018$ 15:56$.
 */
public class MultiItemTypeAdapter<T> {

    private static final String TAG = MultiItemTypeAdapter.class.getSimpleName();

    protected final List<T> mDatas;
    private final Context mContext;
    private final ItemViewDelegateManager mItemViewDelegateManager;
    public OnItemClickListener mOnItemClickListener;

    public MultiItemTypeAdapter(Context context, List<T> data) {
        mContext = context;
        mDatas = data;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public View createItemView(ItemViewDelegate<T> itemViewDelegate, ViewGroup parent) {
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder viewHolder = null;
        View convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        viewHolder = new ViewHolder(mContext, convertView, parent, -1);
        viewHolder.mLayoutId = layoutId;
        onViewHolderCreated(viewHolder, viewHolder.getConvertView());
        return convertView;


    }

    public View createItemView(int position, View convertView, ViewGroup parent) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(mDatas
                .get(position), position);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder(mContext, convertView, parent, position);
            viewHolder.mLayoutId = layoutId;
            onViewHolderCreated(viewHolder, viewHolder.getConvertView());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }
        convert(viewHolder, getItem(position), position);

        return convertView;

    }

    private void convert(ViewHolder viewHolder, T item, int position) {
        mItemViewDelegateManager.convert(viewHolder, item, position);
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    protected void onViewHolderCreated(final ViewHolder viewHolder, View convertView) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(viewHolder.mPosition, v);
                }
            }
        });
    }

    public SparseArrayCompat<ItemViewDelegate<T>> getItemViewDelegate() {
        return mItemViewDelegateManager.getDelegates();
    }


    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public int getViewTypeCount() {
        if (useItemViewDelegateManager())
            return mItemViewDelegateManager.getItemViewDelegateCount();
        return 1;
    }


    /**
     * 该方法注意与 getItemViewType(int position) 保持一致
     *
     * @param viewDelegateSparseArrayCompat
     * @param index
     * @return
     */
    public int getItemViewType(SparseArrayCompat<ItemViewDelegate> viewDelegateSparseArrayCompat, int
            index) {
        return viewDelegateSparseArrayCompat.keyAt(index);

    }

    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            int viewType = mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
            return viewType;
        }
        return 0;
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setDatas(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
