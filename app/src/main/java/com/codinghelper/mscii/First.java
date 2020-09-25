package com.codinghelper.mscii;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class First extends AppCompatActivity {
    private Button slogin;
    private Button guest;
    private Button alogin;
    private Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();
        this.slogin=findViewById(R.id.slogin);
        this.guest=findViewById(R.id.guest);
        this.alogin=findViewById(R.id.alogin);
        this.signup=findViewById(R.id.sign_up);


        slogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(First.this, sloginActivity.class));
                    finish();


            }
        });
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First.this,guestPage.class));

            }
        });
        alogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First.this,aloginActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First.this,Stud_signup.class));
            }
        });
    }

}
