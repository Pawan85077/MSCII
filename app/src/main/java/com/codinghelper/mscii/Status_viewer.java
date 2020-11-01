package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Status_viewer extends AppCompatActivity {

    ImageSlider show_status;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String receiverUserID;
    FloatingActionButton floatingActionButton;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_viewer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        receiverUserID =getIntent().getExtras().get("visit_user_id").toString();
        player =new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);


        floatingActionButton=(FloatingActionButton) findViewById(R.id.floating_add);

        show_status=(ImageSlider) findViewById(R.id.image_slider);

       floatingActionButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               player.stop();
               player.reset();
               startActivity(new Intent(Status_viewer.this,upload_status.class));

           }
       });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player.reset();
                PrepareMediaPlayer();
            }
        });

      //  PrepareMediaPlayer();
    }

    private void PrepareMediaPlayer() {

        reference.child("studentDetail").child(receiverUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("audioUrl").exists()){
                    String SongUrl =String.valueOf(dataSnapshot.child("audioUrl").getValue());
                    try {
                        player.setDataSource(SongUrl);
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.start();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        PrepareMediaPlayer();
        final List<SlideModel> remoteimages = new ArrayList<>();
        reference.child("studentDetail").child(receiverUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Simg =String.valueOf(dataSnapshot.child("states1").getValue());
                String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());

                if(Simg.equals("no")&&Simg2.equals("no")){
                    player.stop();
                    player.reset();
                    Status_viewer.super.onBackPressed();
                }else {
                    if(!Simg.equals("no")){
                        remoteimages.add(new SlideModel(Simg, ScaleTypes.CENTER_INSIDE));
                    }
                    if(!Simg2.equals("no")){
                        remoteimages.add(new SlideModel(Simg2, ScaleTypes.CENTER_INSIDE));
                    }
                    show_status.setImageList(remoteimages,ScaleTypes.CENTER_INSIDE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(receiverUserID.equals(user.getUid())){
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setEnabled(true);
        }else {
            floatingActionButton.setVisibility(View.INVISIBLE);
            floatingActionButton.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        player.stop();
        player.reset();
        super.onBackPressed();
    }
}