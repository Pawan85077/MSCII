package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

public class AdminComposeMessage extends AppCompatActivity {

    RecyclerView AdminComposeToRecyclerView;
    Admin_composeTo_Recycler_Adapter admin_composeTo_recycler_adapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_compose_message);

        Toolbar toolbar = findViewById(R.id.compose_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back button pressed
                finish();
                onBackPressed();
            }
        });

        AdminComposeToRecyclerView = findViewById(R.id.composeTo_RecyclerView);
        admin_composeTo_recycler_adapter = new Admin_composeTo_Recycler_Adapter();

        AdminComposeToRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AdminComposeToRecyclerView.setAdapter(admin_composeTo_recycler_adapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}