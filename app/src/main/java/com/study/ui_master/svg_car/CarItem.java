package com.study.ui_master.svg_car;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

import javax.xml.transform.Source;

public class CarItem {
    private Path path;
    private int color = Color.BLACK;
    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public CarItem(Path path, int color) {
        this.path = path;
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(Canvas canvas, Paint mPaint) {
        mPaint.setColor(getColor());
        if (isSelect()) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawPath(getPath(), mPaint);
    }

    public void isContains(float x, float y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region rectRegion = new Region();
        rectRegion.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        Region region = new Region();
        region.setPath(path, rectRegion);
        if (region.contains((int) x, (int) y)) {
            isSelect = !isSelect;
        }
    }
}
