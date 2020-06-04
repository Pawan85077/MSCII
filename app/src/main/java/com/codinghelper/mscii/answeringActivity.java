package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private String currentUserId,receiver_question_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        Answer=(EditText)findViewById(R.id.AnsweringLayout);
        receiver_question_Id=getIntent().getExtras().get("question_id").toString();
        Viewans=(TextView)findViewById(R.id.VAns);
        Post=(Button)findViewById(R.id.post);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        reference= FirebaseDatabase.getInstance().getReference().child("Questions");
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans=Answer.getText().toString();
                Answer.setText("");
                reference.child(receiver_question_Id).child("Answer").setValue(ans);
                root.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                        reference.child(receiver_question_Id).child("AnswererImage").setValue(Simg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
