package com.study.ui_master.suspend;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SuspendAdapter extends RecyclerView.Adapter<SuspendAdapter.SuspendViewHolder> {


    @NonNull
    @Override
    public SuspendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SuspendViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SuspendViewHolder extends RecyclerView.ViewHolder{

        public SuspendViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
