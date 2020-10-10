package com.codinghelper.mscii;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_home,fragment)
                    .commit();
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        BottomNavigationView navView = v.findViewById(R.id.nav_view_home);

       navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               Fragment fragment=null;

               if(menuItem.getItemId()==R.id.navigation_explore){
                   fragment=new Explore();
               }
               else if(menuItem.getItemId()==R.id.navigation_activities){
                   fragment=new Activities();
                /*   FragmentManager fragmentManager=getFragmentManager();
                   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.nav_host_fragment_home,fragment).addToBackStack(null).commit();*/
               }
               else if(menuItem.getItemId()==R.id.navigation_grade){
                   fragment=new Grade();
                 /*  FragmentManager fragmentManager=getFragmentManager();
                   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.nav_host_fragment_home,fragment).addToBackStack(null).commit();*/
               }
               return loadFragment(fragment);
           }
       });
        loadFragment(new Explore());

        return v;
    }


}
