package com.lifeistech.twibooker;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.navdrawer.SimpleSideDrawer;

import java.util.List;

import butterknife.OnClick;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by ShingoIH49 on 2016/03/01.
 */
public class TwitterTL extends ListActivity {
/*
    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String PREF_NAME = "twitter_access_token";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        initTwitter();

        //Twitter tw = getTwitterInstance(this);
        Twitter tw = TwitterFactory.getSingleton();
        try {
            //TLの取得
            //ResponseList<Status> homeTl = tw.getHomeTimeline();
            List<Status> homeTl = tw.getHomeTimeline();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);

            for (Status status : homeTl) {
                //つぶやきのユーザーIDの取得
                String userName = status.getUser().getScreenName();
                //つぶやきの取得
                String tweet = status.getText();
                adapter.add("ユーザーID：" + userName + "\r\n" + "tweet：" + tweet);
            }

            //最後のつぶやきを取得
            Status s = homeTl.get(homeTl.size());
            //Pagingオブジェクトの作成
            Paging p = new Paging();
            p.setMaxId(s.getId());

            homeTl = null;
            //Pagingオブジェクトで取得済みのつぶやき以降のつぶやきを取得
            homeTl = tw.getHomeTimeline(p);

            for (Status status : homeTl) {
                //つぶやきのユーザーIDの取得
                String userName = status.getUser().getScreenName();
                //つぶやきの取得
                String tweet = status.getText();
                adapter.add("ユーザーID：" + userName + "\r\n" + "tweet：" + tweet);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
            if(e.isCausedByNetworkIssue()){
                Toast.makeText(getApplicationContext(), "ネットワークに接続して下さい", Toast.LENGTH_LONG);
            }else{
                Toast.makeText(getApplicationContext(), "エラーが発生しました。", Toast.LENGTH_LONG);
            }
        }
    }

    private void initTwitter(){
        twitter4j.auth.AccessToken token = null;

        //Twitterの認証画面から発行されるIntentからUriを取得
        Uri uri = getIntent().getData();

        if(uri != null && uri.toString().startsWith("Callback://CallBackActivity")){
            //oauth_verifierを取得する
            String verifier = uri.getQueryParameter("oauth_verifier");
            try {
                //AccessTokenオブジェクトを取得
                token = TwitterOathSignIn._oauth.getOAuthAccessToken(TwitterOathSignIn._req, verifier);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }

    }


    public static Twitter getTwitterInstance(Context context) {
        String consumerKey = context.getString(R.string.twitter_consumer_key);
        String consumerSecret = context.getString(R.string.twitter_consumer_secret);

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        if (hasAccessToken(context)) {
            twitter.setOAuthAccessToken(loadAccessToken(context));
        }
        return twitter;
    }
    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }


    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null;
    }
*/



    private TweetAdapter mAdapter;
    private Twitter mTwitter;
    private SimpleSideDrawer mSlidingMenu;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSlidingMenu = new SimpleSideDrawer(this);
        mSlidingMenu.setLeftBehindContentView(R.layout.sidemenu_twitter);

        mGestureDetector = new GestureDetector(this,mOnGestureListener);

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOathSignIn.class);
            startActivity(intent);
            finish();
        } else {
            mAdapter = new TweetAdapter(this);
            setListAdapter(mAdapter);

            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private final GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velx,float vely){
            float dx = Math.abs(velx);
            float dy = Math.abs(vely);
            if(dx > dy && dx > 300){
                if (e1.getX() < e2.getX()){
                    mSlidingMenu.toggleLeftDrawer();
                }
            }
            return false;
        }
    };


    @OnClick(R.id.btn_twitter) void onClickTweetButton(){
        Intent intent = new Intent(this, TweetActivity.class);
        startActivity(intent);
        reloadTimeLine();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reload:
                reloadTimeLine();
                return true;
            case R.id.menu_tweet:
                Intent intent = new Intent(this, TweetActivity.class);
                startActivity(intent);
                reloadTimeLine();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TweetAdapter extends ArrayAdapter<Status> {

        private LayoutInflater mInflater;

        public TweetAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_twitter, null);
            }
            Status item = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(item.getUser().getName());
            TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
            screenName.setText("@" + item.getUser().getScreenName());
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(item.getText());
            SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
            icon.setImageUrl(item.getUser().getProfileImageURL());
            return convertView;
        }
    }

    private void reloadTimeLine() {
        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getHomeTimeline();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    mAdapter.clear();
                    for (twitter4j.Status status : result) {
                        mAdapter.add(status);
                    }
                    getListView().setSelection(0);
                } else {
                    showToast("GetTimeline_Failed");
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}