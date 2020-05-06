package com.codinghelper.mscii;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Pattern;

public class Signup_student extends AppCompatActivity {
private static final String TAG="Signup_student";
    EditText t_username,t_examrall,t_email,t_phoneno,t_tempassword,t_conpassword;
    private Button btn_register;
    private TextView t_session;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    RadioButton radioGenderMale,radioGenderFemale;
    DatabaseReference databaseReference;
    String gender="";
    String Account="Student";
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_student);

        t_username=(EditText)findViewById(R.id.un);
        t_examrall=(EditText)findViewById(R.id.rl);
        t_session=(TextView) findViewById(R.id.ss);
        t_email=(EditText)findViewById(R.id.emal);
        t_phoneno=(EditText)findViewById(R.id.pn);
        t_tempassword=(EditText)findViewById(R.id.sp);
        t_conpassword=(EditText)findViewById(R.id.cp);
        this.btn_register=findViewById(R.id.sign_up);
        progressDialog = new ProgressDialog(this,R.style.AlertDialogTheme);
        progressDialog.setMessage("Registering User...");
        radioGenderMale=(RadioButton)findViewById(R.id.radio_male);
        radioGenderFemale=(RadioButton)findViewById(R.id.radio_female);


        databaseReference= FirebaseDatabase.getInstance().getReference("studentDetail");
        firebaseAuth=FirebaseAuth.getInstance();
        t_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        Signup_student.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.show();
            }
        });
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=month+"/"+day+"/"+year;
                t_session.setText(date);
            }
        };

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = t_username.getText().toString();
                final String examRall = t_examrall.getText().toString();
                final String session = t_session.getText().toString();
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
                if (TextUtils.isEmpty(session)) {
                    t_session.setError("enter session");
                    t_session.setFocusable(true);
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
                            .addOnCompleteListener(Signup_student.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        studentDetail information = new studentDetail(
                                                userName,
                                                examRall,
                                                session,
                                                email,
                                                phoneno,
                                                gender,
                                                Account

                                        );

                                        FirebaseDatabase.getInstance().getReference("studentDetail")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
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
