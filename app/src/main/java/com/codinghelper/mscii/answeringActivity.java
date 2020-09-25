package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class answeringActivity extends AppCompatActivity {
    private EditText Answer;
    private TextView Viewans;
    private Button Post;
    private DatabaseReference reference,root;
    private FirebaseAuth mAuth;
    private String currentUserId,receiver_question_Id,CurrentAnswererId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        Answer=(EditText)findViewById(R.id.AnsweringLayout);
        receiver_question_Id=getIntent().getExtras().get("question_id").toString();
        CurrentAnswererId=String.valueOf(getIntent().getExtras().get("current_answerer_id"));
        Viewans=(TextView)findViewById(R.id.VAns);
        Post=(Button)findViewById(R.id.post);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        reference= FirebaseDatabase.getInstance().getReference().child("Questions");
            Post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(currentUserId.equals(CurrentAnswererId)) {

                        String ans = Answer.getText().toString();
                        Answer.setText("");
                        if (!ans.isEmpty()) {
                        reference.child(receiver_question_Id).child("Answer").setValue(ans);
                        root.child(CurrentAnswererId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                String Sname =String.valueOf(dataSnapshot.child("username").getValue());
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
