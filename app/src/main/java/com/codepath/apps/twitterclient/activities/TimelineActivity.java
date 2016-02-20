package com.codepath.apps.twitterclient.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapters.DividerItemDecoration;
import com.codepath.apps.twitterclient.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

public class TimelineActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
    private static final String LOG_TAG = TimelineActivity.class.getSimpleName();

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private long maxId = 0;
    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        rvTweets.setAdapter(aTweets);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(llm);
        rvTweets.setItemAnimator(new FlipInBottomXAnimator());

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });

        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_compose_tweet) {
            showComposeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Send an API request to get the timeline JSON
    // Fill the listview as well by creating the tweets objects from JSON
    private void populateTimeline() {
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
                Gson gson = new GsonBuilder().create();

                ArrayList<Tweet> newTweets = (ArrayList<Tweet>) gson.fromJson(json.toString(), collectionType);
                findMaxId(newTweets);
                tweets.addAll(newTweets);
                aTweets.notifyDataSetChanged();
                Log.d("DEBUG", aTweets.getItemCount() + "");
            }


            // FAILURE (Failure won't be an array)
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(LOG_TAG, errorResponse.toString());
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

    // Actions to perform when dialog is dismissed
    @Override
    public void onDismiss(DialogInterface dialog) {
        // initialize everything and get tweets again
        maxId = 0;
        tweets.clear();
        populateTimeline();
    }
}