package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

        ((ViewHolderTweet)holder).tvName.setText(tweet.getUser().getName());
        ((ViewHolderTweet)holder).tvBody.setText(tweet.getBody());
        ((ViewHolderTweet)holder).ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(mContext).load(tweet.getUser().getProfileImageUrl()).into(
                ((ViewHolderTweet) holder).ivProfileImage);
        ((ViewHolderTweet)holder).tvUserName.setText("@" + tweet.getUser().getUserName());
        ((ViewHolderTweet)holder).tvTimeElapsed.setText(getRelativeTimeAgo(tweet.getCreatedAt()));


    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class ViewHolderTweet extends RecyclerView.ViewHolder {
        @Bind(R.id.tvUserName) TextView tvUserName;
        @Bind(R.id.tvBody) TextView tvBody;
        @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
        @Bind(R.id.tvTimeElapsed) TextView tvTimeElapsed;
        @Bind(R.id.tvName) TextView tvName;

        public ViewHolderTweet(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // getCreationTime("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
}
