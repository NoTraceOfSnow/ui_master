package com.study.ui_master;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MoveScaleView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private ValueAnimator mValueAnimator;
    private float rotation = 0;
    private int x = 0;
    private int y = 0;
    private float scale = 1;

    public MoveScaleView(Context context) {
        this(context, null);
    }

    public MoveScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl, options);
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(10000);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setFloatValues(0, 1, 0);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                System.out.println(animatedValue);
                rotation = animatedValue * 360;
                y = (int) (100 + animatedValue * 100);
                x = (int) (100 + animatedValue * 100);
                scale = 1 + animatedValue;
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //平移操作
        canvas.translate(x, y);
        //缩放操作
        canvas.scale(scale, scale);
        //旋转操作
        canvas.rotate(rotation, x + mBitmap.getWidth() / 2, y + mBitmap.getHeight()/ 2);
        //绘制图片
        canvas.drawBitmap(mBitmap, x, y, mPaint);
        //绘制中心点
        canvas.drawPoint(x + mBitmap.getWidth() / 2, y + mBitmap.getHeight() / 2, mPaint);
    }

}
