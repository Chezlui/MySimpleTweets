package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.fragments.UserTimeLineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tvTagline) TextView tvTagline;
    @Bind(R.id.tvFollowers) TextView tvFollowers;
    @Bind(R.id.tvFollowing) TextView tvFollowing;
    @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        client = TwitterApplication.getRestClient();

        String screenName = getIntent().getStringExtra("screen_name");

        client.getAnyUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, response.toString());

                Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
                user = gson.fromJson(response.toString(), User.class);

                getSupportActionBar().setTitle("@" + user.getName());
                populateProfileHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(LOG_TAG, errorResponse.toString());
            }
        });


        if (savedInstanceState == null) {
            UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimeLineFragment);

            ft.commit();
        }
    }
    private void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowersCount() + " followers");
        tvFollowing.setText(user.getFollowingCount() + " following");

        Glide.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

}
