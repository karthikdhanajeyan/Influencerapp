package com.socialbeat.influencer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class SMProfile extends AppCompatActivity {

    private static final String PREFS_NAME = "SMProfile";
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = SMProfile.class.getSimpleName();
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid,token;
    ListView list;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> pageList;
    SMLazyAdapter adapter;
    private FloatingActionButton fab;
    String key1_text = null,key1 = null,key2_text = null,key2 = null,key3_text = null,key3 = null;
    String value = null;
    String key = null;

    public static final String TAG_SOCIALMEDIA = "socialmedia";
    public static final String TAG_UNAME = "uname";
    public static final String TAG_LINK = "link";
    public static final String TAG_PIMAGE = "profile_image";
    public static final String TAG_FOLLOWERS= "followers";
    public static final String TAG_KEY1= "key1_lab";
    public static final String TAG_VALUE1= "key1_val";
    public static final String TAG_KEY2= "key2_lab";
    public static final String TAG_VALUE2= "key2_val";
    public static final String TAG_KEY3= "key3_lab";
    public static final String TAG_VALUE3= "key3_val";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smproflie);
        pageList = new ArrayList<HashMap<String, String>>();

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Connected Socialmedia");

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        fab = findViewById(R.id.fab);
        list = findViewById(R.id.smlist);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMProfile.this, SocialMediaAuthentication.class);
                startActivity(intent);
            }
        });

//        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
//        boolean firstStart = settings1.getBoolean("firstStart", true);
        if (cid.length() != 0) {
            if (isInternetPresent) {
                smConnectAccount();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });
                // Changing message text color
                snackbar.setActionTextColor(Color.RED);
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void smConnectAccount() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(SMProfile.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String smConnectlist = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.smconnection_details_url);
            StringRequest smConnectedList = new StringRequest(Request.Method.POST,smConnectlist , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);
                        pageList.clear();
                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                            token = responseObj.getString("token");
                            Log.v("Token value :", token);

                            SharedPreferences preferences = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                        }

                        if (responstatus.equalsIgnoreCase("true")) {
                            String data = responseObj.getString("data");
                            JSONObject object2 = new JSONObject(data);
                            JSONArray jArray1 = object2.getJSONArray("connected");
                            //JSONArray jArray2 = object2.getJSONArray("notConnected");

                            for(int i = 0; i < jArray1 .length(); i++)
                            {
                                JSONObject object3 = jArray1.getJSONObject(i);
                                String socialmedia = object3.getString("socialmedia");

                                JSONObject objectvalue= object3.getJSONObject("metrics");
                                Iterator<String> iter = objectvalue.keys();
                                while (iter.hasNext()) {
                                     value = null;
                                     key = iter.next();
                                    try {
                                        value = objectvalue.getString(key);
                                    } catch (JSONException e) {
                                        // Something went wrong!
                                    }
                                    Log.v("key : ",key);
                                    Log.v("value : ",value);
                                    if(key.equalsIgnoreCase("Followers")){
                                      key1_text = key;
                                      key1 = value;
                                    }
                                    if(key.equalsIgnoreCase("Likes")){
                                        key2_text = key;
                                        key2 = value;
                                    }
                                    if(key.equalsIgnoreCase(" Subcribers")){
                                        key3_text = key;
                                        key3 = value;
                                    }
                                }


                                JSONObject userDetails= object3.getJSONObject("userDetails");
                                String uname= userDetails.getString("name");
                                String link= userDetails.getString("link");
                                String profile_image= userDetails.getString("profile_image");

                                Log.v("Social Media : ",socialmedia);
                                Log.v("name : ",uname);
                                Log.v("link : ",link);
                                Log.v("profile_image : ",profile_image);
                                Log.v("key : ",key);
                                Log.v("value : ",value);

                                // tmp hashmap for single contact
                                HashMap<String, String> page = new HashMap<String, String>();
                                // adding each child node to HashMap key => value
                                page.put(TAG_SOCIALMEDIA,socialmedia);
                                page.put(TAG_UNAME,uname);
                                page.put(TAG_LINK,link);
                                page.put(TAG_PIMAGE,profile_image);
                                if(key1_text!=null && key1!=null){
                                    page.put(TAG_KEY1,key1_text);
                                    page.put(TAG_VALUE1,key1);
                                }
                                if(key2_text!=null && key2!=null){
                                    page.put(TAG_KEY2,key2_text);
                                    page.put(TAG_VALUE2,key2);
                                }
                                if(key3_text!=null && key3!=null){
                                    page.put(TAG_KEY3,key3_text);
                                    page.put(TAG_VALUE3,key3);
                                }
//                                page.put(TAG_KEY,key);
//                                page.put(TAG_VALUE,value);

                                pageList.add(page);
                            }

                            JSONArray jArray2 = object2.getJSONArray("notConnected");
                            for(int i = 0; i < jArray2 .length(); i++)
                            {
                                JSONObject object4 = jArray2.getJSONObject(i);
                                String socialmedia = object4.getString("socialmedia");
                                Log.v("New Social Media : ",socialmedia);
                            }

                        }else if (responstatus.equalsIgnoreCase("false")){
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Live Campaigns", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(SMProfile.this, Influencer_UserSettings.class);
                                            startActivity(intent);
                                        }
                                    });
                            // Changing message text color
                            snackbar.setActionTextColor(Color.YELLOW);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        }
                        adapter = new SMLazyAdapter(SMProfile.this, pageList);
                        list.setAdapter(adapter);
                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Socialmedia Connected.", Snackbar.LENGTH_INDEFINITE).setAction("Connect", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(SMProfile.this, SocialMediaAuthentication.class);
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
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                        // HTTP Status Code: 401 Unauthorized
                        Log.e(TAG, "Failure Error: " + " HTTP Status Code: 401 Unauthorized");
                        hidePDialog();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SMProfile.this, Influencer_Login.class);
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
                    // Basic Authentication
                    //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cid", cid);
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            smConnectedList.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(smConnectedList);

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


}
