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
    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smproflie);
//
//        ActionBar bar = getSupportActionBar();
//        assert bar != null;
//        bar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("My Profile");

//        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
//        cid = prfs.getString("valueofcid", "");
        cid = "1";


        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();


        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
        boolean firstStart = settings1.getBoolean("firstStart", true);
        if (cid.length() != 0) {
            if (isInternetPresent) {
                MyApplication.getInstance().trackEvent("Myprofile Screen", "OnClick", "Track MyProfileDummy Event");
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
        String CONVERSATION_URL = "https://www.influencer.in/API/v6/api_v6.php/getSMConnectionDetails?cid="+cid;
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

                                String metrics = object3.getString("metrics");
                                JSONObject objectvalue = new JSONObject(metrics);

                                Iterator<String> iter = objectvalue.keys();
                                while (iter.hasNext()) {
                                    String value = null;
                                    String key = iter.next();
                                    try {
                                        //Object value = objectvalue.get(key);
                                         value = objectvalue.getString(key);
                                    } catch (JSONException e) {
                                        // Something went wrong!
                                    }
                                    Log.v("key : ",key);
                                    Log.v("value : ",value);
                                }

                                JSONObject userDetails= object3.getJSONObject("userDetails");
                                String uname= userDetails.getString("name");
                                String link= userDetails.getString("link");
                                String profile_image= userDetails.getString("profile_image");

                                Log.v("Social Media : ",socialmedia);
                                Log.v("name : ",uname);
                                Log.v("link : ",link);
                                Log.v("profile_image : ",profile_image);
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
        // super.onBackPressed();  // optional depending on your needs
        Intent intent = new Intent(this, UserSettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



}
