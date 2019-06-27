package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;

public class TwitterAuthentication extends AppCompatActivity {

    private static final String TAG = TwitterAuthentication.class.getSimpleName();
    private ImageView userProfileImageView;
    private TextView userDetailsLabel;
    private TwitterAuthClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitterauthentication);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Twitter Authentication ");

        TwitterConfig config = new TwitterConfig.Builder(TwitterAuthentication.this.getApplicationContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("PZ5IoWlTiDQ7iItZWyTFSczuB", "fbjKSoudqYUJ2ZG2ENYq6fR94YtxTPAt2gzfdTB48Eagj4hARs"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        client = new TwitterAuthClient();
        userProfileImageView = findViewById(R.id.user_profile_image_view);
        userDetailsLabel = findViewById(R.id.user_details_label);
    }

    public void customLoginTwitter(View view) {
        if (getTwitterSession() == null) {
            client.authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession twitterSession = result.data;
                    fetchTwitterEmail(twitterSession);
                }

                @Override
                public void failure(TwitterException e) {
                    Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
        }
    }

    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(Result<String> result) {
                userDetailsLabel.setText("User Id : " + twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName() + "\nEmail Id : " + result.data + "\nToken : " + twitterSession.getAuthToken());
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchTwitterImage(View view) {

        if (getTwitterSession() != null) {
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
            call.enqueue(new Callback<User>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void success(Result<User> result) {
                    User user = result.data;
                    userDetailsLabel.setText("User Id : " + user.id + "\nUser Name : " + user.name + "\nTwitter Handle : " + user.screenName
                            + "\nFollowers Count : " + user.followersCount);
                    String imageProfileUrl = user.profileImageUrlHttps;
                    Log.e(TAG, "Data : " + imageProfileUrl);
                    imageProfileUrl = imageProfileUrl.replace("_normal", "");
                    ///load image using Picasso
                    Picasso.with(TwitterAuthentication.this)
                            .load(imageProfileUrl)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .into(userProfileImageView);
                }
                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is not authenticated first ask user to do authentication
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        return session;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (client != null)
        client.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}