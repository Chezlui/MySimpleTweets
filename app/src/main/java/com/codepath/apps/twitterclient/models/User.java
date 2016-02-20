package com.codepath.apps.twitterclient.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chezlui on 18/02/16.
 */
public class User {
    // list attributes
    @SerializedName("name")
    private String name;
    @SerializedName("id") private long uid;
    @SerializedName("screen_name")
    private String userName;
    @SerializedName("profile_image_url")
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}
