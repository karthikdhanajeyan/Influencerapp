package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

public class FacebookAuthentication extends AppCompatActivity {
    String cid,pageid,pagename,pagefancount,pagetoken,usertoken,userid,url,link,new_like_count,rating_count,talking_about_count,name,email,imageURL,about,accessToken;

    private LoginButton loginButton;
    ListView list;
    private CallbackManager callbackManager;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> pageList;
    public static final String TAG_FID = "fb_id";
    public static final String TAG_FNAME = "fb_name";
    public static final String TAG_FEMAIL = "fb_email";
    public static final String TAG_FIMAGE = "fb_profile_picture_url";
    public static final String TAG_FUATOKEN = "fb_profile_access_token";

    public static final String TAG_PID = "fb_page_id";
    public static final String TAG_PNAME = "fb_page_name";
    public static final String TAG_PFCOUNT = "fb_page_fan_count";
    public static final String TAG_PLINK = "fb_page_link";
    public static final String TAG_FPATOKEN = "fb_page_access_token";

    public static final String TAG_PABOUT = "fb_page_about";
    public static final String TAG_PNLC = "fb_page_new_like_count";
    public static final String TAG_PRC = "fb_page_rating_count";
    public static final String TAG_PTAC = "fb_page_talking_about_count";
    public static final String TAG_FPIMAGE = "fb_page_picture";
    FBLazyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(FacebookAuthentication.this);//Is now depricated

        setContentView(R.layout.facebookauthentication);
        callbackManager = CallbackManager.Factory.create();
        pageList = new ArrayList<HashMap<String, String>>();
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Facebook Authentication ");

        //getting customer cid value in shared preference
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        loginButton = findViewById(R.id.login_button);


        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_location"));
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        //loginButton.setReadPermissions("accounts");

        list = findViewById(R.id.pagevalues);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("SocialMedia ", response.toString());
                                 accessToken = AccessToken.getCurrentAccessToken().getToken();
                                //session.getAccessToken().getToken()
                                System.out.println("Access Token :"+accessToken);

                                try {
                                    Log.d("Facebook", "Graph Response: " + response);

                                     userid = response.getJSONObject().getString("id");
                                     name = response.getJSONObject().getString("name");
                                     email = response.getJSONObject().getString("email");
                                     imageURL = "http://graph.facebook.com/"+userid+"/picture?type=large";

                                    if (object.has("accounts")) {
                                        JSONObject accounts = object.getJSONObject("accounts");
                                        JSONArray data = accounts.getJSONArray("data");
                                        for (int i=0;i<data.length();i++){
                                            JSONObject legObject = (JSONObject) data.get(i);

                                            pageid = legObject.getString("id");
                                            pagename = legObject.getString("name");
                                            pagefancount = legObject.getString("fan_count");
                                            pagetoken = legObject.getString("access_token");
                                            link = legObject.getString("link");
                                            //about = legObject.getString("about");
                                            new_like_count = legObject.getString("new_like_count");
                                            //rating_count = legObject.getString("rating_count");
                                            talking_about_count = legObject.getString("talking_about_count");
                                            JSONObject picturevalue = legObject.getJSONObject("picture");
                                            JSONObject datavalue = picturevalue.getJSONObject("data");
                                            Log.v("data : ",datavalue.toString());
                                            url = datavalue.getString("url");



                                            // tmp hashmap for single contact
                                            HashMap<String, String> page = new HashMap<String, String>();
                                            // adding each child node to HashMap key => value

                                            page.put(TAG_FID,userid);
                                            page.put(TAG_FNAME,name);
                                            page.put(TAG_FEMAIL,email);
                                            page.put(TAG_FIMAGE,imageURL);
                                            page.put(TAG_FUATOKEN,accessToken);

                                            page.put(TAG_PID,pageid);
                                            page.put(TAG_PNAME,pagename);
                                            page.put(TAG_PFCOUNT,pagefancount);
                                            page.put(TAG_PLINK,link);
                                            page.put(TAG_FPATOKEN,pagetoken);

//                                            if(about!= null && !about.isEmpty()){
//                                                page.put(TAG_PABOUT,about);
//                                            }
                                            page.put(TAG_PNLC,new_like_count);
//                                            if(rating_count!= null && !rating_count.isEmpty()){
//                                                page.put(TAG_PRC,rating_count);
//                                            }
                                            page.put(TAG_PTAC,talking_about_count);
                                            page.put(TAG_FPIMAGE,url);

                                            // adding contact to contact list
                                            pageList.add(page);
                                        }
                                    }


                                    adapter = new FBLazyAdapter(FacebookAuthentication.this, pageList);
                                    list.setAdapter(adapter);

                                    // Click event for single list row
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Toast.makeText(getApplicationContext(), "ID : " + id, Toast.LENGTH_LONG).show();
                                        }

                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture{url},accounts{name,fan_count,access_token,app_id,about,picture{url},link,new_like_count,rating_count,talking_about_count}");
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