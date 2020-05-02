package com.codinghelper.mscii;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class MainActivity extends AwesomeSplash {

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        User user=new User(MainActivity.this);
        if(user.getEmail()!=""){
            startActivity(new Intent(MainActivity.this, student_homepage.class));
            finish();
        }
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
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

    }

    @Override
    public void animationsFinished() {
    startActivity(new Intent(MainActivity.this,First.class));
    }
}
