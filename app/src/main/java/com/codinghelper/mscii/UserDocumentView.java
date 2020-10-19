package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserDocumentView extends AppCompatActivity {

    private boolean rotate = false;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_document_view);

        ImageView fullScreenImageView = (ImageView)findViewById(R.id.docFullScreenImageView);

        Intent callingDocActivityIntent = getIntent();
        if (callingDocActivityIntent != null){
            imageUri =callingDocActivityIntent.getData();
            if (imageUri != null && fullScreenImageView != null){
                Glide.with(this)
                        .load(imageUri)
                        .into(fullScreenImageView);
            }
        }

        //Fab Buttons controls
        final FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        final FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        ViewAnimation.initShowOut(fab_delete);
        ViewAnimation.initShowOut(fab_share);

        ((FloatingActionButton) findViewById(R.id.fab_more)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate = ViewAnimation.rotateFab(v, !rotate);
                if (rotate) {
                    ViewAnimation.showIn(fab_delete);
                    ViewAnimation.showIn(fab_share);
                } else {
                    ViewAnimation.showOut(fab_delete);
                    ViewAnimation.showOut(fab_share);
                }
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Voice clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareLink = imageUri.toString();
                //Toast.makeText(getApplicationContext(), "Call clicked", Toast.LENGTH_SHORT).show();
                final Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("Text/plain");
                intent.putExtra("android.intent.extra.SUBJECT","Hey Buddy...\nHere is a link of my uploaded document from MCR Infotech mobile App");
                intent.putExtra("android.intent.extra.TEXT",""+shareLink);
                startActivity(intent.createChooser(intent,"Share :"));
            }
        });
    }
}