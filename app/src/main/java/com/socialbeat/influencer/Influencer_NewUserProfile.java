package com.socialbeat.influencer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Influencer_NewUserProfile extends AppCompatActivity {

    private static final String PREFS_NAME = "MyProfileNew";
    private static final int REQUEST_TAKE_PHOTO = 1;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = Influencer_NewUserProfile.class.getSimpleName();
    EditText pname,ppassword,pcpassword;
    TextView pemail,pcity,pstate,pmobileno;
    Button psave;
    String mCurrentPhotoPath,location,password,epassword,estate,state,id,cityname=null,statenme,tier,statename=null,cityfinal=null,statefinal=null;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid, userChoosenTask,ecpassword;
    ImageView puserimage;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Spinner state_spinner,city_spinner;

    int currentapiVersion;
    private String filePath = null,egender=null;
    long totalSize = 0;
    int a = 0;
    File fileDesPath = null;
    int k=0;
    boolean flg = true;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String ename,ecity,token;
    String mobileno,message,response,name,email,mobile_no;
    private ProgressDialog pdialog,pDialog;
    private int SELECT_FILE = 1;
    private Uri fileUri;
    ArrayList<String> StateName;
    ArrayList<String> CityName;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static final String KEY_CID = "cid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";
    public static final String KEY_PROFILE_IMAGE = "profile_image";
    public static final String KEY_APPNAME = "Influencer";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.influencer_newuserprofile);

        currentapiVersion = Build.VERSION.SDK_INT;
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");

        pname = findViewById(R.id.nme_profile);
        pemail = findViewById(R.id.email_profile);
        ppassword = findViewById(R.id.password_profile);
        pcpassword = findViewById(R.id.cpassword_profile);
        pmobileno = findViewById(R.id.mobileno_profile);
        radioSexGroup= findViewById(R.id.gender_profile);
        pstate = findViewById(R.id.state_profile);
        pcity = findViewById(R.id.city_profile);
        psave = findViewById(R.id.save_button);
        puserimage = findViewById(R.id.profileimage);
        state_spinner = findViewById(R.id.state_spinner);
        city_spinner = findViewById(R.id.city_spinner);

        StateName=new ArrayList<>();
        CityName=new ArrayList<>();

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

