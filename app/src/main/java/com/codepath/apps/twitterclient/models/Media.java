package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by chezlui on 21/02/16.
 */

@Table(name = "Medias")
@Parcel(analyze={Media.class})
public class Media extends Model {
    @Expose
    @Column(name = "media_url")
    @SerializedName("media_url")
    public String media_url;


    public Media() {
        super();
    }
}
