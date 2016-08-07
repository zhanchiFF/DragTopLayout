package com.zhanchiff.draglayout.library;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class DragTopNewGroup extends ViewGroup {

    private ViewDragHelper mDragHelper;
    private View VBelowView;
    private View VAboveView;

    private int mTopViewHeight;

    private int mTouchSlop;

    public DragTopNewGroup(Context context) {
        this(context, null);
    }

    public DragTopNewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragTopNewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDragHelper = ViewDragHelper.create(this, 1.0f, callback);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("Content view must contains two child views");
        }
        VAboveView = getChildAt(0);
        VBelowView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (VAboveView == null) {
            return;
        }
        VAboveView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));//采用自由测量
        if (mTopViewHeight != VAboveView.getMeasuredHeight())
            mTopViewHeight = VAboveView.getMeasuredHeight();

        if (VBelowView == null) {
            return;
        }
        VBelowView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int totaltop = VAboveView.getTop();
        VAboveView.layout(getPaddingLeft(), totaltop, VAboveView.getMeasuredWidth() + getPaddingLeft(), VAboveView.getMeasuredHeight() + totaltop);
        totaltop += VAboveView.getMeasuredHeight();

        VBelowView.layout(getPaddingLeft(), totaltop, getPaddingLeft() + VBelowView.getMeasuredWidth(), totaltop + VBelowView.getMeasuredHeight());
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (child == VBelowView) {
                mDragHelper.captureChildView(VAboveView, pointerId);
                return false;
            }
            return child == VAboveView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            requestLayout();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return VAboveView.getMeasuredHeight();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e("willzhang"," top  = "+top+" 总共"+VAboveView.getMeasuredHeight());
            if(child == VAboveView) {
                if (top > 0) {
                    return 0;
                }
                if (top < -(VAboveView.getMeasuredHeight() ))
                    return -VAboveView.getMeasuredHeight();
                return top;
            }else if(child == VBelowView){
                Log.e("willzhang"," 下边view ");
                return top;
            }
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //状态：手指在above上：下滑：没有到头
            //                        到头
            //                   上拉：没到底部
            //                        露出底部
            //     手指在Below上：下滑：露出above底部
            //                   上拉：没有动作
            int top=0;
//                if (yvel > 0) {//下滑
//                    if (VAboveView.getTop() >= 0) {//手指在above上,bottom低于底部，top低于顶部
//                        top = 0;
//                    } else if (VAboveView.getTop() > -mTopViewHeight && VAboveView.getTop() < -mTopViewHeight + getHeight()) {//这时候手指在below上,bottom在头顶 && bottom在底部
//                        top = -(mTopViewHeight - getHeight());
//                        if (top >= 0) {
//                            top = 0;
//                        }
//                    } else {//top高于顶部，bottom低于底部
//                        top = 0;
//                    }
//                } else if (yvel<0) {//上拉
//                    if (VAboveView.getTop() >= 0) {//手指在above上,bottom低于底部，top低于顶部
//                        top = 0;
//                    } else if (VAboveView.getTop() >= -mTopViewHeight && VAboveView.getTop() <= -mTopViewHeight + getHeight()) {//这时候手指在below上,bottom在头顶 && bottom在底部
//                        top = -mTopViewHeight;
//                    } else {//top高于顶部，bottom低于底部
//                        top = -(mTopViewHeight - getHeight());
//                        if (top >= 0) {
//                            top = 0;
//                        }
//                    }
//                }
//                mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
//                postInvalidate();

            if(yvel>0) {//下滑
                if (VAboveView.getTop() < 0) {//top在顶上；top在顶下的情况暂不考虑（目前这种情况不可能）
                    if(VAboveView.getTop() > -mTopViewHeight && VAboveView.getTop() < -mTopViewHeight + getHeight()) {//bottom在底上
                        //top底部固定在底部（如果ABove不够一屏，固定在顶部）
                        top = -(mTopViewHeight - getHeight());
                        if (top >= 0) {
                            top = 0;
                        }
                        mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                        postInvalidate();
                    }else if(VAboveView.getTop() > -mTopViewHeight + getHeight()){//bottom在底下
                        //滑动
                        mDragHelper.flingCapturedView(0,-(mTopViewHeight-getHeight()),0,0);
                        invalidate();
                    }

                }else{//目前这种情况
                    mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), 0);
                    postInvalidate();
                }
            }else{//上拉
                if (VAboveView.getTop() < 0) {
                    if (VAboveView.getTop() > -mTopViewHeight && VAboveView.getTop() < -mTopViewHeight + getHeight()) {//bottom在底上
                        //top底部固定在顶
                        top = -mTopViewHeight;
                        mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                        postInvalidate();
                    }else if(VAboveView.getTop() > -mTopViewHeight + getHeight()){//bottom在底下
                        //滑动
                        mDragHelper.flingCapturedView(0,-(mTopViewHeight-getHeight()),0,0);
                        invalidate();
                    }
                }
            }

        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private float mLastY;

    //在这个方法中，DOWN事件要使用mDragHelper.shouldInterceptTouchEvent(ev)，因为这个Draghelper会在这个函数里在DOWN时，初始化一些变量，防止后面在使用mDragHelper.processTouchEvent(event) 出现空指针错误
    //MOVE事件自己来判断，如果手指在上部时，直接返回false；如果手指在下边view时，判断拦截时机
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE: //此时的事件序列是listview在处理，这里判断要不要截断，为我所用
                float dy = y - mLastY;
                if (Math.abs(dy) > mTouchSlop) {
                    View child = mDragHelper.findTopChildUnder((int)ev.getX(),(int)ev.getY());
                    if(dy>0 && child == VBelowView && mBelowListener.canViewPullDown()){
                        return true;
                    }
                    if(dy<0 && child == VBelowView &&
                            (mBelowListener.canViewPullUP()||VAboveView.getTop() >=0)){
                        return true;
                    }
                    return false;
                }else{
                    return false;
                }
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    ///////////////////////////////////////////////
    //下边view要实现这个监听
    public interface BelowTouchListener{
        boolean canViewPullDown();
        boolean canViewPullUP();
    }

    private BelowTouchListener mBelowListener;

    public void setBelowListener(BelowTouchListener belowListener){
        mBelowListener = belowListener;
    }
}

