package com.polizi.iam.polizi.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.polizi.iam.polizi.user.fragments.CreateUser;
import com.polizi.iam.polizi.user.fragments.Login;
import com.polizi.iam.polizi.user.fragments.UserDashboard;

/**
 * Created by shubh on 04-01-2017.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Check In", "Create User", "Dashboard" };
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
            case 2:
                return UserDashboard.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];

    }
}
