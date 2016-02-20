package com.codepath.apps.twitterclient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chezlui on 18/02/16.
 */
// Taking the tweet objects and turning them into a view
public class TweetsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Tweet> tweets;
    private Context mContext;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        this.mContext = context;
        this.tweets = tweets;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);

        View tweetView = li.inflate(R.layout.item_tweet, parent, false);
        ViewHolderTweet convertView = new ViewHolderTweet(tweetView);

        return convertView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        ((ViewHolderTweet)holder).tvUserName.setText(tweet.getUser().getScreenName());
        ((ViewHolderTweet)holder).tvBody.setText(tweet.getBody());
        ((ViewHolderTweet)holder).ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(mContext).load(tweet.getUser().getProfileImageUrl()).into(
                ((ViewHolderTweet)holder).ivProfileImage);


    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class ViewHolderTweet extends RecyclerView.ViewHolder {
        @Bind(R.id.tvUserName) TextView tvUserName;
        @Bind(R.id.tvBody) TextView tvBody;
        @Bind(R.id.ivProfileImage) ImageView ivProfileImage;

        public ViewHolderTweet(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
