package com.android.entrepreneurclub.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.entrepreneurclub.Register_Fragment.EntreprenurerRegisterFragment;
import com.android.entrepreneurclub.Register_Fragment.InvesterRegisterFragment;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:

                return new EntreprenurerRegisterFragment();

            case 1:

                return new InvesterRegisterFragment();


            default:
                break;

        }

        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return "Entrepreneur";
            case 1:
                return "Investor";


            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
