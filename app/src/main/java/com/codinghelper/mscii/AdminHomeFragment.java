package com.codinghelper.mscii;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminHomeFragment extends Fragment {

    public AdminHomeFragment(){
        //Public home fragment
    }

    RecyclerView AdminRecyclerView;
    Admin_Recycler_Adapter admin_recycler_adapter;

    DatabaseReference Rootref;
    FirebaseAuth firebaseAuth;
    String currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.admin_fragment_home, container, false);
        View view = inflater.inflate(R.layout.admin_fragment_home, container, false);

        final ExtendedFloatingActionButton composeMessageFAB = view.findViewById(R.id.composeMessageFAB);
        composeMessageFAB.shrink();

        Rootref= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();

        AdminRecyclerView = view.findViewById(R.id.admin_home_RecyclerView);
        admin_recycler_adapter = new Admin_Recycler_Adapter();

        AdminRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdminRecyclerView.setAdapter(admin_recycler_adapter);
//        recyclerView.setHasFixedSize(true);

        //FAB click listener
        composeMessageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ExtendedFloatingActionButton extFab = (ExtendedFloatingActionButton)view;
//                if(extFab.isExtended())
//                    extFab.shrink();
//                else
//                    extFab.extend();
                Intent composeMessageIntent = new Intent(getContext(),AdminComposeMessage.class);
                startActivity(composeMessageIntent);
            }
        });

        AdminRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0) {
                    //scroll Down
                    if (composeMessageFAB.isExtended()) composeMessageFAB.shrink();
                }else if (dy <0){
                    //scroll up
                    if(!composeMessageFAB.isExtended()) composeMessageFAB.extend();
                }
            }
        });

        getActivity().setTitle("Homepage");
        return view;
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
            //Toast.makeText(getContext().getApplicationContext(),"Logout Clicked",Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
            builder.setTitle("Logout confirmation");
            builder.setIcon(R.drawable.ic_warning);
            builder.setMessage("Are you sure ?\n" +
                    "Do you really wanna logout ?");

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseAuth.getInstance().signOut();
                    new User(getContext()).removeUser();
                    startActivity(new Intent(getContext(), aloginActivity.class));
                    Toast.makeText(getContext().getApplicationContext(), "successfully logout!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    Toast.makeText(getContext().getApplicationContext(), "Logout cancelled :)", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();



        }
        if(id == R.id.action_view_student){
            Toast.makeText(getContext().getApplicationContext(),"View students profile",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


}

