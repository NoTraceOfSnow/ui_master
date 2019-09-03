package com.dream.lib;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View {
    //存放子控件
    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    private TouchTarget mFirstTarget;

    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println(toString() + "  ViewGroup  dispatchTouchEvent");
        int actionMasked = event.getActionMasked();
        boolean intercept = onInterceptTouchEvent(event);
        boolean handle = false;
        TouchTarget newTarget;
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercept) {
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                for (int i = mChildren.length - 1; i >= 0; i--) {
                    View view = mChildren[i];
                    //不能接受事件
                    if (!view.isContain(event.getX(), event.getY())) {
                        continue;
                    }
                    //能够接受事件
                    if (dispatchTransformedTouchEvent(event, view)) {
                        handle = true;
                        newTarget = addTouchTarget(view);
                        break;
                    }
                }
            }
        }
        if (mFirstTarget == null) {
            handle = dispatchTransformedTouchEvent(event, null);
        }
        return handle;
    }

    private TouchTarget addTouchTarget(View view) {
        TouchTarget target = TouchTarget.obtain(view);
        target.next = mFirstTarget;
        mFirstTarget = target;
        return mFirstTarget;
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, View view) {
        boolean handled = false;
        if (view != null) {
            handled = view.dispatchTouchEvent(event);
        } else {
            handled = super.dispatchTouchEvent(event);
        }
        return handled;
    }

    private boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    private static final class TouchTarget {
        private View child;//当前缓存View
        //回收池
        private static TouchTarget sRecycleBin;
        private static int sRecycleCount;
        private TouchTarget next;
        private static final Object sRecycleLock = new boolean[0];

        //取出对象
        public static TouchTarget obtain(View child) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycleCount--;
                target.next = null;
            }
            target.child = child;
            return target;
        }

        //回收改对象
        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("View已经被回收");
            }
            synchronized (sRecycleLock) {
                if (sRecycleCount < 32) {
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycleCount++;
                }
            }
        }
    }
}
