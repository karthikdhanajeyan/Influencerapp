package com.socialbeat.influencer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Influencer_Livecamp_List extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = Influencer_Livecamp_List.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pDialog;
    private List<CampValues> campValuesList = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;
    String cid,url,valueofcid;
    private SwipeRefreshLayout swipeRefreshLayout;
    String token;
    TextView caption;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.influencer_livecamp, container, false);
        context = v.getContext();
        cd = new ConnectionDetector(getActivity());
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prfs = this.getActivity().getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);

        caption = v.findViewById(R.id.caption);

        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        if (isInternetPresent) {

            listView = (ListView) v.findViewById(R.id.overalllist);
            adapter = new CustomListAdapter(getActivity(), campValuesList);

        listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                    String campImg = campValuesList.get(position).getCampImg();
                    String campName = campValuesList.get(position).getCampName();
                    String campShortNote = campValuesList.get(position).getCampShortNote();
                    String campCat = campValuesList.get(position).getCampCat();
                    String campLongNote = campValuesList.get(position).getCampLongNote();
                    String campGoal = campValuesList.get(position).getCampGoal();
                    String campDos = campValuesList.get(position).getCampDos();
                    String campDont = campValuesList.get(position).getCampDont();
                    String campBacklink = campValuesList.get(position).getCampBacklink();
                    String campTag= campValuesList.get(position).getCampTag();
                    String campid = campValuesList.get(position).getCampid();
                    String campApplyTill = campValuesList.get(position).getCampApplyTill();
                    String campRewards = campValuesList.get(position).getCampRewards();
                    String campRewardType = campValuesList.get(position).getCampRewardType();
                    String fixedamount= campValuesList.get(position).getFixedamount();
                    String campEligibility= campValuesList.get(position).getCampEligibility();
                    String campDeliverables= campValuesList.get(position).getCampDeliverables();

                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), Influencer_Livecamp_Details.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("campImg", campImg);
                    bund.putString("campName", campName);
                    bund.putString("campShortNote", campShortNote);
                    bund.putString("campCat", campCat);
                    bund.putString("campLongNote", campLongNote);
                    bund.putString("campGoal", campGoal);
                    bund.putString("campDos", campDos);
                    bund.putString("campDont", campDont);
                    bund.putString("campBacklink", campBacklink);
                    bund.putString("campTag", campTag);
                    bund.putString("campid", campid);
                    bund.putString("campApplyTill", campApplyTill);
                    bund.putString("campRewards", campRewards);
                    bund.putString("campRewardType", campRewardType);
                    bund.putString("fixedamount", fixedamount);
                    bund.putString("campEligibility", campEligibility);
                    bund.putString("campDeliverables", campDeliverables);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    //start the DisplayActivity
                    startActivity(intent);
                    // Toast.makeText(getActivity(),campImg+"_____________"+campBacklink, Toast.LENGTH_LONG).show();
                }
            });

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
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
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
            String LIVE_CAMP_URL = getResources().getString(R.string.base_url_v6)+getResources().getString(R.string.live_campaign_url);
            StringRequest liveCamp = new StringRequest(Request.Method.POST,LIVE_CAMP_URL , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();
                    try {
                        JSONObject responseObj = new JSONObject(response);

                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if(responstatus.equalsIgnoreCase("true")){
                            caption.setVisibility(View.INVISIBLE);
                            JSONArray jsonArray=responseObj.getJSONArray("data");
//                            for(int i=0;i<jsonArray.length();i++){
//                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    CampValues campvalue = new CampValues();
                                    campvalue.setCampImg(obj.getString("campImg"));
                                    campvalue.setCampName(obj.getString("campName"));
                                    campvalue.setCampShortNote(obj.getString("campShortNote"));
                                    campvalue.setCampCat(obj.getString("campCat"));
                                    campvalue.setCampLongNote(obj.getString("campLongNote"));
                                    campvalue.setCampGoal(obj.getString("campGoal"));
                                    campvalue.setCampDos(obj.getString("campDos"));
                                    campvalue.setCampDont(obj.getString("campDont"));
                                    campvalue.setCampBacklink(obj.getString("campBacklink"));
                                    campvalue.setCampTag(obj.getString("campTag"));
                                    campvalue.setCampid(obj.getString("campid"));
                                    campvalue.setCampApplyTill(obj.getString("campApplyTill"));
                                    campvalue.setCampRewards(obj.getString("campRewards"));
                                    campvalue.setCampRewardType(obj.getString("campRewardType"));
                                    campvalue.setFixedamount(obj.getString("fixedamount"));
                                    campvalue.setCampEligibility(obj.getString("campEligibility"));
                                    campvalue.setCampDeliverables(obj.getString("campDeliverables"));

                                    // adding contentofCampaigns to movies array
                                    campValuesList.add(campvalue);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();


                        }else if(responstatus.equalsIgnoreCase("false")){
                            caption.setVisibility(View.VISIBLE);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Live Campaigns Now", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setActionTextColor(Color.RED);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();
                        }

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
            });

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            liveCamp.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(liveCamp);

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
        MyApplication.getInstance().trackScreenView("Livecampaigns List Screen");
    }
    public static Influencer_Livecamp_List newInstance() {
        return (new Influencer_Livecamp_List());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Influencer_Livecamp_List fragment = new Influencer_Livecamp_List();
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
        swipeRefreshLayout.setRefreshing(false);
    }
}