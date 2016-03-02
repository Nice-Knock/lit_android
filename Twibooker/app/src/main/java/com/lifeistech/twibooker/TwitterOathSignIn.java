package com.lifeistech.twibooker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import butterknife.Bind;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by ShingoIH49 on 2016/02/28.
 */
/*
public class TwitterOathSignIn extends Activity{

    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btn = (TextView)findViewById(R.id.btn_twitter);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                executeOauth();
            }
        });
    }

    private void executeOauth() {

        //Twitetr4Jの設定を読み込む
        Configuration conf = ConfigurationContext.getInstance();

        //Oauth認証オブジェクト作成
        _oauth = new OAuthAuthorization(conf);
        //Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
        _oauth.setOAuthConsumer("RUvf1w026yuxbF4UhOjO85ctv", "IUIDpDWRnIZG3EJbxfUMTOw7Tcoa4vrG7sykiO0ul5JGfYeeWu");
        //アプリの認証オブジェクト作成
        try {
            _req = _oauth.getOAuthRequestToken("Callback://MainActivity");
        } catch (
                TwitterException e) {
            e.printStackTrace();
        }
        String _uri;
        _uri = _req.getAuthorizationURL();
        startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(_uri)), 0);
    }
}*/

public class TwitterOathSignIn extends Activity {
    private String mCallbackURL;
    private Twitter mTwitter;
    private RequestToken mRequestToken;


    @Bind(R.id.tid)TextView twitterId;
    @Bind(R.id.ticon)SmartImageView tIcon;
    @Bind(R.id.taccount)TextView tAccount;
    @Bind(R.id.ntfollow)TextView ntFollow;
    @Bind(R.id.ntfollower)TextView ntFollower;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCallbackURL = getString(R.string.twitter_callback_url);
        mTwitter = TwitterUtils.getTwitterInstance(this);

       // findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
                startAuthorize();
        //    }
        //});
    }


    private void startAuthorize() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    mRequestToken = mTwitter.getOAuthRequestToken(mCallbackURL);
                    return mRequestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                }
            }
        };
        task.execute();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent == null
                || intent.getData() == null
                || !intent.getData().toString().startsWith(mCallbackURL)) {
            return;
        }
        String verifier = intent.getData().getQueryParameter("oauth_verifier");

        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(String... params) {
                try {
                    return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {
                if (accessToken != null) {
                    showToast("success");
                    successOAuth(accessToken);
                } else {
                    showToast("failed");
                }
            }
        };
        task.execute(verifier);
    }

    private void successOAuth(AccessToken accessToken) {
        TwitterUtils.storeAccessToken(this, accessToken);
        Intent intent = new Intent(this, TwitterTL.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}

