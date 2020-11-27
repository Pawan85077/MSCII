package com.codinghelper.mscii;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Delayed;

public class AdminComposeMessage extends AppCompatActivity {

    RecyclerView AdminComposeToRecyclerView;
    Admin_composeTo_Recycler_Adapter admin_composeTo_recycler_adapter;
    ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeRecyclerValue_from_addRecipientsArrayList = new ArrayList<>();

    private ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeTo_getter_setters = new ArrayList<Admin_composeRecyclerValue_from_AddRecipients>();

    private Button composeSendButton;
    private EditText composeMessageSubject,composeMessageBody;

    //dekh ke re baba
    private FirebaseAuth auth;
    private String currentAdminID;
    private DatabaseReference RootNode,adminToAdminMessage, adminMessage;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_compose_message);

        final ExtendedFloatingActionButton clearSelectionFAB = findViewById(R.id.clear_selectionFAB);

        //dekh ke re baba
        auth=FirebaseAuth.getInstance();
        currentAdminID=auth.getCurrentUser().getUid();
        adminMessage=FirebaseDatabase.getInstance().getReference().child("adminMessages");
        adminToAdminMessage = FirebaseDatabase.getInstance().getReference().child("adminToAdminMessage");
        RootNode= FirebaseDatabase.getInstance().getReference().child("adminDetail");

        Toolbar toolbar = findViewById(R.id.compose_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //defining Buttons and EditTexts
        composeSendButton = (Button)findViewById(R.id.composeSendButton);
        composeMessageSubject = (EditText)findViewById(R.id.composeMessageSubject);
        composeMessageBody = (EditText)findViewById(R.id.composeMessageBody);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back button pressed
                finish();
                onBackPressed();
            }
        });

        AdminComposeToRecyclerView = findViewById(R.id.composeTo_RecyclerView);
//        admin_composeTo_recycler_adapter = new Admin_composeTo_Recycler_Adapter();
//        AdminComposeToRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        AdminComposeToRecyclerView.setAdapter(admin_composeTo_recycler_adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        AdminComposeToRecyclerView.setLayoutManager(layoutManager);
        admin_composeRecyclerValue_from_addRecipientsArrayList = (ArrayList<Admin_composeRecyclerValue_from_AddRecipients>) getIntent().getExtras().getSerializable("list");
        AdminComposeToRecyclerView.setAdapter(new Admin_composeTo_Recycler_Adapter(admin_composeRecyclerValue_from_addRecipientsArrayList));

        admin_composeTo_getter_setters.addAll(admin_composeRecyclerValue_from_addRecipientsArrayList);


        clearSelectionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent addRecepientsIntent = new Intent(getApplicationContext(),AdminAddRecepients.class);
                startActivity(addRecepientsIntent);
            }
        });

        //int totalItems = admin_composeTo_recycler_adapter.getItemCount();
        //String s = Integer()

        composeSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = AdminComposeToRecyclerView.getAdapter().getItemCount();
                final String messageSubject = composeMessageSubject.getText().toString();
                final String messageBody = composeMessageBody.getText().toString();
                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                final String to = new String();
                final String[] mainKey = new String[1];
                final String[] senderImgURL = new String[1];
                final String[] senderDepartment = new String[1];
                StringBuilder builder = new StringBuilder(to);
                String newTo = new String();

                //EditTexts Should not be empty
                if (TextUtils.isEmpty(messageSubject)) {
                    composeMessageSubject.setError("Message Subject is empty..!!");
                }else if (TextUtils.isEmpty(messageBody)){
                    composeMessageBody.setError("Message Body is empty..!!");
                }else{
                    //Actual messageSending work done here
                    //for getting to list
                    for (int position = 0; position< n; position++)
                    {
                        final String receiverDepartment = admin_composeTo_getter_setters.get(position).getDepartmentName();
                        final String receiverSession = admin_composeTo_getter_setters.get(position).getSessionName();
                        builder.append("[").append(receiverDepartment).append("(").append(receiverSession).append(")]  ");
                        newTo = builder.toString();
                    }

                    //admin to admin messages
                    final String finalNewTo = newTo;
                    RootNode.child(currentAdminID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                mainKey[0] =adminToAdminMessage.push().getKey();
                                senderImgURL[0] =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                senderDepartment[0] =String.valueOf(dataSnapshot.child("position").getValue());

                                HashMap recB=new HashMap();
                                recB.put("senderDepartmentID",currentAdminID);
                                recB.put("messageTitle",messageSubject);
                                recB.put("messageBody",messageBody);
                                recB.put("receiverName", finalNewTo);
                                recB.put("likeCount",0);
                                recB.put("dateOfPost",currentDate);
                                recB.put("timeOfPost",currentTime);
                                recB.put("senderImage", senderImgURL[0]);
                                recB.put("senderDepartmentName", senderDepartment[0]);
                                recB.put("Discussion","");

                                adminToAdminMessage.child(mainKey[0]).updateChildren(recB).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Sending messages...", Toast.LENGTH_LONG).show();
//                                            composeMessageSubject.setText(null);
//                                            composeMessageBody.setText(null);
                                        }
                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //messages to specific departments
                    for (int position = 0; position< n; position++)
                    {

                        final String receiverDepartment = admin_composeTo_getter_setters.get(position).getDepartmentName();
                        final String receiverSession = admin_composeTo_getter_setters.get(position).getSessionName();


                        //unconfirmed changes addValueEventListener
                        RootNode.child(currentAdminID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String key=adminMessage.push().getKey();

                                    HashMap recA=new HashMap();
                                    recA.put("senderDepartmentID",currentAdminID);
                                    recA.put("messageTitle",messageSubject);
                                    recA.put("messageBody",messageBody);
                                    recA.put("receiverName", finalNewTo);
                                    recA.put("likeCount",0);
                                    recA.put("dateOfPost",currentDate);
                                    recA.put("timeOfPost",currentTime);
                                    recA.put("senderImage", senderImgURL[0]);
                                    recA.put("senderDepartmentName", senderDepartment[0]);
                                    recA.put("keyForDiscussion",mainKey[0]);

                                    adminMessage.child(receiverSession).child(receiverDepartment).child(key).updateChildren(recA).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Message Sent Successfully :)", Toast.LENGTH_LONG).show();
                                                composeMessageSubject.setText(null);
                                                composeMessageBody.setText(null);
                                            }
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //errors
                    }
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Message discarded successfully...!!!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}