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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class Signup_student extends AppCompatActivity {

    EditText t_username,t_examrall,t_email,t_phoneno,t_tempassword,t_conpassword;
    private Button btn_register;
    RadioButton radioGenderMale,radioGenderFemale;
    DatabaseReference rootRef;
    String gender="";
    Integer q=0;
    Integer a=0;
    Integer l=0;
    String TeacherNotes="";
    String StudentNotes="";
    String FavNotes="";
    String userSongurl="https://drive.google.com/file/d/15T-kh_wvTpFMv_7RPrr9oRL0uixckzKX/view?usp=sharing";
    String status="hey! I'm using MCR infotech ";
    String images="https://firebasestorage.googleapis.com/v0/b/mscii-8cb88.appspot.com/o/logos.png?alt=media&token=1383c782-bbbf-41a0-a03b-924c6cf7b149";
    String Account="Student";
    String selectedCourse;
    String states1="no";
    String states2="no";
    String selectedSession;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String[] studentCourse = {"--Select Course--","Physics Honours", "Chemistry Honours", "Mathematics Honours", "Botany Honours",
            "Zoology Honours", "Bio-Technology", "Computer Application", "Computer Maintenance", "Information Technology",
            "Fashion Designing", "Business Administration", "Clinical Nutrition and Dietetics", "Commerce", "Economics Honours",
            "Geography Honours", "History Honours", "Home Science", "Philosophy Honours", "Political Science", "Psychology Honours",
            "Hindi Honours", "English Honours", "Bengali Honours", "Sanskrit Honours", "Urdu Honours", "Khadia Honours", "Khuruk Honours",
            "Mundali Honours", "Nagpuri Honours", "Kurmal Honours"};
    String[] studentSession = {"--Select Session--","2016-2019","2017-2020","2018-2021","2019-2022","2020-2023"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student);

        t_username=(EditText)findViewById(R.id.un);
        t_examrall=(EditText)findViewById(R.id.rl);


        t_email=(EditText)findViewById(R.id.emal);
        t_phoneno=(EditText)findViewById(R.id.pn);
        t_tempassword=(EditText)findViewById(R.id.sp);
        t_conpassword=(EditText)findViewById(R.id.cp);
        this.btn_register=findViewById(R.id.sign_up);
        progressDialog = new ProgressDialog(this,R.style.AlertDialogTheme);
        progressDialog.setMessage("Registering User...");
        radioGenderMale=(RadioButton)findViewById(R.id.radio_male);
        radioGenderFemale=(RadioButton)findViewById(R.id.radio_female);


        //Course Spinner
        Spinner spinCourse = (Spinner) findViewById(R.id.studCourse);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, studentCourse);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCourse.setAdapter(courseAdapter);
        spinCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCourse = parent.getItemAtPosition(position).toString();
                if (selectedCourse!="--Select Course--") {
                    Toast.makeText(parent.getContext(), selectedCourse, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Session Spinner
        Spinner spinSession = (Spinner) findViewById(R.id.studSession);
        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, studentSession);
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSession.setAdapter(sessionAdapter);
        spinSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSession = parent.getItemAtPosition(position).toString();
                if (selectedSession!="--Select Session--") {
                    Toast.makeText(parent.getContext(), selectedSession, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        firebaseAuth=FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = t_username.getText().toString();
                final String examRall = t_examrall.getText().toString();
//                course selected by student is stored in selectedCourse
               final String course = selectedCourse;

//                uper wala remove krna hoga aur niiche wala lana hoga
                final String session = selectedSession;

                final String email = t_email.getText().toString().trim();
                final String phoneno = t_phoneno.getText().toString();
                String password = t_tempassword.getText().toString().trim();
                String conpassword = t_conpassword.getText().toString().trim();

                if (radioGenderMale.isChecked()) {
                    gender = "Male";
                }
                if (radioGenderFemale.isChecked()) {
                    gender = "Female";
                }

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
                if (TextUtils.isEmpty(userName)) {
                    t_username.setError("enter username");
                    t_username.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(examRall)) {
                    t_examrall.setError("enter exam rall");
                    t_examrall.setFocusable(true);
                    return;

                }
//                if (TextUtils.isEmpty(session)) {
//                    t_session.setError("enter session");
//                    t_session.setFocusable(true);
//                    return;
//                }
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
                            .addOnCompleteListener(Signup_student.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final String currentUserId=firebaseAuth.getCurrentUser().getUid();

                                        studentDetail information = new studentDetail(
                                                userName,
                                                examRall,
                                                session,
                                                email,
                                                phoneno,
                                                gender,
                                                Account,
                                                course,
                                                status,
                                                images,
                                                TeacherNotes,
                                                StudentNotes,
                                                FavNotes,
                                                userSongurl,
                                                q,
                                                a,
                                                l,
                                                states1,
                                                states2




                                        );

                                        FirebaseDatabase.getInstance().getReference("studentDetail")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                               .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                rootRef=FirebaseDatabase.getInstance().getReference();
                                                String deviceToken= FirebaseInstanceId.getInstance().getToken();
                                                rootRef.child("studentDetail").child(currentUserId).child("device_token")
                                                        .setValue(deviceToken);
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Register complete", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Signup_student.this, sloginActivity.class));
                                                finish();
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
