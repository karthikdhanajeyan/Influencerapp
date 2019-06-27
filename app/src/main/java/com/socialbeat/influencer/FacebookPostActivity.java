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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookPostActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    Context context;
    private ProgressDialog pDialog;
    ListView lv;
    private static String TAG = FacebookPostActivity.class.getSimpleName();
    String cid,campid,campname,full_picture,caption,message,is_published,permalink_url,status_type,type,id;
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
        setContentView(R.layout.facebookpost);

//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("List of Facebook Post");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            campid = extras.getString("campid");
            campname = extras.getString("campname");
        }

        SharedPreferences prefernce2 = getSharedPreferences("FB_DETAILS_LIST", Context.MODE_PRIVATE);
        fbPageId = prefernce2.getString("pid", "");
        fbPageToken = prefernce2.getString("fpatoken", "");

//        fbPageId ="789049217844776";
//        fbPageToken ="EAADv8Hn8IqcBAO538hVDdZBYqOQh9ZA8m6lFOFfjSySWCaNLrBM3xAfmt3d72A9ziZBcvkOyhNTLGg1bMWxajVlzYZCEDZCHHSTntorCINZBNrfcH0OFST0a08StZBxJtBwtx2XJDRS7zl6WdWWc9LFyrwqmXKdULRogKJvREBC9HPZCo6NN90ZBcKdplWCM8Y0TqRWHRkvCGJB2y41MeLqIrRQXq6BNJUDQZD";


        SharedPreferences preferences = getSharedPreferences("SM_FB_VALUE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("campid",campid);
        editor.putString("campname",campname);
        editor.apply();

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        cd = new ConnectionDetector(FacebookPostActivity.this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        postList = new ArrayList<HashMap<String, String>>();

        if (cid.length() != 0) {
            if (isInternetPresent) {
                cid = prfs.getString("valueofcid", "");
                lv = findViewById(R.id.facebookpostvalues);

               // new GetCampaign().execute();
                // Listview on item click listener
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // getting values from selected ListItem
//                        String ffull_picture = ((TextView) view.findViewById(R.id.cid))
//                                .getText().toString();
                        String fpost_id = ((TextView) view.findViewById(R.id.post_id))
                                .getText().toString();
//                        String fccampname = ((TextView) view.findViewById(R.id.campname))
//                                .getText().toString();
                        Log.v("post_id value :",fpost_id);
                    }
                });
                // Calling async task to get json
                //new GetCampaign().execute();
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
            Intent intent = new Intent(FacebookPostActivity.this, Influencer_Login.class);
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

        pDialog = new ProgressDialog(FacebookPostActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //url = "http://stage.influencer.in/API/v6/api_v6.php/getFBPosts";
        url = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.fbpost_url);
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

                            // Getting JSON Array node
                            posts = responseObj.getJSONArray("data");

                            for (int i = 0; i < posts.length(); i++) {
                                JSONObject c = posts.getJSONObject(i);
                                full_picture= c.getString(KEY_FULLPICTURE);
                                //caption = c.getString(KEY_CAPTION);
                                message = c.getString(KEY_MESSAGE);
                                is_published = c.getString(KEY_ISPUBLISHED);
                                permalink_url = c.getString(KEY_PERMALINKURL);
                                status_type = c.getString(KEY_STATUSTYPE);
                                type = c.getString(KEY_TYPE);
                                id = c.getString(KEY_ID);

                                // tmp hashmap for single contact
                                HashMap<String, String> post = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                post.put(KEY_FULLPICTURE,full_picture);
                                //post.put(KEY_CAPTION,caption);
                                post.put(KEY_MESSAGE, message);
                                post.put(KEY_ISPUBLISHED, is_published);
                                post.put(KEY_PERMALINKURL, permalink_url);
                                post.put(KEY_STATUSTYPE, status_type);
                                post.put(KEY_TYPE, type);
                                post.put(KEY_ID, id);

                                // adding contact to contact list
                                postList.add(post);
//
//                                //Image
//                                    Glide.with(getApplicationContext()).load(full_picture)
//                                            .thumbnail(0.5f)
//                                            .crossFade()
//                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                            .into(R.id.post_icon);

//                                ListAdapter adapter = new SimpleAdapter(FacebookPostActivity.this, postList, R.layout.facebookpostlist, new String[]{KEY_ID}, new int[]{R.id.post_id});
//
//                                lv.setAdapter(adapter);
                                //ListAdapter adapter = new SimpleAdapter(FacebookPostActivity.this, postList, R.layout.facebookpostlist, new
                                // Getting adapter by passing xml data ArrayList
                                adapter = new LazyAdapter(FacebookPostActivity.this, postList);
                                lv.setAdapter(adapter);

                                // Click event for single list row
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Toast.makeText(getApplicationContext(), "ID : " + id, Toast.LENGTH_LONG).show();
                                    }

                                });




                            }



                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Click Here", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(FacebookPostActivity.this, SocialMediaReport.class);
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