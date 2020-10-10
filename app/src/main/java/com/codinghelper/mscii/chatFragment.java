package com.codinghelper.mscii;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class chatFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsAccessorAdaptor tabsAccessorAdaptor;
    FirebaseAuth firebaseAuth;

    public chatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
      /* ref.child("GlobalChat").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "created", Toast.LENGTH_SHORT).show();

            }
        });*/
        viewPager=(ViewPager)v.findViewById(R.id.chattab);
        tabsAccessorAdaptor=new TabsAccessorAdaptor(getChildFragmentManager());
        viewPager.setAdapter(tabsAccessorAdaptor);
        tabLayout=(TabLayout)v.findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);
        getActivity().setTitle("Chat");
        return v;
    }

}
