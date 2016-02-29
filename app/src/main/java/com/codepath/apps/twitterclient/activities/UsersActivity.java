package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.fragments.UserFollowersFragment;
import com.codepath.apps.twitterclient.fragments.UserFriendsFragment;

public class UsersActivity extends BaseActivity  {
    private static final String LOG_TAG = UsersActivity.class.getSimpleName();

    public String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new UsersPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(getIntent().getIntExtra("tab", 0));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        screenName = getIntent().getStringExtra("screen_name");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // Return the order of the fragments in the viewpager
    public class UsersPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Followers", "Following"};

        public UsersPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return UserFollowersFragment.newInstance(screenName);
            } else if (position == 1) {
                return UserFriendsFragment.newInstance(screenName);
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
