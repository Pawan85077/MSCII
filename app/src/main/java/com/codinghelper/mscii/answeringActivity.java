package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class answeringActivity extends AppCompatActivity {
    private EditText Answer;
    private TextView Viewans,qn;
    private Button Post,mvLive;
    private String Sans;
    private DatabaseReference reference,root;
    private FirebaseAuth mAuth;
    long value;
    private String currentUserId,receiver_question_Id,CurrentAnswererId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        Answer=(EditText)findViewById(R.id.AnsweringLayout);
        receiver_question_Id= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("question_id")).toString();
        CurrentAnswererId=String.valueOf(getIntent().getExtras().get("current_answerer_id"));
     //   Viewans=(TextView)findViewById(R.id.VAns);
        Post=(Button)findViewById(R.id.post);
        mvLive=(Button)findViewById(R.id.moveLive);
        qn=(TextView)findViewById(R.id.Discuss);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        reference= FirebaseDatabase.getInstance().getReference().child("Questions");
        mvLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   startActivity(new Intent(answeringActivity.this, LiveActivity.class));
                Toast.makeText(getApplicationContext(),"6666777744443333222222!!", Toast.LENGTH_LONG).show();
                Intent profileIntent = new Intent(answeringActivity.this, LiveActivity.class);
                profileIntent.putExtra("question_id", receiver_question_Id);
                startActivity(profileIntent);

            }
        });
        reference.child(receiver_question_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Sask = String.valueOf(dataSnapshot.child("QuestionAsked").getValue());
                qn.setText(Sask);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            Post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentUserId.equals(CurrentAnswererId)) {

                        String ans = Answer.getText().toString();
                        Answer.setText("");
                        if (!ans.isEmpty()) {
                        reference.child(receiver_question_Id).child("Answer").setValue(ans);
                        reference.child(receiver_question_Id).child("FinalAnswererId").setValue(currentUserId);
                        //unconfirmed change but working fine addvalueeentlistner
                        root.child(CurrentAnswererId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                                value= (long)dataSnapshot.child("countA").getValue();
                                value=value+1;
                                root.child(CurrentAnswererId).child("countA").setValue(value);
                                reference.child(receiver_question_Id).child("AnswererImage").setValue(Simg);
                                reference.child(receiver_question_Id).child("AnswererName").setValue(Sname);
                                reference.child(receiver_question_Id).child("position").setValue("update");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        answeringActivity.super.onBackPressed();
                    }else{
                            Toast.makeText(getApplicationContext(),"You haven't Answered!!", Toast.LENGTH_LONG).show();

                        }
                }else {
                        Toast.makeText(getApplicationContext(),"Only Answerer can post !!", Toast.LENGTH_LONG).show();

                    }
                }
            });

        reference.child(receiver_question_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Sans =String.valueOf(dataSnapshot.child("Answer").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if(Sans.equals("Not answered yet!!")){
            reference.child(receiver_question_Id).child("position").setValue("answer");
        }else {
            reference.child(receiver_question_Id).child("position").setValue("update");
        }
        super.onBackPressed();
    }

    /* @Override
    protected void onPause() {
        if(Sans.equals("Not answered yet!!")){
            reference.child(receiver_question_Id).child("position").setValue("answer");
        }else {
            reference.child(receiver_question_Id).child("position").setValue("update");
        }
        super.onPause();
    }/*



  /*  @Override
    protected void onStop() {
        if(Sans.equals("Not answered yet!!")){
            reference.child(receiver_question_Id).child("position").setValue("answer");
        }else {
            reference.child(receiver_question_Id).child("position").setValue("update");
        }
        super.onStop();
    }*/


}
