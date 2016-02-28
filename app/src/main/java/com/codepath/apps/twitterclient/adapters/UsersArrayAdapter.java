package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.models.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chezlui on 18/02/16.
 */
// Taking the tweet objects and turning them into a view
public class UsersArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<User> users;
    private Context mContext;

    public UsersArrayAdapter(Context context, List<User> users) {
        this.mContext = context;
        this.users = users;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);

        View tweetView = li.inflate(R.layout.item_user, parent, false);
        ViewHolderTweet convertView = new ViewHolderTweet(tweetView);

        return convertView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final User user = users.get(position);

        ((ViewHolderTweet)holder).tvName.setText(user.getName());
        ((ViewHolderTweet)holder).tvBody.setText(user.getTagLine());
        ((ViewHolderTweet)holder).ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(mContext).load(user.getProfileImageUrl()).into(
                ((ViewHolderTweet) holder).ivProfileImage);
        ((ViewHolderTweet)holder).tvUserName.setText("@" + user.getUserName());

        ((ViewHolderTweet)holder).ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("screen_name", user.getUserName());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolderTweet extends RecyclerView.ViewHolder {
        @Bind(R.id.tvUserName) TextView tvUserName;
        @Bind(R.id.tvBody) TextView tvBody;
        @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
        @Bind(R.id.tvName) TextView tvName;

        public ViewHolderTweet(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }

    public void addUser(User user) {
        users.add(user);
        notifyDataSetChanged();
    }
}
