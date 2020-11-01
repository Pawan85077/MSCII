package com.codinghelper.mscii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendChat extends Fragment {
    private View friendsView;
    private RecyclerView myFriendList;
    private DatabaseReference Friendsref,UserRef;
    private FirebaseAuth auth;
    private String currentUserID;
    ProgressDialog progressDialog;
    CircularImageView userStatusImage;
    LinearLayout Linear_status;
    public FriendChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friendsView= inflater.inflate(R.layout.fragment_friend_chat, container, false);
        myFriendList=(RecyclerView)friendsView.findViewById(R.id.friend_list);
        Linear_status=(LinearLayout)friendsView.findViewById(R.id.status_cantain);
        myFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        auth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        currentUserID=auth.getCurrentUser().getUid();
        Friendsref= FirebaseDatabase.getInstance().getReference().child("Friend list").child(currentUserID);
        UserRef= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        userStatusImage=(CircularImageView)friendsView.findViewById(R.id.user_profile_status);

        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(userStatusImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Linear_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Simg1 =String.valueOf(dataSnapshot.child("states1").getValue());
                        String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());
                        if(Simg1.equals("no")&&Simg2.equals("no")){
                            Intent profileIntent=new Intent(getActivity(),upload_status.class);
                            startActivity(profileIntent);
                        }else {
                            Intent profileIntent=new Intent(getActivity(),Status_viewer.class);
                            profileIntent.putExtra("visit_user_id",currentUserID);
                            startActivity(profileIntent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        return friendsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(Friendsref,Friends.class)
                .build();
        FirebaseRecyclerAdapter<Friends,FriendsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull Friends model) {
                final String userIDs=getRef(position).getKey();
                UserRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("imageUrl")){
                            String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                            String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                            String Sstatus =String.valueOf(dataSnapshot.child("userstatus").getValue());
                            String pic1 =String.valueOf(dataSnapshot.child("states1").getValue());
                            String pic2 =String.valueOf(dataSnapshot.child("states2").getValue());
                            if(pic1.equals("no")&&pic2.equals("no")){
                                holder.linearLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                            }else {
                                holder.linearLayout.setBackgroundColor(getResources().getColor(R.color.amber_500));

                            }

                                holder.userName.setText(Sname);
                            holder.userStatus.setText(Sstatus);
                            Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.profileImage);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    UserRef.child(userIDs).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Simg1 =String.valueOf(dataSnapshot.child("states1").getValue());
                                            String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());
                                            if(Simg1.equals("no")&&Simg2.equals("no")){
                                                progressDialog.show();
                                                Intent profileIntent=new Intent(getActivity(),Show_profile_Activity.class);
                                                profileIntent.putExtra("visit_user_id",userIDs);
                                                startActivity(profileIntent);
                                                progressDialog.dismiss();
                                            }else {
                                                Intent profileIntent=new Intent(getActivity(),Status_viewer.class);
                                                profileIntent.putExtra("visit_user_id",userIDs);
                                                startActivity(profileIntent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });




                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                FriendsViewHolder viewHolder=new FriendsViewHolder(view);
                return viewHolder;
            }
        };
        myFriendList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userStatus;
        CircularImageView profileImage;
        LinearLayout linearLayout;
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_profile_name);
            userStatus=itemView.findViewById(R.id.user_profile_status);
            profileImage=itemView.findViewById(R.id.user_profile_image);
            linearLayout=itemView.findViewById(R.id.lback);

        }
    }
}
