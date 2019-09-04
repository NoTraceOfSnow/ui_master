package com.study.ui_master.suspend;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.ui_master.R;

public class SuspendActivity extends Activity {
    private RecyclerView rv_content;
    private RelativeLayout rl_parent;
    private ImageView img_avatar;
    private TextView tv_title;
    private int currentPosition = 0;
    private int suspendHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_activity_suspend);
        rv_content = findViewById(R.id.rv_content);
        rl_parent = findViewById(R.id.rl_parent);
        img_avatar = findViewById(R.id.img_avatar);
        tv_title = findViewById(R.id.tv_title);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        rv_content.setAdapter(new SuspendAdapter());
        rv_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                suspendHeight = rl_parent.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                View view = layoutManager.findViewByPosition(currentPosition + 1);
                if (view.getTop() < suspendHeight) {
                    rl_parent.setY(-(suspendHeight - view.getTop()));
                } else {
                    rl_parent.setY(0);
                }

                if (layoutManager.findFirstVisibleItemPosition() != currentPosition) {
                    currentPosition = layoutManager.findFirstVisibleItemPosition();
                    updateSuspend();
                }
            }
        });
        updateSuspend();
    }

    private void updateSuspend() {
        tv_title.setText("条目" + currentPosition);
        img_avatar.setImageDrawable(ContextCompat.getDrawable(this, getAvatar(currentPosition)));
    }

    private int getAvatar(int position) {
        switch (position % 4) {
            case 1:
                return R.drawable.avatar2;
            case 2:
                return R.drawable.avatar3;
            case 3:
                return R.drawable.avatar4;
            default:
                return R.drawable.avatar1;
        }
    }
}
