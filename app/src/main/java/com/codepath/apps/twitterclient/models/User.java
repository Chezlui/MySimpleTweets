package com.codepath.apps.twitterclient.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chezlui on 18/02/16.
 */
public class User {
    // list attributes
    private String name;
    @SerializedName("id")
    private long uid;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("profile_image_url")
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

}
