package com.study.ui_master

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.study.ui_master.color_filter.ColorFilterView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //渲染
//        setContentView(GradientLayout(this))
        //图层混合模式
//        setContentView(XfermodesView(this))
        //图层混合模式实现，刮刮卡
//        setContentView(XfermodeEraserView(this))
        //颜色过滤器
        setContentView(ColorFilterView(this))
    }
}
