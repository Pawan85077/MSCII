package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StudentPagerAdapter extends FragmentPagerAdapter {
    StudentPagerAdapter( FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AdminMessage();
            case 1:
                return new MyAdmin();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Admin  message";
            case 1:
                return "My  admin";
            default:
                return null;

        }
    }
}
