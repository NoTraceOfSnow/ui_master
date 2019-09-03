package com.study.ui_master;

import android.os.Build;
import android.os.Bundle;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayCutoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (hasDisplayCutout(window)) {
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                return;
            }
            //2.让内容区域延伸进刘海屏
            WindowManager.LayoutParams params = window.getAttributes();
            /**
             *LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT 全屏模式，内容下移，非全屏不受影响
             *LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER   允许内容延伸到刘海去
             *LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 不允许内容延伸到刘海屏
             */
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;

            window.setAttributes(params);

            //3.设置沉浸式
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility |= flags; //追加沉浸式
            window.getDecorView().setSystemUiVisibility(visibility);
        }

    }

    private boolean hasDisplayCutout(Window window) {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            return false;
        }
        //判断手机是否是刘海屏
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        WindowInsets insets = rootView.getRootWindowInsets();
        if (insets != null) {
            displayCutout = insets.getDisplayCutout();
            if (displayCutout != null) {
                if (displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0 && displayCutout.getSafeInsetTop() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
