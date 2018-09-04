package com.xj.marqueeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.xj.marqueeview.base.ItemViewDelegate;
import com.xj.marqueeview.base.MultiItemTypeAdapter;

/**
 * 轮播公告 Veiw
 * <p>
 * Created by xujun
 */

public class MarqueeView extends FrameLayout {

    private SparseArray<View> mViews;

    private static final String TAG = MarqueeView.class.getSimpleName();

    // 轮播间隔
    private int mInterval = 2000;

    // 动画时长
    private boolean hasSetAnimDuration = false;
    private int mAnimDuration = 300;

    // 布局的对齐方向
    private int mGravity = Gravity.CENTER;
    public static final int GRAVITY_LEFT = 0;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_RIGHT = 2;

    // 动画执行方向
    private boolean hasSetDirection = false;
    private int direction = DIRECTION_BOTTOM_TO_TOP;
    public static final int DIRECTION_BOTTOM_TO_TOP = 0;
    public static final int DIRECTION_TOP_TO_BOTTOM = 1;
    public static final int DIRECTION_RIGHT_TO_LEFT = 2;
    public static final int DIRECTION_LEFT_TO_RIGHT = 3;

    // 进入进出动画
    @AnimRes
    private int mInAnimResId = R.anim.anim_bottom_in;
    @AnimRes
    private int mOutAnimResId = R.anim.anim_top_out;
    private Animation mInAnimation = null;
    private Animation mOutAnimation = null;

    private int mPosition;

    private Context mContext;
    private MultiItemTypeAdapter mMultiItemTypeAdapter;

    private View mCurView;
    private View mLastView;

    public static final int APPEAR = 1;
    public static final int DIS_APPEAR = 2;

    private boolean isStart = true;

