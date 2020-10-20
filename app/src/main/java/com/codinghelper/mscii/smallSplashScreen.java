package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public class smallSplashScreen extends AppCompatActivity {
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(smallSplashScreen.this, student_homepage.class));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_splash_screen);
        handler.postDelayed(runnable,1000);
    }

}
