package com.study.ui_master.color_filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.study.ui_master.R;

/**
 * 颜色过滤器
 */
public class ColorFilterView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;

    public ColorFilterView(Context context) {
        this(context, null);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * R' = R * mul.R / 0xff + add.R
         * G' = G * mul.G / 0xff + add.G
         * B' = B * mul.B / 0xff + add.B
         * mul: 0xffffff
         * 前两位表示mul.R
         * 中间两位表示mul.G
         * 后面两位表示mul.B
         * add:0x000000
         * 前两位表示add.R
         * 中间两位表示add.G
         * 后面两位表示add.B
         */
        //表示去除红色
//        ColorFilter mFilter = new LightingColorFilter(0x00ffff, 0x000000);
//        mPaint.setColorFilter(mFilter);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        //表示原色
//        ColorFilter mFilter = new LightingColorFilter(0xffffff, 0x000000);
//        mPaint.setColorFilter(mFilter);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        //绿色更亮
//        ColorFilter mFilter = new LightingColorFilter(0xffffff, 0x003000);
//        mPaint.setColorFilter(mFilter);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        //滤镜加图层混合模式，颜色和图片混合
//        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
//        mPaint.setColorFilter(porterDuffColorFilter);
//        canvas.drawBitmap(mBitmap, 100, 0, mPaint);
        //颜色矩阵，矩阵详解网址 https://www.cnblogs.com/tinytiny/p/3317372.html?tdsourcetag=s_pcqq_aiomsg
        float[] colorMatrix = {
                0.8f, 1.6f, 0.2f, 0, -163.9f,//red
                0.8f, 1.6f, 0.2f, 0, -163.9f,//green
                0.8f, 1.6f, 0.2f, 0, -163.9f, //blue
                0, 0, 0, 1.0f, 0   //alpha
        };
        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(colorMatrix);
//        ColorMatrix cm = new ColorMatrix();
        //亮度调节
//        cm.setScale(2, 1, 1, 1);
        //饱和度调节
//        cm.setSaturation(0);
        //色度调节
//        cm.setRotate(0, 45);
//        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(cm);
        mPaint.setColorFilter(mColorFilter);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }
}
