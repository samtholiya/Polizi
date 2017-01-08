package com.polizi.iam.polizi.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.polizi.iam.polizi.user.fragments.CreateUser;
import com.polizi.iam.polizi.user.fragments.Login;

/**
 * Created by shubh on 04-01-2017.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Check In", "Create User", "Tab3" };
    private Context context;

    public FragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("FragmentPageAdapter", String.valueOf(position));
        switch (position) {
            case 0:
                return Login.newInstance();
            case 1:
                return CreateUser.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];

    }
}
