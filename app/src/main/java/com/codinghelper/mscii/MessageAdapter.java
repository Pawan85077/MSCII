package com.codinghelper.mscii;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    public MessageAdapter(List<Messages>userMessageList){
        this.userMessageList=userMessageList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessageText,receiverMessageText;
        public CircularImageView receiverProfileImage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText=(TextView)itemView.findViewById(R.id.sender_message_text);
            receiverMessageText=(TextView)itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage=(CircularImageView)itemView.findViewById(R.id.message_profile_image);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout,parent,false);
        mAuth=FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
    String messageSenderID=mAuth.getCurrentUser().getUid();
    Messages messages=userMessageList.get(position);
    String fromUserID=messages.getFrom();
    String fromMessageType=messages.getType();
    usersRef= FirebaseDatabase.getInstance().getReference().child("studentDetail").child(fromUserID);
    usersRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild("imageUrl")){
                String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.receiverProfileImage);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    if(fromMessageType.equals("text")){
       holder.receiverMessageText.setVisibility(View.INVISIBLE);
       holder.receiverProfileImage.setVisibility(View.INVISIBLE);
       holder.senderMessageText.setVisibility(View.INVISIBLE);

        if(fromUserID.equals(messageSenderID)){
           holder.senderMessageText.setVisibility(View.VISIBLE);
           holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
           holder.senderMessageText.setTextColor(Color.BLACK);
           holder.senderMessageText.setText(messages.getMessage());
       }else{
           holder.receiverProfileImage.setVisibility(View.VISIBLE);
           holder.receiverMessageText.setVisibility(View.VISIBLE);
           holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_messages_layout);
           holder.receiverMessageText.setTextColor(Color.BLACK);
           holder.receiverMessageText.setText(messages.getMessage());
       }
    }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }


}
