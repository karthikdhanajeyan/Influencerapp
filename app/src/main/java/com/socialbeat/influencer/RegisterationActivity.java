package com.socialbeat.influencer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socialbeat.influencer.app.Config;
import com.socialbeat.influencer.helper.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterationActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = RegisterationActivity.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp;
    private EditText  inputName,inputEmail,inputMobile, inputOtp;
    private ProgressBar progressBar;
    private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile,resendotp;
    private LinearLayout layoutEditMobile;
    String cid,mobileno,rname,remail,rmobile,nmessage,type;
    TextView create,terms,login;
    boolean flg = true;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.registration_screen);

        viewPager = findViewById(R.id.viewPagerVertical);

        inputName = findViewById(R.id.name);
        inputEmail = findViewById(R.id.email);
        inputMobile = findViewById(R.id.mobile);
        inputOtp = findViewById(R.id.inputOtp);
        login = findViewById(R.id.login);
        terms = findViewById(R.id.terms);
        resendotp = findViewById(R.id.resendotp);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        btnRequestSms = findViewById(R.id.sendotp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        progressBar = findViewById(R.id.progressBar);
        btnEditMobile = findViewById(R.id.btn_edit_mobile);
        txtEditMobile = findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = findViewById(R.id.layout_edit_mobile);

        resendotp.setPaintFlags(resendotp.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputName.setTypeface(myFont);
        Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputEmail.setTypeface(myFont1);
        Typeface myFont4 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputMobile.setTypeface(myFont4);

        Spanned sp = Html.fromHtml("By Signing up with us, you agree to the Influencer " +
                "<a href=\"http://www.influencer.in/terms-and-conditions/\"> Terms &amp; Conditions.</a>");
        terms.setText(sp);
        terms.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface myFont6 = Typeface.createFromAsset(getAssets(), "font/gothic.ttf");
        terms.setTypeface(myFont6);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);

        pref = new PrefManager(this);
        // Checking for user session
        // if user is already logged in, take him to main activity
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(RegisterationActivity.this, MyWallet.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("alert", "open");
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegisterationActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });

        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(1);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendotp:
                validateForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;
        }
    }

    /**
     * Validating user details form
     */
    private void validateForm() {
        if (isInternetPresent) {
            flg = true;
            final String username1 = inputName.getText().toString();
            if ((TextUtils.isEmpty(username1))) {
                flg = false;
                inputName.setError("Name field is empty");
                return;
            }
            final String emailid1 = inputEmail.getText().toString();
            if ((TextUtils.isEmpty(emailid1))) {
                flg = false;
                inputEmail.setError("Email id field is empty");
                return;
            } else if (!isValidEmailid1(emailid1)) {
                flg = false;
                inputEmail.setError("Enter Valid Email id");
                return;
            }

            final String mobileno1 = inputMobile.getText().toString();
            if ((TextUtils.isEmpty(mobileno1))) {
                flg = false;
                inputMobile.setError("Enter Valid Mobile Number");
                return;
            } else if (!isValidMobileno1(mobileno1)) {
                flg = false;
                inputMobile.setError("Enter your 10 Digit Mobile Number");
                return;
            }

            if (flg) {
                rname = inputName.getText().toString();
                remail = inputEmail.getText().toString();
                rmobile = inputMobile.getText().toString();
                CheckEmailFunction();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    private void CheckEmailFunction() {

        pDialog = new ProgressDialog(RegisterationActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String CONVERSATION_URL = R.string.base_url+R.string.userexistence_register + remail ;
        System.out.println("conversation url : "+CONVERSATION_URL);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, CONVERSATION_URL, new Response.Listener<String>() {
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
                        Log.d("response message : ",responsemessage);

                        if (responstatus.equalsIgnoreCase("true")){

                            AlertDialog alertDialog = new AlertDialog.Builder(RegisterationActivity.this).create();
                            alertDialog.setMessage(responsemessage);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();

                        }else if (responstatus.equalsIgnoreCase("false")) {
                            //Toast.makeText(getApplicationContext(), responsemessage, Toast.LENGTH_SHORT).show();
                            SendOTP();
                        }

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


    private void SendOTP() {

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_SMS_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    String success = responseObj.getString("success");
                    String message = responseObj.getString("message");
                    //final String cid = responseObj.getString("cid");

                    SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("valueofcid",cid);
                    editor.apply();
                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (success.equalsIgnoreCase("true")) {
                        // boolean flag saying device is waiting for sms
                        pref.setIsWaitingForSms(true);
                        // moving the screen to next pager item i.e otp screen
                        viewPager.setCurrentItem(1);
                        txtEditMobile.setText(pref.getMobileNumber());
                        layoutEditMobile.setVisibility(View.VISIBLE);
                        txtEditMobile.setText(rmobile);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resendotp.setVisibility(View.VISIBLE);
                            }
                        }, 10000);

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    // hiding the progress bar
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", rmobile);
                params.put("action", "first");
                return params;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();
        Log.v("GETTING OTP VALUE:",otp);
        if (!otp.isEmpty()) {
            verifyOtp(otp);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }
    private void verifyOtp(final String otp) {
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_SMS_OTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {

                    JSONObject responseObj = new JSONObject(response);
                    // Parsing json object response
                    // response will be a json object
                    String success = responseObj.getString("success");
                    String message = responseObj.getString("message");
                    Log.v("message value :",message);

                    JSONObject resobj= responseObj.getJSONObject("data");

                    nmessage = resobj.getString("message");
                    type = resobj.getString("type");

                    if (type.equalsIgnoreCase("success")){
                                 Log.v("Result : ","Register working");
                                 Register();

                             }else if (type.equalsIgnoreCase("error")){
                                 Log.v("Result : ","Register not working");
                             }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", rmobile);
                params.put("otp", otp);
                params.put("action", "verify");
                Log.e(TAG, "Posting params: " + params.toString());
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    private void Register() {

        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    String success = responseObj.getString("success");
                    String message = responseObj.getString("message");


                    SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("valueofcid",cid);
                    editor.apply();

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (type.equalsIgnoreCase("success")) {

                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterationActivity.this).create();
                        alertDialog.setMessage(message+". "+"Verification link has been sent to your registered email-id, kindly click on the link to complete registration");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterationActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();


                        snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                        snackbar.setActionTextColor(Color.WHITE);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();

                    } else {
                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterationActivity.this).create();
                        alertDialog.setMessage(message);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        // Showing Alert Message
                        alertDialog.show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", rname);
                params.put("email", remail);
                params.put("mobile_no", rmobile);
                return params;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        public Object instantiateItem(View collection, int position) {
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;
            }
            return findViewById(resId);
        }
    }
  
    // validating email id
    private boolean isValidEmailid1(String emailid1) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailid1);
        return matcher.matches();
    }
    // validating mobile number
    private boolean isValidMobileno1(String mobileno1) {
        String MOBILE_PATTERN = "[0-9]{10}";
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(mobileno1);
        return matcher.matches();
    }
}