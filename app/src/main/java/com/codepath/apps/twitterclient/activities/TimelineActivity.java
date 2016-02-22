package com.codepath.apps.twitterclient.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.Utility;
import com.codepath.apps.twitterclient.adapters.DividerItemDecoration;
import com.codepath.apps.twitterclient.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.adapters.ItemClickSupport;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

public class TimelineActivity extends AppCompatActivity implements DialogInterface.OnDismissListener,
    SwipeRefreshLayout.OnRefreshListener {
    private static final String LOG_TAG = TimelineActivity.class.getSimpleName();

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private long maxId = 0;
    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;
    private SwipeRefreshLayout swipe;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        tweets = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeDialog();
            }
        });

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipe.setOnRefreshListener(this);

        aTweets = new TweetsArrayAdapter(this, tweets);
        rvTweets.setAdapter(aTweets);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(llm);
        rvTweets.setItemAnimator(new FlipInBottomXAnimator());

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        Tweet tweet = tweets.get(position);
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        startActivity(intent);
                    }
                }
        );

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline(page);
            }
        });

        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline(0);
    }

    // Send an API request to get the timeline JSON
    // Fill the listview as well by creating the tweets objects from JSON
    private void populateTimeline(final int page) {
        Log.d(LOG_TAG, "Trying to load page: " + page);
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // SUCCESS (use Array because we know it is an array
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                Log.d("DEBUG", "max ID: " + maxId);
                // JSON here
                // Deserialize JSON
                // Create model and added them to the adapter
                // Load model data into listView
                Type collectionType = new TypeToken<List<Tweet>>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                ArrayList<Tweet> newTweets = (ArrayList<Tweet>) gson.fromJson(json.toString(), collectionType);
                findMaxId(newTweets);
                Tweet.persistTweets(newTweets);
                tweets.addAll(newTweets);
                aTweets.notifyDataSetChanged();
                if(swipe != null) {
                    swipe.setRefreshing(false);
                }
                Log.d(LOG_TAG, "Tweets added to the adapter: " + newTweets.size());
                Log.d(LOG_TAG, "Tweets in adapter: " + aTweets.getItemCount());
            }


            // FAILURE (Failure won't be an array)
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    Log.e(LOG_TAG, errorResponse.toString());
                }
                // If trying to load new pages by scrolling, don't do it
                if(page != 0) return;
                // Loading from ddbb
                ArrayList<Tweet> newTweets = (ArrayList<Tweet>) Tweet.getAll();
                findMaxId(newTweets);
                tweets.addAll(newTweets);
                aTweets.notifyDataSetChanged();
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

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialog composeDialog = ComposeDialog.newInstance();
        composeDialog.show(fm, "Compose your tweet");
    }

    // TODO Send back the tweet instead of refreshing, due to delay it could not be refreshed including the last tweet
    // Actions to perform when dialog is dismissed
    @Override
    public void onDismiss(DialogInterface dialog) {
        // initialize everything and get tweets again
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (Utility.isNetworkAvailable(this)) {
            maxId = 0;
            aTweets.clear();
            // Delete ddbb
            Tweet.eraseTweets();
            populateTimeline(0);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            swipe.setRefreshing(false);
        }
    }
}
