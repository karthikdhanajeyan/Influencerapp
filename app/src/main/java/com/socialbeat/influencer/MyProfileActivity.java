package com.socialbeat.influencer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyProfileNew";
    private static final int REQUEST_TAKE_PHOTO = 1;
    Context context;
    private CoordinatorLayout coordinatorLayout;
    private static final String TAG = MyProfileActivity.class.getSimpleName();
    EditText pname,plocationcity,ppassword,pcpassword;
    TextView pemail,pcity,pcitydummy,pgenderdummy,pstate,pstatedummy,pmobileno;
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

    String ename,eabout,emobileno,eaddress,ecity,edob;
    String about,gender,mobileno,city,message,response,userimage,name,email,mobile_no;
    private ProgressDialog pdialog,pDialog;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;
    ArrayList<String> StateName;
    ArrayList<String> CityName;

    String STATE_URL = "https://www.influencer.in/API/v6/api_v6.php/getStates";
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static final String KEY_CID = "cid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";
    public static final String KEY_PROFILE_IMAGE = "profile_image";
    public static final String KEY_APPNAME = "Influencer";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        currentapiVersion = Build.VERSION.SDK_INT;

//        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
//        cid = prfs.getString("valueofcid", "");
        cid="1";

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


        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
        boolean firstStart = settings1.getBoolean("firstStart", true);
        if(cid.length()!=0){
            if (isInternetPresent) {
                MyApplication.getInstance().trackEvent("Myprofile Screen", "OnClick", "Track MyProfileDummy Event");
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

        loadSpinnerStateData();


        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                Log.v("Test","onclick working");
                statefinal= state;
                Log.v("State Value : ",state);


                if(k>0) {
                    pstate.setVisibility(View.INVISIBLE);
                    pcity.setVisibility(View.INVISIBLE);
                    statefinal= state_spinner.getItemAtPosition(state_spinner.getSelectedItemPosition()).toString();
                }
                k++;

                //Toast.makeText(getApplicationContext(),statefinal,Toast.LENGTH_LONG).show();
                testfun();
            }




            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

                // DO Nothing here
                //Toast.makeText(getApplicationContext(),"Select your Current State",Toast.LENGTH_LONG).show();
                pstate.setVisibility(View.VISIBLE);

            }

        });



        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cityfinal= city_spinner.getItemAtPosition(city_spinner.getSelectedItemPosition()).toString();

                //Toast.makeText(getApplicationContext(),cityfinal,Toast.LENGTH_LONG).show();
