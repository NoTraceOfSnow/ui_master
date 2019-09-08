package com.study.ui_master;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MoveView extends View {
    private Bitmap mBitmap;
    private Paint mPaint;
    private int cL;//记录左边
    private int cT;//记录上边
    private int offx;//记录点击偏差值
    private int offy;//记录点击偏差值
    private boolean downOut = false;

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
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl, options);
        cL = 0;
        cT = 0;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, cL, cT, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isContains(event.getX(), event.getY())) {
                downOut = true;
                Toast.makeText(getContext(), "点在外面", Toast.LENGTH_SHORT).show();
            } else {
                offx = (int) (event.getX() - cL);
                offy = (int) (event.getX() - cT);
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (downOut) {
                return true;
            }
            cL = (int) event.getX() - offx;
            cT = (int) event.getY() - offy;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            downOut = false;
        }
        //拦截onTouch事件
        return true;
    }

    private boolean isContains(float x, float y) {
        Region region = new Region(cL, cT, cL + mBitmap.getWidth(), cT + mBitmap.getHeight());
        return region.contains((int) x, (int) y);
    }
}
