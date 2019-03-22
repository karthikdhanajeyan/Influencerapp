package com.socialbeat.influencer;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;

public class CompletedCampaignFragment extends Fragment {

    private static final String TAG = CompletedCampaignFragment.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<CompletedCamp> completedList = new ArrayList<>();
    private ListView listView;
    private CompCustomListAdapter adapter;
    String cid,url,valueofcid;
    private SwipeRefreshLayout swipeRefreshLayout;
    JsonArrayRequest campobj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.completedcampaign, container, false);
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

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String Completed_URL = "https://www.influencer.in/demo/demoAPI.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Completed_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response.toString());
                hidePDialog();

                try {
                    JSONObject object = new JSONObject(response);
                    object.getJSONArray("data");
                    JSONArray obj1 = object.getJSONArray("data");

                    for (int i = 0; i < obj1.length(); i++) {
                        try {
                            JSONObject obj = obj1.getJSONObject(i);

                            CompletedCamp completedcamp = new CompletedCamp();
                            completedcamp.setCampid(obj.getString("campid"));
                            //completedcamp.setCampImg(obj.getString("campImg"));
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

                    adapter = new CompCustomListAdapter(getActivity(), completedList);
                    listView.setAdapter(adapter);

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
        MyApplication.getInstance().trackScreenView("Completed Campaign Screen");
    }
    public static CompletedCampaignFragment newInstance() {
        return (new CompletedCampaignFragment());
    }

//    @Override
//    public void onRefresh() {
//        swipeRefreshLayout.setRefreshing(true);
//        CompletedCampaignFragment fragment = new CompletedCampaignFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//        swipeRefreshLayout.setRefreshing(false);
//    }
}