package com.lifeistech.android.twittertest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * https://developers.facebook.com/docs/sharing/android
 */
public class ShareActivity extends FragmentActivity {

  private static final String TAG = ShareActivity.class.getSimpleName();
  private static final List<String> PUBLISH_PERMISSIONS = Arrays.asList("publish_actions");
  private static final String SAMPLE_SHARE_URL = "http://google.com";
  private static final String SAMPLE_SHARE_TITLE = "test_title";
  private static final String SAMPLE_SHARE_DESCRIPTION = "test_description";
  private static final String SAMPLE_IMAGE_URL = "";
  private static final String MESSAGE = "message";
  private ArrayList msgList = new ArrayList();

  @Bind(R.id.txt_share_auto_result) TextView txtShareAutoResult;
  @Bind(R.id.listView)ListView feedview;

  private CallbackManager callbackManager;

  static void start(Context context) {
    Intent intent = new Intent(context, ShareActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share);
    ButterKnife.bind(this);
    Log.e(TAG, "onCreate");


    initFacebook();
    initToolbar();

    new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/feed",
            null,
            HttpMethod.GET,
            new GraphRequest.Callback() {
              public void onCompleted(GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try{
                  JSONArray jsons = json.getJSONArray("data");
                  for(int i = 0; i < jsons.length(); i++){
                    JSONObject fData = jsons.getJSONObject(i);
                    if(fData.has("message")){
                      msgList.add(fData.getString("message") + "\n");
                    }
                  }
                  ArrayAdapter adapter = new ArrayAdapter<>(
                          this,android.R.layout.simple_list_item_1,msgList);
                  feedview.setAdapter(adapter);
                }catch (JSONException e){
                  Log.e(TAG, e.getMessage());
                }
              }
            }
    ).executeAsync();
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Logs 'install' and 'app activate' App Events.
    AppEventsLogger.activateApp(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    // Logs 'app deactivate' App Event.
    AppEventsLogger.deactivateApp(this);
  }

  private void initFacebook() {
    FacebookSdk.sdkInitialize(getApplicationContext());

    callbackManager = CallbackManager.Factory.create();
    LoginManager.getInstance()
        .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
            Log.e(TAG, "onSuccess");
            shareToFacebookFeed();
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
  }

  private void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.share);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @OnClick(R.id.btn_getfeed) void onClickBtnGetfeed(){
    new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/feed",
            null,
            HttpMethod.GET,
            new GraphRequest.Callback() {
              public void onCompleted(GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try{
                  JSONArray jsons = json.getJSONArray("data");
                  for(int i = 0; i < jsons.length(); i++){
                    JSONObject fData = jsons.getJSONObject(i);
                    if(fData.has("message")){
                      msgList.add(fData.getString("message") + "\n");
                    }
                  }
                  ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,msgList);

                }catch (JSONException e){
                  Log.e(TAG, e.getMessage());
                }
              }
            }
    ).executeAsync();
  }

  @OnClick(R.id.btn_share_dialog) void onClickBtnShareDialog() {
    if (ShareDialog.canShow(ShareLinkContent.class)) {
      ShareLinkContent content =
          new ShareLinkContent.Builder()//.setContentUrl(Uri.parse(SAMPLE_SHARE_URL))
     //         .setContentTitle(SAMPLE_SHARE_TITLE)
     //         .setContentDescription(SAMPLE_SHARE_DESCRIPTION)
              .build();
      ShareDialog.show(this,content);
    }
  }
/*
  @OnClick(R.id.btn_share_auto) void onClickBtnShareAuto() {
    LoginManager manager = LoginManager.getInstance();
    manager.logInWithPublishPermissions(this, PUBLISH_PERMISSIONS);
  }

  @OnClick(R.id.btn_share_photo_auto) void onClickBtnSharePhotoAuto() {
    LoginManager manager = LoginManager.getInstance();
    manager.logInWithPublishPermissions(this, PUBLISH_PERMISSIONS);
  }
*/
  private void shareToFacebookFeed() {
    ShareLinkContent content =
        new ShareLinkContent.Builder().setContentUrl(Uri.parse(SAMPLE_SHARE_URL))
            .setContentTitle(SAMPLE_SHARE_TITLE)
            .setContentDescription(SAMPLE_SHARE_DESCRIPTION)
            .setImageUrl(Uri.parse(SAMPLE_IMAGE_URL))
            .build();
    ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
      @Override public void onSuccess(Sharer.Result result) {
        Log.e(TAG, "onSuccess");
        txtShareAutoResult.setText(R.string.share_auto_completed);
      }

      @Override public void onCancel() {
        Log.e(TAG, "onCancel");
        txtShareAutoResult.setText(R.string.share_auto_failed);
      }

      @Override public void onError(FacebookException e) {
        Log.e(TAG, "onError");
        txtShareAutoResult.setText(R.string.share_auto_failed);
      }
    });
  }

  private void sharePhotoToFacebook() {
    SharePhoto photo = new SharePhoto.Builder().setImageUrl(Uri.parse(SAMPLE_IMAGE_URL)).build();
    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo)
        .setContentUrl(Uri.parse(SAMPLE_SHARE_URL))
        .setRef(SAMPLE_SHARE_TITLE)
        .build();

    ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
      @Override public void onSuccess(Sharer.Result result) {
        Log.e(TAG, "onSuccess");
        txtShareAutoResult.setText(R.string.share_auto_completed);
      }

      @Override public void onCancel() {
        Log.e(TAG, "onCancel");
        txtShareAutoResult.setText(R.string.share_auto_failed);
      }

      @Override public void onError(FacebookException e) {
        Log.e(TAG, "onError");
        txtShareAutoResult.setText(R.string.share_auto_failed);
      }
    });
  }
}
