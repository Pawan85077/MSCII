package com.codinghelper.mscii;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomepage extends AppCompatActivity {

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();


        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminHomeFragment()).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item selected
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            Toast.makeText(getApplicationContext(),"Refresh",Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.action_admin_logout){
            //Toast.makeText(getContext().getApplicationContext(),"Logout Clicked",Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder=new AlertDialog.Builder(AdminHomepage.this,R.style.Theme_AppCompat_Light_Dialog_MinWidth);
            builder.setTitle("Are you sure want to logout?");
            builder.setIcon(R.drawable.ic_warning);

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AdminHomepage.this, aloginActivity.class));
                    Toast.makeText(getApplicationContext(), "successfully logout!", Toast.LENGTH_SHORT).show();
                    finish();
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
        if(id == R.id.action_view_student){
            Toast.makeText(getApplicationContext(),"View students profile",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch(menuItem.getItemId()){
                        case R.id.nav_home2:
                            selectedFragment = new AdminHomeFragment();
                            break;
                        case R.id.nav_message2:
                            selectedFragment = new AdminMessageFragment();
                            break;
                        case R.id.nav_student2:
                            selectedFragment = new AdminStudentFragment();
                            break;
                        case R.id.nav_profile2:
                            selectedFragment = new AdminProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };



}