//        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
//        boolean firstStart = settings1.getBoolean("firstStart", true);

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
                snackbar.setActionTextColor(Color.RED);
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
                loadSpinnerStateData();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_SHORT).show();
        }

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("Test","onclick working");
                pstate.setVisibility(View.INVISIBLE);
                pcity.setVisibility(View.INVISIBLE);
                statefinal= state_spinner.getItemAtPosition(state_spinner.getSelectedItemPosition()).toString();
                testfun();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pstate.setVisibility(View.VISIBLE);
            }
        });


        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityfinal= city_spinner.getItemAtPosition(city_spinner.getSelectedItemPosition()).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getApplicationContext(),"Select your Current City",Toast.LENGTH_LONG).show();
            }
        });

        puserimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(checkAndRequestPermissions()) {
                    selectImage();
                }
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {

                    flg = true;
                    ename = pname.getText().toString();
                    if ((TextUtils.isEmpty(ename))) {
                        flg = false;
                        pname.setError("Name field is empty");
                        return;
                    }
                    epassword = ppassword.getText().toString();
                    if ((TextUtils.isEmpty(epassword))) {
                        flg = false;
                        ppassword.setError("Password field is empty");
                        return;
                    } else if (!isValidPassword(epassword)) {
                        flg = false;
                        ppassword.setError("Minimum required value is 6");
                        return;
                    }
                    ecpassword =pcpassword.getText().toString();
                    if ((TextUtils.isEmpty(ecpassword))) {
                        flg = false;
                        pcpassword.setError("Confirm Password field is empty");
                        return;
                    } else if(!epassword.equals(ecpassword)){
                        flg = false;
                        pcpassword.setError("Password Not matching");
                        return;
                    }
                    int selectedId=radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton= findViewById(selectedId);
                    //Toast.makeText(Influencer_MyProfile.this,radioSexButton.getText(),Toast.LENGTH_SHORT).show();
                    egender = radioSexButton.getText().toString();
                    if(egender=="" && egender==null){
                        flg = false;
                        Toast.makeText(Influencer_NewUserProfile.this,"Select Gender Value",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    estate = statefinal;
                    ecity = city_spinner.getSelectedItem().toString();

                    MyApplication.getInstance().trackEvent("Myprofile Update Button Clicked Event", "OnClick", "Track Myprofile Update Event");

                    if (a == 1) {
                        Log.v("Image", "YES");
                        new UploadFileToServerImage().execute();

                    } else {
                        Log.v("Image", "NO");
                        uploadDetails();
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
    }

    private void uploadDetails() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_NewUserProfile.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            //String UPLOAD_URL = "https://www.influencer.in/API/v6/api_v6.php/updateUserDetails";
            String UPLOAD_URL = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.update_userdetails_url);
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
                            Log.v("Test Result : ","Success");
                        }

                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server.Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_CID, email);
                    params.put(KEY_USERNAME, ename);
                    params.put(KEY_PASSWORD, epassword);
                    params.put(KEY_GENDER, egender);
                    params.put(KEY_STATE, estate);
                    params.put(KEY_CITY, ecity);
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

    private  boolean checkAndRequestPermissions() {

        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        String TAG = "tag";
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions

                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.v(TAG, "services permission granted");
                        // process the normal flow
                        Intent i = new Intent(Influencer_NewUserProfile.this, Influencer_Home.class);
                        //   Intent i = new Intent(Influencer_SplashScreen.this, Conversations.class);
                        startActivity(i);
                        finish();

                    } else {
                        Log.v(TAG, "Some permissions are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showDialogOK(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Service Permissions are required for this app")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain() {
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.socialbeat.influencer")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }


    private void testfun() {
        if(statefinal!=null && statefinal!="") {
            loadSpinnerCityData();
           // Toast.makeText(getApplicationContext(),"Condition Working",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(),"No State Found",Toast.LENGTH_LONG).show();
        }

    }

    private void loadSpinnerStateData() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_NewUserProfile.this);
            pDialog.setMessage("State list Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String state_list = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.getstates_url);
            StringRequest STATE_LIST = new StringRequest(Request.Method.POST,state_list , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();

                    try {
                        JSONObject responseObj = new JSONObject(response);
                        StateName.clear();
                        String responstatus = responseObj.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = responseObj.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        if (responstatus.equalsIgnoreCase("true")) {

                            responseObj.getJSONArray("data");
                            JSONArray jsonArray=responseObj.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                statename=jsonObject1.getString("state");
                                StateName.add(statename);
                            }
                            state_spinner.setAdapter(new ArrayAdapter<String>(Influencer_NewUserProfile.this, android.R.layout.simple_spinner_dropdown_item, StateName));
                        }else if (responstatus.equalsIgnoreCase("false")){
                            if(responsemessage.equalsIgnoreCase("Expired token")){

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "User Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
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
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
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
            STATE_LIST.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(STATE_LIST);

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

    private void loadSpinnerCityData() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_NewUserProfile.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String check_email = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.getcity_url);
            StringRequest checkEmail = new StringRequest(Request.Method.POST,check_email , new Response.Listener<String>() {

                @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            CityName.clear();
                            String responstatus = responseObj.getString("success").toString();
                            Log.d("response status : ", responstatus);
                            String responsemessage = responseObj.getString("message").toString();
                            Log.d("response message : ", responsemessage);

                            if (responstatus.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = responseObj.getJSONArray("data");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                                    cityname = jsonObject2.getString("city");
                                    statenme = jsonObject2.getString("state");
                                    Log.d("City Name : ",cityname);
                                    Log.d("State Name : ",statenme);
                                    System.out.println("City Name : "+cityname);
                                    System.out.println("City Name : "+jsonObject2.getString("city"));
                                    CityName.add(cityname);
                                }

                                city_spinner.setAdapter(new ArrayAdapter<String>(Influencer_NewUserProfile.this, android.R.layout.simple_spinner_dropdown_item, CityName));
                            }
                           // city_spinner.setAdapter(new ArrayAdapter<String>(Influencer_NewUserProfile.this, android.R.layout.simple_spinner_dropdown_item, CityName));
                        } catch(JSONException e){
                            Log.e(TAG, "Error Value : " + e.getMessage());

                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
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
                    params.put("state", statefinal);
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
                                    JSONObject resobj = obj1.getJSONObject(i);

                                    cid = resobj.getString("cid");
                                    name = resobj.getString("name");
                                    email = resobj.getString("email");
                                    mobile_no = resobj.getString("mobile_no");

                                    Log.v(" Name value :", name);
                                    Log.v("Email value :", email);
                                    Log.v("Mobileno value :", mobile_no);

                                    pname.setText(name);
                                    pemail.setText(email);
                                    pmobileno.setText(mobile_no);

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
                                        Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
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
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Influencer_NewUserProfile.this);
        builder.setTitle("Select Profile Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = checkPermission(Influencer_MyProfile.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if(checkAndRequestPermissions()) {
                        Log.v("current Version", String.valueOf(currentapiVersion));
                        Log.v("Bulid Version", String.valueOf(Build.VERSION_CODES.N));

                    if (currentapiVersion >= Build.VERSION_CODES.N) {
                        // Do something for lollipop and above versions
                        Log.v("current Version", String.valueOf(currentapiVersion));
                        Log.v("Bulid Version", String.valueOf(Build.VERSION_CODES.N));
                        Log.v("OS Version", "N+");
                        startCamera();
                    } else {
                        Log.v("OS Version", "N-");
                        captureImage();
                    }

                }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void startCamera() {
        try {
            dispatchTakePictureIntent();
        } catch (IOException e) {
        }
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(Influencer_NewUserProfile.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }
    //selecting img from gallery
    private void galleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
    //capture img from camera
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        System.out.println("Camera File URI"+fileUri);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation changes
        outState.putParcelable("file_uri", fileUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);

            filePath = imageUri.getPath();
            System.out.println("filePath value zero: " + filePath);
            if (filePath != null) {
                boolean isImage = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap pictureBitmap = null;
                try {
                    String path = Environment.getExternalStorageDirectory().toString();
                    System.out.println("filePath value one : " + path);
                    OutputStream fOut = null;
                    path += "/Influencer/";
                    fileDesPath = new File(path);
                    System.out.println("File path two : " + fileDesPath);
                    if (!fileDesPath.isDirectory()) {
                        fileDesPath.mkdir();
                    }
                    File file = new File(path, "profileImage" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    System.out.println("File path three : " + file);
                    fOut = new FileOutputStream(file);
                    System.out.println("File path four : " + fOut);
                    pictureBitmap = BitmapFactory.decodeFile(filePath, options);
                    pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    filePath = file.getAbsolutePath();
                    System.out.println("File path five : " + filePath);
                    a = 1;
                } catch (Exception ex) {
                    Log.v("Exception in file get", ex.toString());
                }
                puserimage.setImageBitmap(pictureBitmap);
                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(Influencer_NewUserProfile.this, new String[]{imageUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            }
        }else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    filePath = fileUri.getPath();
                    if (filePath != null) {
                        boolean isImage = true;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        Bitmap pictureBitmap = null;
                        try {
                            String path = Environment.getExternalStorageDirectory().toString();
                            OutputStream fOut = null;
                            path += "/Influencer/";
                            fileDesPath = new File(path);
                            if (!fileDesPath.isDirectory()) {
                                fileDesPath.mkdir();
                            }
                            File file = new File(path, "profileImage" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                            fOut = new FileOutputStream(file);
                            pictureBitmap = BitmapFactory.decodeFile(filePath, options);
                            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                            fOut.flush(); // Not really required
                            fOut.close(); // do not forget to close the stream
                            filePath = file.getAbsolutePath();
                            a = 1;
                        } catch (Exception ex) {
                            Log.v("Exception in file get", ex.toString());
                        }
                        puserimage.setImageBitmap(pictureBitmap);
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, File path is missing!", Toast.LENGTH_LONG).show();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();

                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
    }
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), KEY_APPNAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + KEY_APPNAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        }else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @SuppressLint("StaticFieldLeak")
    class UploadFileToServerImage extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(Influencer_NewUserProfile.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            //String REGISTER_URL = "https://www.influencer.in/API/v6/api_v6.php/updateUserDetails";
            String REGISTER_URL = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.update_userdetails_url);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            httppost.addHeader("Authorization","Bearer " + token);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });


                File sourceFile = new File(filePath);
                System.out.println("sourceFile:" + sourceFile );
                // Adding file data to http body
                entity.addPart(KEY_PROFILE_IMAGE, new FileBody(sourceFile));
                // Extra parameters if you want to pass to server

                entity.addPart(KEY_CID, new StringBody(cid));
                entity.addPart(KEY_USERNAME, new StringBody(ename));
                entity.addPart(KEY_PASSWORD, new StringBody(epassword));
                entity.addPart(KEY_GENDER, new StringBody(egender));
                entity.addPart(KEY_STATE, new StringBody(estate));
                entity.addPart(KEY_CITY, new StringBody(ecity));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();

            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        public void onPostExecute(String success) {
            try {
                JSONObject responseObj = new JSONObject(success);
                String responstatus = responseObj.getString("success").toString();
                Log.d("response status : ", responstatus);
                String responsemessage = responseObj.getString("message").toString();
                Log.d("response message : ", responsemessage);

                pdialog.dismiss();

                if (responseObj.getString("token") != null && !responseObj.getString("token").isEmpty()) {
                    token = responseObj.getString("token");
                    Log.v("Token value :", token);

                    SharedPreferences preferences = Influencer_NewUserProfile.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editors = preferences.edit();
                    editors.putString("token",token);
                    editors.apply();
                } else {
                    token = "novalue";
                    Log.v("Token value :",token);
                }

                if (responstatus.equalsIgnoreCase("true")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_Login.class);
                            startActivity(intent);
                        }
                    });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                }else if (responstatus.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_NewUserProfile.this, Influencer_NewUserProfile.class);
                            startActivity(intent);
                        }
                    });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // validating password
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }
}
