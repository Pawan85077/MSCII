package com.codinghelper.mscii;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView name=(TextView)v.findViewById(R.id.full_name);
        final TextView Pname=(TextView)v.findViewById(R.id.pro_name);
        final TextView Proll=(TextView)v.findViewById(R.id.Pro_roll);
        final TextView Pphone=(TextView)v.findViewById(R.id.pro_phone);
        final TextView Pemail=(TextView)v.findViewById(R.id.pro_email);
        final TextView Psession=(TextView)v.findViewById(R.id.pro_session);
        final TextView Pgender=(TextView)v.findViewById(R.id.Pro_gender);
        final TextView status=(TextView)v.findViewById(R.id.status_name);
        final ImageView imageView=(ImageView)v.findViewById(R.id.profile_image);
        final TextView Pcourse=(TextView)v.findViewById(R.id.Pro_course);

        Button Pedit=(Button)v.findViewById(R.id.Edit_profile);



        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                name.setText(Sname);
                Pname.setText(Sname);
                String Sroll =String.valueOf(dataSnapshot.child("Examrall").getValue());
                Proll.setText(Sroll);
                String Sphone =String.valueOf(dataSnapshot.child("phoneno").getValue());
                Pphone.setText(Sphone);
                String Semail =String.valueOf(dataSnapshot.child("Email").getValue());
                Pemail.setText(Semail);
                String Ssession =String.valueOf(dataSnapshot.child("session").getValue());
                Psession.setText(Ssession);
                String Sgender =String.valueOf(dataSnapshot.child("gender").getValue());
                Pgender.setText("    "+Sgender);
                String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(imageView);
                String Sstatus =String.valueOf(dataSnapshot.child("userstatus").getValue());
                status.setText(Sstatus);
                String Stcourse =String.valueOf(dataSnapshot.child("Scourse").getValue());
                Pcourse.setText(Stcourse);

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

        return  v;

    }


}
