package com.study.ui_master;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * view加载流程
 * 调用LayoutInflater.inflate
 * 子控件的layoutParams是怎么初始化的，请看View_Inflater.md文件
 */
public class PercentLayout extends RelativeLayout {
    public PercentLayout(Context context) {
        super(context);
    }

    public PercentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取父容器尺寸
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (checkLayoutParams(layoutParams)) {
                PercentLayoutParam param = (PercentLayoutParam) layoutParams;
                //如果是百分比设置
                float leftMargin = param.leftMargin;
                float topMargin = param.topMargin;
                float bottomMargin = param.bottomMargin;
                float rightMargin = param.rightMargin;
                float widthPercent = param.widthPercent;
                float heightPercent = param.heightPercent;
                if (widthPercent > 0) {
                    param.width = (int) (widthPercent * wSize);
                }
                if (widthPercent > 0) {
                    param.height = (int) (heightPercent * wSize);
                }
                if (widthPercent > 0) {
                    param.leftMargin = (int) (leftMargin * wSize);
                }
                if (widthPercent > 0) {
                    param.topMargin = (int) (topMargin * wSize);
                }
                if (widthPercent > 0) {
                    param.rightMargin = (int) (rightMargin * wSize);
                }
                if (widthPercent > 0) {
                    param.bottomMargin = (int) (bottomMargin * wSize);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PercentLayoutParam(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof PercentLayoutParam;
    }

    private static class PercentLayoutParam extends RelativeLayout.LayoutParams {
        private float leftMargin;
        private float topMargin;
        private float bottomMargin;
        private float rightMargin;
        private float widthPercent;
        private float heightPercent;

        public PercentLayoutParam(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            widthPercent = typedArray.getFloat(R.styleable.PercentLayout_widthPercent, 0);
            heightPercent = typedArray.getFloat(R.styleable.PercentLayout_heightPercent, 0);
            leftMargin = typedArray.getFloat(R.styleable.PercentLayout_marginLeftPercent, 0);
            topMargin = typedArray.getFloat(R.styleable.PercentLayout_marginTopPercent, 0);
            rightMargin = typedArray.getFloat(R.styleable.PercentLayout_marginRightPercent, 0);
            bottomMargin = typedArray.getFloat(R.styleable.PercentLayout_marginBottomPercent, 0);
            typedArray.recycle();
        }
    }

}
