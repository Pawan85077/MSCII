package com.codinghelper.mscii;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class Admin_composeTo_Recycler_Adapter extends RecyclerView.Adapter<Admin_composeTo_Recycler_Adapter.AdminComposeToViewHolder>{

    ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeRecyclerValue_from_addRecipientsList = new ArrayList<>();

    public Admin_composeTo_Recycler_Adapter(ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeRecyclerValue_from_addRecipientsList) {
        this.admin_composeRecyclerValue_from_addRecipientsList = admin_composeRecyclerValue_from_addRecipientsList;
    }

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

        Admin_composeRecyclerValue_from_AddRecipients admin_composeRecyclerValue_from_addRecipients = admin_composeRecyclerValue_from_addRecipientsList.get(position);
        Random rnd = new Random();
        int randomColour = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.parentLL.setBackgroundColor(randomColour);

        holder.textDepartmentName.setText(admin_composeRecyclerValue_from_addRecipients.getDepartmentName());
        holder.textSessionName.setText(admin_composeRecyclerValue_from_addRecipients.getSessionName());

    }

    @Override
    public int getItemCount() {
        return admin_composeRecyclerValue_from_addRecipientsList.size();
    }


    class AdminComposeToViewHolder extends RecyclerView.ViewHolder{

        TextView textDepartmentName,textSessionName;
        LinearLayout parentLL;
        public AdminComposeToViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLL = (LinearLayout)itemView.findViewById(R.id.parentLL);
            textDepartmentName = (TextView)itemView.findViewById(R.id.riciverDepartmentName);
            textSessionName = (TextView)itemView.findViewById(R.id.riciverSessionName);
        }
    }
}
