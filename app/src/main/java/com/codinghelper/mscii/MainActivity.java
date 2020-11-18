package com.codinghelper.mscii;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AwesomeSplash {

    FirebaseAuth auth;
    String currentUserID;
    private DatabaseReference Root;
    FirebaseUser currentuser;

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setBackgroundDrawable(null);
        hideNavigationBar();

        auth= FirebaseAuth.getInstance();
        currentuser=FirebaseAuth.getInstance().getCurrentUser();
        Root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
            if(currentuser!=null){
         //   updateUserStatus("online");
            startActivity(new Intent(MainActivity.this, smallSplashScreen.class));
            finish();
        }

    }

   /* @Override
    protected void onStop() {
        super.onStop();
        if(currentuser!=null){
            updateUserStatus("offline");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentuser!=null){
            updateUserStatus("offline");
        }
    }*/

    private void updateUserStatus(String state) {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        HashMap<String,Object> onlineState=new HashMap<>();
        onlineState.put("time",saveCurrentTime);
        onlineState.put("date",saveCurrentDate);
        onlineState.put("state",state);

        currentUserID=auth.getCurrentUser().getUid();
        Root.child(currentUserID).child("userOnlineState").updateChildren(onlineState);
    }

    private void hideNavigationBar(){
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }

    @Override
    public void initSplash(ConfigSplash configSplash) {
       // ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //animation

        configSplash.setBackgroundColor(R.color.bg_spalsh);
        configSplash.setAnimCircularRevealDuration(800);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);
        //title
        configSplash.setTitleSplash(getString(R.string.title));
        configSplash.setTitleTextColor(R.color.colorAccent);
        configSplash.setTitleTextSize(40f);
        configSplash.setAnimTitleTechnique(Techniques.FadeInRight);
        configSplash.setAnimTitleDuration(1000);
        //logo
        configSplash.setLogoSplash(R.drawable.lo);
        configSplash.setAnimLogoSplashDuration(3000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

    }

    @Override
    public void animationsFinished() {
    startActivity(new Intent(MainActivity.this,First.class));
    }
}
