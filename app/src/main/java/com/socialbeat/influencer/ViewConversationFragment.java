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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    String cid,campid,campname,url,valueofcid;
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

        if (isInternetPresent) {
            listView = v.findViewById(R.id.viewconversationlist);
            AppliedCampaignsFunction();

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

    private void AppliedCampaignsFunction() {

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String CONVERSATION_URL = "https://www.influencer.in/API/v6/api_v6.php/getAllConversations?cid=" + cid + "&campid="+campid+"";
        System.out.println("conversation url : "+CONVERSATION_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CONVERSATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response.toString());
                hidePDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (response != null) {

                        String responstatus = object.getString("success").toString();
                        Log.d("response status : ",responstatus);
                        String responsemessage = object.getString("message").toString();


                        if (responstatus == "true") {
                    object.getJSONArray("content");
                    JSONArray obj1 = object.getJSONArray("content");

                    for (int i = 0; i < obj1.length(); i++) {
                        try {
                            JSONObject obj = obj1.getJSONObject(i);

                            ViewConversation viewconversation = new ViewConversation();
                            viewconversation.setId_post_conversation(obj.getString("id_post_conversation"));
                            viewconversation.setMessage(obj.getString("message"));
                            viewconversation.setConversation_author(obj.getString("conversation_author"));
                            viewconversation.setAdded_date(obj.getString("added_date"));

                            JSONArray obj2 = obj.getJSONArray("files");

                            for (int j = 0; j < obj2.length(); j++) {

                                JSONObject obj3 = obj2.getJSONObject(j);
                                viewconversation.setId_post_attach(obj3.getString("id_post_attach"));
                                viewconversation.setFile_name(obj3.getString("file_name"));
                                viewconversation.setFile_location(obj3.getString("file_location"));
                                viewconversation.setApproved_status(obj3.getString("approved_status"));
                            }
                            // adding contentofCampaigns to movies array
                            viewConversationList.add(viewconversation);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyApplication.getInstance().trackException(e);
                            Log.e(TAG, "Exception: " + e.getMessage());
                        }
                    }
                } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data shows in Chat Converstration", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Home", new View.OnClickListener() {
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
//        AppliedCampaignFragment fragment = new AppliedCampaignFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//        swipeRefreshLayout.setRefreshing(false);
//    }
}