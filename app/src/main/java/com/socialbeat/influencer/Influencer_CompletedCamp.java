package com.socialbeat.influencer;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Influencer_CompletedCamp extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Influencer_CompletedCamp.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<Influencer_CompletedCamp_Declare> completedList = new ArrayList<>();
    private ListView listView;
    private Influencer_Completed_ListAdapter adapter;
    String cid,url,valueofcid,token;
    private SwipeRefreshLayout swipeRefreshLayout;
    JsonArrayRequest campobj;
    TextView caption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.influencer_completedcamp, container, false);
        context = v.getContext();
        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);
        SharedPreferences prfs1 = this.getActivity().getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");
//        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
//        swipeRefreshLayout.setOnRefreshListener(this);
        caption = v.findViewById(R.id.caption);
        if (isInternetPresent) {

            listView = v.findViewById(R.id.completedcampvalues);
            CampaignsFunction();

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
        return v;
    }

    private void CampaignsFunction() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String completed_list = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.completed_camp_url);
            StringRequest completedListURL = new StringRequest(Request.Method.POST,completed_list , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);
                        completedList.clear();
                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                            token = responseObj.getString("token");
                            Log.v("Token value :", token);

                            SharedPreferences preferences = getActivity().getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                        }

                        if (responstatus.equalsIgnoreCase("true")) {
                            caption.setVisibility(View.INVISIBLE);
                            responseObj.getJSONArray("data");
                            JSONArray obj1 = responseObj.getJSONArray("data");

                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj = obj1.getJSONObject(i);

                                    Influencer_CompletedCamp_Declare completedcamp = new Influencer_CompletedCamp_Declare();
                                    completedcamp.setCampid(obj.getString("campid"));
                                    completedcamp.setCampImg(obj.getString("campImg"));
                                    completedcamp.setCampname(obj.getString("campname"));
                                    completedcamp.setStatus(obj.getString("status"));
                                    completedcamp.setcompleteddate(obj.getString("completeddate"));
                                    completedcamp.setCampbrief(obj.getString("campbrief"));

                                    JSONObject obj2 = obj.getJSONObject("payment");

                                    completedcamp.setPaymentstatus(obj2.getString("paymentstatus"));
                                    completedcamp.setTransactionstatus(obj2.getString("transactionstatus"));
                                    completedcamp.setTransactionid(obj2.getString("transactionid"));
                                    completedcamp.setDate(obj2.getString("date"));
                                    completedcamp.setAmount(obj2.getString("amount"));

                                    // adding contentofCampaigns to movies array
                                    completedList.add(completedcamp);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                        }else if (responstatus.equalsIgnoreCase("false")){
                            Log.d("success : ", "False");
                            caption.setVisibility(View.VISIBLE);
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Campaigns in Completed Campaign List.", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Live Campaigns", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getActivity(), NewHomeActivity.class);
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
                        adapter = new Influencer_Completed_ListAdapter(getActivity(), completedList);
                        listView.setAdapter(adapter);
                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), Influencer_Home.class);
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
                                Intent intent = new Intent(getActivity(), Influencer_Login.class);
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
            completedListURL.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(completedListURL);

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
        MyApplication.getInstance().trackScreenView("Completed Campaign Screen");
    }
    public static Influencer_CompletedCamp newInstance() {
        return (new Influencer_CompletedCamp());
    }

    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Influencer_CompletedCamp fragment = new Influencer_CompletedCamp();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        swipeRefreshLayout.setRefreshing(false);
    }
}