package com.codinghelper.mscii;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SimpleTimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalChat extends Fragment {
    private ImageButton send_btn;
    private EditText userMessage;
    private ScrollView scrollView;
    private TextView displayText;
    private String CurrentUserId,CurrentUserName,CurrentDate,CurrentTime;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,global,global_message_key;

    public GlobalChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth=FirebaseAuth.getInstance();
        CurrentUserId=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        global=FirebaseDatabase.getInstance().getReference().child("GlobalChat");
        View v= inflater.inflate(R.layout.fragment_global_chat, container, false);
        send_btn=(ImageButton)v.findViewById(R.id.send_text_btn);
        userMessage=(EditText)v.findViewById(R.id.input_message);
        displayText=(TextView)v.findViewById(R.id.global_chat_text);
        scrollView=(ScrollView)v.findViewById(R.id.my_scroll_view);
        databaseReference.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    CurrentUserName=String.valueOf(dataSnapshot.child("Username").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=userMessage.getText().toString();
                String messageKey=global.push().getKey();
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
                    global.updateChildren(global_message);
                    global_message_key=global.child(messageKey);
                    HashMap<String,Object> messageInfo=new HashMap<>();
                    messageInfo.put("name",CurrentUserName);
                    messageInfo.put("message",message);
                    messageInfo.put("date",CurrentDate);
                    messageInfo.put("time",CurrentTime);
                    global_message_key.updateChildren(messageInfo);
                    userMessage.setText("");
                }
            }
        });
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        global.addChildEventListener(new ChildEventListener() {
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

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while(iterator.hasNext()){
            String charData=(String)((DataSnapshot)iterator.next()).getValue();
            String charMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String charName=(String)((DataSnapshot)iterator.next()).getValue();
            String charTime=(String)((DataSnapshot)iterator.next()).getValue();
            displayText.append(charName+":\n"+charMessage+":\n"+charTime+"    "+charData+"\n\n\n");
        }
    }

}
