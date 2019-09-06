package com.study.ui_master.svg_car;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.study.ui_master.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CarSvgView extends View {
    List<CarItem> items = new ArrayList<>();
    Paint mPaint = new Paint();
    private RectF rectF;
    private float scale = 1;


    public CarSvgView(Context context) {
        this(context, null);
    }

    public CarSvgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarSvgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        scale = MeasureSpec.getSize(widthMeasureSpec) / rectF.width();
        System.out.println("" + MeasureSpec.getSize(widthMeasureSpec) + "   " + rectF.width());
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    InputStream inputStream = getContext().getResources().openRawResource(R.raw.car);
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(inputStream);
                    Element element = document.getDocumentElement();
                    NodeList path = element.getElementsByTagName("path");
                    int left = -1;
                    int top = -1;
                    int right = -1;
                    int bottom = -1;
                    for (int i = 0; i < path.getLength(); i++) {
                        Element item = (Element) path.item(i);
                        Path pathData = PathParser.createPathFromPathData(item.getAttribute("d"));
                        items.add(new CarItem(pathData, Color.BLACK));
                        RectF rect = new RectF();
                        pathData.computeBounds(rect, true);
                        left = (int) (left == -1 ? rect.left : Math.min(rect.left, left));
                        top = (int) (top == -1 ? rect.top : Math.min(rect.top, top));
                        right = (int) (right == -1 ? rect.right : Math.max(rect.right, right));
                        bottom = (int) (bottom == -1 ? rect.bottom : Math.max(rect.bottom, bottom));
                    }
                    rectF = new RectF(left, top, right, bottom);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        canvas.drawRect(rectF, mPaint);
//        canvas.translate(-rectF.left * scale, -rectF.top * scale);
        canvas.scale(scale, scale);
        canvas.translate(-rectF.left, -rectF.top);
        for (CarItem item : items) {
            item.draw(canvas, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleTouch(event.getX() / scale + rectF.left, event.getY() / scale + rectF.top);
        }
        return super.onTouchEvent(event);
    }

    private void handleTouch(float eventX, float eventY) {
        if (items == null) {
            return;
        }
        for (CarItem item : items) {
            item.isContains(eventX, eventY);
        }
        postInvalidate();
    }
}
