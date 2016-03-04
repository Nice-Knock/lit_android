package com.lifeistech.android.twittertest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by ShingoIH49 on 2016/03/04.
 */
public class TweetActivity extends Activity{
    private static final String PATH = "/new_tweet";
    StatusesService statusesService;

    private EditText mInputText;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputtweet);

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        // statusAPI用のserviceクラス
        statusesService = twitterApiClient.getStatusesService();
        findViewById(R.id.tweet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tweet();
            }
        });

        initToolbar();

    }
    private void tweet(){
        mInputText = (EditText)findViewById(R.id.input_text);
        String message = mInputText.getText().toString();
        statusesService.update(message, null, null, null, null, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> tweetResult) {
                Log.i("Tweet", "Success");
                finish();
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("Tweet", "Failed");
            }
        });
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tweet);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
