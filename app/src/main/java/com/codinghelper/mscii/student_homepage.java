package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class student_homepage extends AppCompatActivity {

    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String currentUserID;
    DatabaseReference reference,Rootref,deleteref,Root;
    BottomNavigationView navView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homepage);



        toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.student_homepage);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        user= firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail").child(user.getUid());
        Root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        Rootref=FirebaseDatabase.getInstance().getReference();
        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
        navView = (BottomNavigationView)findViewById(R.id.nav_view_home);
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
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(R.id.nav_profile,
                R.id.nav_dashboard,R.id.nav_home,R.id.nav_chat,R.id.nav_notification,
                R.id.nav_developer,R.id.navigation_explore,R.id.navigation_activities,
                R.id.navigation_grade,R.id.nav_admin_feed,R.id.navigation_settings, R.id.nav_Election,
                R.id.nav_question_bank,R.id.nav_HelpAndFeedback,R.id.nav_share,R.id.nav_rateUs,
                R.id.nav_about_app,R.id.nav_privacy_policy)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this,R.id.navHostfrag);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
        NavigationUI.setupWithNavController(navView,navController);

//        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nested_scroll_view);
//        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Toast.makeText(student_homepage.this, "her here hrer hre", Toast.LENGTH_SHORT).show();
//                if (scrollY < oldScrollY) { // up
//                    animateNavigation(false);
//                    animateSearchBar(false);
//                }
//                if (scrollY > oldScrollY) { // down
//                    animateNavigation(true);
//                    animateSearchBar(true);
//                }
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        updateUserStatus("online");

    }

    @Override
    protected void onStop() {
        super.onStop();
      //  updateUserStatus("offline");
    }


//    Animate tabbar and bottom navigation bar
//    boolean isNavigationHide = false;
//
//    private void animateNavigation(final boolean hide) {
//        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
//        isNavigationHide = hide;
//        int moveY = hide ? (2 * navView.getHeight()) : 0;
//        navView.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
//        Toast.makeText(this, "animate Navigation", Toast.LENGTH_SHORT).show();
//    }
//
//    boolean isSearchBarHide = false;
//
//    private void animateSearchBar(final boolean hide) {
//        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
//        isSearchBarHide = hide;
//        int moveY = hide ? -(2 * toolbar.getHeight()) : 0;
//        toolbar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
//        Toast.makeText(this, "animate Toolbar", Toast.LENGTH_SHORT).show();
//    }



    private void updateUserStatus(String state) {
       String saveCurrentTime, saveCurrentDate;
       Calendar calendar=Calendar.getInstance();
       SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
       saveCurrentDate=currentDate.format(calendar.getTime());

       SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
       saveCurrentTime=currentTime.format(calendar.getTime());

       HashMap<String,Object> onlineState=new HashMap<>();
       onlineState.put("time",saveCurrentTime);
       onlineState.put("date",saveCurrentDate);
       onlineState.put("state",state);

       Root.child(currentUserID).child("userOnlineState").updateChildren(onlineState);
   }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();

            if (doubleBackToExitPressedOnce) {
                //    updateUserStatus("offline");
                super.onBackPressed();
                finish();
                return;

            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);        }
       /* if (doubleBackToExitPressedOnce) {
        //    updateUserStatus("offline");
            super.onBackPressed();
            return;

        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);*/
    }

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

            Rootref.child("studentDetail").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        final String Simg1 =String.valueOf(dataSnapshot.child("states1").getValue());
                        final String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());

                        if(Simg1.equals("no")&&Simg2.equals("no")){
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(student_homepage.this, sloginActivity.class));
                            Toast.makeText(getApplicationContext(), "successfully logout!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            AlertDialog.Builder builder=new AlertDialog.Builder(student_homepage.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                            builder.setTitle("you need to delete your status before logout!!");

                            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
            if(!Simg1.equals("no")){
                            StorageReference referenceDel= FirebaseStorage.getInstance().getReferenceFromUrl(Simg1);
                            referenceDel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Rootref.child("studentDetail").child(currentUserID).child("states1").setValue("no");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        if(!Simg2.equals("no")){
                            StorageReference referencedel=FirebaseStorage.getInstance().getReferenceFromUrl(Simg2);
                            referencedel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Rootref.child("studentDetail").child(currentUserID).child("states2").setValue("no");
                                }
                            });
                        }
                                    Toast.makeText(getApplicationContext(), "status deleted, now you can logout!!", Toast.LENGTH_SHORT).show();


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



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







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
        startActivity(new Intent(this, CreateGroup.class));

       /* AlertDialog.Builder builder=new AlertDialog.Builder(student_homepage.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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
        builder.show();*/

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