package com.codepath.apps.twitterclient.models;

/**
 * Created by chezlui on 18/02/16.
 */

import com.google.gson.annotations.SerializedName;

/**
        [
             {


             }
        ]

 */

// Parse the JSON + Store the data, encapsulate state logic or display logic
public class Tweet {
    // Atrributes
    @SerializedName("text")
    private String body;
    @SerializedName("user")
    private User user;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("id")
    private long uid;

    public Tweet() {

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

}
