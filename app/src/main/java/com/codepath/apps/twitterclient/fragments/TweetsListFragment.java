package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ComposeDialog;
import com.codepath.apps.twitterclient.activities.DetailActivity;
import com.codepath.apps.twitterclient.adapters.DividerItemDecoration;
import com.codepath.apps.twitterclient.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.adapters.ItemClickSupport;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

/**
 * Created by chezlui on 26/02/16.
 */
public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

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

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showComposeDialog();
//            }
//        });
        return v;
    }

    // logic

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
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
               // populateTimeline(page);
            }
        });


    }

    private void showComposeDialog() {
        FragmentManager fm = getChildFragmentManager();
        ComposeDialog composeDialog = ComposeDialog.newInstance();
        composeDialog.show(fm, "Compose your tweet");
    }

    public TweetsArrayAdapter getAdapter() {
        return aTweets;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }
}
