package com.codinghelper.mscii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Stud_signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_signup);
    }

    public void lock(View view){
        Toast.makeText(getApplicationContext(),"ADMIN SIGNUP",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Stud_signup.this,Admin_activity.class));
    }
    public void unlock(View view){
        Toast.makeText(getApplicationContext(),"STUDENT SIGNUP",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Stud_signup.this,Signup_student.class));

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
