package com.socialbeat.influencer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Influencer_FOI extends AppCompatActivity {

    private static final String PREFS_NAME = "MyProfileNew";
    private static final int REQUEST_TAKE_PHOTO = 1;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = Influencer_FOI.class.getSimpleName();
    TextView primary_profile,secondary_profile;
    Button psave;
    String location,primaryname=null,secondaryname=null,primaryfinal=null,secondaryfinal=null;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid, field_of_interest,primaryvalue,secondaryvalue,token;
    Spinner primary_spinner;
    MultiSpinnerSearch secondary_spinner;
    ProgressDialog pdialog;
    String newDataString,svalue1,svalue2;

    int currentapiVersion;
    int a = 0;
    int k=0,l=0;
    String[] values,result;
    Snackbar snackbar;
    String eprimary,esecondary;
    String message,response,name,email,termID,slug;
    private ProgressDialog pDialog;

    ArrayList<String> PrimaryName,SecondaryName;
    List<String> dataString = new ArrayList<String>();

    public List<KeyPairBoolData> listArray = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.influencer_foi);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Field Of Interest");

        currentapiVersion = Build.VERSION.SDK_INT;

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");

        psave = findViewById(R.id.save_button);
        primary_spinner = findViewById(R.id.primary_spinner);
        secondary_spinner = findViewById(R.id.secondary_spinner);
        primary_profile = findViewById(R.id.primary_profile);
        secondary_profile = findViewById(R.id.secondary_profile);
        PrimaryName=new ArrayList<>();
        SecondaryName=new ArrayList<>();

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.foi_array));

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray.add(h);
        }

