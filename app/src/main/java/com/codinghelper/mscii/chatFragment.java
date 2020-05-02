package com.codinghelper.mscii;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class chatFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsAccessorAdaptor tabsAccessorAdaptor;

    public chatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        viewPager=(ViewPager)v.findViewById(R.id.chattab);
        tabsAccessorAdaptor=new TabsAccessorAdaptor(getChildFragmentManager());
        viewPager.setAdapter(tabsAccessorAdaptor);
        tabLayout=(TabLayout)v.findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

}
