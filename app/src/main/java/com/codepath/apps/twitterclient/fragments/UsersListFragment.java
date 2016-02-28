package com.codepath.apps.twitterclient.fragments;

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
import com.codepath.apps.twitterclient.adapters.DividerItemDecoration;
import com.codepath.apps.twitterclient.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.adapters.UsersArrayAdapter;
import com.codepath.apps.twitterclient.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

/**
 * Created by chezlui on 26/02/16.
 */
public class UsersListFragment extends Fragment {

    private ArrayList<User> users;
    private UsersArrayAdapter aUsers;
    public ArrayList<Long> listIds;


//    @Bind(R.id.fab)
//    FloatingActionButton fab;

    @Bind(R.id.rvTweets)
    RecyclerView rvUsers;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        users = new ArrayList<>();
        aUsers = new UsersArrayAdapter(getActivity(), users);
        rvUsers.setAdapter(aUsers);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvUsers.addItemDecoration(itemDecoration);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(llm);
        rvUsers.setItemAnimator(new FlipInBottomXAnimator());

//        ItemClickSupport.addTo(rvUsers).setOnItemClickListener(
//                new ItemClickSupport.OnItemClickListener() {
//                    @Override
//                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                        Intent intent = new Intent(getActivity(), DetailActivity.class);
//                        Tweet tweet = users.get(position);
//                        intent.putExtra("tweet", Parcels.wrap(tweet));
//                        startActivity(intent);
//                    }
//                }
//        );

        rvUsers.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO reactivar
               populateMore(page);
            }
        });

    }

    public void populateMore(int page) {};

    private void showComposeDialog() {
        FragmentManager fm = getChildFragmentManager();
        ComposeDialog composeDialog = ComposeDialog.newInstance();
        composeDialog.show(fm, "Compose your tweet");
    }

    public UsersArrayAdapter getAdapter() {
        return aUsers;
    }

    public void addAll(List<User> users) {
        aUsers.addAll(users);
    }



}
