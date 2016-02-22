package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.tvUserName) TextView tvUserName;
    @Bind(R.id.tvBody) TextView tvBody;
    @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
    @Bind(R.id.tvTimeElapsed) TextView tvTimeElapsed;
    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tvRetwFavs) TextView tvRetwFavs;
    @Bind(R.id.ivDetailView) ImageView ivDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        tvUserName.setText("@" + tweet.getUser().getUserName());
        tvTimeElapsed.setText(getCreationTime(tweet.getCreatedAt()));

        String rtfavsText = String.format(getString(R.string.format_retweets_favorites),
                tweet.getRetweet_count(),
                tweet.getFavorite_count());
        tvRetwFavs.setText(rtfavsText);

        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        String url_extended =
                tweet.entities != null ?
                    tweet.entities.medias != null ?
                            tweet.entities.medias.get(0) != null ?
                                    tweet.entities.medias.get(0).media_url != null ?
                                            tweet.entities.medias.get(0).media_url : null
                                    : null
                            : null
                    : null;

        if (url_extended != null) {
            Glide.with(this).load(url_extended).into(ivDetailView);
        }
    }


    // getCreationTime("Mon Apr 01 21:16:23 +0000 2014");
    public String getCreationTime(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String creationDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();

            Date date = new Date(dateMillis);

            String detailFormat = "HH:mm aa dd MMM yy";
            SimpleDateFormat df = new SimpleDateFormat(detailFormat, Locale.ENGLISH);
            df.setLenient(true);

            creationDate = df.format(date);

//                    DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return creationDate;
    }
}
