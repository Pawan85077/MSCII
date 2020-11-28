package com.codinghelper.mscii;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminMessage extends Fragment {

    RecyclerView RecyclerView;
    DatabaseReference Rootref;
    FirebaseAuth firebaseAuth;
    String currentUserID;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_message, container, false);
        Rootref= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        RecyclerView = v.findViewById(R.id.admin_feed_student_recyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();

        Rootref.child("studentDetail").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Ssession =String.valueOf(dataSnapshot.child("session").getValue());
                String Stcourse =String.valueOf(dataSnapshot.child("Scourse").getValue());
                DatabaseReference StudentAdminMessage = FirebaseDatabase.getInstance().getReference().child("adminMessages").child(Ssession).child(Stcourse);

                FirebaseRecyclerOptions<Admin_Home_Getter_Setter> options=
                        new FirebaseRecyclerOptions.Builder<Admin_Home_Getter_Setter>()
                                .setQuery(StudentAdminMessage,Admin_Home_Getter_Setter.class)
                                .build();

                FirebaseRecyclerAdapter<Admin_Home_Getter_Setter, AdminHomeFragment.AdminMessageViewHolder> adapter=
                        new FirebaseRecyclerAdapter<Admin_Home_Getter_Setter, AdminHomeFragment.AdminMessageViewHolder>(options) {

                            @Override
                            protected void onBindViewHolder(@NonNull AdminHomeFragment.AdminMessageViewHolder holder, int position, @NonNull Admin_Home_Getter_Setter model) {
                                holder.senderDepartmentName.setText(model.getSenderDepartmentName());
                                holder.receiverName.setText(model.getReceiverName());
                                holder.messageTitle.setText(model.getMessageTitle());
                                holder.messageBody.setText(model.getMessageBody());
                                holder.likeCount.setText(model.getLikeCount());
                                holder.dateOfPost.setText(model.getDateOfPost());
                                holder.timeOfPost.setText(model.getTimeOfPost());
                                Picasso.get().load(model.getSenderImage()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.senderImage);
                                holder.likes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(view.getContext(), "Like Clicked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                holder.discussionLinearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(view.getContext(), "Discussion Tab Clicked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public AdminHomeFragment.AdminMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_home_row_item,parent,false);
                                AdminHomeFragment.AdminMessageViewHolder viewHolder=new AdminHomeFragment.AdminMessageViewHolder(view);
                                progressDialog.dismiss();
                                return viewHolder;
                            }
                        };
                RecyclerView.setAdapter(adapter);
                adapter.startListening();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}