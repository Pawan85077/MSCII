package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;              //1....2...3...are the code sequence matlb code iss number ke hisab ke sath chlaga
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class Show_profile_Activity extends AppCompatActivity {
    private String senderUserId, receiverUserID, currentState;
    private CircularImageView userImage;
    private ImageView Background;
    MediaPlayer player;

    private TextView userProfilename, userProfileStatus, userSession, userCourse, userSem, sq, sa, moder, moderdel;
    private Button AddRequest, RemoveRequest, Playsong;
    private DatabaseReference reference1, addRequestref, friendref, NotificationRef, bannedref;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_);

        //ToosBars
        initToolbar();
        initComponent();

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        auth = FirebaseAuth.getInstance();
        senderUserId = auth.getCurrentUser().getUid();
        sq = (TextView) findViewById(R.id.q);
        sa = (TextView) findViewById(R.id.a);
        Background = (ImageView) findViewById(R.id.profileBackround);
        moder = (TextView) findViewById(R.id.mod);
        moderdel = (TextView) findViewById(R.id.model);
        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        userImage = (CircularImageView) findViewById(R.id.visit_profile_image);
        userProfilename = (TextView) findViewById(R.id.visit_username);
        userProfileStatus = (TextView) findViewById(R.id.visit_userstatus);
        userCourse = (TextView) findViewById(R.id.visit_course);
        userSession = (TextView) findViewById(R.id.visit_session);
        userSem = (TextView) findViewById(R.id.visit_sem);
        AddRequest = (Button) findViewById(R.id.add_friend);
        RemoveRequest = (Button) findViewById(R.id.remove_friend);
        Playsong = (Button) findViewById(R.id.playbtn);
        reference1 = FirebaseDatabase.getInstance().getReference().child("studentDetail");
        bannedref = FirebaseDatabase.getInstance().getReference();
        addRequestref = FirebaseDatabase.getInstance().getReference().child("Add Friend Request");
        NotificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        friendref = FirebaseDatabase.getInstance().getReference().child("Friend list");
        //user position=current state
        currentState = "new";
        //1.
        moderdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference1.child(senderUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Madmod = String.valueOf(dataSnapshot.child("Level").getValue());
                        if (Madmod.equals("moderator")) {
                            bannedref.child("BannedAccount").child("BannedRequest").setValue(receiverUserID);
                            Toast.makeText(getApplicationContext(), "ID will be deleted under 24 hours !!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Only moderator can delete someone ID !!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        RetriveUserInfo();


        Playsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!player.isPlaying()) {
                    player.start();
                    Playsong.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    Toast.makeText(getApplicationContext(), "start!!", Toast.LENGTH_SHORT).show();
                } else {
                    player.pause();
                    Playsong.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                    Toast.makeText(getApplicationContext(), "stop!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        PrepareMediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Playsong.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                player.reset();
                PrepareMediaPlayer();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ViewProfile);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        final CircularImageView imagex = (CircularImageView) findViewById(R.id.visit_profile_image);
        final CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.viewProfile_collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.viewProfile_app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                imagex.setScaleX(scale >= 0 ? scale : 0);
                imagex.setScaleY(scale >= 0 ? scale : 0);
            }
        });
    }

    private void PrepareMediaPlayer() {

        player.reset();

        reference1.child(receiverUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String SongUrl = String.valueOf(dataSnapshot.child("audioUrl").getValue());
                    try {
                        player.reset();
                        player.setDataSource(SongUrl);
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //1-->
    private void RetriveUserInfo() {
        reference1.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && (dataSnapshot.hasChild("imageUrl"))) {
                    String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                    Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(userImage);
                    String Sback = String.valueOf(dataSnapshot.child("imageUrlBackground").getValue());
                    Picasso.get().load(Sback).fit().centerCrop().noFade().placeholder(R.drawable.bk).into(Background);
                    String Sname = String.valueOf(dataSnapshot.child("username").getValue());
                    userProfilename.setText(Sname);
                    String Sstatus = String.valueOf(dataSnapshot.child("userstatus").getValue());
                    userProfileStatus.setText(" " + Sstatus);
                    String Scourse = String.valueOf(dataSnapshot.child("Scourse").getValue());
                    userCourse.setText(" " + Scourse);
                    String Ssession = String.valueOf(dataSnapshot.child("session").getValue());
                    userSession.setText(" " + Ssession);
                    String Ssem = String.valueOf(dataSnapshot.child("userSem").getValue());
                    userSem.setText(" Semister " + Ssem);
                    String Sq = String.valueOf(dataSnapshot.child("countQ").getValue());
                    sq.setText(Sq);
                    String Sa = String.valueOf(dataSnapshot.child("countA").getValue());
                    sa.setText(Sa);
                    if (Integer.parseInt(Sq) >= 20 && Integer.parseInt(Sa) >= 20) {
                        reference1.child(receiverUserID).child("Level").setValue("moderator");
                    } else {
                        reference1.child(receiverUserID).child("Level").setValue("");
                    }
                    String Madmod = String.valueOf(dataSnapshot.child("Level").getValue());
                    moder.setText(Madmod);

                    //2.
                    ManageAddRequest();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //2-->
    private void ManageAddRequest() {
        addRequestref.child(senderUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(receiverUserID)) {
                            String request_type = String.valueOf(dataSnapshot.child(receiverUserID).child("Request_type").getValue());
                            if (request_type.equals("sent")) {
                                currentState = "request_sent";
                                AddRequest.setText("Cancel Request");
                            } else if (request_type.equals("received")) {
                                currentState = "request_received";
                                AddRequest.setText("Confirm friend");
                                RemoveRequest.setVisibility(View.VISIBLE);
                                RemoveRequest.setEnabled(true);
                                RemoveRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CancelFreirdRequest();
                                    }
                                });
                            }
                        } else {
                            friendref.child(senderUserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(receiverUserID)) {
                                                currentState = "friends";
                                                AddRequest.setText("Unfriend");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //3.checks if current user not sending request to him/her self
        //*agr nhi equal hota hn tab request sent hoga
        if (!senderUserId.equals(receiverUserID)) {
            AddRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //ek baar add request mai click krne ka baad dubara na work ho isliy esetenabled function ka use kr re hn
                    AddRequest.setEnabled(false);
                    //add request enabled false matlb abb user uss button ko press nhi kr skta jb tak enabled true na ho jay
                    if (currentState.equals("new")) {
                        sendAddRequest();
                    }
                    if (currentState.equals("request_sent")) {
                        CancelFreirdRequest();
                    }
                    if (currentState.equals("request_received")) {
                        ConfirmFriendRequest();
                    }
                    if (currentState.equals("friends")) {
                        RemoveFriend();
                    }
                }
            });
        } else {
            //*agr user ka current id reciver id ke sath match hota hn too usme add friend ka option he nhi ana chahya
            AddRequest.setVisibility(View.INVISIBLE);
        }
    }

    private void RemoveFriend() {
        friendref.child(senderUserId).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendref.child(receiverUserID).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                AddRequest.setEnabled(true);
                                                currentState = "new";
                                                AddRequest.setText("Add friend");
                                                RemoveRequest.setVisibility(View.INVISIBLE);
                                                RemoveRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void ConfirmFriendRequest() {
        friendref.child(senderUserId).child(receiverUserID)
                .child("Friends").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            friendref.child(receiverUserID).child(senderUserId)
                                    .child("Friends").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addRequestref.child(senderUserId).child(receiverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    addRequestref.child(receiverUserID).child(senderUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    AddRequest.setEnabled(true);
                                                                                    currentState = "friends";
                                                                                    AddRequest.setText("Unfriend");
                                                                                    RemoveRequest.setVisibility(View.INVISIBLE);
                                                                                    RemoveRequest.setEnabled(false);

                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void CancelFreirdRequest() {
        addRequestref.child(senderUserId).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addRequestref.child(receiverUserID).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                AddRequest.setEnabled(true);
                                                currentState = "new";
                                                AddRequest.setText("Add friend");
                                                RemoveRequest.setVisibility(View.INVISIBLE);
                                                RemoveRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendAddRequest() {
        //sender--->receiver
        addRequestref.child(senderUserId).child(receiverUserID)
                .child("Request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //receiver--->sender
                            addRequestref.child(receiverUserID).child(senderUserId)
                                    .child("Request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            HashMap<String, String> friendNotificationMap = new HashMap<>();
                                            friendNotificationMap.put("from", senderUserId);
                                            friendNotificationMap.put("type", "request");
                                            NotificationRef.child(receiverUserID).push()
                                                    .setValue(friendNotificationMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                AddRequest.setEnabled(true);
                                                                currentState = "request_sent";
                                                                AddRequest.setText("Cancel Request");
                                                            }
                                                        }
                                                    });

                                        }
                                    });

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        player.stop();
        player.reset();

        super.onBackPressed();
    }
}
