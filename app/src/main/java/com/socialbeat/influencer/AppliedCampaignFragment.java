package com.socialbeat.influencer;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AppliedCampaignFragment extends Fragment {

    private static final String TAG = AppliedCampaignFragment.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<AppliedCamp> appliedList = new ArrayList<>();
    private ListView listView;
    private AppliedCustomListAdapter adapter;
    String cid,url,valueofcid;
    ImageView campImg;
    private SwipeRefreshLayout swipeRefreshLayout;
    JSONObject object;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.appliedcampaign, container, false);
        context = v.getContext();
        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);
//        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
//        swipeRefreshLayout.setOnRefreshListener(this);

        if (isInternetPresent) {

            listView = v.findViewById(R.id.appliedcampvalues);
            AppliedCampaignsFunction();

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

    private void AppliedCampaignsFunction() {

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String Applied_URL = "https://influencer.in/API/v6/appliedList.php?cid=" + cid + "";
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Applied_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response.toString());
                hidePDialog();

                try {
                    object = new JSONObject(response);
                    if (response != null) {

                        String responstatus = object.getString("success").toString();
                        String responsemessage = object.getString("message").toString();
                        Log.d("response status : ",responstatus);
                        Log.d("response message : ",responsemessage);


                        if (responstatus == "true") {

                            object.getJSONArray("appliedlist");
                            JSONArray obj1 = object.getJSONArray("appliedlist");

                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj = obj1.getJSONObject(i);

                                    AppliedCamp appliedcamp = new AppliedCamp();
                                    appliedcamp.setCid(obj.getString("cid"));
                                    appliedcamp.setCampid(obj.getString("campid"));
                                    appliedcamp.setCampImg(obj.getString("campImg"));
                                    appliedcamp.setCampname(obj.getString("campname"));
                                    appliedcamp.setCampappliedstatus(obj.getString("campappliedstatus"));
                                    appliedcamp.setCampaignquote(obj.getString("campaignquote"));
                                    appliedcamp.setCampapplieddate(obj.getString("campapplieddate"));
                                    appliedcamp.setCampdeliverystatus(obj.getString("campdeliverystatus"));
                                    appliedcamp.setCamppaymentstatus(obj.getString("camppaymentstatus"));

                                    // adding contentofCampaigns to movies array
                                    appliedList.add(appliedcamp);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Campaigns in Applied List.", Snackbar.LENGTH_INDEFINITE)
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
                        adapter = new AppliedCustomListAdapter(getActivity(), appliedList);
                        listView.setAdapter(adapter);
                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Could not get any data from the server", Snackbar.LENGTH_INDEFINITE);
                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
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
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Approved Campaign Screen");
    }
    public static AppliedCampaignFragment newInstance() {
        return (new AppliedCampaignFragment());
    }

//    @Override
//    public void onRefresh() {
//        swipeRefreshLayout.setRefreshing(true);
//        AppliedCampaignFragment fragment = new AppliedCampaignFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//        swipeRefreshLayout.setRefreshing(false);
//    }
}