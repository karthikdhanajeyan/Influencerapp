package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoiActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyProfileNew";
    private static final int REQUEST_TAKE_PHOTO = 1;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = FoiActivity.class.getSimpleName();
    TextView primary_profile,secondary_profile;
    Button psave;
    String mCurrentPhotoPath,location,primaryname=null,secondaryname=null,primaryfinal=null,secondaryfinal=null;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid, userChoosenTask,field_of_interest,primaryvalue,secondaryvalue;
    ImageView puserimage;
    Spinner primary_spinner;
    MultiSpinnerSearch secondary_spinner;
    ProgressDialog pdialog;
    String fval;

    int currentapiVersion;
    private String filePath = null;
    long totalSize = 0;
    int a = 0;
    File fileDesPath = null;
    int k=0,l=0;
    String[] values;

    String eprimary,esecondary;
    String about,mobileno,city,message,response,userimage,name,email,mobile_no,termID,slug;
    private ProgressDialog pDialog;
    private Uri fileUri;
    ArrayList<String> PrimaryName,SecondaryName;

    String FOI_URL = "https://www.influencer.in/API/v6/api_v6.php/getFOI";
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static final String KEY_CID = "cid";
    public static final String KEY_PRIMARY = "primFOI";
    public static final String KEY_SECONDARY = "secFOI";

    public List<KeyPairBoolData> listArray = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foi_activity);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Field Of Interest");

        currentapiVersion = Build.VERSION.SDK_INT;

//        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
//        cid = prfs.getString("valueofcid", "");
            cid="1";


//        pstatedummy = findViewById(R.id.state_profile0);
//        pcitydummy = findViewById(R.id.city_profile0);
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


//
//        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
//        boolean firstStart = settings1.getBoolean("firstStart", true);
        if(cid.length()!=0){
            if (isInternetPresent) {

                profileFunction();
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

        loadSpinnerPrimaryData();
        loadSpinnerSecondaryData();


        primary_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getApplicationContext(),statefinal,Toast.LENGTH_LONG).show();
                Log.v("Test","onclick working");

                if (primaryfinal != null && !primaryfinal.isEmpty()) {
                    primaryfinal= primaryvalue;
                    Log.v("State Value : ",primaryvalue);
                    if(k>0) {
                        primary_profile.setVisibility(View.INVISIBLE);

                        primaryfinal= primary_spinner.getItemAtPosition(primary_spinner.getSelectedItemPosition()).toString();

                    }
                    k++;
                }else {
                    //primary_profile.setVisibility(View.INVISIBLE);
                }


            }

            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

                // DO Nothing here
                //Toast.makeText(getApplicationContext(),"Select your Current State",Toast.LENGTH_LONG).show();

            }

        });



