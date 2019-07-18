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
import android.provider.Settings;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Influencer_Registeration extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = Influencer_Registeration.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp;
    private EditText  inputName,inputEmail,inputMobile, inputOtp,inputPassword,inputconfirmPassword;
    private ProgressBar progressBar;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile,resendotp;
    private LinearLayout layoutEditMobile;
    String cid,rname,remail,rmobile,nmessage,rpassword,type,message;
    TextView create,terms,login;
    boolean flg = true;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    Snackbar snackbar;
    String mail,nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.influencer_registration);

        viewPager = findViewById(R.id.viewPagerVertical);

        inputName = findViewById(R.id.name);
        inputEmail = findViewById(R.id.email);
        inputMobile = findViewById(R.id.mobile);
        inputPassword = findViewById(R.id.password);
        inputconfirmPassword = findViewById(R.id.confirmpassword);
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

        //viewPager.setCurrentItem(0);
        resendotp.setPaintFlags(resendotp.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOtp.setText("");
                SendOTP();
            }
        });

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputName.setTypeface(myFont);
        Typeface myFont1 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputEmail.setTypeface(myFont1);
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputMobile.setTypeface(myFont2);
        Typeface myFont3 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputPassword.setTypeface(myFont3);
        Typeface myFont4 = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        inputconfirmPassword.setTypeface(myFont4);

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



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Influencer_Registeration.this, Influencer_Login.class);
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

            final String epassword = inputPassword.getText().toString();
            if ((TextUtils.isEmpty(epassword))) {
                flg = false;
                inputPassword.setError("Password field is empty");
                return;
            } else if (!isValidPassword(epassword)) {
                flg = false;
                inputPassword.setError("Minimum required value is 6");
                return;
            }

            final String ecpassword =inputconfirmPassword.getText().toString();
            if ((TextUtils.isEmpty(ecpassword))) {
                flg = false;
                inputconfirmPassword.setError("Confirm Password field is empty");
                return;
            } else if(!epassword.equals(ecpassword)){
                flg = false;
                inputconfirmPassword.setError("Password Not matching");
                return;
            }

            if (flg) {
                rname = inputName.getText().toString();
                remail = inputEmail.getText().toString();
                rmobile = inputMobile.getText().toString();
                rpassword = inputPassword.getText().toString();
                CheckEmailFunction();
            }
        } else {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
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

    private void CheckEmailFunction() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Registeration.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String check_email = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.userexit_register_url);
            StringRequest checkEmail = new StringRequest(Request.Method.POST,check_email , new Response.Listener<String>() {

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

                        if (responstatus.equalsIgnoreCase("true")) {

                            AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Registeration.this).create();
                            alertDialog.setMessage(responsemessage);
                            alertDialog.setCancelable(false);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();

                        } else if (responstatus.equalsIgnoreCase("false")) {
                            SendOTP();
                        }

                    } catch (JSONException e){
                        Log.e(TAG, "Error Value : " + e.getMessage());

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Influencer_Registeration.this, Influencer_Home.class);
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
                progressBar.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    Log.e(TAG, "Failure Error: " + " HTTP Status Code: 401 Unauthorized");
                    hidePDialog();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_Registeration.this, Influencer_Login.class);
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
                params.put("email", remail);
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




    private void SendOTP() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Registeration.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String send_otp = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.sendotp_url);
            StringRequest strReq = new StringRequest(Request.Method.POST, send_otp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidePDialog();
                try {
                    JSONObject responseObj = new JSONObject(response);

                    String responstatus = responseObj.getString("success").toString();
                    Log.d("response status : ",responstatus);
                    String responsemessage = responseObj.getString("message").toString();
                    Log.d("response message : ",responsemessage);

                    JSONObject resobj = responseObj.getJSONObject("data");
                    message = resobj.getString("message");
                    type = resobj.getString("type");
                    Log.v("message value :", message);
                    Log.v("type value :", type);

                    if (type.equalsIgnoreCase("success")) {
                        // moving the screen to next pager item i.e otp screen
                        viewPager.setCurrentItem(1);
                        layoutEditMobile.setVisibility(View.VISIBLE);
                        txtEditMobile.setText(rmobile);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resendotp.setVisibility(View.VISIBLE);
                            }
                        }, 10000);

                        Toast.makeText(getApplicationContext(), responsemessage, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), responsemessage, Toast.LENGTH_LONG).show();
                    }

                    // hiding the progress bar
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e){
                    Log.e(TAG, "Error Value : " + e.getMessage());
                    progressBar.setVisibility(viewPager.GONE);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_Registeration.this, Influencer_Home.class);
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
                progressBar.setVisibility(View.GONE);
                hidePDialog();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Data from Server.Please try some time later..!", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Influencer_Registeration.this, Influencer_Home.class);
                        startActivity(intent);

                    }
                });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }) {

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
    } else {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
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

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {
        if (isInternetPresent) {
        String otp = inputOtp.getText().toString().trim();
        Log.v("GETTING OTP VALUE:",otp);
        if (!otp.isEmpty()) {
            verifyOtp(otp);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                @Override
                public void onClick(View view) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
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
    private void verifyOtp(final String otp) {
        if(isInternetPresent){
            pDialog = new ProgressDialog(Influencer_Registeration.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String verify_otp = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.sendotp_url);
            StringRequest strReq = new StringRequest(Request.Method.POST, verify_otp, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidePDialog();

                try {
                    JSONObject responseObj = new JSONObject(response);
                    String responstatus = responseObj.getString("success").toString();
                    Log.d("response status : ",responstatus);
                    String responsemessage = responseObj.getString("message").toString();
                    Log.d("response message : ",responsemessage);

                    JSONObject resobj= responseObj.getJSONObject("data");
                    nmessage = resobj.getString("message");
                    type = resobj.getString("type");

                    if (type.equalsIgnoreCase("success")){
                                Log.v("Result : ","Register working");
                                viewPager.setCurrentItem(0);
                                layoutEditMobile.setVisibility(View.GONE);
                                Register();
                             }else if (type.equalsIgnoreCase("error")){
                                 Log.v("Result : ","Register not working");
                                    AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Registeration.this).create();
                                    alertDialog.setMessage("Invalid OTP");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setButton("Try Again", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        inputOtp.setText("");
                                        }
                                    });
                                    alertDialog.show();

                             }
                } catch (JSONException e){
                    Log.e(TAG, "Error Value : " + e.getMessage());

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_Registeration.this, Influencer_Home.class);
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
                Log.e(TAG, "Error: " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    Log.e(TAG, "Failure Error: " + " HTTP Status Code: 401 Unauthorized");
                    hidePDialog();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_Registeration.this, Influencer_Login.class);
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
                params.put("mobile", rmobile);
                params.put("otp", otp);
                params.put("action", "verify");
                Log.e(TAG, "Posting params: " + params.toString());
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
                @Override
                public void onClick(View view) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
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

    private void Register() {
        if(isInternetPresent){
            pDialog = new ProgressDialog(Influencer_Registeration.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String register = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.register_newuser_url);
            StringRequest strReq = new StringRequest(Request.Method.POST, register, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidePDialog();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    String responstatus = responseObj.getString("success").toString();
                    Log.d("response status : ",responstatus);
                    String responsemessage = responseObj.getString("message").toString();
                    Log.d("response message : ",responsemessage);

                    JSONObject resobj= responseObj.getJSONObject("data");
                    cid = resobj.getString("cid");
                    mail = resobj.getString("mail");
                    nextPage = resobj.getString("nextPage");

                    Log.d("cid : ",cid);
                    Log.d("mail : ",mail);
                    Log.d("nextPage : ",nextPage);

                    SharedPreferences preferences = getSharedPreferences("REG_CID_VALUE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("valueofcid",cid);
                    editor.apply();

                    if (type.equalsIgnoreCase("success")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Registeration.this).create();
                        alertDialog.setMessage("New User Registered. Verification link has been sent to your registered email-id, kindly click on the link to complete registration");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Influencer_Registeration.this, Influencer_Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                viewPager.setCurrentItem(0);
                            }
                        });
                        alertDialog.show();


                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Registeration.this).create();
                        alertDialog.setMessage(responsemessage);
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }


                }  catch (JSONException e){
                    Log.e(TAG, "Error Value : " + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_Registeration.this, Influencer_Home.class);
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
                progressBar.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    Log.e(TAG, "Failure Error: " + " HTTP Status Code: 401 Unauthorized");
                    hidePDialog();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Session Expired.", Snackbar.LENGTH_INDEFINITE).setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Influencer_Registeration.this, Influencer_Login.class);
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
                params.put("password", rpassword);

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
    } else {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    // validating password
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}