package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FacebookPostSumbitActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    Context context;
    private ProgressDialog pDialog;
    ListView lv;
    private static String TAG = FacebookPostSumbitActivity.class.getSimpleName();
    String cid,campid,campname,full_picture,caption,message,is_published,permalink_url,status_type,type,id,postid;
    Boolean isInternetPresent = false;
    long totalSize = 0;

    String fbPageId;
    String fbPageToken;
    String source = "app";

    // Connection detector class
    ConnectionDetector cd;
    // URL to get contacts JSON
    private static String url ;
    LazyAdapter adapter;

    // JSON node keys
    public static final String KEY_FULLPICTURE = "full_picture";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_MESSAGE= "message";
    public static final String KEY_ISPUBLISHED = "is_published";
    public static final String KEY_PERMALINKURL = "permalink_url";
    public static final String KEY_STATUSTYPE = "status_type";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String KEY_CAMPDATA = "data";

    // contacts JSONArray
    JSONArray posts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> postList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummyactivity);

//        ActionBar bar = getSupportActionBar();
//        assert bar != null;
//        bar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("List of Facebook Post");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            postid = extras.getString("postid");
        }

        SharedPreferences prefernce2 = getSharedPreferences("FB_DETAILS_LIST", Context.MODE_PRIVATE);
        fbPageId = prefernce2.getString("pid", "");
        fbPageToken = prefernce2.getString("fpatoken", "");

//        fbPageId ="789049217844776";
//        fbPageToken ="EAADv8Hn8IqcBAO538hVDdZBYqOQh9ZA8m6lFOFfjSySWCaNLrBM3xAfmt3d72A9ziZBcvkOyhNTLGg1bMWxajVlzYZCEDZCHHSTntorCINZBNrfcH0OFST0a08StZBxJtBwtx2XJDRS7zl6WdWWc9LFyrwqmXKdULRogKJvREBC9HPZCo6NN90ZBcKdplWCM8Y0TqRWHRkvCGJB2y41MeLqIrRQXq6BNJUDQZD";


        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prefernce1 = getSharedPreferences("SM_FB_VALUE", Context.MODE_PRIVATE);
        campid = prefernce1.getString("campid", "");
        campname = prefernce1.getString("campname", "");

        cd = new ConnectionDetector(FacebookPostSumbitActivity.this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        postList = new ArrayList<HashMap<String, String>>();

        if (cid.length() != 0) {
            if (isInternetPresent) {
                cid = prfs.getString("valueofcid", "");
                FacebookPost();
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
            // return v;
        } else {
            Toast.makeText(getApplicationContext(), "User Could not login properly,Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FacebookPostSumbitActivity.this, Influencer_Login.class);
            startActivity(intent);
        }
    }




    @Override
    public void onBackPressed() {
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, SocialMediaReport.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                // this.finish();
                //super.onBackPressed();
                Intent intent  = new Intent(this, SocialMediaReport.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void FacebookPost() {

        pDialog = new ProgressDialog(FacebookPostSumbitActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
       // url = "http://stage.influencer.in/API/v6/api_v6.php/addNewFBAnalyticsPost";
        url = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.fbanalytics_url);
        System.out.println(url);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidePDialog();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    Log.v("Response : ",response);

                    if (response != null) {

                        String responstatus = responseObj.getString("success");
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message");
                        Log.d("response message : ", responsemessage);

                        if (responstatus == "true") {
                            Toast.makeText(getApplicationContext(), responsemessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FacebookPostSumbitActivity.this, NewHomeActivity.class);
                            startActivity(intent);
                        }else if (responstatus == "false") {
                            Toast.makeText(getApplicationContext(), responsemessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FacebookPostSumbitActivity.this, FacebookPostActivity.class);
                            startActivity(intent);
                        }
                    }

                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.v("cid : ",cid);
                Log.v("campid : ",campid);
                Log.v("fbPostId : ", postid);
                Log.v("fbPageToken: ", fbPageToken);
                Log.v("fbPageId: ", fbPageId);
                Log.v("source: ", source);

                params.put("cid", cid);
                params.put("campid", campid);
                params.put("fbPostId", postid);
                params.put("fbPageToken", fbPageToken);
                params.put("fbPageId", fbPageId);
                params.put("source", source);
                return params;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
        /**
         * Updating parsed JSON data into ListView
         * */

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

}