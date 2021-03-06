package com.socialbeat.influencer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMReportsUpdate extends AppCompatActivity {

    private static final String PREFS_NAME = "SMReportsUpdate";
    private static final int REQUEST_TAKE_PHOTO = 1;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = SMReportsUpdate.class.getSimpleName();
    boolean flg = true;
    String mCurrentPhotoPath,last,location,lasted;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid, userChoosenTask,token;
    ImageView puserimage;
    RadioGroup radioGender;
    private ProgressDialog pDialog;
    private DatePicker datePicker;
    JSONObject object;
    private Calendar calendar;
    private int year, month, day;
    private static final int SELECT_PICTURE = 100;
    private String imagepath=null;
    private String imagepathvalue=null;
    int currentapiVersion;
    private String filePath = null,egender=null,filePathengage = null,imagesize = null,filesize = null;
    long length;
    long totalSize = 0;
    int a = 0;
    File fileDesPath = null;

    EditText ucampid,usocialmedia,ureach,uengagement,udop,ufromdate,utodate;
    TextView uengagefilename,ureachfilename,upostlink ;
    Button ubuttonreach,ubuttonengage,uclear,usubmit;
    String fcid,fcampid,fsocialmedia,freach,fengagement,fpostlink,ffromdate,ftodate,fengagefilename,freachfilename,freportid ;
    ImageView ureachscreenshot,uengagescreenshot,ustrcalendar,uendcalendar;
    String cname,campid,campname,about,gender,mobileno,address,city,message,response,userimage,dob,contentid;
    int sdate,smonth,syear;
    String smcampid,smreportid,smsocial_media,smposted_date,smposted_link,smfrom_date,smto_date,smstatus,smreach,smreach_attach,smengagement,smengage_attach;

    private ProgressDialog pdialog;
    String gendervalue=null;
    String socialmedia="";
    String valueofgender  = null;
    RadioButton radioMale,radioFemale;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static final String KEY_APPNAME = "Influencer";
    public static final String KEY_CID = "cid";
    public static final String KEY_CAMPID = "campid";
    public static final String KEY_REPORTID = "reportid";
    public static final String KEY_SOCIALMEDIA = "social_media";
    public static final String KEY_REACH = "reach";
    public static final String KEY_ENGAGEMENT = "engagement";
    public static final String KEY_POSTLINK = "post_link";
    public static final String KEY_FROMDATE = "from_date";
    public static final String KEY_TODATE = "to_date";
    public static final String KEY_REACHSCREENSHOT = "reach_screenshot";
    public static final String KEY_ENGAGESCREENSHOT = "engage_screenshot";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smreportsupdate);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Reports");

        currentapiVersion = Build.VERSION.SDK_INT;

        ucampid = findViewById(R.id.campid);//edtxt
        usocialmedia = findViewById(R.id.socialmediatype);//edtxt
        ureach = findViewById(R.id.userreach);//edtxt
        uengagement = findViewById(R.id.userengage);//edtxt
        upostlink = findViewById(R.id.posturl);//edtxt
        // udop = findViewById(R.id.dop);//edtxt
        ufromdate = findViewById(R.id.startdate);//edtxt
        utodate = findViewById(R.id.enddate);//edtxt
        uengagefilename = findViewById(R.id.engagefilename);//txtvw
        ureachfilename = findViewById(R.id.reachfilename);//txtvw
        ureachscreenshot = findViewById(R.id.reachimg);//img
        uengagescreenshot= findViewById(R.id.engageimg);//img
        ustrcalendar = findViewById(R.id.calender_icon1);//img
        uendcalendar = findViewById(R.id.calender_icon2);//img
        ubuttonreach = findViewById(R.id.show_file);//button
        ubuttonengage = findViewById(R.id.show_file1);//button
        uclear = findViewById(R.id.clear);//button
        usubmit = findViewById(R.id.submit);//button

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            campid = extras.getString("campid");
            campname = extras.getString("campname");
            contentid = extras.getString("contentid");
            Log.v("SM CCampid : ",campid);
            Log.v("SM CCampnme: ",campname);
            Log.v("SM CContentid: ",contentid);
        }else {
            SharedPreferences prefernce1 = getSharedPreferences("ANALYTICS_REPORT", Context.MODE_PRIVATE);
            campid = prefernce1.getString("campid", "");
            campname = prefernce1.getString("campname", "");
            contentid = prefernce1.getString("contentid", "");
            Log.v("Bundle value : ","Empty");
            Log.v("campid Value : ",campid);
            Log.v("SM CCampid : ", campid);
            Log.v("SM CCampnme: ", campname);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String value="4";

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        Log.v("Cid Value : ",cid);

        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");

        if(cid.length()!=0){
            if (isInternetPresent) {
                SMReportUpdateFunction();
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
            Toast.makeText(getApplicationContext(), "CID value is Empty", Toast.LENGTH_LONG).show();
        }

        ustrcalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SMReportsUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //sets date in EditText
                        ufromdate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        sdate=dayOfMonth;
                        smonth=month + 1;
                        syear=year;
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        uendcalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SMReportsUpdate.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //sets date in EditText

                        utodate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }
                }, year, month, day);
                //shows DatePickerDialog
                Log.v("Date value:",year+"-"+month+"-"+day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        ubuttonreach.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Reach"), 1);
            }
        });
        ubuttonengage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                Intent intent = new Intent();
                intent.setType("image/*");
                //intent.setType("image/*|application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Engage"), 2);
            }
        });

        uclear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                usocialmedia.setText("");
                ureach.setText("");
                uengagement.setText("");
                upostlink.setText("");
                ufromdate.setText("");
                utodate.setText("");
                uengagefilename.setText("");
                ureachfilename.setText("");
            }
        });

        usubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    flg = true;
                    //urllinkvalidation
                    fpostlink = upostlink.getText().toString();
                    if ((TextUtils.isEmpty(fpostlink))) {
                        flg = false;
                        upostlink.setError("URL Field is empty");
                        return;
                    }
                    else if (!isValidUrl(fpostlink)) {
                        flg = false;
                        upostlink.setError("Invaild URL format");
                        return;
                    }

                    //startdatevalidation
                    ffromdate = ufromdate.getText().toString();
                    ftodate = utodate.getText().toString();
                    Log.v("Start Date : ",ffromdate);
                    Log.v("End Date : ",ftodate);
                    if ((TextUtils.isEmpty(ffromdate))) {
                        Log.v("Start Date Result:","Empty");
                        flg = false;
                        ufromdate.setError("Start date range is missing");
                        return;
                    }else{
                        Log.v("Start Date Result:",ffromdate);
                        Log.v("End Date Result:",ftodate);
                    }

                    //enddatevalidation
                    ffromdate = ufromdate.getText().toString();
                    ftodate = utodate.getText().toString();
                    Log.v("Start Date : ",ffromdate);
                    Log.v("End Date : ",ftodate);

                    if ((TextUtils.isEmpty(ftodate))) {
                        Log.v("End Date Result:","Empty");
                        flg = false;
                        utodate.setError("End date range is missing");
                        return;
                    }else{
                        Log.v("Start Date Result:",ffromdate);
                        Log.v("End Date Result:",ftodate);
                    }


                    //reachtext&imagevalidation
                    freach = ureach.getText().toString();
                    freachfilename= ureachfilename.getText().toString();

                    if ((TextUtils.isEmpty(freach))) {
                        Log.v("Response","Empty value");
                    }else{
                        if ((TextUtils.isEmpty(freachfilename))) {
                            flg = false;
                            ureach.setError("Reach screenshot image missing");
                            return;
                        }
                    }
                    freach = ureach.getText().toString();
                    freachfilename= ureachfilename.getText().toString();
                    if ((TextUtils.isEmpty(freachfilename))) {
                        Log.v("Response","Empty value");
                    }else{
                        if ((TextUtils.isEmpty(freach))) {
                            flg = false;
                            ureach.setError("Reach value is missing");
                            return;
                        }
                    }

                    //engagetext&imagevalidation
                    fengagement = uengagement.getText().toString();
                    fengagefilename= uengagefilename.getText().toString();
                    if ((TextUtils.isEmpty(fengagement))) {
                        Log.v("Response","Empty value");
                    }else{
                        if ((TextUtils.isEmpty(fengagefilename))) {
                            flg = false;
                            uengagement.setError("Engage screenshot image missing");
                            return;
                        }
                    }
                    fengagement = uengagement.getText().toString();
                    fengagefilename= uengagefilename.getText().toString();
                    if ((TextUtils.isEmpty(fengagefilename))) {
                        Log.v("Response","Empty value");
                    }else{
                        if ((TextUtils.isEmpty(fengagefilename))) {
                            flg = false;
                            uengagement.setError("Engage value is missing");
                            return;
                        }
                    }

                    Log.v("Length value  : ",Long.toString(length));
                    if ((TextUtils.isEmpty(imagesize))) {
                        Log.v("Response","Empty value");

                    }else if (length >= 2048) {
                        Log.v("Length value 1 : ",Long.toString(length));
                            flg = false;
                            //ureach.setError("Engage value is missing");
                        Toast.makeText(SMReportsUpdate.this, "your image is more than 5 MB,so add image below 5 MB", Toast.LENGTH_LONG).show();
                            return;

                    }
                    if (flg) {

                        fcid = cid;
                        fcampid = campid;//ucampid.getText().toString();
                        freportid = contentid;
                        fsocialmedia = socialmedia;//usocialmedia.getText().toString();
                        freach = ureach.getText().toString();
                        fengagement = uengagement.getText().toString();
                        fpostlink = upostlink.getText().toString();
                        ffromdate = ufromdate.getText().toString();
                        ftodate = utodate.getText().toString();

                        new UploadFileToServerImage().execute();
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


    private void SMReportUpdateFunction() {

        if (isInternetPresent) {
            pDialog = new ProgressDialog(SMReportsUpdate.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String report_list = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.singlecamp_report_url);
            StringRequest reportListURL = new StringRequest(Request.Method.POST,report_list , new Response.Listener<String>() {

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

                            SharedPreferences preferences = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = preferences.edit();
                            editors.putString("token",token);
                            editors.apply();

                        } else {
                            token = "novalue";
                        }

                        if (responstatus.equalsIgnoreCase("true")) {

                            responseObj.getJSONArray("data");
                            JSONArray object1 = responseObj.getJSONArray("data");

                            for (int i = 0; i < object1.length(); i++) {
                                try {
                                    JSONObject obj = object1.getJSONObject(i);

                                    smcampid = obj.getString("campid");
                                    smreportid = obj.getString("reportid");
                                    smsocial_media = obj.getString("social_media");
                                    smposted_date = obj.getString("posted_date");
                                    smposted_link = obj.getString("posted_link");
                                    smfrom_date = obj.getString("from_date");
                                    smto_date = obj.getString("to_date");
                                    smstatus = obj.getString("status");
                                    smreach = obj.getString("reach");
                                    smreach_attach = obj.getString("reach_attach");
                                    smengagement = obj.getString("engagement");
                                    smengage_attach = obj.getString("engage_attach");

                                    usocialmedia.setText(smsocial_media);
                                    ureach.setText(smreach);
                                    uengagement.setText(smengagement);
                                    upostlink.setText(smposted_link);
                                    ufromdate.setText(smfrom_date);
                                    utodate.setText(smto_date);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MyApplication.getInstance().trackException(e);
                                    Log.e(TAG, "Exception: " + e.getMessage());
                                }
                            }
                        }else if (responstatus.equalsIgnoreCase("false")){
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Live Campaigns", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(SMReportsUpdate.this, NewHomeActivity.class);
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
                    } catch(JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SMReportsUpdate.this, Influencer_Home.class);
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
                                Intent intent = new Intent(SMReportsUpdate.this, Influencer_Login.class);
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
                    params.put("reportid", contentid);
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            reportListURL.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(reportListURL);

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
         super.onBackPressed();  // optional depending on your needs
//        Intent intent = new Intent(this, SocialMediaReport.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
            ureachscreenshot.setImageBitmap(bitmap);
            ureachfilename.setText("Uploading file path:" + imagepath);

            filePath = imagepath;
            System.out.println("reach filePath1: " + filePath);
            if (filePath != null) {
                boolean isImage = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap pictureBitmap = null;
                try {
                    String path = Environment.getExternalStorageDirectory().toString();
                    //System.out.println("filePath value one : " + path);
                    OutputStream fOut = null;
                    path += "/Influencer/";
                    fileDesPath = new File(path);
                    //System.out.println("File path two : " + fileDesPath);
                    if (!fileDesPath.isDirectory()) {
                        fileDesPath.mkdir();
                    }
                    File file = new File(path, "reachscreenshots" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    //System.out.println("File path three : " + file);
                    length = file.length() / 1024; // Size in KB
                    System.out.println("reach imagesize is :" + length + " KB");
                    imagesize = Long.toString(length);
                    //Toast.makeText(SMReportsUpdate.this, "Selected Image Size : " + imagesize + " " + "KB", Toast.LENGTH_LONG).show();
                    fOut = new FileOutputStream(file);
                    //System.out.println("File path four : " + fOut);
                    pictureBitmap = BitmapFactory.decodeFile(filePath, options);
                    pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                    filePath = file.getAbsolutePath();
                    System.out.println("reach filePath2: " + filePath);
                    a = 1;
                } catch (Exception ex) {
                    Log.v("Exception in file get", ex.toString());
                }
                ureachscreenshot.setImageBitmap(pictureBitmap);
                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(SMReportsUpdate.this, new String[]{imagepath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            }

        }else if (requestCode == 2 && resultCode == RESULT_OK) {
            Log.v("Result : ", "Engage is clicked");
            Uri selectedImageUrivalue = data.getData();
            imagepathvalue = getPath(selectedImageUrivalue);
//                Bitmap bitmapnew = BitmapFactory.decodeFile(imagepathvalue);
//                uengagescreenshot.setImageBitmap(bitmapnew);
            uengagefilename.setText("Uploading file path:" + imagepathvalue);

            filePathengage = imagepathvalue;
            System.out.println("engage filePath: " + filePathengage);
            if (filePathengage != null) {
                boolean isImage = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap pictureBitmapnew = null;
                try {
                    String pathvalue = Environment.getExternalStorageDirectory().toString();
                    //System.out.println("filePathnew value one : " + path);
                    OutputStream fengageout = null;
                    pathvalue += "/Influencer/";
                    fileDesPath = new File(pathvalue);
                    //System.out.println("File path two : " + fileDesPath);
                    if (!fileDesPath.isDirectory()) {
                        fileDesPath.mkdir();
                    }
                    File fileengage = new File(pathvalue, "engagescreenshots" + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    //System.out.println("File path three : " + file);
                    length = fileengage.length() / 1024; // Size in KB
                    System.out.println("finallength value is :" + length + " KB");
                    imagesize = Long.toString(length);
                    //Toast.makeText(SMReportsUpdate.this, "Selected Image Size : " + imagesize + " " + "KB", Toast.LENGTH_LONG).show();
                    fengageout = new FileOutputStream(fileengage);
                    //System.out.println("File path four : " + fengageout);
                    pictureBitmapnew = BitmapFactory.decodeFile(filePathengage, options);
                    pictureBitmapnew.compress(Bitmap.CompressFormat.JPEG, 85, fengageout); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fengageout.flush(); // Not really required
                    fengageout.close(); // do not forget to close the stream
                    filePathengage = fileengage.getAbsolutePath();
                    System.out.println("engage filepath : " + filePathengage);
                    a = 1;
                } catch (Exception ex) {
                    Log.v("Exception in file get", ex.toString());
                }
                uengagescreenshot.setImageBitmap(pictureBitmapnew);
                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(SMReportsUpdate.this, new String[]{imagepathvalue}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ureachscreenshot.setImageBitmap(bm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Myprofile Screen");
    }


    class UploadFileToServerImage extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(SMReportsUpdate.this);
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
            //String REGISTER_URL = "https://www.influencer.in/API/v6/api_v6.php/editCampaignReport";
            String REGISTER_URL = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.editcamp_report_url);
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

                if ((TextUtils.isEmpty(filePath))) {
                    Log.v("reachfile","Empty");
                }else{
                    File sourceFile = new File(filePath);
                    System.out.println("sourceFile of reach :" + sourceFile );
                    // Adding file data to http body
                    entity.addPart(KEY_REACHSCREENSHOT, new FileBody(sourceFile));
                    // Extra parameters if you want to pass to server
                }

                if ((TextUtils.isEmpty(filePathengage))) {
                    Log.v("engagefile","Empty");
                }else{
                    File sourceFileone = new File(filePathengage);
                    System.out.println("sourceFile of engage:" + sourceFileone );
                    // Adding file data to http body
                    entity.addPart(KEY_ENGAGESCREENSHOT, new FileBody(sourceFileone));
                    // Extra parameters if you want to pass to server
                }

                entity.addPart(KEY_CID, new StringBody(cid));
                entity.addPart(KEY_CAMPID, new StringBody(fcampid));
                entity.addPart(KEY_REPORTID, new StringBody(freportid));
                entity.addPart(KEY_SOCIALMEDIA, new StringBody(fsocialmedia));
                entity.addPart(KEY_REACH, new StringBody(freach));
                entity.addPart(KEY_ENGAGEMENT, new StringBody(fengagement));
                entity.addPart(KEY_POSTLINK, new StringBody(fpostlink));
                entity.addPart(KEY_FROMDATE, new StringBody(ffromdate));
                entity.addPart(KEY_TODATE, new StringBody(ftodate));

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

                    SharedPreferences preferences = SMReportsUpdate.this.getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editors = preferences.edit();
                    editors.putString("token",token);
                    editors.apply();
                } else {
                    token = "novalue";
                    Log.v("Token value :",token);
                }

                if (responstatus.equalsIgnoreCase("true")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE).setAction("Go Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                }else if (responstatus.equalsIgnoreCase("false")){
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SMReportsUpdate.this, Conversations.class);
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
    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }


}
