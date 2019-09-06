package com.study.ui_master;

import android.app.Activity;
import android.content.Context;
import android.gesture.GestureUtils;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class ImageStateBarActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_activity_statebar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4
            //设置沉浸式状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置虚拟导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            int visibility = getWindow().getDecorView().getSystemUiVisibility();
            //布局内容全屏展示
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //隐藏虚拟导航栏
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //防止布局内容区域大小发生变化
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(visibility);
        }

    }

    private int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    private void setHeightAndPadding(Context context, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += getStatusBarHeight(context);
        view.setLayoutParams(layoutParams);
        view.setPadding(view.getLeft(), view.getTop() + getStatusBarHeight(context), view.getRight(), view.getBottom());
    }
}
