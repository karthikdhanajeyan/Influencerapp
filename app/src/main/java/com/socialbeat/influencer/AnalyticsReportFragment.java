package com.socialbeat.influencer;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsReportFragment extends Fragment {

    private static final String TAG = AnalyticsReportFragment.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<AnalyticsReport> analyticsList = new ArrayList<>();
    private ListView listView;
    private AnalyticsCustomListAdapter adapter;
    String cid,campid,url,valueofcid,campnamee;
    private FloatingActionButton fab,fab1;
    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    //ImageView campImg;
    private SwipeRefreshLayout swipeRefreshLayout;
    JSONObject object;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.analyticsreport, container, false);
//        // Set title bar
//        ((MyCampaigns) getActivity()).setActionBarTitle("Your title");
        context = v.getContext();

        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        //FloatingActionButton fab = v.findViewById(R.id.fab);
         fab = v.findViewById(R.id.fab);
         fab1 = v.findViewById(R.id.fab1);

        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backward);

        TextView campname = v.findViewById(R.id.campname);
        isInternetPresent = cd.isConnectingToInternet();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            campid = bundle.getString("campid", "");
            campnamee = bundle.getString("campname", "");
        }else {
            Log.v("AR value : ","Empty");
            SharedPreferences prefernce1 = this.getActivity().getSharedPreferences("COMPLETE_CAMP_CONTENT", Context.MODE_PRIVATE);
            campid = prefernce1.getString("campid", "");
            Log.v("campid Value : ", campid);
        }

        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value for AR : ",cid);
//        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
//        swipeRefreshLayout.setOnRefreshListener(this);

        if (isInternetPresent) {

            listView = v.findViewById(R.id.analyticsvalues);
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
                if(isFabOpen){

                    fab.startAnimation(rotate_backward);
                    fab1.startAnimation(fab_close);
                    fab1.setClickable(false);
                    isFabOpen = false;
                    Log.d("Button", "close");

                } else {

                    fab.startAnimation(rotate_forward);
                    fab1.startAnimation(fab_open);
                    fab1.setClickable(true);
                    isFabOpen = true;
                    Log.d("Button","open");

                }
            }
        });
//
        fab1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SocialMediaReport.class);
                startActivity(intent);
            }
        });
//
        return v;
    }

    private void AnalyticsReportFunction() {

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String Applied_URL = "https://www.influencer.in/API/v6/api_v6.php/getAllCampaignReports?cid=" + cid +"&campid="+campid;
        System.out.println("Applied_URL : "+Applied_URL);
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
                        Log.d("response status : ",responstatus);
                        String responsemessage = object.getString("message").toString();


                        if (responstatus == "true") {

                            object.getJSONArray("data");
                            JSONArray obj1 = object.getJSONArray("data");

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
                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Reports,Add New Report", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Click Here", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getActivity(), SocialMediaReport.class);
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
                        adapter = new AnalyticsCustomListAdapter(getActivity(), analyticsList);
                        listView.setAdapter(adapter);
                    } else {
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
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        //MyApplication.getInstance().trackScreenView("Analytics Report Fragment);
    }
    public static AnalyticsReportFragment newInstance() {
        return (new AnalyticsReportFragment());
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
//        AnalyticsReportFragment fragment = new AnalyticsReportFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//        swipeRefreshLayout.setRefreshing(false);
//    }
}