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
//     implements MyAdmin.OnFragmentInteractionListener, AdminMessage.OnFragmentInteractionListener

//    private ViewPager viewPager;
//    private TabLayout tabLayout;
//    private StudentPagerAdapter studentPagerAdapter;
//    String currentUserID;
//
//
//    private FragmentManager getSupportFragmentManager() {
//        return null;
//    }
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
//
//      /* ref.child("GlobalChat").setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getContext(), "created", Toast.LENGTH_SHORT).show();
//
//            }
//        });*/
//        viewPager = (ViewPager) v.findViewById(R.id.chattab);
//        studentPagerAdapter = new StudentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(studentPagerAdapter);
//        tabLayout = (TabLayout) v.findViewById(R.id.main_tab);
//        tabLayout.setupWithViewPager(viewPager);
//        getActivity().setTitle("My Admin");
        return v;
    }
}

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public StudentAdminFeedMainPage2() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment StudentAdminFeedMainPage2.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static StudentAdminFeedMainPage2 newInstance(String param1, String param2) {
//        StudentAdminFeedMainPage2 fragment = new StudentAdminFeedMainPage2();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//
//    private void setContentView(int activity_student_admin_feed_main_page) {



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_admin_feed_main_page);
//
//        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("My Admin"));
//        tabLayout.addTab(tabLayout.newTab().setText("Admin Message"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        final ViewPager viewPager= (ViewPager)findViewById(R.id.pager);
//        final StudentPagerAdapter adapter=new StudentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

//}
