package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by chezlui on 21/02/16.
 */

@Table(name = "Entities")
@Parcel(analyze={Entitie.class})
public class Entitie extends Model {
    @Expose
    @Column(name = "medias")
    @SerializedName("media")
    public ArrayList<Media> medias;


    public Entitie() {
        super();
    }
}
