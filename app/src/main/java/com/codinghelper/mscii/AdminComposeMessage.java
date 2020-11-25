package com.codinghelper.mscii;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.Delayed;

public class AdminComposeMessage extends AppCompatActivity {

    RecyclerView AdminComposeToRecyclerView;
    Admin_composeTo_Recycler_Adapter admin_composeTo_recycler_adapter;
    ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeRecyclerValue_from_addRecipientsArrayList = new ArrayList<>();

    private ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeTo_getter_setters = new ArrayList<Admin_composeRecyclerValue_from_AddRecipients>();

    private Button composeSendButton;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_compose_message);

        final ExtendedFloatingActionButton clearSelectionFAB = findViewById(R.id.clear_selectionFAB);

        Toolbar toolbar = findViewById(R.id.compose_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //defining Buttons
        composeSendButton = (Button)findViewById(R.id.composeSendButton);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back button pressed
                finish();
                onBackPressed();
            }
        });

        AdminComposeToRecyclerView = findViewById(R.id.composeTo_RecyclerView);
//        admin_composeTo_recycler_adapter = new Admin_composeTo_Recycler_Adapter();
//        AdminComposeToRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        AdminComposeToRecyclerView.setAdapter(admin_composeTo_recycler_adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        AdminComposeToRecyclerView.setLayoutManager(layoutManager);
        admin_composeRecyclerValue_from_addRecipientsArrayList = (ArrayList<Admin_composeRecyclerValue_from_AddRecipients>) getIntent().getExtras().getSerializable("list");
        AdminComposeToRecyclerView.setAdapter(new Admin_composeTo_Recycler_Adapter(admin_composeRecyclerValue_from_addRecipientsArrayList));

        admin_composeTo_getter_setters.addAll(admin_composeRecyclerValue_from_addRecipientsArrayList);


        clearSelectionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent addRecepientsIntent = new Intent(getApplicationContext(),AdminAddRecepients.class);
                startActivity(addRecepientsIntent);
            }
        });

        //int totalItems = admin_composeTo_recycler_adapter.getItemCount();
        //String s = Integer()

        composeSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = AdminComposeToRecyclerView.getAdapter().getItemCount();

                //Toast.makeText(AdminComposeMessage.this, "dsdgfghfg  " + department, Toast.LENGTH_SHORT).show();
                for (int position = 0; position< n; position++)
                {
                    
                    String receiverDepartment = admin_composeTo_getter_setters.get(position).getDepartmentName();
                    String receiverSession = admin_composeTo_getter_setters.get(position).getSessionName();

                    //Yaha Bhejna hai Message
                    Toast.makeText(AdminComposeMessage.this, receiverDepartment+" " + receiverSession, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Message discarded successfully...!!!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}