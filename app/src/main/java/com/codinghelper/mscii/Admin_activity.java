package com.codinghelper.mscii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Admin_activity extends AppCompatActivity {
    private Spinner spinner;
    List<String> list;

    EditText t_email,t_phoneno,t_tempassword,t_conpassword;
    String Aposition="";
    String Account="Admin";
    private Button btn_register;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_activity);

        t_email=(EditText)findViewById(R.id.afemail);
        t_phoneno=(EditText)findViewById(R.id.afpn);
        t_tempassword=(EditText)findViewById(R.id.afsp);
        t_conpassword=(EditText)findViewById(R.id.afcp);
        this.btn_register=findViewById(R.id.Ad_signupad);
        progressDialog = new ProgressDialog(this,R.style.AlertDialogTheme);
        progressDialog.setMessage("Registering User...");


        databaseReference= FirebaseDatabase.getInstance().getReference("adminDetail");
        firebaseAuth=FirebaseAuth.getInstance();

        spinner = (Spinner) findViewById(R.id.spinner);
        list = new ArrayList<String>();
        list.add("Select Admin Type:-");
        list.add("Principal");
        list.add("Vice Principal");
        list.add("Placement cell");
        list.add("Scholarship Department");
        list.add("Vocational Department");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_admin_java_file, list);

        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_bg);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Toast.makeText(getApplicationContext(),"Not Selected",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Aposition="Principal";
                        break;
                    case 2:
                        Aposition="Vice Principal";
                        break;
                    case 3:
                        Aposition="Placement cell";
                        break;
                    case 4:
                        Aposition="Scholarship Department";
                        break;
                    case 5:
                        Aposition="Vocational Department";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = t_email.getText().toString().trim();
                final String phoneno = t_phoneno.getText().toString();
                String password = t_tempassword.getText().toString().trim();
                String conpassword = t_conpassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    t_email.setError("Invalid email");
                    t_email.setFocusable(true);
                    return;
                }


                if (TextUtils.isEmpty(email)) {
                    t_email.setError("enter email");
                    t_email.setFocusable(true);
                    return;
                }

                if (TextUtils.isEmpty(phoneno)) {
                    t_phoneno.setError("enter phoneno");
                    t_phoneno.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    t_tempassword.setError("enter password");
                    t_email.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(conpassword)) {
                    t_conpassword.setError("enter conpasword");
                    t_conpassword.setFocusable(true);
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "password short", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (password.equals(conpassword)) {

                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Admin_activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        adminDetail information = new adminDetail(
                                                Aposition,
                                                email,
                                                phoneno,
                                                Account

                                        );

                                        FirebaseDatabase.getInstance().getReference("adminDetail")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Register complete", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Admin_activity.this, aloginActivity.class));
                                            }
                                        });


                                    } else {

                                        progressDialog.dismiss();

                                        Toast.makeText(getApplicationContext(), "Authontication failed", Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "password not matched", Toast.LENGTH_SHORT).show();

                }

            }



        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
