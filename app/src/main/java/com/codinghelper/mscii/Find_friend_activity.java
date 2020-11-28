package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Find_friend_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    String currentUserID;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_activity);
       // updateUserStatus("online");

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Find friends");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        recyclerView=(RecyclerView)findViewById(R.id.ff_recycle);
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this,R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        FirebaseRecyclerOptions<Friends> options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(reference,Friends.class)
                .build();

        FirebaseRecyclerAdapter<Friends,FindFriendViewHolder> adapter=
                new FirebaseRecyclerAdapter<Friends, FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FindFriendViewHolder holder, final int position, @NonNull Friends model) {
                        String v_user_id=getRef(position).getKey();
                        reference.child(v_user_id).child("userOnlineState").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Sstate =String.valueOf(dataSnapshot.child("state").getValue());
                                if(Sstate.equals("online")){
                                    holder.online.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
                        progressDialog.dismiss();
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{
        TextView userName,userStatus;
        CircularImageView ProfileImage;
        ImageView online;
        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            online=itemView.findViewById(R.id.user_online);
            userName=itemView.findViewById(R.id.user_profile_name);
            userStatus=itemView.findViewById(R.id.user_profile_status);
            ProfileImage=itemView.findViewById(R.id.user_profile_image);

        }
    }




   /* private void updateUserStatus(String state) {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        HashMap<String,Object> onlineState=new HashMap<>();
        onlineState.put("time",saveCurrentTime);
        onlineState.put("date",saveCurrentDate);
        onlineState.put("state",state);

       // currentUserID=firebaseAuth.getCurrentUser().getUid();
        reference.child(currentUserID).child("userOnlineState").updateChildren(onlineState);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateUserStatus("online");
    }*/
}
