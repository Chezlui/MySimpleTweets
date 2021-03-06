package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.activities.BaseActivity;
import com.codepath.apps.twitterclient.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chezlui on 26/02/16.
 */
public class UserFriendsFragment extends UsersListFragment {
    private static final String LOG_TAG = UserFriendsFragment.class.getSimpleName();
    public int downloaderTracker;
    public int downloaderTrackObjective;

    private TwitterClient client;

    private SwipeRefreshLayout swipe;

    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    // Define the code block to be executed
    Runnable progessChecker = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            handler.postDelayed(progessChecker, 200);
            if (downloaderTracker >= downloaderTrackObjective) {
                ((BaseActivity) getActivity()).hideProgressBar();
            }
        }
    };

    @Override
    public void onPause() {
        handler.removeCallbacks(progessChecker);
        super.onPause();
    }

    public static UserFriendsFragment newInstance(String screenName) {
        UserFriendsFragment userTimeLineFragment = new UserFriendsFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimeLineFragment.setArguments(args);
        return userTimeLineFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApplication.getRestClient(); // singleton client

        getFriendsIds();
    }


    private void getFriendsIds() {
        String screenName = getArguments().getString("screen_name");
        client.getFriends(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                Type type = new TypeToken<List<Long>>() {}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
                try {
                    JSONArray arrayIds = json.getJSONArray("ids");
                    listIds = (ArrayList<Long>) gson.fromJson(arrayIds.toString(), type);
                    populateMore(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                Log.d(LOG_TAG, "Users ids added to the list: " + listIds.size());
            }


            // FAILURE (Failure won't be an array)
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    Log.e(LOG_TAG, errorResponse.toString());
                }
            }
        });
    }


    @Override
    public void populateMore(int page) {
        super.populateMore(page);
        getUsers(page);
    }


    // Send an API request to get the timeline JSON
    // Fill the listview as well by creating the tweets objects from JSON
    private void getUsers(final int page) {

        Log.d(LOG_TAG, "Trying to load page: " + page);
        ((BaseActivity) getActivity()).showProgressBar();

        // When downloadTracker reach 10 it will hideProgressBar
        handler.post(progessChecker);

        downloaderTrackObjective = (page + 1)*10 - 1;

        for (int i = page * 10; i < (page + 1) * 10; i++) {
            client.getAnyUserInfo(listIds.get(i), new JsonHttpResponseHandler() {
                                           // SUCCESS (use Array because we know it is an array
                                           @Override
                                           public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                                               Log.d("DEBUG", json.toString());
                                               Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

                                               User user = gson.fromJson(json.toString(), User.class);
                                               getAdapter().addUser(user);
                                               if(swipe != null) {
                                                   swipe.setRefreshing(false);
                                               }
                                               downloaderTracker++;
                                               Log.d(LOG_TAG, ""+  downloaderTracker);
                                           }


                                           // FAILURE (Failure won't be an array)
                                           @Override
                                           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                               if (errorResponse != null) {
                                                   Log.e(LOG_TAG, errorResponse.toString());
                                               }
                                                downloaderTracker++;
                                                Log.d(LOG_TAG, ""+  downloaderTracker);
                                           }
                                       }
            );
        }
    }
}
