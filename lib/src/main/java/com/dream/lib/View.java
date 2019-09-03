package com.dream.lib;

import com.dream.lib.listener.OnClickListener;
import com.dream.lib.listener.OnTouchListener;

public class View {
    private int left;
    private int top;
    private int right;
    private int bottom;
    private OnTouchListener touchListener;
    private OnClickListener clickListener;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public View() {
    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    public void setTouchListener(OnTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    protected boolean isContain(int x, int y) {
        if (x > left && x < right && y > top && y < bottom) {
            System.out.println("111");
            return true;
        }
        return false;
    }

    protected boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println(toString() + "  View dispatchTouchEvent");
        boolean result = false;
        if (touchListener != null && touchListener.onTouch(this, event)) {
            result = true;
        }
        if (!result && onTouchEvent(event)) {
            result = true;
        }
        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        System.out.println(toString() + "  View onTouchEvent");
        if (clickListener != null) {
            clickListener.onClick(this);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "name = " + name;
    }
}
