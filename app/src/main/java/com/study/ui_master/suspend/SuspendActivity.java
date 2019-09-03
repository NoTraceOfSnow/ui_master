package com.study.ui_master.suspend;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.study.ui_master.R;

public class SuspendActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_activity_suspend);
    }
}
