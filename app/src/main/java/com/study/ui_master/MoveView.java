package com.study.ui_master;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

public class MoveView extends View {
    private Bitmap mBitmap;
    private Paint mPaint;
    private int downX;//记录按下时候x坐标
    private int downY;//记录按下时候y坐标
    private int cL;//记录左边
    private int cT;//记录上边
    private int cR;//记录右边
    private int cB;//记录下边

    public MoveView(Context context) {
        this(context, null);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl, options);
        cB = mBitmap.getHeight();
        cL = 0;
        cR = mBitmap.getWidth();
        cT = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        System.out.println(mBitmap.getWidth() + "    " + mBitmap.getHeight());
        //设置宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 100 - mBitmap.getWidth() / 2, 100 - mBitmap.getHeight() / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            downY = (int) event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int moveX = (int) (event.getX() - downX);//计算x轴移动多少
            int moveY = (int) (event.getY() - downY);//计算y轴移动多少
            cB += moveY;//计算
            cT += moveY;
            cL += moveX;
            cR += moveX;
            this.layout(cL, cT, cR, cB);
        }
        //拦截onTouch事件
        return true;
    }
}
