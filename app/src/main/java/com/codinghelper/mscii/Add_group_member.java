package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Add_group_member extends AppCompatActivity {
    private RecyclerView myFriendList1;
    private DatabaseReference Friendsref,UserRef,Rootref;
    private FirebaseAuth auth;
    private String currentUserID,GroupID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_member);
        myFriendList1=(RecyclerView)findViewById(R.id.friend_list1);
        myFriendList1.setLayoutManager(new LinearLayoutManager(this));
        auth=FirebaseAuth.getInstance();
        GroupID=getIntent().getExtras().get("group_id").toString();
        currentUserID=auth.getCurrentUser().getUid();
        Friendsref= FirebaseDatabase.getInstance().getReference().child("Friend list").child(currentUserID);
        UserRef= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        Rootref= FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(Friendsref,Friends.class)
                        .build();
        FirebaseRecyclerAdapter<Friends, FriendChat.FriendsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Friends, FriendChat.FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendChat.FriendsViewHolder holder, int position, @NonNull Friends model) {
                final String userIDs=getRef(position).getKey();
                UserRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("imageUrl")){
                            String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                            String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                            String Sstatus =String.valueOf(dataSnapshot.child("userstatus").getValue());
                            holder.userName.setText(Sname);
                            holder.userStatus.setText(Sstatus);
                            Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.profileImage);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(Add_group_member.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                        builder.setTitle("Add?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Rootref.child("Group").child(GroupID).child("Participater").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(userIDs)){
                                            Toast.makeText(Add_group_member.this, "Already in Group", Toast.LENGTH_LONG).show();
                                        }else {
                                            Rootref.child("Group").child(GroupID).child("Participater").child(userIDs).setValue("member");
                                            UserRef.child(userIDs).child("InGroup").push().child("saved").setValue(GroupID)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Add_group_member.this, "Added Successfully!!", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public FriendChat.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                FriendChat.FriendsViewHolder viewHolder=new FriendChat.FriendsViewHolder(view);
                return viewHolder;
            }
        };
        myFriendList1.setAdapter(adapter);
        adapter.startListening();
    }
}