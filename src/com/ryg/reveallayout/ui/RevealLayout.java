package com.ryg.reveallayout.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import com.authentication.activity.R;



public class RevealLayout extends LinearLayout implements Runnable {


    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 目标View的宽度*/
    private int mTargetWidth;
    /**
     * 目标View的高度*/
    private int mTargetHeight;
    /**
     * 长宽之间取最小距离*/
    private int mMinBetweenWidthAndHeight;
    /**
     * 长宽之间取最大距离*/
    private int mMaxBetweenWidthAndHeight;
    /**
     * 水波纹的最大半径*/
    private int mMaxRevealRadius;
    /**
     * 水波纹的半径间隔*/
    private int mRevealRadiusGap;
    /**
     * 水波纹的初始半径为0*/
    private int mRevealRadius = 0;
    /**
     * 中心点X*/
    private float mCenterX;
    /**
     * 中心点Y*/
    private float mCenterY;
    /**
     * 屏幕中的落点（DOWN，UP）*/
    private int[] mLocationInScreen = new int[2];
    /**
     * 是否显示水波纹动画*/
    private boolean mShouldDoAnimation = false;
    /**
     * 是否被点击*/
    private boolean mIsPressed = false;
    /**
     * 水波纹的延迟时间，越小越快*/
    private int INVALIDATE_DURATION = 5;
    /**
     * 被点击的View*/
    private View mTouchTarget;
    /**
     * 用于DispatchUpTouchEvent绘制的Runnable*/
    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();
    /**
     * 一个参数的构造函数*/
    public RevealLayout(Context context) {
        this(context,null);
        init();
    }
    /**
     * 2个参数的构造函数*/
    public RevealLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }
    /**
     * 3个参数的构造函数*/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 视图的初始化*/
        init();
    }
    /**
     * 视图的初始化*/
    private void init() {
    	 /**
         * 为了是draw方法或者类似的方法执行有效*/
        setWillNotDraw(false);
        /**
         * 画笔颜色的初始化*/
        mPaint.setColor(getResources().getColor(R.color.reveal_color));
    }
    /**
     * 布局的初始化*/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /**
         * 获取屏幕中的落点*/
        this.getLocationOnScreen(mLocationInScreen);
    }
    /**
     * 测量子View的一些参数，如触摸点和宽高等数据*/
    private void initParametersForChild(MotionEvent event, View view) {
    	/**
         * 将触摸点的设置为圆心坐标mCenterX，mCenterY*/
        mCenterX = event.getX() ;
        mCenterY = event.getY() ;
        /**
         * 测量子View的高度和宽度*/
        mTargetWidth = view.getMeasuredWidth();
        mTargetHeight = view.getMeasuredHeight();
        /**
         * 长宽之间取最小距离*/
        mMinBetweenWidthAndHeight = Math.min(mTargetWidth, mTargetHeight);
        /**
         * 长宽之间取最大距离*/
        mMaxBetweenWidthAndHeight = Math.max(mTargetWidth, mTargetHeight);
        /**
         * 水波纹的半径初始为0*/
        mRevealRadius = 0;
        /**
         * 显示水波纹*/
        mShouldDoAnimation = true;
        /**
         * 按下标志位设置为true*/
        mIsPressed = true;
        /**
         *水波纹之间间隔为mMinBetweenWidthAndHeight / 8*/
        
        mRevealRadiusGap = mMinBetweenWidthAndHeight / 8;
        /**
         * 触摸点的坐标x,y放在location数组中*/
        int[] location = new int[2];
        /**
         * 获取子View中的落点*/
        view.getLocationOnScreen(location);
        /**
         * 子View中心location【0】-落点的x坐标就是落点距离子view的左边边距
         * mLocationInScreen[0]=0*/
        int left = location[0] - mLocationInScreen[0];
        /**
         * 触摸点距离左边View的边距*/
        int transformedCenterX = (int)mCenterX - left;
        /**
         * 水波纹的最大半径*/
        mMaxRevealRadius = Math.max(transformedCenterX, mTargetWidth - transformedCenterX);
       
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mShouldDoAnimation || mTargetWidth <= 0 || mTouchTarget == null) {
            return;
        }

        if (mRevealRadius > mMinBetweenWidthAndHeight / 2) {
            mRevealRadius += mRevealRadiusGap * 4;
        } else {
            mRevealRadius += mRevealRadiusGap;
        }
        this.getLocationOnScreen(mLocationInScreen);
        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        int left = location[0] - mLocationInScreen[0];
        int top = location[1] - mLocationInScreen[1];
        int right = left + mTouchTarget.getMeasuredWidth();
        int bottom = top + mTouchTarget.getMeasuredHeight();
        
        Log.e("TAG", "left:"+left+"top:"+top+"right:"+right+"bottom"+bottom+"mRevealRadius"+mRevealRadius);
        canvas.save();
        canvas.clipRect(left, top, right, bottom);
        canvas.drawCircle(mCenterX, mCenterY, mRevealRadius, mPaint);
        canvas.restore();

        if (mRevealRadius <= mMaxRevealRadius) {
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        } else if (!mIsPressed) {
            mShouldDoAnimation = false;
            //重新触发dispatchDraw事件会直接退出
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	/**
         * 触摸点的x，y坐标*/
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        /**
         * 触摸行为是down还是up还是move等其他*/
        int action = event.getAction();
        /**
         * 是Down的话*/
        if (action == MotionEvent.ACTION_DOWN) {
        	//根据坐标获取到view,可点击并且落点在可点击View内部返回值才不为空，其他都为空
            View touchTarget = getTouchTarget(this, x, y);
            if (touchTarget != null && touchTarget.isClickable() && touchTarget.isEnabled()) {
            	//将点击子View传给mTouchTarget
                mTouchTarget = touchTarget;
                //初始化子View的参数和水波纹半径
                initParametersForChild(event, touchTarget);
                //触发dispatchDraw
                postInvalidateDelayed(INVALIDATE_DURATION);
            }
        } else if (action == MotionEvent.ACTION_UP) {
        	//抬起时候将点击标志位设置为false
            mIsPressed = false;
            //触发dispatchDraw
            postInvalidateDelayed(INVALIDATE_DURATION);
            mDispatchUpTouchEventRunnable.event = event;
            //后面的参数会影响点是绘制播放时间，设置适量大一点可以实现播放完波纹在执行点击事件
            postDelayed(mDispatchUpTouchEventRunnable, 0);
            return true;
        } else if (action == MotionEvent.ACTION_CANCEL) {
            mIsPressed = false;
            //触发dispatchDraw
            postInvalidateDelayed(INVALIDATE_DURATION);
        }
        return super.dispatchTouchEvent(event);
    }
    /**
     * 根据x,y坐标来获取触摸的子View
    */   
    private View getTouchTarget(View view, int x, int y) {
        View target = null;
        ArrayList<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }

        return target;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        /**
         * 判断触摸点是不是在子View的上下左右四个点围成矩形内部
        */ 
        if (view.isClickable() && y >= top && y <= bottom
                && x >= left && x <= right) {
            return true;
        }
        return false;
    }
 
   
	@Override
    public boolean performClick() {
    	postDelayed(this, 400);
        return true;
    }
    
    
    public void run() {
        /**
         * 模拟点击事件
        */  
        super.performClick();
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;

        public void run() {
            if (mTouchTarget == null || !mTouchTarget.isEnabled()) {
                return;
            }

            if (isTouchPointInView(mTouchTarget, (int)event.getRawX(), (int)event.getRawY())) {
            	//目标的点击时间延迟400ms保证动画执行完之后才触发点击事件，模拟点击事件
                mTouchTarget.performClick();
            }
        }
    };

}
