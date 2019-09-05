package com.study.ui_master

import android.app.Activity
import android.os.Bundle

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
//        setContentView(ColorFilterView(this))

        //平移转换操作
//        setContentView(TransformView(this))

        //canvas保存重置操作
//        setContentView(SaveAndRestoreCanvas(this))
        //canvas 实现粒子爆炸效果
//        setContentView(SplitImageView(this))
//        layoutInflater
//        SurfaceView
//        TextInputLayout
//        setContentView(MoveView(this))
        setContentView(CustomEditText(this))
    }
}
