package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsReportActivity extends AppCompatActivity {

    private static final String TAG = AnalyticsReportActivity.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<AnalyticsReport> analyticsList = new ArrayList<>();
    private ListView listView;
    private AnalyticsCustomListAdapter adapter;
    String cid,campid,url,valueofcid,campnamee,token;
    private FloatingActionButton fab;

    //ImageView campImg;
    JSONObject object;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyticsreport);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Analytics Report");

        cd = new ConnectionDetector(this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
         fab = findViewById(R.id.fab);
        isInternetPresent = cd.isConnectingToInternet();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            campid = bundle.getString("campid", "");
            campnamee = bundle.getString("campname", "");
        }else {
            Log.v("AR value : ","Empty");
            SharedPreferences prefernce1 = getSharedPreferences("COMPLETE_CAMP_CONTENT", Context.MODE_PRIVATE);
            campid = prefernce1.getString("campid", "");
            Log.v("campid Value : ", campid);
        }

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value for AR : ",cid);

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");


        if (isInternetPresent) {

            listView = findViewById(R.id.analyticsvalues);
            AnalyticsReportFunction();

        }else {
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyticsReportActivity.this, SocialMediaReport.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campnamee);
                intent.putExtras(bund);
                startActivity(intent);
                startActivity(intent);
            }
        });

    }

    private void AnalyticsReportFunction() {

        if (isInternetPresent) {
            pDialog = new ProgressDialog(AnalyticsReportActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String view_conversation = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.camp_report_url);
            StringRequest viewConversation = new StringRequest(Request.Method.POST,view_conversation , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);
                        analyticsList.clear();
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

                            responseObj.getJSONArray("data");
                            JSONArray obj1 = responseObj.getJSONArray("data");

                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj = obj1.getJSONObject(i);

                                    AnalyticsReport analyticsreport = new AnalyticsReport();
                                    analyticsreport.setCampid(obj.getString("campid"));
                                    analyticsreport.setCampname(obj.getString("campname"));
                                    analyticsreport.setContentid(obj.getString("reportid"));
                                    analyticsreport.setSocial_media(obj.getString("social_media"));
                                    analyticsreport.setPosted_date(obj.getString("posted_date"));
                                    analyticsreport.setPosted_link(obj.getString("posted_link"));
                                    analyticsreport.setFrom_date(obj.getString("from_date"));
                                    analyticsreport.setTo_date(obj.getString("to_date"));
                                    analyticsreport.setStatus(obj.getString("status"));
                                    analyticsreport.setReach(obj.getString("reach"));
                                    analyticsreport.setReach_attach(obj.getString("reach_attach"));
                                    analyticsreport.setEngagement(obj.getString("engagement"));
                                    analyticsreport.setEngage_attach(obj.getString("engage_attach"));

                                    // adding contentofCampaigns to movies array
                                    analyticsList.add(analyticsreport);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }

                        }else if (responstatus.equalsIgnoreCase("false")){
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Reports, Add New Report", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Add Report", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(AnalyticsReportActivity.this, SocialMediaReport.class);
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
                        adapter = new AnalyticsCustomListAdapter(AnalyticsReportActivity.this, analyticsList);
                        listView.setAdapter(adapter);
                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(AnalyticsReportActivity.this, Influencer_Home.class);
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
                                Intent intent = new Intent(AnalyticsReportActivity.this, Influencer_Login.class);
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
                    params.put("campid", campid);
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            viewConversation.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(viewConversation);

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
    public void onResume() {
        super.onResume();

        // Tracking the screen view
        //MyApplication.getInstance().trackScreenView("Analytics Report Fragment);

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