package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private ImageButton send_btn;
    private EditText userMessage;
    private ScrollView scrollView;
    private TextView displayText;
    private String GroupID;
    RecyclerView recyclerView1;
    private String CurrentUserId,CurrentUserName,CurrentDate,CurrentTime;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,GroupNameRef,global_message_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        recyclerView1=(RecyclerView)findViewById(R.id.groupChatRecycler);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager2);

        firebaseAuth=FirebaseAuth.getInstance();
        CurrentUserId=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        GroupID=getIntent().getExtras().get("group_id").toString();
        GroupNameRef=FirebaseDatabase.getInstance().getReference().child("Group").child(GroupID).child("Messages");
      //  actionBar.setTitle(currentGroupName);
        send_btn=(ImageButton)findViewById(R.id.send_text_btn);
        userMessage=(EditText)findViewById(R.id.input_message);
       // displayText=(TextView)findViewById(R.id.global_chat_text);
       // scrollView=(ScrollView)findViewById(R.id.my_scroll_view);
        GetUserInfo();
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=userMessage.getText().toString();
                String messageKey=GroupNameRef.push().getKey();
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
                    GroupNameRef.updateChildren(global_message);
                    global_message_key=GroupNameRef.child(messageKey);
                    HashMap<String,Object> messageInfo=new HashMap<>();
                    GroupNameRef.updateChildren(messageInfo);
                    HashMap<String,Object> privateMessageInfo=new HashMap<>();
                    privateMessageInfo.put("name",CurrentUserName);
                    privateMessageInfo.put("senderID",CurrentUserId);
                    privateMessageInfo.put("message",message);
                    privateMessageInfo.put("date",CurrentDate);
                    privateMessageInfo.put("time",CurrentTime);
                    global_message_key.updateChildren(privateMessageInfo);
                    userMessage.setText("");
                 //   scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                }
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

        FirebaseRecyclerOptions<Livechatadaptor> options1=
                new FirebaseRecyclerOptions.Builder<Livechatadaptor>()
                        .setQuery(GroupNameRef,Livechatadaptor.class)
                        .build();
        FirebaseRecyclerAdapter<Livechatadaptor, LiveActivity.LiveWatchViewHolder> adapter1=
                new FirebaseRecyclerAdapter<Livechatadaptor, LiveActivity.LiveWatchViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull final LiveActivity.LiveWatchViewHolder holder, int position, @NonNull Livechatadaptor model) {
                        holder.receiver.setVisibility(View.INVISIBLE);
                        holder.receiverImage.setVisibility(View.INVISIBLE);
                        holder.sender.setVisibility(View.INVISIBLE);
                        if(model.getsenderID().equals(CurrentUserId)){
                            holder.sender.setVisibility(View.VISIBLE);
                            holder.sender.setBackgroundResource(R.drawable.sender_messages_layout);
                            holder.sender.setTextColor(Color.BLACK);
                            holder.sender.setText(model.getmessage());
                        }else{
                            holder.receiverImage.setVisibility(View.VISIBLE);
                            holder.receiver.setVisibility(View.VISIBLE);
                            holder.receiver.setBackgroundResource(R.drawable.receiver_messages_layout);
                            holder.receiver.setTextColor(Color.BLACK);
                            holder.receiver.setText(model.getmessage());
                        }
                        databaseReference.child(model.getsenderID()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.receiverImage);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public LiveActivity.LiveWatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout,parent,false);
                        LiveActivity.LiveWatchViewHolder viewHolder=new LiveActivity.LiveWatchViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView1.setAdapter(adapter1);

        // recyclerView1.getLayoutManager().smoothScrollToPosition(recyclerView1,new RecyclerView.State(),recyclerView1.getAdapter().getItemCount());
        adapter1.startListening();





        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                  //  DisplayMessages(dataSnapshot);
                    recyclerView1.smoothScrollToPosition(recyclerView1.getAdapter().getItemCount());

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                  //  DisplayMessages(dataSnapshot);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_member) {
            Intent profileIntent=new Intent(this,Add_group_member.class);
            profileIntent.putExtra("group_id",GroupID);
            startActivity(profileIntent);
        }
        if (id == R.id.Delete_group) {
        }
        return super.onOptionsItemSelected(item);
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while(iterator.hasNext()){
            String charData=(String)((DataSnapshot)iterator.next()).getValue();
            String charMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String charName=(String)((DataSnapshot)iterator.next()).getValue();
            String charTime=(String)((DataSnapshot)iterator.next()).getValue();
            displayText.append(charName+":\n"+charMessage+":\n"+charTime+"    "+charData+"\n\n\n");
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
}
