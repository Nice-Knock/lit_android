package com.lifeistech.android.twittertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "RUvf1w026yuxbF4UhOjO85ctv";
    private static final String TWITTER_SECRET = "IUIDpDWRnIZG3EJbxfUMTOw7Tcoa4vrG7sykiO0ul5JGfYeeWu";

    private TwitterLoginButton twitterLoginButton;
    Long userId;
    TwitterSession session;
    TextView textView;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final List<String> READ_PERMISSIONS =
            Arrays.asList("email", "user_birthday", "user_friends");

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PICTURE = "picture";
    private static final String EMAIL = "email";
    private static final String BIRTHDAY = "birthday";
    private static final String GENDER = "gender";
    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, PICTURE, EMAIL, BIRTHDAY, GENDER});
    private static final String FIELDS = "fields";
    private static final String MESSAGE = "message";

    @Bind(R.id.btn_login) TextView btn_facebook;
    @Bind(R.id.btn_twitter)TextView btn_twitter;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;


    boolean tLoginFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        Log.e(TAG, "onCreate");

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loginButton = (LoginButton)findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("user_posts");
        initFacebook();
        initToolbar();
        initTwitter();
    }
    public void initTwitter(){
        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitterLogin);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                startTwitterTL();
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
        if(tLoginFlag != false)
        twitterLoginButton.setVisibility(View.VISIBLE);
    }

    private void startTwitterTL(){
        final Intent intent = new Intent(this,TwitterTL.class);
        tLoginFlag = true;
        btn_twitter.setText("Twitter_TL");
        startActivity(intent);
    }


    void getUserData() {
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {

                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        String twitterImage = user.profileImageUrl;

                        try {
                            Log.d("imageurl", user.profileImageUrl);
                            Log.d("name", user.name);
                            //Log.d("email",user.email);
                            Log.d("des", user.description);
                            Log.d("followers ", String.valueOf(user.followersCount));
                            Log.d("createdAt", user.createdAt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });
// Can also use Twitter directly: Twitter.getApiClient()

    }

    /*
    @OnClick(R.id.btn_getfeed) void onClickBtnGetfeed(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG,response.toString());
                        JSONObject json = response.getJSONObject();
                        Log.d(TAG,json.toString());
                        try{
                            String message = json.getString(MESSAGE);
                            Log.d(TAG, String.valueOf(message));
                        }catch (JSONException e){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
        ).executeAsync();
    }*/

    private void initFacebook() {

        btn_facebook.setText("Facebook_Feed & Post");
        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance()
                .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(TAG, "onSuccess");
                        requestUserInfo(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(TAG, "onError");
                    }
                });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                // On AccessToken changes fetch the new profile which fires the event on
                // the ProfileTracker if the profile is different
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        profileTracker = new ProfileTracker() {
            @Override protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                setProfile(currentProfile);
            }
        };

        // Ensure that our profile is up to date
        Profile.fetchProfileForCurrentAccessToken();
        setProfile(Profile.getCurrentProfile());
    }

    private void requestUserInfo(AccessToken accessToken) {
        GraphRequest request =
                GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG, "response: " + response.toString());
                        JSONObject json = response.getJSONObject();
                        renderView(json);
                    }
                });
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),"/me/feed",null, HttpMethod.GET,new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                JSONObject json = response.getJSONObject();
                Log.i(TAG,json.toString());
                try{
                    String message = json.getString(MESSAGE);
                    Log.i(TAG, String.valueOf(message));
                }catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        ).executeAsync();
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, REQUEST_FIELDS);
        request.setParameters(parameters);
        request.executeAsync();
        ShareActivity.start(this);
    }

    private void renderView(JSONObject json) {
        try {
            final long id = json.getLong(ID);
            final String name = json.getString(NAME);
            final String email = json.getString(EMAIL);
            final String picture = json.getJSONObject("picture").getJSONObject("data").getString("url");
            final String birthday = json.getString(BIRTHDAY);
            final String gender = json.getString(GENDER);

            //txtId.setText(String.valueOf(id));
            //txtName.setText(name);
            //txtBirthday.setText(birthday);
            //txtGender.setText(gender);
            //txtEmail.setText(email);
            Log.e(TAG, "picture: " + picture);
            //ImageLoader.getInstance().displayImage(picture, imgProfile);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setProfile(Profile profile) {
        if (profile != null) {
            //txtId.setText(String.valueOf(profile.getId()));
            //txtName.setText(profile.getName());
            //ImageLoader.getInstance().displayImage(profile.getProfilePictureUri(100, 100).toString(), imgProfile);
        } else {
            Log.e(TAG, "profile is null.");
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @OnClick(R.id.btn_login) void onClickBtnLogin() {
        Log.e(TAG, "onClickBtnLogin");
        LoginManager manager = LoginManager.getInstance();
        manager.logInWithReadPermissions(this, READ_PERMISSIONS);
    }

    @OnClick(R.id.btn_twitter) void onClickBtnTwitterLogin() {

        //ShareActivity.start(this);
        if(tLoginFlag == false){
            tLoginFlag = true;
            btn_twitter.setText("Twitter_TL");
            Intent intent = new Intent(this,TwitterTL.class);
            startActivity(intent);
      /*
      if (!TwitterUtils.hasAccessToken(this)) {
        Intent intent = new Intent(this, TwitterOathSignIn.class);
        startActivity(intent);
      }*/
        }else{
            Intent intent = new Intent(this,TwitterTL.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_tweet) {
            Intent intent = new Intent(this, TwitterTL.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this,ShareActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
