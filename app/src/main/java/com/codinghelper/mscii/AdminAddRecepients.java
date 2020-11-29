package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminAddRecepients extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layoutLists;
    Button buttonAdd;
    Button buttonSubmitList;

    //Lists of strings for department and session spinner
    List<String> departmentsLists = new ArrayList<>();
    List<String> sessionLists = new ArrayList<>();

    ArrayList<Admin_composeRecyclerValue_from_AddRecipients> admin_composeRecyclerValue_from_addRecipientsArrayList = new ArrayList<>();


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
        buttonSubmitList = findViewById(R.id.submit_Recipients_List);

        buttonAdd.setOnClickListener(this);
        buttonSubmitList.setOnClickListener(this);

        String[] studentCourse = {"--Select Course--","Physics Honours", "Chemistry Honours", "Mathematics Honours", "Botany Honours",
                "Zoology Honours", "Bio-Technology", "Computer Application", "Computer Maintenance", "Information Technology",
                "Fashion Designing", "Business Administration", "Clinical Nutrition and Dietetics", "Commerce", "Economics Honours",
                "Geography Honours", "History Honours", "Home Science", "Philosophy Honours", "Political Science", "Psychology Honours",
                "Hindi Honours", "English Honours", "Bengali Honours", "Sanskrit Honours", "Urdu Honours", "Khadia Honours", "Khuruk Honours",
                "Mundali Honours", "Nagpuri Honours", "Kurmal Honours"};
        String[] studentSession = {"--Select Session--","2016-2019","2017-2020","2018-2021","2019-2022","2020-2023"};

        //DEPARTMENT lIST ITEMS
        departmentsLists.addAll(Arrays.asList(studentCourse));

        //SESSION lIST ITEM
        sessionLists.addAll(Arrays.asList(studentSession));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.add_NewRecipient:
                addView();
                break;

            case R.id.submit_Recipients_List:

                if (checkIfValidAndRead()){

                    finish();
                    Intent intent = new Intent(AdminAddRecepients.this,AdminComposeMessage.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list",admin_composeRecyclerValue_from_addRecipientsArrayList);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                break;
        }


    }

    private boolean checkIfValidAndRead() {

        admin_composeRecyclerValue_from_addRecipientsArrayList.clear();
        boolean result = true;

        for (int i=0; i<layoutLists.getChildCount(); i++){

            View recepientsView = layoutLists.getChildAt(i);
            AppCompatSpinner spinnerDepartments = (AppCompatSpinner)recepientsView.findViewById(R.id.spinner_departments);
            AppCompatSpinner spinnerSession = (AppCompatSpinner)recepientsView.findViewById(R.id.spinner_session);

            Admin_composeRecyclerValue_from_AddRecipients admin_composeRecyclerValue_from_addRecipients = new Admin_composeRecyclerValue_from_AddRecipients();

            if (spinnerDepartments.getSelectedItemPosition()!=0){
                admin_composeRecyclerValue_from_addRecipients.setDepartmentName(departmentsLists.get(spinnerDepartments.getSelectedItemPosition()));
            }else {
                result = false;
                break;
            }

            if (spinnerSession.getSelectedItemPosition()!=0){
                admin_composeRecyclerValue_from_addRecipients.setSessionName(sessionLists.get(spinnerSession.getSelectedItemPosition()));
            }else {
                result = false;
                break;
            }

            admin_composeRecyclerValue_from_addRecipientsArrayList.add(admin_composeRecyclerValue_from_addRecipients);

        }

        if (admin_composeRecyclerValue_from_addRecipientsArrayList.size() == 0){
            result = false;
            Toast.makeText(this, "Add at least one recipient", Toast.LENGTH_SHORT).show();
        }else if (!result){
            Toast.makeText(this, "Enter all details correctly", Toast.LENGTH_SHORT).show();
        }


        return result;
    }

    private void addView() {

        final View recepientsView = getLayoutInflater().inflate(R.layout.admin_addrecipients_row_item,null,false);

        AppCompatSpinner spinnerDepartments = (AppCompatSpinner)recepientsView.findViewById(R.id.spinner_departments);
        AppCompatSpinner spinnerSession = (AppCompatSpinner)recepientsView.findViewById(R.id.spinner_session);
        ImageView imageClose = (ImageView)recepientsView.findViewById(R.id.image_remove_recipient_item);

        ArrayAdapter departmentArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,departmentsLists);
        spinnerDepartments.setAdapter(departmentArrayAdapter);

        ArrayAdapter sessionArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,sessionLists);
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