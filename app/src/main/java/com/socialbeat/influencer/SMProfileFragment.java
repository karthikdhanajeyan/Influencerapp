package com.socialbeat.influencer;

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
//import android.support.v4.app.Fragment;
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
import java.util.Iterator;
import java.util.List;

public class SMProfileFragment extends Fragment {

    private static final String TAG = SMProfileFragment.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<SMProfileDetails> smprofiledetailsList = new ArrayList<>();
    private ListView listView;
    private SMProfileCustomListAdapter adapter;
    String cid,url,valueofcid;
    ImageView campImg;
    private SwipeRefreshLayout swipeRefreshLayout;
    JSONObject object;
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.smproflie, container, false);
        context = v.getContext();
        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);
        fab = v.findViewById(R.id.fab);
//        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
//        swipeRefreshLayout.setOnRefreshListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SocialMediaAuthentication.class);
                startActivity(intent);
            }
        });

        if (isInternetPresent) {

            listView = v.findViewById(R.id.smlist);
            SMDetailsFunction();

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

    private void SMDetailsFunction() {

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String Applied_URL = "https://www.influencer.in/API/v6/api_v6.php/getSMConnectionDetails?cid=" + cid + "";
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

                        String responstatus = object.getString("success");
                        Log.d("response status : ", responstatus);
                        String responsemessage = object.getString("message");
                        Log.d("response message : ", responsemessage);

                        if (responstatus == "true") {

                            String data = object.getString("data");
                            JSONObject object2 = new JSONObject(data);

                           // SMProfileDetails Smprofiledetails = new SMProfileDetails();

                            JSONArray jArray1 = object2.getJSONArray("connected");
                            //JSONArray jArray2 = object2.getJSONArray("notConnected");

                            for(int i = 0; i < jArray1 .length(); i++)
                            {
                                JSONObject object3 = jArray1.getJSONObject(i);
                                SMProfileDetails Smprofiledetails = new SMProfileDetails();
                               // String socialmedia = object3.getString("socialmedia");
                                Smprofiledetails.setSocialmedia(object3.getString("socialmedia"));

                                String metrics = object3.getString("metrics");
                                JSONObject objectvalue = new JSONObject(metrics);

//                                Iterator<String> iter = objectvalue.keys();
//                                while (iter.hasNext()) {
//                                    String value = null;
//                                    String key = iter.next();
//                                    try {
//                                        //Object value = objectvalue.get(key);
//                                        value = objectvalue.getString(key);
//                                    } catch (JSONException e) {
//                                        // Something went wrong!
//                                    }
//                                    Log.v("key : ",key);
//                                    Log.v("value : ",value);
//                                    Smprofiledetails.setKey1_text(key);
//                                    Smprofiledetails.setKey1(objectvalue.getString(key));
//                                }
                                Smprofiledetails.setKey1_text("Followers");
                                Smprofiledetails.setKey1(objectvalue.getString("Followers"));
                                Smprofiledetails.setKey2_text("Likes");
                                Smprofiledetails.setKey2(objectvalue.getString("Likes"));

                                JSONObject userDetails= object3.getJSONObject("userDetails");
//                                String uname= userDetails.getString("name");
//                                String link= userDetails.getString("link");
//                                String profile_image= userDetails.getString("profile_image");


                                Smprofiledetails.setName(userDetails.getString("name"));
                                Smprofiledetails.setLink(userDetails.getString("link"));
                                Smprofiledetails.setProfile_image(userDetails.getString("profile_image"));


                                //Log.v("Social Media : ",socialmedia);
//                                Log.v("name : ",uname);
//                                Log.v("link : ",link);
//                                Log.v("profile_image : ",profile_image);

                                smprofiledetailsList.add(Smprofiledetails);
                            }

//                            JSONArray jArray2 = object2.getJSONArray("notConnected");
//                            for(int i = 0; i < jArray2 .length(); i++)
//                            {
//                                JSONObject object4 = jArray2.getJSONObject(i);
//                                String socialmedia = object4.getString("socialmedia");
//                                Log.v("New Social Media : ",socialmedia);
//                            }

                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data", Snackbar.LENGTH_INDEFINITE)
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
                        adapter = new SMProfileCustomListAdapter(getActivity(), smprofiledetailsList);
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
        //MyApplication.getInstance().trackScreenView("Analytics Report Fragment);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static SMProfileFragment newInstance() {
        return (new SMProfileFragment());
    }

//    @Override
//    public void onRefresh() {
//        swipeRefreshLayout.setRefreshing(true);
//        SMProfileFragment fragment = new SMProfileFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, fragment);
//        fragmentTransaction.commit();
//        swipeRefreshLayout.setRefreshing(false);
//    }
}