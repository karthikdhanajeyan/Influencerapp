package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

public class Influencer_Livecamp_Details extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button applynow,alreadyapplied,campaignclosed,submit;
    TextView campName,campShortNote,campCat,campLongNote,campGoal,campDos,campDont,campBacklink,
            campTag,campid,campApplyTill,campRewards,campRewardType,fixedamount,conditiontext,cancel,infotext,campEligibility,campDeliverables;
    ImageView campImg;
    EditText famtval;
   // Button applynow,alreadyapplied,campaignclosed,submit;
    boolean flg = true;
    String famt,cid,cdcampImg,cdcampName,cdcampShortNote,cdcampCat,cdcampLongNote,cdcampGoal,cdcampDos,cdcampDont,cdcampBacklink,token,
            cdcampTag,cdcampid,cdcampApplyTill,cdcampRewards,cdcampRewardType,cdfixedamount,TAG,campaignid,campaignname,cdcampEligibility,cdcampDeliverables;
    int first,second;
    ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.influencer_livecamp_details);

        //bundle values from Campain Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            cdcampImg = extras.getString("campImg");
            cdcampName = extras.getString("campName");
            cdcampShortNote = extras.getString("campShortNote");
            cdcampCat = extras.getString("campCat");
            cdcampLongNote = extras.getString("campLongNote");
            cdcampGoal = extras.getString("campGoal");
            cdcampDos = extras.getString("campDos");
            cdcampDont = extras.getString("campDont");
            cdcampBacklink = extras.getString("campBacklink");
            cdcampTag = extras.getString("campTag");
            cdcampid = extras.getString("campid");
            cdcampApplyTill = extras.getString("campApplyTill");
            cdcampRewards = extras.getString("campRewards");
            cdcampRewardType = extras.getString("campRewardType");
            cdfixedamount = extras.getString("fixedamount");
            cdcampEligibility = extras.getString("campEligibility");
            cdcampDeliverables = extras.getString("campDeliverables");
        }

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(cdcampName);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        SharedPreferences prfs1 = Influencer_Livecamp_Details.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        //token = prfs1.getString("token", "");
         token= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1NjEzNzIwMTksIm5iZiI6MTU2MTM3MjAxOSwiZXhwIjoxNTYxOTc2ODE5LCJlbWFpbCI6ImthcnRoaWtkaGFuYWpleWFuQGdtYWlsLmNvbSIsImNpZCI6IjE2In0.GPxUs8C3880ZGA1J_gH9jxXVj4xBKCYyaIZcdj0M3W8";
        Log.v("Token Value : ",token);

        // Displaying all values on the screen

        campName = findViewById(R.id.campName);
        campShortNote = findViewById(R.id.campShortNote);
        campCat = findViewById(R.id.campCat);
        campLongNote = findViewById(R.id.campLongNote);
        campGoal = findViewById(R.id.campGoal);
        campDos = findViewById(R.id.campDos);
        campDont = findViewById(R.id.campDont);
        campBacklink = findViewById(R.id.campBacklink);
        campTag = findViewById(R.id.campTag);
        campid = findViewById(R.id.campid);
        campApplyTill = findViewById(R.id.campApplyTill);
        campRewards = findViewById(R.id.campRewards);
        campRewardType = findViewById(R.id.campRewardType);
        fixedamount = findViewById(R.id.fixedamount);
        campEligibility = findViewById(R.id.campEligibility);
        campDeliverables = findViewById(R.id.campDeliverables);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        campImg = findViewById(R.id.campImg);
        campaignclosed = findViewById(R.id.campaignclosed);
        alreadyapplied = findViewById(R.id.alreadyapplied);
        applynow = findViewById(R.id.applynow);
        campImg.setImageResource(R.mipmap.influencerlistimg);
        new DownloadImageTask(campImg).execute(cdcampImg);
        campName.setText(cdcampName);
        Spanned sp = Html.fromHtml(cdcampShortNote);
        campShortNote.setText(sp);
        campShortNote.setMovementMethod(LinkMovementMethod.getInstance());
        campCat.setText(cdcampCat);

        Spanned sp1 = Html.fromHtml(cdcampLongNote);
        campLongNote.setText(sp1);
        campLongNote.setMovementMethod(LinkMovementMethod.getInstance());

        campGoal.setText(cdcampGoal);

        Spanned sp2 = Html.fromHtml(cdcampDos);
        campDos.setText(sp2);
        campDos.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sp3 = Html.fromHtml(cdcampDont);
        campDont.setText(sp3);
        campDont.setMovementMethod(LinkMovementMethod.getInstance());

        String link = "<a href=" + cdcampBacklink + ">" + cdcampBacklink + " </a>";
        Spanned spd = Html.fromHtml(link);
        campBacklink.setText(spd);
        campBacklink.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sp4 = Html.fromHtml(cdcampTag);
        campTag.setText(sp4);
        campTag.setMovementMethod(LinkMovementMethod.getInstance());

        campid.setText(cdcampid);
        campApplyTill.setText(cdcampApplyTill);
        campRewards.setText(cdcampRewards);
        campRewardType.setText(cdcampRewardType);
        fixedamount.setText(cdfixedamount);

        Spanned sp5 = Html.fromHtml(cdcampEligibility);
        campEligibility.setText(sp5);
        campEligibility.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sp6 = Html.fromHtml(cdcampDeliverables);
        campDeliverables.setText(sp6);
        campDeliverables.setMovementMethod(LinkMovementMethod.getInstance());

        System.out.println("cfixedamount value  :" + cdfixedamount);
        if (cdfixedamount != null) {
            if (cdfixedamount.length() > 0) {
                fixedamount.setText("Maximum amount Rs : " + cdfixedamount);
                System.out.println("Fixed Amount value is :" + cdfixedamount);
            } else {
                fixedamount.setText("Quote Amount is less than 0 for this Campaign.");
            }
        } else {
            fixedamount.setText("No Quote Amount Condition for this Campaign.");
            System.out.println("Fixed Amount value is Null");
        }


        campaignid = campid.getText().toString();
        campaignname = campName.getText().toString();

        if (cid.length() != 0) {
            Log.v("Cid Value : ",cid);
            campStatus();
        }else{
            Log.v("Cid Value : ","Empty");
        }

        campaignclosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Livecamp_Details.this).create();
                alertDialog.setMessage("This Campaign is closed already.");
                alertDialog.setCancelable(false);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Influencer_Livecamp_Details.this, NewHomeActivity.class);
                        startActivity(intent);
                    }
                });
                try {
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alreadyapplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Livecamp_Details.this).create();
                alertDialog.setMessage("You have already applied to this campaign");
                alertDialog.setCancelable(false);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Influencer_Livecamp_Details.this, NewHomeActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        applynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CampaignTheresold();
            }
        });

    }

    private void campStatus() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Livecamp_Details.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String LIVE_CAMP_URL = getResources().getString(R.string.base_url_v6)+getResources().getString(R.string.applied_list_url);
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

                        if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                            token = responseObj.getString("token");
                            Log.v("Token value :", token);

                            SharedPreferences preferences = Influencer_Livecamp_Details.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                            Log.v("Token value :",token);
                        }


                        if(responstatus.equalsIgnoreCase("true")){
                            if(responsemessage.equalsIgnoreCase("Not applied")){
                                applynow.setVisibility(View.VISIBLE);
                                campaignclosed.setVisibility(View.INVISIBLE);
                                alreadyapplied.setVisibility(View.INVISIBLE);
                            }
                            else if(responsemessage .equalsIgnoreCase("Applied")){
                                alreadyapplied.setVisibility(View.VISIBLE);
                                applynow.setVisibility(View.INVISIBLE);
                                campaignclosed.setVisibility(View.INVISIBLE);
                            }
                            else if(responsemessage .equalsIgnoreCase("Campaign closed")){
                                campaignclosed.setVisibility(View.VISIBLE);
                                alreadyapplied.setVisibility(View.INVISIBLE);
                                applynow.setVisibility(View.INVISIBLE);
                            }

                        }else if(responstatus.equalsIgnoreCase("false")){
                            Log.v("Message : ",responsemessage);
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Login.class);
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
                    params.put("campid", cdcampid);
                    return params;
                }
            };

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


    private void CampaignTheresold() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Livecamp_Details.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String CAMP_THERESOLD = getResources().getString(R.string.base_url_v6)+getResources().getString(R.string.check_camp_eligible_url);
            StringRequest campTheresold = new StringRequest(Request.Method.POST,CAMP_THERESOLD , new Response.Listener<String>() {

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

                        if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                            token = responseObj.getString("token");
                            Log.v("Token value :", token);

                            SharedPreferences preferences = Influencer_Livecamp_Details.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                            Log.v("Token value :",token);
                        }

                        String eligibility = responseObj.getString("eligibility");
                        Log.d("eligibility status : ", eligibility);
                        final String reason = responseObj.getString("reason");
                        Log.d("reason : ", reason);

                        if(eligibility.equalsIgnoreCase("true")){

                            //Toast.makeText(Influencer_Livecamp_Details.this, reason, Toast.LENGTH_LONG).show();
                            Log.v("Reason : ",reason);
                            ApplyCampaign();

                        } else if(eligibility.equalsIgnoreCase("false")){

                            //Toast.makeText(Influencer_Livecamp_Details.this, reason, Toast.LENGTH_LONG).show();
                            Log.v("Reason : ",reason);
                            AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Livecamp_Details.this).create();
                            alertDialog.setMessage(reason);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                            if(reason.equalsIgnoreCase("Email ID or Mobile number not verified. Kindly verify to continue.")){
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_UserValidation.class);
                                startActivity(intent);
                            }else if(reason.equalsIgnoreCase("Social media data not available")){
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, SocialMediaAuthentication.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, NewHomeActivity.class);
                                startActivity(intent);
                            }
                                }
                            });
                            alertDialog.show();
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Login.class);
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
                    params.put("campid", campaignid);
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            campTheresold.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(campTheresold);

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

    private void ApplyCampaign() {
        if (isInternetPresent) {
            if (cid.length() != 0) {
                MyApplication.getInstance().trackEvent("Campaign Apply Button Clicked Event", "OnClick", "Track Campaign Apply button Event");
                if (cdcampRewardType.equalsIgnoreCase("probono")) {
                    Log.v("Campaign Type : ", "Probono");
                    //campaignwithoutQoute();
                } else if (cdcampRewardType.equalsIgnoreCase("barter")) {
                    Log.v("Campaign Type : ", "Barter");
                    MyApplication.getInstance().trackEvent("Barter Campaign Applied Clicked Event", "OnClick", "Barter Campaign Clicked Event");
                    //campaignwithoutQoute();
                } else if (cdcampRewardType.equalsIgnoreCase("quote")) {
                    Log.v("Campaign Type : ", "Quote");
                    final Dialog dialog = new Dialog(Influencer_Livecamp_Details.this);
                    dialog.setContentView(R.layout.quoteamount);
                    dialog.setCancelable(false);
                    dialog.setTitle("Quote Amount");

                    submit = dialog.findViewById(R.id.okbutton);
                    famtval = dialog.findViewById(R.id.fquoteamt);
                    cancel = dialog.findViewById(R.id.cancel_button);
                    infotext = dialog.findViewById(R.id.infotext);

                    Typeface myFont = Typeface.createFromAsset(getAssets(), "font/rock.ttf");
                    infotext.setTypeface(myFont);
                    isInternetPresent = cd.isConnectingToInternet();

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            flg = true;
                            if (isInternetPresent) {
                                if(dialog!= null){
                                    dialog.dismiss();
                                }
                                famt = famtval.getText().toString();
                                Log.v("Quoted Amount Value : ", famt);

                                campaignQuote();

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
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (cdcampRewardType.equalsIgnoreCase("fixedamount")) {
                    Log.v("Campaign Type : ", "Fixed Amount");
                    final Dialog dialog = new Dialog(Influencer_Livecamp_Details.this);
                    dialog.setContentView(R.layout.quoteamounts);
                    dialog.setCancelable(false);
                    dialog.setTitle("Quote Amount");

                    submit = dialog.findViewById(R.id.okbutton);
                    famtval = dialog.findViewById(R.id.femailid);
                    cancel = dialog.findViewById(R.id.cancel_button);
                    infotext = dialog.findViewById(R.id.infotext);
                    conditiontext = dialog.findViewById(R.id.conditiontext);
                    Typeface myFont = Typeface.createFromAsset(getAssets(), "font/rock.ttf");
                    infotext.setTypeface(myFont);
                    Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/rock.ttf");
                    conditiontext.setTypeface(myFont1);

                    isInternetPresent = cd.isConnectingToInternet();

                    if (cdfixedamount.length() > 0) {
                        conditiontext.setText("(Maximum Quote amount for this campaign is RS : " + cdfixedamount + " ,so give your quote lessthan maximum amount.)");
                    } else {
                        conditiontext.setText("(No Quote Amount Condition for this Campaign.)");
                    }

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            flg = true;
                            if (isInternetPresent) {
                                famt = famtval.getText().toString();
                                Log.v("quotevalue : ", famt);
                                if (cdfixedamount != null && cdfixedamount.trim().length() != 0) {

                                    if (famt != null && famt.trim().length() != 0) {
                                        first = Integer.parseInt(cdfixedamount);
                                        second = Integer.parseInt(famt);
                                        System.out.println("Fixed Amount value : " + first);
                                        System.out.println("Quoted Amount value :" + second);
                                        if (first >= second) {
                                            campaignQuote();
                                        } else {
                                            Toast.makeText(Influencer_Livecamp_Details.this, "Your quote amount is greater than Maximum Amount,so give your quote lessthan maximum amount. ", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(Influencer_Livecamp_Details.this, "Please enter the amount you would like to charge for this campaign.", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    //Toast.makeText(Influencer_Livecamp_Details.this, "No minimum Value ", Toast.LENGTH_LONG).show();
                                    Log.v("Message : ", "No minimum Value");
                                }

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
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                } else {
                    Toast.makeText(Influencer_Livecamp_Details.this, "Campaign Categeory could not mention.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Influencer_Livecamp_Details.this, "CID value is Empty", Toast.LENGTH_LONG).show();
            }
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
    }

    private void campaignQuote() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Livecamp_Details.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String CAMP_APPLY = getResources().getString(R.string.base_url_v6)+getResources().getString(R.string.apply_camp_with_quote_url);
            StringRequest applyCamp = new StringRequest(Request.Method.POST,CAMP_APPLY , new Response.Listener<String>() {

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

                        if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                            token = responseObj.getString("token");
                            Log.v("Token value :", token);

                            SharedPreferences preferences = Influencer_Livecamp_Details.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                            Log.v("Token value :",token);
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Livecamp_Details.this).create();
                        alertDialog.setMessage(responsemessage);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, NewHomeActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Login.class);
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
                    params.put("campid", campaignid);
                    params.put("quote", famt);
                    params.put("os", "Android");
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            applyCamp.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(applyCamp);

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

    private void campaignwithoutQoute() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Livecamp_Details.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String CAMP_APPLY = getResources().getString(R.string.base_url_v6)+getResources().getString(R.string.apply_camp_without_quote_url);
            StringRequest applyCamp = new StringRequest(Request.Method.POST,CAMP_APPLY , new Response.Listener<String>() {

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

                        if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                            token = responseObj.getString("token");
                            Log.v("Token value :", token);

                            SharedPreferences preferences = Influencer_Livecamp_Details.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                            Log.v("Token value :",token);
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Livecamp_Details.this).create();
                        alertDialog.setMessage(responsemessage);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, NewHomeActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();
                        finish();

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_Livecamp_Details.this, Influencer_Login.class);
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
                    params.put("campid", campaignid);
                    params.put("os", "Android");
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            applyCamp.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(applyCamp);

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








@SuppressLint("StaticFieldLeak")
private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            MyApplication.getInstance().trackException(e);
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return mIcon11;
    }
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, NewHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Livecampaign Details Screen");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
