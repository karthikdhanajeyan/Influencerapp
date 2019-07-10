package com.socialbeat.influencer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.nashapp.androidsummernote.Summernote;

public class SocialMediaReport extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    LinearLayout facebook,twitter,instagram,blogger,youtube,facebook_clone,twitter_clone,instagram_clone;
    String cid,campid,campname,Facebook,Instagram,GA,Twitter,Youtube,token;
    private static final String TAG = SocialMediaReport.class.getSimpleName();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newreport);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Socialmedia Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        //instagram = findViewById(R.id.instagram);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            campid = extras.getString("campid");
            campname = extras.getString("campname");
        }
        SharedPreferences prefernce1 = getSharedPreferences("ANALYTICS_NEW_REPORT", Context.MODE_PRIVATE);
        campid = prefernce1.getString("campid", "");
        campname = prefernce1.getString("campname", "");
        Log.v("SMReport CCampid : ", campid);
        Log.v("SMReport CCampnme: ", campname);

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");


        if(cid.length()!=0){
            if (isInternetPresent) {
                ScreenVisible();

            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }


            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Facebook.equalsIgnoreCase("true")){
                        Intent intent = new Intent(SocialMediaReport.this, FacebookPostActivity.class);
                        Bundle bund = new Bundle();
                        bund.putString("campid", campid);
                        bund.putString("campname", campname);
                        bund.putString("socialmedia", "facebook");
                        intent.putExtras(bund);
                        startActivity(intent);
                    }else if(Facebook.equalsIgnoreCase("false")) {
                        Intent intent = new Intent(SocialMediaReport.this, SMDataReportActivity.class);
                        Bundle bund = new Bundle();
                        bund.putString("campid", campid);
                        bund.putString("campname", campname);
                        bund.putString("socialmedia", "facebook");
                        intent.putExtras(bund);
                        startActivity(intent);
                    }
                }
            });

            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SocialMediaReport.this, SMDataReportActivity.class);
                    Bundle bund = new Bundle();
                    bund.putString("campid", campid);
                    bund.putString("campname", campname);
                    bund.putString("socialmedia", "twitter");
                    intent.putExtras(bund);
                    startActivity(intent);
                }
            });

//            instagram.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(SocialMediaReport.this, SMDataReportActivity.class);
//                    Bundle bund = new Bundle();
//                    bund.putString("campid", campid);
//                    bund.putString("campname", campname);
//                    bund.putString("socialmedia", "instagram");
//                    intent.putExtras(bund);
//                    startActivity(intent);
//                }
//            });

    }

    private void ScreenVisible() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(SocialMediaReport.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String connection_details = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.smconnection_sample_url);
            StringRequest connectionDetails = new StringRequest(Request.Method.POST,connection_details , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
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
                            JSONObject obj = new JSONObject(data);

                            Facebook = obj.getString("Facebook");
                            Instagram = obj.getString("Instagram");
                            GA = obj.getString("GA");
                            Twitter = obj.getString("Twitter");
                            Youtube = obj.getString("Youtube");

                            SharedPreferences preferences = getSharedPreferences("SM_SCREEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("valueoffacefook",Facebook);
                            editor.putString("valueofinstagram",Instagram);
                            editor.putString("valueofga",GA);
                            editor.putString("valueoftwitter",Twitter);
                            editor.putString("valueofyoutube",Youtube);
                            editor.apply();

                            Log.v(" Facebook value :", Facebook);
                            Log.v(" Instagram value :", Instagram);
                            Log.v(" GA value :", GA);
                            Log.v(" Twitter value :", Twitter);
                            Log.v(" Youtube value :", Youtube);

                        }else if (responstatus.equalsIgnoreCase("false")){
                            Log.d("success : ", "False");
                            Log.d("response message : ", responsemessage);
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SocialMediaReport.this, Influencer_Home.class);
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
                                Intent intent = new Intent(SocialMediaReport.this, Influencer_Login.class);
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
            connectionDetails.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(connectionDetails);

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