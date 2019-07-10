package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class TwitterAuthentication extends AppCompatActivity {

    private static final String TAG = TwitterAuthentication.class.getSimpleName();
    private ImageView userProfileImage;
    private TextView userName,followers;
    private TwitterAuthClient client;
    Button select,selected;
    CardView cardview;
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
   ProgressDialog pDialog;
    String twitter_token,twitter_username,twitter_picture,twitter_followers,twitter_link,cid,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitterauthentication);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Twitter Authentication ");

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");

        TwitterConfig config = new TwitterConfig.Builder(TwitterAuthentication.this.getApplicationContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("b5itKAWxS0JN5TU0wDuDnUJlb", "uywxGGeLAmolo4VfQw5YTczzzE9r5TZhn11HSDJeaDW1ydU8pf"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        client = new TwitterAuthClient();
        userProfileImage = findViewById(R.id.user_icon);
        userName = findViewById(R.id.uname);
        followers = findViewById(R.id.followers);
        select = findViewById(R.id.select);
        selected = findViewById(R.id.selected);
        cardview = findViewById(R.id.cardview);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setVisibility(View.INVISIBLE);
                selected.setVisibility(View.VISIBLE);

                uploadDetails();
            }
        });
    }

    private void uploadDetails() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(TwitterAuthentication.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            //String UPLOAD_URL = "https://www.influencer.in/API/v6/api_v6.php/updateUserDetails";
            String UPLOAD_URL = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.update_twitter_url);
            StringRequest uploadurl = new StringRequest(Request.Method.POST,UPLOAD_URL , new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();
                    try {
                        JSONObject responseObj = new JSONObject(response);

                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if(responstatus.equalsIgnoreCase("true")){
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(TwitterAuthentication.this, SMProfile.class);
                                    startActivity(intent);
                                    //onBackPressed();
                                }
                            });
                            snackbar.setActionTextColor(Color.RED);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Not Saved", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(TwitterAuthentication.this, SocialMediaAuthentication.class);
                                startActivity(intent);
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.getMessage());
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                        // HTTP Status Code: 401 Unauthorized
                        Log.e(TAG, "Failure Error: " + " HTTP Status Code: 401 Unauthorized");
                        hidePDialog();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(TwitterAuthentication.this, Influencer_Login.class);
                                startActivity(intent);
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    Log.v("Token Value : ",token);
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("twitter_username", twitter_username);
                    params.put("twitter_picture", twitter_picture);
                    params.put("twitter_followers", twitter_followers);
                    params.put("twitter_link", twitter_link);
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            uploadurl.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(uploadurl);

        } else {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                @Override
                public void onClick(View view) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
            });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    public void customLoginTwitter(View view) {
        if (getTwitterSession() == null) {
            client.authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    TwitterSession twitterSession = result.data;
                    fetchTwitterEmail(twitterSession);

//                    if (twitterSession != null) {
//                        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//                        Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
//                        call.enqueue(new Callback<User>() {
//                            @SuppressLint("SetTextI18n")
//                            @Override
//                            public void success(Result<User> result) {
//                                User user = result.data;
////                    userDetailsLabel.setText("User Id : " + user.id + "\nUser Name : " + user.name + "\nTwitter Handle : " + user.screenName
////                            + "\nFollowers Count : " + user.followersCount);
//                                //userDetailsLabel.setText("User Name : " + user.name + "\nTwitter Handle : " + user.screenName + "\nFollowers Count : " + user.followersCount);
//                                userName.setText(user.screenName);
//                                followers.setText(user.followersCount);
//                                String imageProfileUrl = user.profileImageUrlHttps;
//                                Log.e(TAG, "Data : " + imageProfileUrl);
//                                imageProfileUrl = imageProfileUrl.replace("_normal", "");
//                                ///load image using Picasso
//                                Picasso.with(TwitterAuthentication.this)
//                                        .load(imageProfileUrl)
//                                        .placeholder(R.mipmap.ic_launcher_round)
//                                        .into(userProfileImage);
//                            }
//                            @Override
//                            public void failure(TwitterException exception) {
//                                Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else {
//                        //if user is not authenticated first ask user to do authentication
//                        Toast.makeText(TwitterAuthentication.this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
//                    }

                }

                @Override
                public void failure(TwitterException e) {
                    Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            //fetchTwitterEmail(getTwitterSession());
            if (getTwitterSession() != null) {
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
                call.enqueue(new Callback<User>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void success(Result<User> result) {
                        User user = result.data;
                        //userDetailsLabel.setText("User Name : " + user.name + "\nTwitter Handle : " + user.screenName + "\nFollowers Count : " + user.followersCount);
                        cardview.setVisibility(View.VISIBLE);



                        twitter_username = user.screenName;
                        twitter_picture = user.profileImageUrlHttps;
                        twitter_followers = String.valueOf(user.followersCount);
                        twitter_link = "https://twitter.com/"+twitter_username;

                        Log.v("twitter_username",twitter_username);
                        Log.v("twitter_picture",twitter_picture);
                        Log.v("twitter_followers",twitter_followers);
                        Log.v("twitter_link",twitter_link);

                        userName.setText(twitter_username);
                        followers.setText(twitter_followers);

                        twitter_picture = twitter_picture.replace("_normal", "");
                        ///load image using Picasso
                        Picasso.with(TwitterAuthentication.this)
                                .load(twitter_picture)
                                .placeholder(R.mipmap.ic_launcher_round)
                                .into(userProfileImage);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(Result<String> result) {
                //userDetailsLabel.setText("User Id : " + twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName() + "\nEmail Id : " + result.data + "\nToken : " + twitterSession.getAuthToken());
                userName.setText(twitterSession.getUserName());

                twitter_token = String.valueOf(twitterSession.getAuthToken());
                Log.v("twitter_token",twitter_token);
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public void uploadTwitterDetails() {
//
//    }

//    public void fetchTwitterImage(View view) {
//
//        if (getTwitterSession() != null) {
//            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
//            call.enqueue(new Callback<User>() {
//                @SuppressLint("SetTextI18n")
//                @Override
//                public void success(Result<User> result) {
//                    User user = result.data;
////                    userDetailsLabel.setText("User Id : " + user.id + "\nUser Name : " + user.name + "\nTwitter Handle : " + user.screenName
////                            + "\nFollowers Count : " + user.followersCount);
//                    //userDetailsLabel.setText("User Name : " + user.name + "\nTwitter Handle : " + user.screenName + "\nFollowers Count : " + user.followersCount);
//                    userDetailsLabel.setText(user.screenName);
//                    String imageProfileUrl = user.profileImageUrlHttps;
//                    Log.e(TAG, "Data : " + imageProfileUrl);
//                    imageProfileUrl = imageProfileUrl.replace("_normal", "");
//                    ///load image using Picasso
//                    Picasso.with(TwitterAuthentication.this)
//                            .load(imageProfileUrl)
//                            .placeholder(R.mipmap.ic_launcher_round)
//                            .into(userProfileImageView);
//                }
//                @Override
//                public void failure(TwitterException exception) {
//                    Toast.makeText(TwitterAuthentication.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            //if user is not authenticated first ask user to do authentication
//            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
//        }
//    }

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
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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