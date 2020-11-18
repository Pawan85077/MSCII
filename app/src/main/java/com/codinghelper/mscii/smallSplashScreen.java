package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class smallSplashScreen extends AppCompatActivity {
    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    private boolean animationStarted = false;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressBar bar;
    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.my_app);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        bar=(ProgressBar)findViewById(R.id.spbar);
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail");

        if(user!=null){

            //   updateUserStatus("online");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bar.setVisibility(View.VISIBLE);

                    if(dataSnapshot.hasChild(user.getUid())){
                        Thread thread=new Thread()
                        {
                            @Override
                            public void run() {
                                try {
                                    sleep(1000);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }finally {
                                    startActivity(new Intent(smallSplashScreen.this, student_homepage.class));
                                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                }
                            }
                        };
                        thread.start();

                        //  finish();


                    }else {
                        Thread thread=new Thread()
                        {
                            @Override
                            public void run() {
                                try {
                                    sleep(1000);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }finally {
                                    startActivity(new Intent(smallSplashScreen.this, AdminHomepage.class));
                                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                }
                            }
                        };
                        thread.start();
                        //  finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Thread thread=new Thread()
            {
                @Override
                public void run() {
                    try {
                 //       bar.setVisibility(View.VISIBLE);
                        sleep(4000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        startActivity(new Intent(smallSplashScreen.this,First.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }
                }
            };
            thread.start();
        }

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }
    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(50).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(1000);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }

}
