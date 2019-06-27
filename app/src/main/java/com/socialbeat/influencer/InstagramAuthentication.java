package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InstagramAuthentication extends AppCompatActivity {
    String cid,url,link,name,email,accessToken,id,followers_count,follows_count,media_count,profile_picture_url,biography;

    private LoginButton loginButton;
    ListView list;
    private CallbackManager callbackManager;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> pageList;
    FBLazyAdapter adapter;
    TextView iname,iid,ifollowers,ifollowing,imedia,ibiography;
    String t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(InstagramAuthentication.this);//Is now depricated

        setContentView(R.layout.instagramauthentication);
        callbackManager = CallbackManager.Factory.create();
        pageList = new ArrayList<HashMap<String, String>>();

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Instagram Authentication ");

        //getting customer cid value in shared preference
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        loginButton = findViewById(R.id.login_button);

        iname = findViewById(R.id.names);
        iid = findViewById(R.id.ids);
        ifollowers = findViewById(R.id.followers);
        ifollowing = findViewById(R.id.following);
        imedia = findViewById(R.id.media);
        ibiography = findViewById(R.id.bio);

        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_location"));
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        //loginButton.setReadPermissions("accounts");

        list = findViewById(R.id.pagevalues);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newGraphPathRequest(loginResult.getAccessToken(), "/17841402979900713",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                // Insert your code here
                                accessToken = AccessToken.getCurrentAccessToken().getToken();

                                try {
                                    Log.d("Facebook", "Graph Response: " + response);

                                    name = response.getJSONObject().getString("name");
                                    id = response.getJSONObject().getString("id");
                                    followers_count = response.getJSONObject().getString("followers_count");
                                    follows_count = response.getJSONObject().getString("follows_count");
                                    media_count = response.getJSONObject().getString("media_count");

                                    profile_picture_url = response.getJSONObject().getString("profile_picture_url");
                                    biography = response.getJSONObject().getString("biography");

                                    Log.v("name :",name);
                                    Log.v("id :",id);
                                    Log.v("followers_count :",followers_count);
                                    Log.v("follows_count :",follows_count);
                                    Log.v("media_count :",media_count);
                                    Log.v("profile_picture_url :",profile_picture_url);
                                    Log.v("biography :",biography);
                                    Log.v("accessToken :",accessToken);

                                    iname.setText(name);
                                    iid.setText(id);
                                    ifollowers.setText(followers_count);
                                    ifollowing.setText(follows_count);
                                    imedia.setText(media_count);
                                    ibiography.setText(biography);


//                                    adapter = new FBLazyAdapter(InstagramAuthentication.this, pageList);
//                                    list.setAdapter(adapter);
//
//                                    // Click event for single list row
//                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            Toast.makeText(getApplicationContext(), "ID : " + id, Toast.LENGTH_LONG).show();
//                                        }
//
//                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,id,followers_count,follows_count,media_count,profile_picture_url,biography");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}