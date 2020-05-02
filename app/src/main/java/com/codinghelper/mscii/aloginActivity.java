package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class aloginActivity extends AppCompatActivity {
    private Button signup;
    EditText txtEmail, txtPassword;
    private Button btn_login;
    private Button btn_recover;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
  //  private Spinner spinner;
  //  List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alogin);
        this.signup=findViewById(R.id.signup_admin);
        txtEmail = (EditText) findViewById(R.id.Ademal);
        txtPassword = (EditText) findViewById(R.id.Asp);
        this.btn_login = findViewById(R.id.Ad_login);
        this.btn_recover = findViewById(R.id.recover_admin);
        progressDialog = new ProgressDialog(this, R.style.AlertDialogTheme);
        progressDialog.setMessage("Loging In...");
        firebaseAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(aloginActivity.this,Admin_activity.class));
            }
        });
   /*     spinner = (Spinner) findViewById(R.id.spinner);
        list = new ArrayList<String>();
        list.add("Select Admin Type:-");
        list.add("Principal");
        list.add("Vice Principal");
        list.add("Placement cell");
        list.add("Scholarship Department");
        list.add("Vocational Department");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_bg, list);

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
                        Toast.makeText(getApplicationContext(),"Principal",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(),"Vice Principal",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),"Placement cell",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(),"Scholarship Department",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(),"Vocational Department",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("enter email");
                    txtEmail.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("enter password");
                    txtPassword.setFocusable(true);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtEmail.setError("Invalid email");
                    txtEmail.setFocusable(true);
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "password short", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(aloginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user=FirebaseAuth.getInstance().getCurrentUser();
                                    reference=FirebaseDatabase.getInstance().getReference().child("adminDetail").child(user.getUid());
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String ACtype =String.valueOf(dataSnapshot.child("AccountType").getValue());
                                                        String DAT="Admin";
                                                        if(ACtype.equals(DAT)){
                                                        progressDialog.dismiss();
                                                        startActivity(new Intent(aloginActivity.this, AdminHomepage.class));
                                                      //      }
                                                  }
                                                  else{
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getApplicationContext(), "not admin", Toast.LENGTH_SHORT).show();
                                                  }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });


            }
        });


    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Recover Password");

        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Enter email");
        emailEt.setHintTextColor(Color.LTGRAY);
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginRecovery(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
