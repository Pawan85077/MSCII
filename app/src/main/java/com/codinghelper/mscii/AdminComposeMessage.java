package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class AdminComposeMessage extends AppCompatActivity {

    RecyclerView AdminComposeToRecyclerView;
    Admin_composeTo_Recycler_Adapter admin_composeTo_recycler_adapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_compose_message);

        final ExtendedFloatingActionButton addRecepientsFAB = findViewById(R.id.add_RecepientsFAB);

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

        addRecepientsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addRecepientsIntent = new Intent(getApplicationContext(),AdminAddRecepients.class);
                startActivity(addRecepientsIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}