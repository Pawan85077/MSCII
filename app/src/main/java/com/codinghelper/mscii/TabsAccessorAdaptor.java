package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdaptor extends FragmentPagerAdapter {
    TabsAccessorAdaptor( FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GlobalChat();
            case 1:
                return new FriendChat();
            case 2:
                return new ChatsFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Groups";
            case 1:
                return "Friends";
            case 2:
                return "Chat";
            default:
                return null;

        }
    }
}
