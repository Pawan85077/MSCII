package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class student_homepage extends AppCompatActivity {

    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;

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
        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
        View view=navigationView.inflateHeaderView(R.layout.student_nav_header);
        final ImageView imageView=(ImageView)view.findViewById(R.id.nimg);
        final TextView textView=(TextView)view.findViewById(R.id.ntitle);
        final  TextView textView2=(TextView)view.findViewById(R.id.nsubtitle);
       // imageView.setImageResource(R.drawable.capt);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name =String.valueOf(dataSnapshot.child("Username").getValue());
                String email =String.valueOf(dataSnapshot.child("Email").getValue());
                textView.setText(name);
                textView2.setText(email);
                String Simg =String.valueOf(dataSnapshot.child("Image").child("imageUrl").getValue());
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
        if (id == R.id.action_logout) {
            firebaseAuth.getInstance().signOut();
            new User(student_homepage.this).removeUser();
            startActivity(new Intent(student_homepage.this, sloginActivity.class));
            finish();
            Toast.makeText(getApplicationContext(), "successfully logout!", Toast.LENGTH_SHORT).show();

        }

      /*  if(id==R.id.nav_profile){
            startActivity(new Intent(student_homepage.this, Profile.class));
        }*/
        return super.onOptionsItemSelected(item);
    }


    }