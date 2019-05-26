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
    String cid,pageid,pagename,pagefancount,pagetoken,usertoken,userid,url;

    private LoginButton loginButton;
    ListView list;
    private CallbackManager callbackManager;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> pageList;
    public static final String TAG_PID = "id";
    public static final String TAG_PNAME = "name";
    public static final String TAG_PFANCOUNT = "fan_count";
    public static final String TAG_PTOKEN = "access_token";
    public static final String TAG_URL = "url";
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
        // Listview on item click listener
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // getting values from selected ListItem
//                String pgname = ((TextView) view.findViewById(R.id.pgname)).getText().toString();
//                String pgid = ((TextView) view.findViewById(R.id.pgid)).getText().toString();
//                String pgabout = ((TextView) view.findViewById(R.id.pgabout)).getText().toString();
//                String pgfancount = ((TextView) view.findViewById(R.id.pgfancount)).getText().toString();
//                String pgtoken = ((TextView) view.findViewById(R.id.pgtoken)).getText().toString();
//                //String url_value = ((TextView) view.findViewById(R.id.pgtoken)).getText().toString();
//                Toast.makeText(FacebookAuthentication.this,pgname+"---"+pgtoken, Toast.LENGTH_LONG).show();
//            }
//        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Toast.makeText(FacebookAuthentication.this,"HAi..!", Toast.LENGTH_LONG).show();
            }
        });




        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("SocialMedia ", response.toString());
                                String accessToken = AccessToken.getCurrentAccessToken().getToken();
                                //session.getAccessToken().getToken()
                                System.out.println("Access Token :"+accessToken);
                                try {
                                    Log.d("Facebook", "Graph Response: " + response);

                                    if (object.has("accounts")) {
                                        JSONObject accounts = object.getJSONObject("accounts");
                                        JSONArray data = accounts.getJSONArray("data");
                                        for (int i=0;i<data.length();i++){
                                            JSONObject legObject = (JSONObject) data.get(i);
                                            pageid = legObject.getString("id");
                                            pagename = legObject.getString("name");
                                            pagefancount = legObject.getString("fan_count");
                                            pagetoken = legObject.getString("access_token");

                                            JSONObject picturevalue = legObject.getJSONObject("picture");
                                            JSONObject datavalue = picturevalue.getJSONObject("data");
                                            Log.v("data : ",datavalue.toString());
                                            //JSONObject urlvalue = datavalue.getJSONObject("url");
                                            url = datavalue.getString("url");


                                            Log.v("value of page Id : ",data.getJSONObject(i).getString("id"));
                                            Log.v("value of Name : ",data.getJSONObject(i).getString("name"));
                                            Log.v("value of Fan Count : ",data.getJSONObject(i).getString("fan_count"));
                                            Log.v("value of Token : ",data.getJSONObject(i).getString("access_token"));
                                            Log.v("value of url : ",url);

                                            // tmp hashmap for single contact
                                            HashMap<String, String> page = new HashMap<String, String>();
                                            // adding each child node to HashMap key => value
                                            page.put(TAG_PID,pageid);
                                            page.put(TAG_PNAME,pagename);
                                            page.put(TAG_PFANCOUNT,pagefancount);
                                            page.put(TAG_URL,url);
                                            page.put(TAG_PTOKEN,pagetoken);

                                            // adding contact to contact list
                                            pageList.add(page);
                                        }
                                    }

//                                    //Image
//                                    Glide.with(getApplicationContext()).load(image_url)
//                                            .thumbnail(0.5f)
//                                            .crossFade()
//                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                            .into(fb_profileimage);

//                                    ListAdapter adapter = new SimpleAdapter(FacebookAuthentication.this, pageList,
//                                            R.layout.fbpagelist, new String[]{TAG_PID,TAG_PNAME,TAG_PFANCOUNT,TAG_PTOKEN},
//                                            new int[]{R.id.pgid,R.id.pgname,R.id.pgfancount,R.id.pgtoken});
//                                    list.setAdapter(adapter);


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
                parameters.putString("fields", "id,name,accounts{name,app_id,fan_count,access_token,about,picture{url}}");
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
        Intent intent  = new Intent(this, UserSettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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