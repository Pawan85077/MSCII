package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class Find_friend_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_activity);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Find friends");
        recyclerView=(RecyclerView)findViewById(R.id.ff_recycle);
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Friends> options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(reference,Friends.class)
                .build();
        FirebaseRecyclerAdapter<Friends,FindFriendViewHolder> adapter=
                new FirebaseRecyclerAdapter<Friends, FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, final int position, @NonNull Friends model) {
                       holder.userName.setText(model.getUsername());
                       holder.userStatus.setText(model.getUserstatus());
                       Picasso.get().load(model.getImageUrl()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.ProfileImage);
                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               String visit_user_id=getRef(position).getKey();
                               Intent profileIntent=new Intent(Find_friend_activity.this,Show_profile_Activity.class);
                               profileIntent.putExtra("visit_user_id",visit_user_id);
                               startActivity(profileIntent);
                           }
                       });


                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        FindFriendViewHolder viewHolder=new FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{
        TextView userName,userStatus;
        CircularImageView ProfileImage;
        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_profile_name);
            userStatus=itemView.findViewById(R.id.user_profile_status);
            ProfileImage=itemView.findViewById(R.id.user_profile_image);

        }
    }
}
