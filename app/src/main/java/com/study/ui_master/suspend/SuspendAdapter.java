package com.study.ui_master.suspend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.study.ui_master.R;

public class SuspendAdapter extends RecyclerView.Adapter<SuspendAdapter.SuspendViewHolder> {

    public SuspendAdapter() {
    }

    @NonNull
    @Override
    public SuspendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item_suspend, parent, false);
        return new SuspendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuspendViewHolder holder, int position) {
        holder.img_content.setTag(getContent(position));
        holder.img_avatar.setTag(getAvatar(position));
        holder.tv_title.setText("条目:" + position);
        holder.img_content.setImageDrawable(ContextCompat.getDrawable(holder.context, (Integer) holder.img_content.getTag()));
        holder.img_avatar.setImageDrawable(ContextCompat.getDrawable(holder.context, (Integer) holder.img_avatar.getTag()));
    }

    @Override
    public int getItemCount() {
        return 100;
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

    private int getContent(int position) {
        switch (position % 4) {
            case 1:
                return R.drawable.taeyeon_two;
            case 2:
                return R.drawable.taeyeon_three;
            case 3:
                return R.drawable.taeyeon_four;
            default:
                return R.drawable.taeyeon_one;
        }
    }


    public static class SuspendViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_avatar;
        private TextView tv_title;
        private ImageView img_content;
        private Context context;

        public SuspendViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            img_avatar = itemView.findViewById(R.id.img_item_avatar);
            tv_title = itemView.findViewById(R.id.tv_item_title);
            img_content = itemView.findViewById(R.id.img_item_content);
        }
    }
}