//        secondary_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                secondaryfinal= secondary_spinner.getItemAtPosition(secondary_spinner.getSelectedItemPosition()).toString();
//
//                //Toast.makeText(getApplicationContext(),cityfinal,Toast.LENGTH_LONG).show();
//
//            }
//
//
//
//            @Override
//
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                // DO Nothing here
//                Toast.makeText(getApplicationContext(),"Select your Current City",Toast.LENGTH_LONG).show();
//            }
//
//        });
//
        secondary_spinner.setItems(listArray, -1, new SpinnerListener() {

            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {



//                for (int i = 0; i < items.size(); i++) {
//                    if (items.get(i).isSelected()) {
//                        Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
//                    }
//                }
                if(l>=0) {
                    secondary_profile.setVisibility(View.INVISIBLE);

                    //secondaryfinal= secondary_spinner.getItemAtPosition(secondary_spinner.getSelectedItemPosition()).toString();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        }
                    }
                }
                l++;
            }
        });

        secondary_spinner.setLimit(2, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getApplicationContext(),
                        "Already two fields selected ", Toast.LENGTH_LONG).show();
            }
        });



        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {



                    eprimary = primary_spinner.getSelectedItem().toString();
                    esecondary = secondary_spinner.getSelectedItem().toString();

                    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FoiActivity.this, UserSettings.class);
                    startActivity(intent);

                    Log.v("Primary Value : ",eprimary);
                    Log.v("Secondary Value : ",esecondary);
                    //UpdateDetails();


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
    }


    private void loadSpinnerPrimaryData() {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        System.out.println("Primary url : "+FOI_URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, FOI_URL, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {

                try{

                    JSONObject jsonObject=new JSONObject(response);

                    String responstatus = jsonObject.getString("success");
                    Log.d("response status : ", responstatus);
                    String responsemessage = jsonObject.getString("message");
                    Log.d("response message : ", responsemessage);

                    if(responstatus.equalsIgnoreCase("true")){

                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            termID=jsonObject1.getString("termID");
                            primaryname=jsonObject1.getString("name");
                            slug=jsonObject1.getString("slug");

                            PrimaryName.add(primaryname);


                        }
                    }

                    primary_spinner.setAdapter(new ArrayAdapter<String>(FoiActivity.this, android.R.layout.simple_spinner_dropdown_item, PrimaryName));

                }catch (JSONException e){e.printStackTrace();}

            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }

        });

        int socketTimeout = 30000;

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        requestQueue.add(stringRequest);

    }

    private void loadSpinnerSecondaryData() {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        System.out.println("Secondary url : "+FOI_URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, FOI_URL, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {


                try{
                    JSONObject jsonObject=new JSONObject(response);

                    String responstatus = jsonObject.getString("success");
                    Log.d("response status : ", responstatus);
                    String responsemessage = jsonObject.getString("message");
                    Log.d("response message : ", responsemessage);

                    if(responstatus.equalsIgnoreCase("true")){

                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            termID=jsonObject1.getString("termID");
                            secondaryname=jsonObject1.getString("name");
                            slug=jsonObject1.getString("slug");

                            SecondaryName.add(secondaryname);


                        }
                    }
                    secondary_spinner.setAdapter(new ArrayAdapter<String>(FoiActivity.this, android.R.layout.simple_spinner_dropdown_item, SecondaryName));

                }catch (JSONException e){e.printStackTrace();}

            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }

        });

        int socketTimeout = 30000;

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        requestQueue.add(stringRequest);

    }



    private void profileFunction() {
        pDialog = new ProgressDialog(FoiActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String CONVERSATION_URL = "https://www.influencer.in/API/v6/api_v6.php/getUserDetails?cid="+cid;
        System.out.println("conversation url : "+CONVERSATION_URL);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, CONVERSATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response);
                hidePDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (response != null) {

                        String responstatus = object.getString("success");
                        Log.d("response status : ", responstatus);
                        String responsemessage = object.getString("message");
                        Log.d("response message : ", responsemessage);

                        if (responstatus == "true") {

                            object.getJSONArray("data");
                            JSONArray obj1 = object.getJSONArray("data");

                            for (int i = 0; i < obj1.length(); i++) {
                                try {
                                    JSONObject obj = obj1.getJSONObject(i);

                                    cid = obj.getString("cid");
                                    field_of_interest = obj.getString("field_of_interest");

                                    if(field_of_interest != null && !field_of_interest.isEmpty()) {

                                        Log.v(" FOI value :", field_of_interest);

                                        values = field_of_interest.split(",");

//                                    System.out.println("final values : "+Arrays.toString(values));
//                                    System.out.println("first values : "+values[0]);
//                                    System.out.println("second values : "+values[1]);
//                                    System.out.println("third values : "+values[2]);


                                        if (values[0] != null && !values[0].isEmpty()) {
                                            primaryvalue = values[0];
                                            System.out.println("first values : " + values[0]);
                                        }
                                        if (values[1] != null && !values[1].isEmpty()) {
                                            secondaryvalue = values[1];
                                            System.out.println("second values : " + values[1]);
                                        }
                                        if (values[2] != null && !values[2].isEmpty()) {
                                            secondaryvalue = values[1] + "," + values[2];
                                            System.out.println("Third values : " + values[2]);
                                        }

                                        //set value to edittext box
                                        primary_profile.setText(primaryvalue);
                                        secondary_profile.setText(secondaryvalue);

                                    }else{
                                        Log.v("FOI Value : ","No Value");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Click Here", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(FoiActivity.this, SocialMediaReport.class);
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

                    }else {
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
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent = new Intent(this, UserSettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(this, UserSettings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
