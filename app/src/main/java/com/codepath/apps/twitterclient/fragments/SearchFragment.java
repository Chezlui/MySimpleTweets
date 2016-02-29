package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.Utility;
import com.codepath.apps.twitterclient.activities.BaseActivity;
import com.codepath.apps.twitterclient.models.Tweet;
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
public class SearchFragment extends TweetsListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String LOG_TAG = SearchFragment.class.getSimpleName();

    private TwitterClient client;
    private long maxId = 0;

    private SwipeRefreshLayout swipe;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApplication.getRestClient(); // singleton client

        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipe.setOnRefreshListener(this);
        populateSearch(0);
    }

    public void populateMore(int page) {
        populateSearch(page);
    }

    // Send an API request to get the timeline JSON
    // Fill the listview as well by creating the tweets objects from JSON
    private void populateSearch(final int page) {
        String query = getActivity().getIntent().getStringExtra("search");
        Log.d(LOG_TAG, "Looking for " + query);
        getActivity().setTitle(query);

        Log.d(LOG_TAG, "Trying to load page: " + page);
        ((BaseActivity)getActivity()).showProgressBar();
        client.getSearch(query, new JsonHttpResponseHandler() {
                                   // SUCCESS (use Array because we know it is an array
                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                                       ((BaseActivity)getActivity()).hideProgressBar();
                                       Log.d("DEBUG", json.toString());
                                       Log.d("DEBUG", "max ID: " + maxId);
                                       // JSON here
                                       // Deserialize JSON
                                       // Create model and added them to the adapter
                                       // Load model data into listView
                                       Type collectionType = new TypeToken<List<Tweet>>(){}.getType();
                                       Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

                                       ArrayList<Tweet> newTweets = null;
                                       try {
                                           JSONArray jsonArray = json.getJSONArray("statuses");

                                           newTweets = (ArrayList<Tweet>) gson.fromJson(jsonArray.toString(), collectionType);
                                           findMaxId(newTweets);
                                           addAll(newTweets);
                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }
                                       getAdapter().notifyDataSetChanged();
                                       if(swipe != null) {
                                           swipe.setRefreshing(false);
                                       }
                                       Log.d(LOG_TAG, "Tweets added to the adapter: " + newTweets.size());
                                       Log.d(LOG_TAG, "Tweets in adapter: " + getAdapter().getItemCount());
                                   }


                                   // FAILURE (Failure won't be an array)
                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                       ((BaseActivity)getActivity()).hideProgressBar();
                                       if (errorResponse != null) {
                                           Log.e(LOG_TAG, errorResponse.toString());
                                       }
                                       // If trying to load new pages by scrolling, don't do it
                                       if(page != 0) return;
                                       // Loading from ddbb
                                       ArrayList<Tweet> newTweets = (ArrayList<Tweet>) Tweet.getAll();
                                       findMaxId(newTweets);
                                       addAll(newTweets);
                                       getAdapter().notifyDataSetChanged();
                                       Log.d(LOG_TAG, "Loaded " + newTweets.size() + " items from the ddbb");
                                   }
                               },

                maxId);
    }

    private void findMaxId(ArrayList<Tweet> newTweets) {
        if(maxId == 0) {
            maxId = Long.MAX_VALUE;
        }
        for (Tweet  tweet: newTweets) {
            maxId = tweet.getUid() < maxId ? tweet.getUid() - 1 : maxId;
        }
    }


    @Override
    public void onRefresh() {
        if (Utility.isNetworkAvailable(getActivity())) {
            maxId = 0;
            getAdapter().clear();
            populateSearch(0);
        } else {
            Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_LONG).show();
            swipe.setRefreshing(false);
        }
    }

}
