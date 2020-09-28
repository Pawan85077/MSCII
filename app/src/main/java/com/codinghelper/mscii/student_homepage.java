package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static java.security.AccessController.getContext;

public class student_homepage extends AppCompatActivity {

    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference,Rootref,deleteref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);



        Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.student_homepage);
        firebaseAuth = FirebaseAuth.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        user= firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail").child(user.getUid());
        Rootref=FirebaseDatabase.getInstance().getReference();
        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
        View view=navigationView.inflateHeaderView(R.layout.student_nav_header);
        final ImageView imageView=(ImageView)view.findViewById(R.id.nimg);
        final TextView textView=(TextView)view.findViewById(R.id.ntitle);
        final  TextView textView2=(TextView)view.findViewById(R.id.nsubtitle);
       // imageView.setImageResource(R.drawable.capt);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name =String.valueOf(dataSnapshot.child("username").getValue());
                String email =String.valueOf(dataSnapshot.child("Email").getValue());
                textView.setText(name);
                textView2.setText(email);
                String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.logos).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        NavController navController = Navigation.findNavController(this,R.id.navHostfrag);
        NavigationUI.setupWithNavController(navigationView,navController);


    }

  /*  @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
    }*/

   /* private void checkUserStatus(){
        FirebaseUser user= firebaseAuth.getCurrentUser();
        if (user !=null){
        a.setText(user.getEmail());
        }
        else{
            startActivity(new Intent(student_homepage.this, MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ff_f) {
            startActivity(new Intent(student_homepage.this, Find_friend_activity.class));
        }
        if (id == R.id.create_group) {
            requestNewGroup();
        }
        if (id == R.id.action_logout) {
            firebaseAuth.getInstance().signOut();
            new User(student_homepage.this).removeUser();
            startActivity(new Intent(student_homepage.this, sloginActivity.class));
            finish();
            Toast.makeText(getApplicationContext(), "successfully logout!", Toast.LENGTH_SHORT).show();

        }
        if (id == R.id.action_Delete) {
            requestToDeleteAccount();
        }

      /*  if(id==R.id.nav_profile){
            startActivity(new Intent(student_homepage.this, Profile.class));
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void requestToDeleteAccount() {

        AlertDialog.Builder builder=new AlertDialog.Builder(student_homepage.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        builder.setTitle("Delete account");
        builder.setIcon(R.drawable.ic_warning);
        final TextView Warning=new TextView(student_homepage.this);
        Warning.setTextSize(16);
        Warning.setText("                                                                                  "+"Are you sure want to delete account!!");
        builder.setView(Warning);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final AlertDialog.Builder builder2=new AlertDialog.Builder(student_homepage.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                builder2.setTitle("Proofs that's you!!");
                final EditText groupNameField=new EditText(student_homepage.this);
                groupNameField.setHint("Password");
                builder2.setView(groupNameField);
                builder2.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          AuthCredential credential;
                          String password=groupNameField.getText().toString();
                          credential= EmailAuthProvider.getCredential(user.getEmail(),password);
                        user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reference.removeValue();
                                new User(student_homepage.this).removeUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(),"Account deleted", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(student_homepage.this, sloginActivity.class));
                                                    finish();

                                                }else {
                                                    Toast.makeText(getApplicationContext(),"Error occured", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder2.show();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void requestNewGroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(student_homepage.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        builder.setTitle("Enter Group Name");
        final EditText groupNameField=new EditText(student_homepage.this);
        groupNameField.setHint("e.g, SUPR-D");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName=groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    groupNameField.setError("enter email");
                    groupNameField.setFocusable(true);
                }else{
                    CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
            }
        });
        builder.show();

    }

    private void CreateNewGroup(final String groupName) {
        Rootref.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), groupName+" is created successfully...!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}