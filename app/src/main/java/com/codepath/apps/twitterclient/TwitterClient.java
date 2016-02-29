package com.codepath.apps.twitterclient;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.io.UnsupportedEncodingException;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    private final static String LOG_TAG = TwitterClient.class.getSimpleName();

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "frJfLutG3OjHk6XQMSEkKq9fW";       // Change this
	public static final String REST_CONSUMER_SECRET = "W41HuYLmcMOSOq0B13c6pW2h9J99FAwR3GFZ2FqSMywRYlarAX"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	// Get home timeline
	public void getHomeTimeline(AsyncHttpResponseHandler handler, long offset) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Specify the params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
        if (offset != 0) {
            params.put("max_id", offset);
        }
		// Execute the request
		getClient().get(apiUrl, params, handler);
        Log.d(LOG_TAG, "Looked for data with max_id = " + offset);
	}

	// Composing a tuit
	public void sendTweet(AsyncHttpResponseHandler handler, String text) throws UnsupportedEncodingException {

		String apiUrl = getApiUrl("statuses/update.json");
		// Specify the params
		RequestParams params = new RequestParams();
		params.put("status", text);

		// Execute the request
		getClient().post(apiUrl, params, handler);
		Log.d(LOG_TAG, "Text sended: " + text);
	}

	public void getMentionsTimeLine(AsyncHttpResponseHandler handler, long offset) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Specify the params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		if (offset != 0) {
			params.put("max_id", offset);
		}
		// Execute the request
		getClient().get(apiUrl, params, handler);
		Log.d(LOG_TAG, "Looked for data with max_id = " + offset);
	}

	public void getUserTimeLine(String screenName, AsyncHttpResponseHandler handler, long offset) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screenName);
		if (offset != 0) {
			params.put("max_id", offset);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, null, handler);

	}

	public void getAnyUserInfo(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");

		if(screenName == null || screenName == "") {
			getUserInfo(handler);
			return;
		}

		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void getAnyUserInfo(long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");

		RequestParams params = new RequestParams();
		params.put("user_id", id);
		getClient().get(apiUrl, params, handler);
	}

	public void getFollowers(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/ids.json");

		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void getFriends(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/ids.json");

		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void retweet(Long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");
		getClient().post(apiUrl, null, handler);
	}

	public void unretweet(Long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/unretweet/" + id + ".json");
		getClient().post(apiUrl, null, handler);
	}

	public void unlike(Long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(apiUrl, params, handler);
	}

	public void like(Long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(apiUrl, params, handler);
	}
}