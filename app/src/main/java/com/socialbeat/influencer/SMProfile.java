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

import com.android.volley.DefaultRetryPolicy;
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
    String cid;
    ListView list;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> pageList;
    SMLazyAdapter adapter;
    private FloatingActionButton fab;

    public static final String TAG_SOCIALMEDIA = "socialmedia";
    public static final String TAG_UNAME = "uname";
    public static final String TAG_LINK = "link";
    public static final String TAG_PIMAGE = "profile_image";
    public static final String TAG_FOLLOWERS= "followers";

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

//        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
//        cid = prfs.getString("valueofcid", "");
        cid = "1";


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

        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
        boolean firstStart = settings1.getBoolean("firstStart", true);
        if (cid.length() != 0) {
            if (isInternetPresent) {
                profileFunction();
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

    private void profileFunction() {
        pDialog = new ProgressDialog(SMProfile.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String CONVERSATION_URL = "http://stage.influencer.in/API/v6/api_v6.php/getSMConnectionDetails?cid="+cid;
        System.out.println("conversation url : "+CONVERSATION_URL);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, CONVERSATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response);
                hidePDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (response != null) {

                        String responstatus = object.getString("success");
                        Log.d("response status : ", responstatus);
                        String responsemessage = object.getString("message");
                        Log.d("response message : ", responsemessage);

                        if (responstatus == "true") {

                            String data = object.getString("data");
                            JSONObject object2 = new JSONObject(data);

                            JSONArray jArray1 = object2.getJSONArray("connected");
                            //JSONArray jArray2 = object2.getJSONArray("notConnected");

                            for(int i = 0; i < jArray1 .length(); i++)
                            {
                                JSONObject object3 = jArray1.getJSONObject(i);

                                String socialmedia = object3.getString("socialmedia");


                                JSONObject metrics= object3.getJSONObject("metrics");
                                String followers= metrics.getString("Followers");
//                                if(metrics.getString("Followers")!= null && !metrics.getString("Followers").isEmpty()) {
//                                    String followers= metrics.getString("Followers");
//                                    Log.v("followers : ",followers);
//                                }
//                                Log.v("val : : ",metrics.getString("Likes"));
//                                if(metrics.getString("Likes")!= null && !metrics.getString("Likes").isEmpty()) {
//                                    String likes= metrics.getString("Likes");
//                                    Log.v("likes : ",likes);
//                                }

                                JSONObject userDetails= object3.getJSONObject("userDetails");
                                String uname= userDetails.getString("name");
                                String link= userDetails.getString("link");
                                String profile_image= userDetails.getString("profile_image");
//                                String page_id= userDetails.getString("page_id");
//                                String access_token= userDetails.getString("access_token");

                                Log.v("Social Media : ",socialmedia);


                                Log.v("name : ",uname);
                                Log.v("link : ",link);
                                Log.v("profile_image : ",profile_image);
//                                Log.v("page_id : ",page_id);
//                                Log.v("access_token : ",access_token);
                                Log.v("followers : ",followers);

                                // tmp hashmap for single contact
                                HashMap<String, String> page = new HashMap<String, String>();
                                // adding each child node to HashMap key => value
                                page.put(TAG_SOCIALMEDIA,socialmedia);
                                page.put(TAG_UNAME,uname);
                                page.put(TAG_LINK,link);
                                page.put(TAG_PIMAGE,profile_image);
                                page.put(TAG_FOLLOWERS,followers);


                                // adding contact to contact list
                                pageList.add(page);
                            }

                            JSONArray jArray2 = object2.getJSONArray("notConnected");
                            for(int i = 0; i < jArray2 .length(); i++)
                            {
                                JSONObject object4 = jArray2.getJSONObject(i);

                                String socialmedia = object4.getString("socialmedia");
                                Log.v("New Social Media : ",socialmedia);
                            }

                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Click Here", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(SMProfile.this, SocialMediaReport.class);
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

                    }else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }
                    adapter = new SMLazyAdapter(SMProfile.this, pageList);
                    list.setAdapter(adapter);

//                    // Click event for single list row
//                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Toast.makeText(getApplicationContext(), "ID : " + id, Toast.LENGTH_LONG).show();
//                        }
//
//                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error

                    }
                }
        );

        MyApplication.getInstance().addToRequestQueue(stringRequest);
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
