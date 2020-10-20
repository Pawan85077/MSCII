package com.codinghelper.mscii;


import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    WebView webView;
    ProgressDialog progressDialog;


    public HomeFragment() {
        // Required empty public constructor
    }
   /* private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_home,fragment)
                    .commit();
        }
        return false;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        webView=(WebView)v.findViewById(R.id.webView);
        progressDialog = new ProgressDialog(getActivity(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("http://www.marwaricollegeranchi.ac.in/");
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Error:"+description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl("http://www.marwaricollegeranchi.ac.in/");


      /*  BottomNavigationView navView = v.findViewById(R.id.nav_view_home);

       navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               Fragment fragment=null;

               if(menuItem.getItemId()==R.id.navigation_explore){
                   fragment=new Explore();
               }
               else if(menuItem.getItemId()==R.id.navigation_explore){
                   fragment=new Activities();
                /*   FragmentManager fragmentManager=getFragmentManager();
                   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.nav_host_fragment_home,fragment).addToBackStack(null).commit();
               }
               else if(menuItem.getItemId()==R.id.navigation_grade){
                   fragment=new Grade();
                 /*  FragmentManager fragmentManager=getFragmentManager();
                   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.nav_host_fragment_home,fragment).addToBackStack(null).commit();
               }
               return loadFragment(fragment);
           }
       });
        loadFragment(new Explore());*/

        return v;
    }


}