    private IFlipListener mIFlipListener;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mViews = new SparseArray<>();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView,
                defStyleAttr, 0);

        mInterval = typedArray.getInteger(R.styleable.MarqueeView_mvInterval, mInterval);
        hasSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeView_mvAnimDuration);
        mAnimDuration = typedArray.getInteger(R.styleable.MarqueeView_mvAnimDuration,
                mAnimDuration);

        int gravityType = typedArray.getInt(R.styleable.MarqueeView_mvGravity, GRAVITY_CENTER);
        switch (gravityType) {
            case GRAVITY_LEFT:
                mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case GRAVITY_CENTER:
                mGravity = Gravity.CENTER;
                break;
            case GRAVITY_RIGHT:
                mGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }

        hasSetDirection = typedArray.hasValue(R.styleable.MarqueeView_mvDirection);
        direction = typedArray.getInt(R.styleable.MarqueeView_mvDirection, direction);
        if (hasSetDirection) {
            handleAnimationDir();
        } else {
            mInAnimResId = R.anim.anim_bottom_in;
            mOutAnimResId = R.anim.anim_top_out;
        }

        typedArray.recycle();


    }

    private void handleAnimationDir() {
        switch (direction) {
            case DIRECTION_BOTTOM_TO_TOP:
                mInAnimResId = R.anim.anim_bottom_in;
                mOutAnimResId = R.anim.anim_top_out;
                break;
            case DIRECTION_TOP_TO_BOTTOM:
                mInAnimResId = R.anim.anim_top_in;
                mOutAnimResId = R.anim.anim_bottom_out;
                break;
            case DIRECTION_RIGHT_TO_LEFT:
                mInAnimResId = R.anim.anim_right_in;
                mOutAnimResId = R.anim.anim_left_out;
                break;
            case DIRECTION_LEFT_TO_RIGHT:
                mInAnimResId = R.anim.anim_left_in;
                mOutAnimResId = R.anim.anim_right_out;
                break;
        }
    }

    @SuppressWarnings("deprecation")
    public void setAdapter(MultiItemTypeAdapter multiItemTypeAdapter) {
        if (multiItemTypeAdapter == null) {
            return;
        }
        mMultiItemTypeAdapter = multiItemTypeAdapter;
        postStart(mInAnimResId, mOutAnimResId);
    }

    private void postStart(final @AnimRes int inAnimResId, final @AnimRes int outAnimResID) {
        post(new Runnable() {
            @Override
            public void run() {
                start(inAnimResId, outAnimResID);
            }
        });
    }

    private void start(final @AnimRes int inAnimResId, final @AnimRes int outAnimResID) {
        removeAllViews();
        clearAnimation();
        addAllTypeView();
        mPosition = 0;
        int itemViewType = mMultiItemTypeAdapter.getItemViewType(mPosition);
        View convertView = mViews.get(itemViewType);
        View itemView = mMultiItemTypeAdapter.createItemView(mPosition, convertView, MarqueeView
                .this);
        mCurView = itemView;
        mLastView = mCurView;
        sendAppear();

    }

    private void sendAppear() {
        mHandler.removeMessages(APPEAR);
        if (!isStart) {
            return;
        }
        mHandler.sendEmptyMessageDelayed(APPEAR, 0);
    }

    private void sendDisappear() {
        mHandler.removeMessages(DIS_APPEAR);
        if (!isStart) {
            return;
        }
        mHandler.sendEmptyMessageDelayed(DIS_APPEAR, mInterval);
    }

    private void addAllTypeView() {
        int viewTypeCount = mMultiItemTypeAdapter.getViewTypeCount();
        if (viewTypeCount < 1) {
            return;
        }
        mViews.clear();
        int curItemViewType = mMultiItemTypeAdapter.getItemViewType(mPosition);
        SparseArrayCompat<ItemViewDelegate> itemViewDelegate = mMultiItemTypeAdapter
                .getItemViewDelegate();
        int size = itemViewDelegate.size();
        for (int i = 0; i < size; i++) {
            ItemViewDelegate delegate = itemViewDelegate.valueAt(i);
            View itemView = mMultiItemTypeAdapter.createItemView(delegate, MarqueeView.this);
            int type = mMultiItemTypeAdapter.getItemViewType(itemViewDelegate, i);
            itemView.setTag(R.id.key_marquee_view_item_type, type);
            LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = mGravity;
            addView(itemView, layoutParams);
            mViews.put(type, itemView);

            // 设置当前 itemView 可见，其他不可见
            if (type == curItemViewType) {
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private View getItemView(int index) {
        int itemViewType = mMultiItemTypeAdapter.getItemViewType(index);
        // 获取缓存的 convertView
        View convertView = mViews.get(itemViewType);
        View itemView = mMultiItemTypeAdapter.createItemView(index, convertView, MarqueeView.this);
        return itemView;
    }

    // detached window 的时候，停止 flip
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
        stopFilp();

    }

    public void stopFilp() {
        removeAllMes();
    }

    public void startFlip() {
        isStart = true;
        sendAppear();
    }

    private void removeAllMes() {
        isStart = false;
        if (mHandler != null) {
            mHandler.removeMessages(APPEAR);
            mHandler.removeMessages(DIS_APPEAR);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case APPEAR:
                    handleAppearMes();
                    break;

                case DIS_APPEAR:
                    handleDisappearMes();
                    break;

                default:
                    break;

            }
        }


    };

    private void handleDisappearMes() {
        Animation animation = getOutAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //                Log.i(TAG, "onAnimationStart: mPosition = " +mPosition);
                mLastView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLastView.setVisibility(View.GONE);
                mPosition++;
                int count = mMultiItemTypeAdapter.getCount();
                //                Log.i(TAG, "onAnimationEnd: mPosition = " +mPosition + " count
                // = "+count);
                if (mPosition >= count) {
                    mPosition = 0;
                }
                sendAppear();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mLastView.startAnimation(animation);
    }

    @NonNull
    private Animation getOutAnimation() {
        if (mOutAnimation != null) {
            return mOutAnimation;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, mOutAnimResId);
        animation.setFillAfter(true);
        setAnimationDuration(animation);
        return animation;
    }

    private void handleAppearMes() {
        mLastView = mCurView;
        mCurView = getItemView(mPosition);
        Animation inAnimation = getInAnimation();
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLastView.setVisibility(View.GONE);
                mCurView.setVisibility(View.VISIBLE);
                if (mIFlipListener != null) {
                    mIFlipListener.onFilpStart(mPosition, mCurView);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLastView = mCurView;
                mCurView = getItemView(mPosition);
                if (mIFlipListener != null) {
                    mIFlipListener.onFilpSelect(mPosition, mCurView);
                }
                sendDisappear();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mCurView.startAnimation(inAnimation);
    }

    @NonNull
    private Animation getInAnimation() {
        if (mInAnimation != null) {
            return mInAnimation;
        }
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, mInAnimResId);
        setAnimationDuration(inAnimation);
        inAnimation.setFillAfter(true);
        return inAnimation;
    }

    private void setAnimationDuration(Animation inAnimation) {
        if (hasSetAnimDuration) {
            inAnimation.setDuration(mAnimDuration);
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
        handleAnimationDir();
    }

    /**
     * 设置轮播间隔
     *
     * @param interval
     */
    public void setInterval(int interval) {
        mInterval = interval;
    }

    /**
     * 设置动画执行时长
     *
     * @param animDuration
     */
    public void setAnimDuration(int animDuration) {
        mAnimDuration = animDuration;
    }

    /**
     * 设置对齐方向
     *
     * @param gravity
     */
    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    /**
     * 设置进入进出动画
     *
     * @param inAnimation
     * @param outAnimation
     */
    public void setInAndOutAnimation(Animation inAnimation, Animation outAnimation) {
        mInAnimation = inAnimation;
        mOutAnimation = outAnimation;
    }

    /**
     * 当前是否在执行动画
     *
     * @return
     */
    public boolean isStart() {
        return isStart;
    }

    /**
     * 监听当前 flip
     *
     * @param IFlipListener
     */
    public void setIFlipListener(IFlipListener IFlipListener) {
        mIFlipListener = IFlipListener;
    }

    /**
     * 监听当前 Flip
     */
    public interface IFlipListener {

        /**
         * 第 position 个 item 开始 flip
         *
         * @param position
         * @param view
         */
        void onFilpStart(int position, View view);

        /**
         * 第 position 个 item 结束 flip
         *
         * @param position
         * @param view
         */
        void onFilpSelect(int position, View view);
    }

}


