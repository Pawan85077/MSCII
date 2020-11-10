package com.codinghelper.mscii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Admin_Recycler_Adapter extends RecyclerView.Adapter<Admin_Recycler_Adapter.AdminViewHolder> {

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_home_row_item, parent, false);
        AdminViewHolder adminViewHolder = new AdminViewHolder(view);
        return adminViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class AdminViewHolder extends RecyclerView.ViewHolder {

        ImageView senderImage,likes,comment;
        TextView senderName, receiverName, message, likecount, commentCount, timeAgo;
        LinearLayout discussionLinearLayout;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);

            senderImage = itemView.findViewById(R.id.senderProfilePic);
            likes = itemView.findViewById(R.id.likeImage);
            comment = itemView.findViewById(R.id.commentImage);
            senderName = itemView.findViewById(R.id.senderName);
            receiverName = itemView.findViewById(R.id.receiverName);
            message = itemView.findViewById(R.id.messageBody);
            likecount = itemView.findViewById(R.id.like_Count);
            commentCount = itemView.findViewById(R.id.message_Count);
            timeAgo = itemView.findViewById(R.id.timeAgo);
            discussionLinearLayout = itemView.findViewById(R.id.message_Discussion);



        }
    }
}
