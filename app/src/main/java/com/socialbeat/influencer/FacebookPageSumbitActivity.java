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

public class FacebookPageSumbitActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    Context context;
    private ProgressDialog pDialog;
    ListView lv;
    private static String TAG = FacebookPageSumbitActivity.class.getSimpleName();
    String cid,campid,campname,full_picture,caption,message,is_published,permalink_url,status_type,type,id,postid;
    Boolean isInternetPresent = false;
    long totalSize = 0;
    String fid,fname,femail,fimage,fuatoken,pname,pid,fpatoken,pfcount,plink,pnlc,ptac,fpimage;


    // Connection detector class
    ConnectionDetector cd;
    // URL to get contacts JSON
    private static String url ;
    LazyAdapter adapter;

    // JSON node keys
    public static final String TAG_CID = "cid";
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

    //public static final String TAG_PABOUT = "fb_page_about";
    public static final String TAG_PNLC = "fb_page_new_like_count";
    //public static final String TAG_PRC = "fb_page_rating_count";
    public static final String TAG_PTAC = "fb_page_talking_about_count";
    public static final String TAG_FPIMAGE = "fb_page_picture";

    // contacts JSONArray
    JSONArray posts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> postList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummyactivity);

//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("My Campaigns");




        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prefernce1 = getSharedPreferences("SM_FB_VALUE", Context.MODE_PRIVATE);
        campid = prefernce1.getString("campid", "");
        campname = prefernce1.getString("campname", "");


        SharedPreferences prefernce2 = getSharedPreferences("FB_DETAILS_LIST", Context.MODE_PRIVATE);

        fid = prefernce2.getString("fid", "");
        fname = prefernce2.getString("fname", "");
        femail = prefernce2.getString("femail", "");
        fimage = prefernce2.getString("fimage", "");
        fuatoken = prefernce2.getString("fuatoken", "");

        pname = prefernce2.getString("pname", "");
        pid = prefernce2.getString("pid", "");
        fpatoken = prefernce2.getString("fpatoken", "");
        pfcount = prefernce2.getString("pfcount", "");
        plink = prefernce2.getString("plink", "");

        pnlc = prefernce2.getString("pnlc", "");
        ptac = prefernce2.getString("ptac", "");
        fpimage = prefernce2.getString("fpimage", "");

        cd = new ConnectionDetector(FacebookPageSumbitActivity.this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        postList = new ArrayList<HashMap<String, String>>();

        if (cid.length() != 0) {
            if (isInternetPresent) {
                cid = prfs.getString("valueofcid", "");
                FacebookPage();
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
            Intent intent = new Intent(FacebookPageSumbitActivity.this, Influencer_Login.class);
            startActivity(intent);
        }
    }




    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, NewHomeActivity.class);
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
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void FacebookPage() {

        pDialog = new ProgressDialog(FacebookPageSumbitActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //url = "http://stage.influencer.in/API/v6/api_v6.php/updateFBConnectionDetails";
        url = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.fbconnection_details_url);
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
                            Intent intent = new Intent(FacebookPageSumbitActivity.this, SMProfile.class);
                            startActivity(intent);
                        }else if (responstatus == "false") {
                            Toast.makeText(getApplicationContext(), responsemessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FacebookPageSumbitActivity.this, SocialMediaAuthentication.class);
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

                params.put(TAG_CID, cid);
                params.put(TAG_FID, fid);
                params.put(TAG_FNAME, fname);
                params.put(TAG_FEMAIL, femail);
                params.put(TAG_FIMAGE, fimage);
                params.put(TAG_FUATOKEN, fuatoken);
                params.put(TAG_PID, pid);
                params.put(TAG_PNAME, pname);
                params.put(TAG_PFCOUNT, pfcount);
                params.put(TAG_PLINK, plink);
                params.put(TAG_FPATOKEN, fpatoken);
                params.put(TAG_PNLC, pnlc);
                params.put(TAG_PTAC, ptac);
                params.put(TAG_FPIMAGE, fpimage);

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