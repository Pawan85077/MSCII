package com.codinghelper.mscii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Admin_composeTo_Recycler_Adapter extends RecyclerView.Adapter<Admin_composeTo_Recycler_Adapter.AdminComposeToViewHolder>{

    @NonNull
    @Override
    public AdminComposeToViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_compose_to_row_item, parent, false);
        AdminComposeToViewHolder adminComposeToViewHolder = new AdminComposeToViewHolder(view);
        return adminComposeToViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminComposeToViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class AdminComposeToViewHolder extends RecyclerView.ViewHolder{

        public AdminComposeToViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
