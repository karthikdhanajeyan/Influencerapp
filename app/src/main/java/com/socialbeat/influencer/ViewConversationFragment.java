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
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ViewConversationFragment extends Fragment {

    private static final String TAG = ViewConversationFragment.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<ViewConversation> viewConversationList = new ArrayList<>();
    private ListView listView;
    private ViewConversationCustomListAdapter adapter;
    String cid,campid,campname,url,valueofcid,token;
    ImageView campImg;
    private SwipeRefreshLayout swipeRefreshLayout;
    JSONObject object;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.viewconversation, container, false);
        context = v.getContext();
        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            campid = bundle.getString("campid", "");
            campname = bundle.getString("campname", "");
            Log.v("viewCon CCampid : ",campid);
            Log.v("viewCon CCampnme : ",campname);
        }else{
            Log.v("VC value : ","Empty");
            SharedPreferences prefernce1 = this.getActivity().getSharedPreferences("COMPLETE_CAMP_CONTENT", Context.MODE_PRIVATE);
            campid = prefernce1.getString("campid", "");
            campname = prefernce1.getString("campname", "");
            Log.v("viewCon sCCampid : ",campid);
            Log.v("viewCon sCCampnme : ",campname);
        }

        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);

        SharedPreferences prfs1 = this.getActivity().getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");

        if (isInternetPresent) {
            listView = v.findViewById(R.id.viewconversationlist);
            CampaignsFunction();
        }else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS)); }
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
            String view_conversation = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.allcoversation_url);
            StringRequest viewConversation = new StringRequest(Request.Method.POST,view_conversation , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);
                        viewConversationList.clear();
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

                            responseObj.getJSONArray("content");
                            JSONArray obj1 = responseObj.getJSONArray("content");

                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj = obj1.getJSONObject(i);

                                    ViewConversation viewconversation = new ViewConversation();
                                    viewconversation.setId_post_conversation(obj.getString("id_post_conversation"));
                                    viewconversation.setMessage(obj.getString("message"));
                                    viewconversation.setConversation_author(obj.getString("conversation_author"));
                                    viewconversation.setConversation_thumbnail_url(obj.getString("conversation_thumbnail_url"));
                                    viewconversation.setAdded_date(obj.getString("added_date"));

                                    JSONArray obj2 = obj.getJSONArray("files");

                                    for (int j = 0; j < obj2.length(); j++) {
                                        // Log.v("J value : ", String.valueOf(j));
                                        JSONObject obj3 = obj2.getJSONObject(j);
                                        //System.out.println(String.valueOf(obj3[j]));
                                        Log.v("J value result : ", String.valueOf(obj3.getString("file_name")));

                                        viewconversation.setId_post_attach(obj3.getString("id_post_attach"));
                                        viewconversation.setFile_name(obj3.getString("file_name"));
                                        viewconversation.setFile_location(obj3.getString("file_location"));
                                        viewconversation.setThumbnail_url(obj3.getString("thumbnail_url"));
                                        viewconversation.setApproved_status(obj3.getString("approved_status"));

                                        //viewConversationList.add(viewconversation);
                                        // adding contentofCampaigns to movies array
                                    }
                                    viewConversationList.add(viewconversation);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                        }else if (responstatus.equalsIgnoreCase("false")){
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data shows in Chat Converstration", Snackbar.LENGTH_INDEFINITE)
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
                        adapter = new ViewConversationCustomListAdapter(getActivity(), viewConversationList);
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
       // MyApplication.getInstance().trackScreenView("Approved Campaign Screen");
    }
    public static ViewConversationFragment newInstance() {
        return (new ViewConversationFragment());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @Override
//    public void onRefresh() {
//        swipeRefreshLayout.setRefreshing(true);
//        Influencer_AppliedCamp fragment = new Influencer_AppliedCamp();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//        swipeRefreshLayout.setRefreshing(false);
//    }
}