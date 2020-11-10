package com.codinghelper.mscii;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentAdminFeedMainPage2 extends Fragment{

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private StudentPagerAdapter studentPagerAdapter;
    FirebaseAuth firebaseAuth;
    DatabaseReference Root;
    String currentUserID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_admin_feed_main_page2, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        viewPager=(ViewPager)v.findViewById(R.id.myadmintab);
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        studentPagerAdapter=new StudentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(studentPagerAdapter);
        Root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        tabLayout=(TabLayout)v.findViewById(R.id.main_tab_A);
        tabLayout.setupWithViewPager(viewPager);
        getActivity().setTitle("Admin Feed");
        return v;
    }
}

