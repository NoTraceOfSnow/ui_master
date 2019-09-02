package com.dream.lib.listener;

import com.dream.lib.MotionEvent;
import com.dream.lib.View;

public interface OnTouchListener {
    boolean onTouch(View view, MotionEvent motionEvent);
}
