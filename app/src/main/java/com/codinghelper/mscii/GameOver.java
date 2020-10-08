package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    Button P;
    TextView t;
    String scoreG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        P=(Button)findViewById(R.id.playagain);
        t=(TextView)findViewById(R.id.scoregame);
        scoreG=getIntent().getExtras().get("score").toString();
        P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(GameOver.this,gameFirstPage.class);
                startActivity(mainIntent);
            }
        });
        t.setText("Score : "+scoreG);
    }


}