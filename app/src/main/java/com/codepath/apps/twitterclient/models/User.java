package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by chezlui on 18/02/16.
 */
@Table(name = "Users")
@Parcel(analyze={User.class})
public class User extends Model implements Serializable{
    // list attributes
    @Expose
    @Column (name = "name")
    @SerializedName("name")
    public String name;
    @Expose
    @Column (name = "uid")
    @SerializedName("id")
    public long uid;
    @Expose
    @Column (name = "screen_name")
    @SerializedName("screen_name")
    public String userName;
    @Expose
    @Column (name = "profileImage")
    @SerializedName("profile_image_url")
    public String profileImageUrl;

    public User() {
        super();
    }

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

    public static void persist(User user) {
        user.save();
    }
}
