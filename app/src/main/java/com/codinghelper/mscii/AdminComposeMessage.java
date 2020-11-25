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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Delayed;

public class AdminComposeMessage extends AppCompatActivity {

    RecyclerView AdminComposeToRecyclerView;
    Admin_composeTo_Recycler_Adapter admin_composeTo_recycler_adapter;
    ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeRecyclerValue_from_addRecipientsArrayList = new ArrayList<>();

    private ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeTo_getter_setters = new ArrayList<Admin_composeRecyclerValue_from_AddRecipients>();

    private Button composeSendButton;
    private EditText composeMessageSubject,composeMessageBody;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_compose_message);

        final ExtendedFloatingActionButton clearSelectionFAB = findViewById(R.id.clear_selectionFAB);

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

                //EditTexts Should not be empty
                if (TextUtils.isEmpty(messageSubject)) {
                    composeMessageSubject.setError("Message Subject is empty..!!");
                }else if (TextUtils.isEmpty(messageBody)){
                    composeMessageBody.setError("Message Body is empty..!!");
                }else{
                    //Actual messageSending work done here
                    //Toast.makeText(AdminComposeMessage.this, "dsdgfghfg  " + department, Toast.LENGTH_SHORT).show();
                    for (int position = 0; position< n; position++)
                    {

                        String receiverDepartment = admin_composeTo_getter_setters.get(position).getDepartmentName();
                        String receiverSession = admin_composeTo_getter_setters.get(position).getSessionName();
                        Toast.makeText(AdminComposeMessage.this, receiverDepartment+" " + receiverSession, Toast.LENGTH_SHORT).show();

                        //Yaha Bhejna hai Message
                        //Toast.makeText(AdminComposeMessage.this, receiverDepartment+" " + receiverSession, Toast.LENGTH_SHORT).show();

//                        final String question = SearchBar.getText().toString();
//                        final String Topic = selectedTopic;
//                        if (TextUtils.isEmpty(question)){
//                            SearchBar.setError("Ask your question!!");
//                            SearchBar.setFocusable(true);
//                        }else if (Topic.equals("--Select Topic--")){
//                            Toast.makeText(getActivity(), "select Topic related to your question!!", Toast.LENGTH_LONG).show();
//                        }else{
//                            SearchBar.setText("");
//                            recyclerView.setVisibility(View.INVISIBLE);
//                            recyclerView.setEnabled(false);
//                            //  userQuestion.push().child("QuestionAsked").setValue(question)
//                            //unconfirmed change but working fine addvalueeentlistner
//                            Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        String key=userQuestion.push().getKey();
//                                        String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
//                                        String Sname =String.valueOf(dataSnapshot.child("username").getValue());
//                                        value= (long)dataSnapshot.child("countQ").getValue();
//                                        value=value+1;
//                                        Root.child(currentUserID).child("countQ").setValue(value);
//                                        HashMap rec=new HashMap();
//                                        rec.put("QuestionAsked",question);
//                                        rec.put("askerUID",currentUserID);
//                                        rec.put("Answer","Not answered yet!!");
//                                        rec.put("AskerImage",Simg);
//                                        rec.put("Topic",Topic);
//                                        rec.put("position","answer");
//                                        rec.put("AnswererImage","https://firebasestorage.googleapis.com/v0/b/mscii-8cb88.appspot.com/o/skull%20(1).png?alt=media&token=22a5e53b-4270-40b9-bbf2-41109c135557");
//                                        rec.put("AnswererId","Not yet");
//                                        rec.put("FinalAnswererId","Not yet");
//                                        rec.put("AskerName",Sname);
//                                        rec.put("AnswererName","unknown");
//                                        rec.put("reportedTimes",0);
//                                        userQuestion.child(key).updateChildren(rec).addOnCompleteListener(new OnCompleteListener() {
//                                            @Override
//                                            public void onComplete(@NonNull Task task) {
//                                                if(task.isSuccessful()){
//                                                    Toast.makeText(getActivity(), "Go to Activities <3", Toast.LENGTH_LONG).show();
//
//                                                }
//                                            }
//                                        });
//
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//                        }

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