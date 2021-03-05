package com.denchic45.appbarcontroller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleHolder> {

    private String[] list;

    public SimpleAdapter(String[] list) {
        this.list = list;
    }

    public String[] getList() {
        return list;
    }

    public SimpleAdapter setList(String[] list) {
        this.list = list;
        return this;
    }

    @NonNull
    @Override
    public SimpleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SimpleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleHolder holder, int position) {
        ((TextView) holder.itemView).setText(list[position]);

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public static class SimpleHolder extends RecyclerView.ViewHolder {

        public SimpleHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
