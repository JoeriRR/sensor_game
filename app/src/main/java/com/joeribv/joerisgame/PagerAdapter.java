package com.joeribv.joerisgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>(3);
    private List<String> names = new ArrayList<>(3);

    PagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        if (position > fragments.size()) {
            throw new IllegalArgumentException();
        }

        return fragments.get(position);
    }

    void addFragment(Fragment f, String name) {
        fragments.add(f);
        names.add(name);
    }

    @Override
    public int getCount(){
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position > fragments.size()) {
            throw new IllegalArgumentException();
        }

        return names.get(position);
    }
}
