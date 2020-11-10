package com.codinghelper.mscii;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;

    public AdminProfileFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.admin_fragment_profile, container, false);

        final TextView name=(TextView)v.findViewById(R.id.admin_full_name);
        final TextView Pname=(TextView)v.findViewById(R.id.admin_pro_name);
        final TextView Pphone=(TextView)v.findViewById(R.id.admin_pro_phone);
        final TextView Pemail=(TextView)v.findViewById(R.id.admin_pro_email);
        final TextView status=(TextView)v.findViewById(R.id.admin_status_name);
        final ImageView imageView=(ImageView)v.findViewById(R.id.admin_profile_image);
        final TextView Pdepartment=(TextView)v.findViewById(R.id.admin_pro_department);

        Button Pedit=(Button)v.findViewById(R.id.Edit_admin_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                name.setText(Sname);
                Pname.setText(Sname);

                String Sphone =String.valueOf(dataSnapshot.child("phoneno").getValue());
                Pphone.setText(Sphone);
                String Semail =String.valueOf(dataSnapshot.child("Email").getValue());
                Pemail.setText(Semail);
                String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(imageView);
                String Sstatus =String.valueOf(dataSnapshot.child("userstatus").getValue());
                status.setText(Sstatus);
                String Sdepartment =String.valueOf(dataSnapshot.child("Scourse").getValue());
                Pdepartment.setText(Sdepartment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Pedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), editProfile.class));
            }
        });

        return v;
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
        //hide item
        menu.findItem(R.id.action_refresh).setVisible(false);
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
