package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class StudentPagerAdapter extends FragmentStatePagerAdapter {

    int nNoOfTabs;

    public StudentPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.nNoOfTabs = NumberOfTabs;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyAdmin ma= new MyAdmin();
                return ma;
            case 1:
                AdminMessage am= new AdminMessage();
                return am;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nNoOfTabs;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "My Admin";
            case 1:
                return "Admin Message";
            default:
                return null;

        }
    }
}
