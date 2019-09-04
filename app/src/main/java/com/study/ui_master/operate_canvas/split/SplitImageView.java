package com.study.ui_master.operate_canvas.split;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.Nullable;

import com.study.ui_master.R;

import java.util.ArrayList;
import java.util.List;

public class SplitImageView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private List<Ball> balls = new ArrayList<>();
    private float d = 3;//粒子直径
    private int windowWidth;
    private int windowHeight;

    public SplitImageView(Context context) {
        this(context, null);
    }

    public SplitImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplitImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        windowWidth = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context) {
        mPaint = new Paint();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 6;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl, options);
        for (int i = 0; i < mBitmap.getWidth(); i++) {
            for (int j = 0; j < mBitmap.getHeight(); j++) {
                Ball ball = new Ball();
                ball.setColor(mBitmap.getPixel(i, j));
                ball.setX(i * d + d / 2);
                ball.setY(j * d + d / 2);
                ball.setR(d / 2);
                balls.add(ball);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawArc();
//        SurfaceView.MeasureSpec
//        LayoutInflater.from(getContext()).inflate()
        View view=new View(getContext());
        PathMeasure pathMeasure=new PathMeasure();

        Log.e("sss", "" + (windowWidth / 2 - mBitmap.getWidth() / 2));
        Log.e("sss", "" + (windowHeight / 6 - mBitmap.getHeight() / 2));
        canvas.translate(windowWidth / 2 - mBitmap.getWidth() / 2, windowHeight / 6 - mBitmap.getHeight() / 2);
        for (Ball ball : balls) {
            mPaint.setColor(ball.getColor());
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), mPaint);
        }
    }
}
