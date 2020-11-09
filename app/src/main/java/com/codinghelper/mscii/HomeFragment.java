package com.codinghelper.mscii;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference root;
    private FirebaseAuth mAuth;
    private String currentUserId;
    MediaPlayer player;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        player =new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        recyclerView=(RecyclerView)v.findViewById(R.id.user_update);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        root= FirebaseDatabase.getInstance().getReference();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseRecyclerOptions<watch> options=
                new FirebaseRecyclerOptions.Builder<watch>()
                        .setQuery(root.child("PublicView"),watch.class)
                        .build();
        FirebaseRecyclerAdapter<watch, HomeFragment.QuestionViewHolder> adapter=
                new FirebaseRecyclerAdapter<watch, HomeFragment.QuestionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final HomeFragment.QuestionViewHolder holder, int position, @NonNull final watch model) {

                        String public_id=getRef(position).getKey();

                     // holder.username.setText(model.getPublicUserName());
                     // Picasso.get().load(model.getPublicImgURL()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.userImage);
                      holder.userBigImage.setVisibility(View.INVISIBLE);
                      holder.userBio.setVisibility(View.INVISIBLE);
                      holder.UserSong.setVisibility(View.INVISIBLE);
                      root.child("studentDetail").child(model.getpeopleUID()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                              String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                              Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.userImage);
                              holder.username.setText(Sname);
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });

                      root.child("PublicView").child(public_id).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if(dataSnapshot.child("publicImage").exists()){
                                  holder.userBigImage.setVisibility(View.VISIBLE);
                                  String Simg =String.valueOf(dataSnapshot.child("publicImage").getValue());
                                  Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.userBigImage);

                              }
                              if(dataSnapshot.child("publicBio").exists()){
                                  holder.userBio.setVisibility(View.VISIBLE);
                                  String SBio =String.valueOf(dataSnapshot.child("publicBio").getValue());
                                  holder.userBio.setText(SBio);
                              }
                              if(dataSnapshot.child("publicSong").exists()){
                                  holder.UserSong.setVisibility(View.VISIBLE);
                                 // String Ssong =String.valueOf(dataSnapshot.child("publicSong").getValue());

                                  holder.UserSong.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {


                                          Intent profileIntent=new Intent(getActivity(),Show_profile_Activity.class);
                                          profileIntent.putExtra("visit_user_id",model.getpeopleUID());
                                          startActivity(profileIntent);

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
                    public HomeFragment.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home_feed_view,parent,false);
                        HomeFragment.QuestionViewHolder viewHolder=new HomeFragment.QuestionViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView userBio;
        ImageView userBigImage;
        CircularImageView userImage;
        ImageButton UserSong;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.user_updated_name);
            userBio=itemView.findViewById(R.id.user_updated_bio);
            userImage=itemView.findViewById(R.id.user_updated_image);
            userBigImage=itemView.findViewById(R.id.user_updated_big_image);
            UserSong=itemView.findViewById(R.id.user_updated_song);
        }

    }
}
