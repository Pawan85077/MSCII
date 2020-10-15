package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class LiveActivity extends AppCompatActivity {
    private TextView qnView;
    String receiver_question_Id,receiver_pid;
    DatabaseReference reference,Watchref;
    private ImageButton send_btn;
    CircularImageView globali;
    private RecyclerView recyclerView;
    private EditText userMessage;
    private ScrollView scrollView;
    private TextView displayText,Liveans;
    private String currentGroupName;
    private String CurrentUserId,CurrentUserName,CurrentDate,CurrentTime;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,GroupNameRef1,global_message_key,referencetopeople,root,Livereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        qnView=(TextView)findViewById(R.id.DiscussLive);
        recyclerView=(RecyclerView)findViewById(R.id.watch_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Liveans=(TextView)findViewById(R.id.lans);
        receiver_question_Id= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("question_id")).toString();
        receiver_pid= Objects.requireNonNull(getIntent().getExtras().get("pid")).toString();
        reference= FirebaseDatabase.getInstance().getReference().child("Questions");
        root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        referencetopeople= FirebaseDatabase.getInstance().getReference().child("Questions").child(receiver_question_Id).child("people");




        Watchref=FirebaseDatabase.getInstance().getReference().child("Watching");
        firebaseAuth=FirebaseAuth.getInstance();
        CurrentUserId=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("studentDetail");


       // GroupNameRef=FirebaseDatabase.getInstance().getReference().child("LiveGroup");
        Livereference= FirebaseDatabase.getInstance().getReference().child("Questions").child(receiver_question_Id).child("LiveGroup");


        send_btn=(ImageButton)findViewById(R.id.send_text_btn1);
        userMessage=(EditText)findViewById(R.id.input_message1);
        displayText=(TextView)findViewById(R.id.global_chat_text1);
        scrollView=(ScrollView)findViewById(R.id.my_scroll_view1);
        GetUserInfo();





        reference.child(receiver_question_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String Sansss = String.valueOf(dataSnapshot.child("AnswererId").getValue());
                databaseReference.child(Sansss).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Sansssuser = String.valueOf(dataSnapshot.child("username").getValue());
                        Liveans.setText(Sansssuser+" is currently answering...");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=userMessage.getText().toString();
                String messageKey=Livereference.push().getKey();
                if (TextUtils.isEmpty(message)) {
                    userMessage.setError("Type something!!");
                    userMessage.setFocusable(true);
                    return;
                }else{
                    Calendar calendar_date=Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd, yyyy");
                    CurrentDate=simpleDateFormat.format(calendar_date.getTime());

                    Calendar calendar_time=Calendar.getInstance();
                    SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm a");
                    CurrentTime=simpleTimeFormat.format(calendar_time.getTime());

                    HashMap<String,Object> global_message=new HashMap<>();
                    Livereference.updateChildren(global_message);
                    global_message_key=Livereference.child(messageKey);
                    HashMap<String,Object> messageInfo=new HashMap<>();
                    Livereference.updateChildren(messageInfo);
                    HashMap<String,Object> privateMessageInfo=new HashMap<>();
                    privateMessageInfo.put("name",CurrentUserName);
                    privateMessageInfo.put("message",message);
                    privateMessageInfo.put("date",CurrentDate);
                    privateMessageInfo.put("time",CurrentTime);
                    global_message_key.updateChildren(privateMessageInfo);
                    userMessage.setText("");
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                }
            }
        });









        reference.child(receiver_question_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Sask = String.valueOf(dataSnapshot.child("QuestionAsked").getValue());
                qnView.setText(Sask);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onStart(){
        super.onStart();


        FirebaseRecyclerOptions<watch> options=
                new FirebaseRecyclerOptions.Builder<watch>()
                        .setQuery(referencetopeople,watch.class)
                        .build();
        FirebaseRecyclerAdapter<watch, LiveActivity.WatchViewHolder> adapter=
                new FirebaseRecyclerAdapter<watch, LiveActivity.WatchViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final LiveActivity.WatchViewHolder holder, int position, @NonNull final watch model) {
                        //holder.Questions.setText(model.getQuestionAsked());
                        //String question_id=getRef(position).getKey();
                       // Picasso.get().load(model.getAskerImage()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.AskerImage);
                        databaseReference.child(model.getpeopleUID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.watcherImage);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public LiveActivity.WatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.watching,parent,false);
                        LiveActivity.WatchViewHolder viewHolder=new LiveActivity.WatchViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();















        Livereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public static class WatchViewHolder extends RecyclerView.ViewHolder{

        CircularImageView watcherImage;

        public WatchViewHolder(@NonNull View itemView) {
            super(itemView);

            watcherImage=itemView.findViewById(R.id.watch_profile_image);

        }


    }
















    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while(iterator.hasNext()){
            String charData=(String)((DataSnapshot)iterator.next()).getValue();
            String charMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String charName=(String)((DataSnapshot)iterator.next()).getValue();
            String charTime=(String)((DataSnapshot)iterator.next()).getValue();
            displayText.setTextSize(16);
            displayText.append(charName+":\n"+charMessage+"\n\n");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }


    private void GetUserInfo() {
        databaseReference.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    CurrentUserName=String.valueOf(dataSnapshot.child("username").getValue());



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        referencetopeople.child(receiver_pid).removeValue();
    }
}