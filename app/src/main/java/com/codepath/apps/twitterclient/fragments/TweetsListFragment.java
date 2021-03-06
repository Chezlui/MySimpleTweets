package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.activities.ComposeDialog;
import com.codepath.apps.twitterclient.activities.DetailActivity;
import com.codepath.apps.twitterclient.adapters.DividerItemDecoration;
import com.codepath.apps.twitterclient.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.adapters.ItemClickSupport;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

/**
 * Created by chezlui on 26/02/16.
 */
public class TweetsListFragment extends Fragment implements TweetsArrayAdapter.onTweetInteraction {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

    String profileImageUrl;

    public static final String LOG_TAG = TweetsListFragment.class.getSimpleName();

//    @Bind(R.id.fab)
//    FloatingActionButton fab;

    @Bind(R.id.rvTweets)
    RecyclerView rvTweets;

    // Inflation
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this, v);

        getProfileImage();
        return v;
    }

    // logic


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets, this);
        rvTweets.setAdapter(aTweets);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(llm);
        rvTweets.setItemAnimator(new FlipInBottomXAnimator());

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        Tweet tweet = tweets.get(position);
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        startActivity(intent);
                    }
                }
        );

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO reactivar
               populateMore(page);
            }
        });

    }

    public void populateMore(int page) {};

    public TweetsArrayAdapter getAdapter() {
        return aTweets;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }


    @Override
    public void onRetweet(Tweet tweet) {
        TwitterClient client = TwitterApplication.getRestClient();
        client.retweet(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, "Retweeted succesfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(LOG_TAG, "Retweeted FAILED " + errorResponse.toString() );
            }
        });
    }

    @Override
    public void onUnretweet(Tweet tweet) {
        TwitterClient client = TwitterApplication.getRestClient();
        client.unretweet(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, "Unretweeted succesfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(LOG_TAG, "Unretweeted FAILED " + errorResponse.toString());
            }
        });
    }

    @Override
    public void onLike(Tweet tweet) {
        TwitterClient client = TwitterApplication.getRestClient();
        client.like(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, "Liked succesfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(LOG_TAG, "Liked FAILED " + errorResponse.toString());
            }
        });
    }

    @Override
    public void onUnlike(Tweet tweet) {
        TwitterClient client = TwitterApplication.getRestClient();
        client.unlike(tweet.getUid(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, "Unliked succesfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(LOG_TAG, "Unliked FAILED " + errorResponse.toString());
            }
        });    }

    @Override
    public void onReply(Tweet tweet) {
        showComposeDialog("@" + tweet.getUser().getUserName());
    }

    private void showComposeDialog(String prefix) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ComposeDialog composeDialog = ComposeDialog.newInstance(profileImageUrl, prefix);
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
