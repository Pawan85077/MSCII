package com.codinghelper.mscii;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminStudentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_student, container, false);

    }

    //Options menu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.admin_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu item selected
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            Toast.makeText(getContext().getApplicationContext(),"Refresh",Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.action_admin_logout){
            Toast.makeText(getContext().getApplicationContext(),"Logout Clicked",Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.action_view_student){
            Toast.makeText(getContext().getApplicationContext(),"View students prifile",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