//                if(l>0) {
//                    Log.v("Test : ",l+" times working");
//                    pcity.setVisibility(View.INVISIBLE);
//                }
//                l++;

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
                // capture picture
                boolean result = checkPermission(MyProfileActivity.this);
                userChoosenTask = "Take Photo";
                if (result) {
                    selectImage();
                } else {
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
                    //Toast.makeText(MyProfileActivity.this,radioSexButton.getText(),Toast.LENGTH_SHORT).show();
                    egender = radioSexButton.getText().toString();
                    if(egender=="" && egender==null){
                        flg = false;
                        Toast.makeText(MyProfileActivity.this,"Select Gender Value",Toast.LENGTH_SHORT).show();
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
                        new UploadFileToServer().execute();
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

    private void testfun() {

        if(statefinal!=null && statefinal!="") {
            loadSpinnerCityData();
            //Toast.makeText(getApplicationContext(),"Condition Working",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(),"No State Found",Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinnerStateData() {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        System.out.println("State url : "+STATE_URL);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, STATE_URL, new Response.Listener<String>() {

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
                            statename=jsonObject1.getString("state");
                            StateName.add(statename);


                        }
                    }

                    state_spinner.setAdapter(new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, StateName));


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

    private void loadSpinnerCityData() {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
         String CITY_URL = "https://www.influencer.in/API/v6/api_v6.php/getCitiesInState?state="+statefinal;
         System.out.println("City url : "+CITY_URL);
         StringRequest stringRequest=new StringRequest(Request.Method.GET, CITY_URL, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {

                city_spinner.setAdapter(null);
                CityName.clear();
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

                            id=jsonObject1.getString("id");
                            cityname=jsonObject1.getString("city");
                            statenme=jsonObject1.getString("state");
                            tier=jsonObject1.getString("tier");


                            CityName.add(cityname);

                        }
                    }

                    city_spinner.setAdapter(new ArrayAdapter<String>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, CityName));

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
        pDialog = new ProgressDialog(MyProfileActivity.this);
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
                                    name = obj.getString("name");
                                    email = obj.getString("email");
                                    mobile_no = obj.getString("mobile_no");
                                    password = obj.getString("password");
                                    gender = obj.getString("gender");
                                    state = obj.getString("state");
                                    city = obj.getString("city");
                                    userimage = obj.getString("profile_image");

                                    Log.v(" Name value :", name);
                                    Log.v("Email value :", email);
                                    Log.v("Mobileno value :", mobile_no);
                                    Log.v(" password value :", password);
                                    Log.v("gender value :", gender);
                                    Log.v("state value :", state);
                                    Log.v("city value :", city);
                                    Log.v("userimage value :", userimage);

                                    //set value to edittext box
                                    pname.setText(name);
                                    pemail.setText(email);
                                    pmobileno.setText(mobile_no);
                                    ppassword.setText(password);
                                    pcpassword.setText(password);
                                    pstate.setText(state);
                                    pcity.setText(city);

                                    if (gender.equalsIgnoreCase("male")) {
                                        radioSexGroup.check(R.id.radioButton);
                                    } else if (gender.equalsIgnoreCase("female")) {
                                        radioSexGroup.check(R.id.radioButton2);
                                    }


                                   // state_spinner.setSelection(StateName.indexOf(5));

//                                    String myString = "West Bengal"; //the value you want the position for
//                                    ArrayAdapter myAdap = (ArrayAdapter) state_spinner.getAdapter(); //cast to an ArrayAdapter
//                                    int spinnerPosition = myAdap.getPosition(myString);
//                                    //set the default according to value
//                                    state_spinner.setSelection(spinnerPosition);


                                //Image
                                Glide.with(getApplicationContext()).load(userimage)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(puserimage);

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
                                            Intent intent = new Intent(MyProfileActivity.this, SocialMediaReport.class);
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
                Intent intent = new Intent(this, UserSettings.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setTitle("Select Profile Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(MyProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)


                        Log.v("current Version", String.valueOf(currentapiVersion));
                        Log.v("Bulid Version", String.valueOf(Build.VERSION_CODES.N));

                        if (currentapiVersion >= Build.VERSION_CODES.N) {
                            // Do something for lollipop and above versions
                            Log.v("current Version", String.valueOf(currentapiVersion));
                            Log.v("Bulid Version", String.valueOf(Build.VERSION_CODES.N));
                            Log.v("OS Version","N+");
                            startCamera();
                        } else {
                            Log.v("OS Version","N-");
                            captureImage();
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
                Uri photoURI = FileProvider.getUriForFile(MyProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());
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
                MediaScannerConnection.scanFile(MyProfileActivity.this, new String[]{imageUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
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

    /**
     * Uploading the file to server
     * */
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            pdialog = new ProgressDialog(MyProfileActivity.this);
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
            String REGISTER_URL = "https://www.influencer.in/API/v6/api_v6.php/updateUserDetails";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                entity.addPart(KEY_CID, new StringBody(cid));
                entity.addPart(KEY_USERNAME, new StringBody(ename));
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
                JSONObject json = new JSONObject(success);
                success = json.getString("success");
                message = json.getString("message");
                Log.v("success", success);
                Log.v("message", message);
                pdialog.dismiss();

                if (success == "true") {

                    Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyProfileActivity.this, UserSettings.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                    Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (success == "false") {
                    Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            pdialog = new ProgressDialog(MyProfileActivity.this);
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
            String REGISTER_URL = "https://www.influencer.in/API/v6/api_v6.php/updateUserDetails";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(REGISTER_URL);
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
                JSONObject json = new JSONObject(success);
                success = json.getString("success");
                message = json.getString("message");
                Log.v("success", success);
                Log.v("message", message);
//                Log.v("userimage", userimage);
                pdialog.dismiss();

                if (success == "true") {
                    if (fileDesPath.isDirectory()) {
                        String[] children = fileDesPath.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(fileDesPath, children[i]).delete();
                        }
                        fileDesPath.delete();
                    }
                    Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyProfileActivity.this, UserSettings.class);
                    Bundle bund = new Bundle();
                    //Inserts a String value into the mapping of this Bundle
                    bund.putString("CID",cid);
                    //Add the bundle to the intent.
                    intent.putExtras(bund);
                    startActivity(intent);
                    Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                } else if (success == "false") {
                    Toast.makeText(MyProfileActivity.this, message, Toast.LENGTH_SHORT).show();
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
