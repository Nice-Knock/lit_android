package com.lifeistech.android.twibooker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CallbackManager  callbackManager;
    LoginButton loginButton;

    private static final String TAG = "MainActivity";
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private EditText mEditText;
   /* private class SessionStateCallback implements Session.StatusCallback{
        public void call(Session session,SessionState state,Exception exception){
            Log.d(TAG,"SessionStatusCallback");
            onSessionStateChange(session,state,exception);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());



        loginButton = (LoginButton)findViewById(R.id.login_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onResume(){
        super.onResume();
        AppEventsLogger.activateApp(this);
    }
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

}
