package com.codepath.apps.twitterclient.models;

/**
 * Created by chezlui on 18/02/16.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Parse the JSON + Store the data, encapsulate state logic or display logic
@Table(name = "Tweets")
@Parcel(analyze={Tweet.class})
public class Tweet extends Model implements Serializable {
    // Atrributes
    @Expose
    @Column (name = "body")
    @SerializedName("text")
    public String body;
    @Expose
    @Column (name = "user")
    @SerializedName("user")
    public User user;
    @Expose
    @Column (name = "created_at")
    @SerializedName("created_at")
    public String createdAt;
    @Expose
    @Column (name = "id")
    @SerializedName("id")
    public long uid;

    @Expose
    @Column (name = "entities")
    @SerializedName("entities")
    public Entitie entities;

    @Expose
    @Column (name = "retweet_count")
    @SerializedName("retweet_count")
    public int retweet_count;

    @Expose
    @Column (name = "favorite_count")
    @SerializedName("favorite_count")
    public int favorite_count;

    @Expose
    @Column (name = "favorited")
    @SerializedName("favorited")
    public boolean liked;

    @Expose
    @Column (name = "retweeted")
    @SerializedName("retweeted")
    public boolean retweeted;

    public Tweet() {
        super();
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public static List<Tweet> getAll() {
        // This is how you execute a query
        return new Select()
                .from(Tweet.class)
                .orderBy("uid DESC")
                .execute();
    }


    public static void persistTweets(final ArrayList<Tweet> newTweets) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (Tweet tweet : newTweets) {
                    User.persist(tweet.getUser());
                    tweet.save();
                }
            }
        });
        Log.d("Tweets", "Persisted " + newTweets.size() + " items");
    }

    public static void eraseTweets() {
        new Delete().from(Tweet.class).execute();
        new Delete().from(User.class).execute();
    }
}
