package com.study.ui_master;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AndroidException;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.study.ui_master.R;
import com.study.ui_master.ui.UIUtils;

/**
 * 自定义密码输入框
 */
public class CustomEditText extends AppCompatEditText {
    private int textLength;
    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);//设置抗锯齿
    private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);//设置抗锯齿
    private int defaultColor = Color.BLACK;
    private int defaultRadius = 8;
    private int passwordLength = 6;
    private int passwordWidth = 10;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint.setStrokeWidth(4);
        borderPaint.setColor(defaultColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        passwordPaint.setStrokeWidth(0);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(defaultColor);
        //去除EditText默认背景色
        setBackgroundColor(Color.TRANSPARENT);
        //设置最长6位
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置密码栏固定高度
        setMeasuredDimension(getMeasuredWidth(), 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //绘制最外边矩形
        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(defaultColor);
        canvas.drawRoundRect(rect, defaultRadius, defaultRadius, borderPaint);
        //绘制中间线条
        for (int i = 1; i < passwordLength; i++) {
            //中间线条x=(总宽度width / 默认密码长度) * i 当前的密码个数
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }
        float cx, cy = height / 2;
        float half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        System.out.println(text.toString());
        this.textLength = text.toString().length();
        invalidate();
    }
}