package com.codepath.apps.twitterclient.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chezlui on 20/02/16.
 */
public class ComposeDialog extends DialogFragment {

    public static final String LOG_TAG = ComposeDialog.class.getSimpleName();
    public static final int MAX_CHARACTERS = 140;
    @Bind(R.id.etTweetText)
    EditText etTweetText;
    @Bind(R.id.ivCloseCompose)
    ImageView ivCloseCompose;
    @Bind(R.id.tvCharactersLeft)
    TextView tvCharactersLeft;
    @Bind(R.id.btTweetNow)
    Button btTweetNow;

    String prefix;

    public ComposeDialog() {
    }

    public static ComposeDialog newInstance() {
        ComposeDialog composeDialog = new ComposeDialog();
        return composeDialog;
    }

    public static ComposeDialog newInstance(String prefix) {
        ComposeDialog composeDialog = new ComposeDialog();
        composeDialog.prefix = prefix;
        return composeDialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_compose, container);
        ButterKnife.bind(this, view);
        btTweetNow.setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.title_compose_dialog);
        setListeners();
        if (prefix != null) {
            etTweetText.setText(prefix);
            etTweetText.setSelection(etTweetText.length() + 1);
        }
    }


    private void setListeners() {

        ivCloseCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        etTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharactersLeft.setText("" + (MAX_CHARACTERS - s.length()));
                if (s.length() > MAX_CHARACTERS) {
                    tvCharactersLeft.setTextColor(getResources().getColor(R.color.red));
                    btTweetNow.setEnabled(false);
                } else {
                    tvCharactersLeft.setTextColor(getResources().getColor(R.color.gray_text));
                    btTweetNow.setEnabled(true);
                }

                if(s.length() == 0) {
                    btTweetNow.setEnabled(false);
                }

                Log.d(LOG_TAG, "Length: " + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btTweetNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tweet();
            }
        });

    }

    private void Tweet() {
        TwitterClient client = TwitterApplication.getRestClient();
        try {
            client.sendTweet(new AsyncHttpResponseHandler() {
                                 @Override
                                 public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                     Toast.makeText(getContext(), "Tweet sended!", Toast.LENGTH_SHORT).show();
                                     dismiss();
                                 }

                                 @Override
                                 public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                     Toast.makeText(getContext(), "Tweet couldn't be sended!", Toast.LENGTH_SHORT).show();
                                     Log.e(LOG_TAG, responseBody.toString());
                                 }
                             },

                    etTweetText.getText().toString());
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getContext(), "Characters not supported", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}