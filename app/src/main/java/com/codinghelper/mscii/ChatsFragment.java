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
public class ChatsFragment extends Fragment {
    private RecyclerView chatsList;
    private DatabaseReference ChatsRef, UserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);
        chatsList = (RecyclerView) v.findViewById(R.id.friend_chat_list);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ChatsRef = FirebaseDatabase.getInstance().getReference().child("Friend list").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("studentDetail");
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(ChatsRef, Friends.class)
                        .build();

        FirebaseRecyclerAdapter<Friends, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Friends, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Friends model) {
                        final String userIDs = getRef(position).getKey();
                        final String[] retImage = {"default image"};

                        UserRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if (dataSnapshot.hasChild("imageUrl")){
                                        retImage[0] =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                        Picasso.get().load(retImage[0]).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.ProfileImage);

                                    }
                                    final String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                                    holder.userName.setText(Sname);
                                    final String Sstatus =String.valueOf(dataSnapshot.child("userstatus").getValue());
                                    holder.userStatus.setText("last seen: " +"\n"+"Date "+" Time");
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent chatIntent =new Intent(getActivity(),FriendChatActivity.class);
                                            chatIntent.putExtra("visit_user_id",userIDs);
                                            chatIntent.putExtra("visit_user_name",Sname);
                                            chatIntent.putExtra("visit_user_image", retImage[0]);
                                            startActivity(chatIntent);
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
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                        return new ChatsViewHolder(view);
                    }
                };
        chatsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userStatus;
        CircularImageView ProfileImage;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_profile_status);
            ProfileImage = itemView.findViewById(R.id.user_profile_image);

        }
    }
}
