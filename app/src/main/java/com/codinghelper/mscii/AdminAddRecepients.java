package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminAddRecepients extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layoutLists;
    Button buttonAdd;

    //Lists of strings for department and session spinner
    List<String> departmentsLists = new ArrayList<>();
    List<String> sessionLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_recepients);

        //Setting up toolbar
        Toolbar toolbar = findViewById(R.id.recipient_toolbar);
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

        layoutLists = findViewById(R.id.recipients_List);
        buttonAdd = findViewById(R.id.add_NewRecipient);

        buttonAdd.setOnClickListener(this);

        //DEPARTMENT lIST ITEMS
        departmentsLists.add("--Select Department--");
        departmentsLists.add("BCA");
        departmentsLists.add("IT");
        departmentsLists.add("BCM");
        departmentsLists.add("BBA");
        departmentsLists.add("Maths");
        departmentsLists.add("Physics");
        departmentsLists.add("Chemistry");
        departmentsLists.add("Botany");
        departmentsLists.add("Zoology");
        departmentsLists.add("Bio Technology");

        //SESSION lIST ITEM
        sessionLists.add("--Select Session--");
        sessionLists.add("2016-19");
        sessionLists.add("2017-20");
        sessionLists.add("2018-21");
        sessionLists.add("2019-22");
        sessionLists.add("2020-23");
        sessionLists.add("2021-24");
        sessionLists.add("2021-25");
        sessionLists.add("2022-26");
        sessionLists.add("2023-27");
        sessionLists.add("2024-28");


    }

    @Override
    public void onClick(View v) {
        addView();
    }

    private void addView() {

        final View recepientsView = getLayoutInflater().inflate(R.layout.admin_addrecipients_row_item,null,false);

        AppCompatSpinner spinnerDepartments = (AppCompatSpinner)recepientsView.findViewById(R.id.spinner_departments);
        AppCompatSpinner spinnerSession = (AppCompatSpinner)recepientsView.findViewById(R.id.spinner_session);
        ImageView imageClose = (ImageView)recepientsView.findViewById(R.id.image_remove_recipient_item);

        ArrayAdapter departmentArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,departmentsLists);
        spinnerDepartments.setAdapter(departmentArrayAdapter);

        ArrayAdapter sessionArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sessionLists);
        spinnerSession.setAdapter(sessionArrayAdapter);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(recepientsView);
            }
        });

        layoutLists.addView(recepientsView);

    }

    private void removeView(View view){
        layoutLists.removeView(view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}