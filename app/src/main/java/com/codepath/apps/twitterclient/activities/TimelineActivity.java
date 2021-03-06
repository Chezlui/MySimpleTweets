package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.Utility;
import com.codepath.apps.twitterclient.fragments.HomeTimeLineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimeLineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends BaseActivity  {
    private static final String LOG_TAG = TimelineActivity.class.getSimpleName();
    String query;
    String profileImageUrl;

    @Bind(R.id.viewpager) ViewPager viewPager;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        tabStrip.setViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeDialog();
            }
        });

        getProfileImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TimelineActivity.this.query = query;

                // perform query here
                onTweetSearch(searchView);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // Return the order of the fragments in the viewpager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        private String tabTitles[] = {"Home", "Mentions"};
        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimeLineFragment();
            } else if (position == 1) {
                return new MentionsTimeLineFragment();
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
    public void onTweetSearch(View view) {
        if (!Utility.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        // Launch Activity
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("search", ((SearchView)view).getQuery().toString());
        startActivity(intent);
    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialog composeDialog = ComposeDialog.newInstance(profileImageUrl);
        composeDialog.show(fm, "Compose your tweet");
    }

    public void getProfileImage() {
        TwitterClient client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    profileImageUrl = response.getString("profile_image_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(LOG_TAG, errorResponse.toString());
            }
        });
    }
}
