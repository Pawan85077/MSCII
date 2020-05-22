package com.codinghelper.mscii;

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
import android.widget.TextView;

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
    public FriendChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friendsView= inflater.inflate(R.layout.fragment_friend_chat, container, false);
        myFriendList=(RecyclerView)friendsView.findViewById(R.id.friend_list);
        myFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        auth=FirebaseAuth.getInstance();
        currentUserID=auth.getCurrentUser().getUid();
        Friendsref= FirebaseDatabase.getInstance().getReference().child("Friend list").child(currentUserID);
        UserRef= FirebaseDatabase.getInstance().getReference().child("studentDetail");

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
                String userIDs=getRef(position).getKey();
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
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_profile_name);
            userStatus=itemView.findViewById(R.id.user_profile_status);
            profileImage=itemView.findViewById(R.id.user_profile_image);


        }
    }
}
