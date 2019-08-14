package com.study.ui_master

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        This is a bit weird. In the framework, the window sizing attributes control the decor view's size, meaning that any padding is inset for the min/max widths below. We don't control measurement at that level, so we need to workaround it by making sure that the decor view's padding is taken into account.
    }
}
