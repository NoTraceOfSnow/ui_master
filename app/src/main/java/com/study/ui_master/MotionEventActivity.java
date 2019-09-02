package com.study.ui_master;

import com.dream.lib.MotionEvent;
import com.dream.lib.View;
import com.dream.lib.ViewGroup;
import com.dream.lib.listener.OnClickListener;

public class MotionEventActivity {
    public static void main(String[] args) {
        ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("顶层容器");
        ViewGroup viewGroup1 = new ViewGroup(0, 0, 600, 600);
        viewGroup1.setName("二级容器");
        View view = new View(0, 0, 300, 300);
        view.setName("子控件");

        viewGroup1.addView(view);
        viewGroup.addView(viewGroup1);
        view.setClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("点击事件");
            }
        });
        //模拟点击事件
        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);
        viewGroup.dispatchTouchEvent(motionEvent);
    }
}
