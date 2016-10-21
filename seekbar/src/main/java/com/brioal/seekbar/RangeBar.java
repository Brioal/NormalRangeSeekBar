package com.brioal.seekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Range Select Bar
 * Created by Brioal on 2016/9/14.
 */
public class RangeBar extends View {
    private int mHeight = 0; // 组件高度
    private int mWidth = 0; // 组件宽度
    private int mCircleRadius; //圆形的半径
    private int mLineColor; //线条的颜色
    private int mCenterColor; //选中圆形的颜色
    private Paint mPaint;
    private int mTextSize; //文字大小
    private float mSingleWidth = 0; //每个刻度的宽度
    private OnRangeChangedListener mListener;
    private float mStartX; //起始点坐标
    private float mEndX; //终止点坐标

    private int mBeginValue = 0; // 计数开始的值
    private int mFinishValue = 0; // 计数结束的值

    private int mStartValue = 0; //开始的值
    private int mEndValue = 0; //结束的值
    private String mIndex = ""; //后缀

    private float mStartProgress = 0; //开始的进度
    private float mEndProgress = 0; // 结束的进度

    public RangeBar(Context context) {
        this(context, null);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //设置后缀
    public void setIndex(String index) {
        mIndex = index;
    }

    //设置起始值
    public void setBeginValue(int beginValue) {
        mBeginValue = beginValue;
        mStartValue = mBeginValue;
        mStartProgress = 0;
    }

    //设置结束值
    public void setFinishValue(int finishValue) {
        mFinishValue = finishValue;
        mEndValue = mFinishValue;
        mEndProgress = 1.0f;
    }

    //设置开始的初始值
    public void setInitValue(int startValue, int endValue) {
        mStartValue = startValue;
        mStartProgress = (mStartValue - mBeginValue) * 1.0f / (mFinishValue - mBeginValue);
        mEndValue = endValue;
        mEndProgress = (mEndValue - mBeginValue) * 1.0f / (mFinishValue - mBeginValue);
        getRightPosition();
    }

    //根据百分比计算开始的坐标和结束的坐标
    private void getRightPosition() {
        mStartX = mCircleRadius * 2 + ((int) (mStartProgress * 100)) * mSingleWidth; //起点坐标
        mEndX = mCircleRadius * 2 + ((int) (mEndProgress * 100)) * mSingleWidth;
        invalidate();
    }

    //设置范围监听器
    public void setRangeChangeListener(OnRangeChangedListener listener) {
        mListener = listener;
    }


    private void init() {
        mCircleRadius = (int) SizeUtil.Dp2Px(getContext(), 15);
        mCenterColor = getResources().getColor(R.color.colorPoint);
        mLineColor = getResources().getColor(android.R.color.black);
        mTextSize = (int) SizeUtil.Sp2Px(getContext(), 15);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    private boolean isStartMoving = false;
    private boolean isEndMoving = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if ((Math.abs(mStartX - mCircleRadius - x) <= mCircleRadius)) {
                    isStartMoving = true;
                    break;
                }
                if ((Math.abs(mEndX + mCircleRadius - x) <= mCircleRadius)) {
                    isEndMoving = true;
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                isStartMoving = false;
                isEndMoving = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isStartMoving) {
                    mStartX = x;
                    if (mStartX >= mEndX - mSingleWidth) {
                        mStartX = mEndX - mSingleWidth;
                        Log.i("SingleWidth", mSingleWidth + "");
                    }
                    if (mStartX - mCircleRadius * 2 < 0) {
                        mStartX = mCircleRadius * 2;
                    }
                    Log.i("StartX", mStartX + "");

                }
                if (isEndMoving) {
                    mEndX = x;
                    if (mEndX < mStartX + mSingleWidth) {
                        mEndX = mStartX + mSingleWidth;
                    }
                    if (mEndX + mCircleRadius * 2 > mWidth) {
                        mEndX = mWidth - mCircleRadius * 2;
                    }
                    Log.i("EndX", mEndX + "");

                }
                getRightValue();//获取正确的值
                invalidate();
                break;
        }

        return true;
    }

    //根据坐标获取正确的值
    public void getRightValue() {
        getRightProgress();
        mStartValue = (int) (mBeginValue + (mFinishValue - mBeginValue) * mStartProgress);
        mEndValue = (int) (mBeginValue + (mFinishValue - mBeginValue) * mEndProgress);
        if (mStartValue == mEndValue) {
            if (isStartMoving) {
                mStartValue -= 1;
            } else if (isEndMoving) {
                mEndValue += 1;
            }
        }
        Log.i("Value", mStartValue + ":" + mEndValue);
    }

    //根据坐标获取正确的进度
    public void getRightProgress() {
        mStartProgress = (mStartX - 2 * mCircleRadius) * 1.0f / mSingleWidth / 100;
        mEndProgress = (mEndX - 2 * mCircleRadius) * 1.0f / mSingleWidth / 100;
        Log.i("Progress", mStartProgress + ":" + mEndProgress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = (int) (SizeUtil.Dp2Px(getContext(), 100) + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(mWidth, mHeight);
        mSingleWidth = ((mWidth * 1.0f - 4 * mCircleRadius) / 100); //每一个刻度的宽度
        getRightPosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆角矩形
        RectF lineRect = new RectF(0, mHeight / 2 - 10, mWidth, mHeight / 2 + 10);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mLineColor);
        canvas.drawRoundRect(lineRect, 20, 20, mPaint);
        //绘制中间的线
        mPaint.setColor(mCenterColor);
        RectF doneRect = new RectF(mStartX - mCircleRadius / 2, mHeight / 2 - 10, mEndX + mCircleRadius / 2, mHeight / 2 + 10);
        canvas.drawRoundRect(doneRect, 20, 20, mPaint);
        //按照进度绘制位置
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCenterColor);
        canvas.drawCircle(mStartX - mCircleRadius, mHeight / 2, mCircleRadius, mPaint);
        canvas.drawCircle(mEndX + mCircleRadius, mHeight / 2, mCircleRadius, mPaint);
        mPaint.setTextSize(25);
        if (mListener != null) {
            mListener.selected(mStartValue, mEndValue);
        }
        String start = mStartValue + mIndex;
        String end = mEndValue + mIndex;
        Rect startText = new Rect();
        Rect endText = new Rect();
        mPaint.getTextBounds(start, 0, start.length(), startText);
        mPaint.getTextBounds(end, 0, end.length(), endText);
        //绘制文字
        mPaint.setColor(Color.WHITE);
        canvas.drawText(start, mStartX - mCircleRadius - (startText.right - startText.left) / 2, mHeight / 2 - (startText.bottom + startText.top) / 2, mPaint);
        canvas.drawText(end, mEndX + mCircleRadius - (endText.right + endText.left) / 2, mHeight / 2 - (endText.bottom + endText.top) / 2, mPaint);

    }
}