//        Gson gson=new Gson();
//        newDataArray=gson.toJson(listArray); // dataarray is list aaray

        if(cid.length()!=0){
            if (isInternetPresent) {
                getUserDetails();
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
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }

        if(cid.length()!=0){
            if (isInternetPresent) {
                loadSpinnerPrimaryData();
                //loadSpinnerSecondaryData();
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
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }

        primary_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("Test","onclick working");
                primaryfinal = primaryvalue;
                secondaryfinal = secondaryvalue;
                Log.v("primary Value : ", primaryfinal);
                Log.v("Secondary Value : ", secondaryfinal);
                if (k > 0) {
                    primary_profile.setVisibility(View.INVISIBLE);
                    primaryfinal = primary_spinner.getItemAtPosition(primary_spinner.getSelectedItemPosition()).toString();
                }
                k++;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                primary_profile.setVisibility(View.VISIBLE);
            }
        });

        secondary_spinner.setItems(listArray, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                secondaryfinal = secondaryvalue;
                Log.v("Secondary Value : ", secondaryvalue);
                if(l >=0) {
                    secondary_profile.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        }
                        secondaryfinal = secondary_spinner.getItemAtPosition(secondary_spinner.getSelectedItemPosition()).toString();
                    }
                }
                l++;
            }
        });

        secondary_spinner.setLimit(2, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                //Toast.makeText(getApplicationContext(), "Already two fields selected ", Toast.LENGTH_LONG).show();
                final AlertDialog alertDialog = new AlertDialog.Builder(Influencer_FOI.this).create();
                alertDialog.setMessage("Already two fields selected.");
                alertDialog.setCancelable(false);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });


        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    eprimary = primaryfinal;
                    esecondary = secondaryfinal;

                    result = esecondary.split(",");



                    if (result != null && result.length>0) {
                        int k = result.length;
                        List<Object> dString = new ArrayList<Object>();
                        System.out.println("length size : "+k);
                        for (int i=0;i<result.length;i++){
                            dString.add(result[i]);
                        }

                       // dString = dString.substring(0,dString.length()-1);
                       // Gson gson=new Gson();
                        //newDataString=gson.toJson(dString);
                    }




                    UpdateFOI();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
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

        });
    }

    private void UpdateFOI() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_FOI.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            //String UPLOAD_URL = "https://www.influencer.in/API/v6/api_v6.php/updateUserDetails";
            String UPLOAD_URL = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.update_foi_url);
            StringRequest uploadurl = new StringRequest(Request.Method.POST,UPLOAD_URL , new Response.Listener<String>() {

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
                            snackbar = Snackbar.make(coordinatorLayout, "FOI updated Successfully", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Influencer_FOI.this, Influencer_UserSettings.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.setActionTextColor(Color.RED);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();
                        }else if(responstatus.equalsIgnoreCase("false")){
                            snackbar = Snackbar.make(coordinatorLayout, "Not Updated, Select Values from List", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Influencer_FOI.this, Influencer_FOI.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.setActionTextColor(Color.RED);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server.Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_FOI.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_FOI.this, Influencer_Login.class);
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
                    Log.v("Token Value : ",token);
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cid", cid);
                    params.put("primFOI", eprimary);
                   // params.put("secFOI[]", newDataString);


                    if (result != null && result.length>0) {
                        int k = result.length;
                       // List<Object> dString = new ArrayList<Object>();
                        String data ="";
                        System.out.println("length size : "+k);
                        for (int i=0;i<result.length;i++){
                            data += result[i]+",";

                        }
                        params.put("secFOI[]", data.substring(0,data.length()-1));

                    }
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            uploadurl.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(uploadurl);

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

    private void loadSpinnerPrimaryData() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_FOI.this);
            pDialog.setMessage("State list Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String primary_list = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.foi_url);
            StringRequest PRIMARY_LIST = new StringRequest(Request.Method.POST,primary_list , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);
                        PrimaryName.clear();
                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if(responstatus.equalsIgnoreCase("true")) {
                            responseObj.getJSONArray("data");
                            JSONArray jsonArray = responseObj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                termID=jsonObject1.getString("termID");
                                primaryname=jsonObject1.getString("name");
                                slug=jsonObject1.getString("slug");
                                PrimaryName.add(primaryname);
                            }
                            primary_spinner.setAdapter(new ArrayAdapter<String>(Influencer_FOI.this, android.R.layout.simple_spinner_dropdown_item, PrimaryName));

                        }else if (responstatus.equalsIgnoreCase("false")){
                            if(responsemessage.equalsIgnoreCase("Expired token")){

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "User Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Influencer_FOI.this, Influencer_Login.class);
                                        startActivity(intent);
                                    }
                                });
                                snackbar.setActionTextColor(Color.RED);
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                            //  state_spinner.setAdapter(new ArrayAdapter<String>(Influencer_MyProfile.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_FOI.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_FOI.this, Influencer_Login.class);
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
            PRIMARY_LIST.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(PRIMARY_LIST);

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


    private void getUserDetails() {
        if (isInternetPresent) {
//            pDialog = new ProgressDialog(Influencer_MyProfile.this);
//            pDialog.setMessage("User Details Loading...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            String check_email = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.user_details_url);
            StringRequest checkEmail = new StringRequest(Request.Method.POST,check_email , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
//                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);

                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if (responstatus.equalsIgnoreCase("true")) {

                            responseObj.getJSONArray("data");
                            JSONArray obj1 = responseObj.getJSONArray("data");

                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj = obj1.getJSONObject(i);

                                    cid = obj.getString("cid");
                                    name = obj.getString("name");
                                    email = obj.getString("email");
                                    field_of_interest = obj.getString("field_of_interest");

                                    Log.v("Cid value :", cid);
                                    Log.v(" Name value :", name);
                                    Log.v("Email value :", email);
                                    Log.v("foi value :", field_of_interest);


                                    if(field_of_interest != null && !field_of_interest.isEmpty()) {
                                        Log.v(" FOI value :", field_of_interest);
                                        values = field_of_interest.split(",");
                                        if (values != null && values.length > 0) {
                                            int s = values.length;
                                            System.out.println("length size : "+s);
                                            if(s == 1) {
                                                primaryvalue = values[0];
                                                System.out.println("First values : " + values[0]);
                                            }else if(s == 2) {
                                                primaryvalue = values[0];
                                                System.out.println("First values : " + values[0]);
                                                secondaryvalue = values[1];
                                                System.out.println("Second values : " + values[1]);
                                            }else if(s == 3) {
                                                primaryvalue = values[0];
                                                System.out.println("First values : " + values[0]);
                                                secondaryvalue = values[1] + "," + values[2];
                                                System.out.println("Third values : " + values[2]);
                                            }
                                        }


                                        //set value to edittext box
                                        primary_profile.setText(primaryvalue);
                                        secondary_profile.setText(secondaryvalue);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                        }else if (responstatus.equalsIgnoreCase("false")){
                            if(responsemessage.equalsIgnoreCase("Expired token")){

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "User Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Influencer_FOI.this, Influencer_Login.class);
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
                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_FOI.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_FOI.this, Influencer_Login.class);
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
            checkEmail.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(checkEmail);

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
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent = new Intent(this, Influencer_UserSettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(this, Influencer_UserSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
