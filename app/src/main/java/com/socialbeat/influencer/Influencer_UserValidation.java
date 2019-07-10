package com.socialbeat.influencer;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Influencer_UserValidation extends AppCompatActivity {
    private static String TAG = Influencer_UserValidation.class.getSimpleName();
    boolean flg = true;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    final Context context = this;
    private CoordinatorLayout coordinatorLayout;
    SharedPreferences.Editor editor;
    Button mo_verifynow_button,em_verifynow_button,okbutton;
    ImageView mobile_correct_icon,mobile_wrong_icon,email_correct_icon,email_wrong_icon;
    TextView mobileno,emailid,resend_button;
    String email,status,cid,name,mobile,message,type,token,mobile_no,mobileValidation,mailValidation,nmessage,username;
    EditText otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.influencer_uservalidation);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");


        SharedPreferences prfs1 = getSharedPreferences("TOKEN_VALUE", Context.MODE_PRIVATE);
        token = prfs1.getString("token", "");
        mobileno = findViewById(R.id.mobileno);
        emailid = findViewById(R.id.emailid);
        mobile_correct_icon = findViewById(R.id.mobile_correct_icon);
        mobile_wrong_icon = findViewById(R.id.mobile_wrong_icon);
        email_correct_icon = findViewById(R.id.email_correct_icon);
        email_wrong_icon = findViewById(R.id.email_wrong_icon);
        mo_verifynow_button = findViewById(R.id.mo_verifynow_button);
        em_verifynow_button = findViewById(R.id.em_verifynow_button);

        if(cid.length()!=0){
            if (isInternetPresent) {
                getUserDetails();
            } else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS", new View.OnClickListener() {
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

        mo_verifynow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOTP();
            }
        });

        em_verifynow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailVerification();
            }
        });
    }

    private void emailVerification() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_UserValidation.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String send_email = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.send_verificationmail_url);
            StringRequest sendEmail = new StringRequest(Request.Method.POST,send_email , new Response.Listener<String>() {

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

                            if (responstatus.equalsIgnoreCase("true")) {

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, responsemessage, Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
                                        startActivity(intent);

                                    }
                                });
                                snackbar.setActionTextColor(Color.RED);
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }else   {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Data from Server.Please try some time later..!", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Home.class);
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

                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
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
                    Log.v("CID Value : ",cid);
                    Log.v("userID Value : ",email);
                    Log.v("username Value : ",username);
                    params.put("userID", cid);
                    params.put("email", email);
                    params.put("userName", username);
                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            sendEmail.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(sendEmail);

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
            pDialog = new ProgressDialog(Influencer_UserValidation.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String check_email = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.user_details_url);
            StringRequest checkEmail = new StringRequest(Request.Method.POST,check_email , new Response.Listener<String>() {

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

                            if (responstatus.equalsIgnoreCase("true")) {

                                responseObj.getJSONArray("data");
                                JSONArray obj1 = responseObj.getJSONArray("data");

                                for (int i = 0; i < obj1.length(); i++) {
                                    try {
                                        JSONObject resobj = obj1.getJSONObject(i);

                                        cid = resobj.getString("cid");
                                        username = resobj.getString("name");
                                        email = resobj.getString("email");
                                        mobile_no = resobj.getString("mobile_no");
                                        mobileValidation = resobj.getString("mobileValidation");
                                        mailValidation = resobj.getString("mailValidation");

                                        Log.v("CID Value : ",cid);
                                        Log.v("username Value : ",username);
                                        Log.v("email Value : ",email);
                                        Log.v("mobile_no Value : ",mobile_no);
                                        Log.v("mobileVal Value : ",mobileValidation);
                                        Log.v("mailVal Value : ",mailValidation);

                                         mobileno.setText(mobile_no);
                                         emailid.setText(email);

                                        if (mobileValidation.equalsIgnoreCase("validated")&& mailValidation.equalsIgnoreCase("notValidated")) {
                                            Log.v("Result : ","1st Working");
                                            mobile_correct_icon.setVisibility(View.VISIBLE);
                                            mobile_wrong_icon.setVisibility(View.INVISIBLE);
                                            email_correct_icon.setVisibility(View.INVISIBLE);
                                            email_wrong_icon.setVisibility(View.VISIBLE);
                                            mo_verifynow_button.setVisibility(View.INVISIBLE);
                                            em_verifynow_button.setVisibility(View.VISIBLE);
                                        }else  if (mobileValidation.equalsIgnoreCase("notValidated")&& mailValidation.equalsIgnoreCase("validated")) {
                                            Log.v("Result : ","2nd Working");
                                            mobile_correct_icon.setVisibility(View.INVISIBLE);
                                            mobile_wrong_icon.setVisibility(View.VISIBLE);
                                            email_correct_icon.setVisibility(View.VISIBLE);
                                            email_wrong_icon.setVisibility(View.INVISIBLE);
                                            mo_verifynow_button.setVisibility(View.VISIBLE);
                                            em_verifynow_button.setVisibility(View.INVISIBLE);
                                        }else  if (mobileValidation.equalsIgnoreCase("notValidated")&& mailValidation.equalsIgnoreCase("notValidated")) {
                                            Log.v("Result : ","3rd Working");
                                            mobile_correct_icon.setVisibility(View.INVISIBLE);
                                            mobile_wrong_icon.setVisibility(View.VISIBLE);
                                            email_correct_icon.setVisibility(View.INVISIBLE);
                                            email_wrong_icon.setVisibility(View.VISIBLE);
                                            mo_verifynow_button.setVisibility(View.VISIBLE);
                                            em_verifynow_button.setVisibility(View.VISIBLE);
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
                                            Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
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
                                    Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
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

    private void SendOTP() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_UserValidation.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            final String send_otp = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.sendotp_url);
            StringRequest strReq = new StringRequest(Request.Method.POST, send_otp, new Response.Listener<String>() {
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

                            JSONObject resobj = responseObj.getJSONObject("data");
                            message = resobj.getString("message");
                            type = resobj.getString("type");
                            Log.v("message value :", message);
                            Log.v("type value :", type);

                            if (type.equalsIgnoreCase("success")) {
                                verifyOtp();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                                Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
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
                    params.put("mobile", mobile_no);
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

    /**
     * sending the OTP to server and activating the user
     */
    private void verifyOtp() {

        final Dialog dialog = new Dialog(Influencer_UserValidation.this);
        dialog.setContentView(R.layout.quoteamount);
        dialog.setCancelable(false);
        dialog.setTitle("OTP Verification");

        otp = dialog.findViewById(R.id.otp);
        okbutton = dialog.findViewById(R.id.okbutton);
        resend_button = dialog.findViewById(R.id.resend_button);

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg = true;
                if (isInternetPresent) {
                    String otpvalue = otp.getText().toString().trim();
                    Log.v("GETTING OTP VALUE:",otpvalue);
                    if (!otpvalue.isEmpty()) {
                        verifyOtp(otpvalue);
                    } else {
                        //Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please enter the OTP", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
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
        resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOTP();
            }
        });
        dialog.show();
    }

    private void verifyOtp(final String otp) {
        if(isInternetPresent){
            pDialog = new ProgressDialog(Influencer_UserValidation.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String verify_otp = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.sendotp_url);
            StringRequest strReq = new StringRequest(Request.Method.POST, verify_otp, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();
                    if (response != null && !response.isEmpty()){
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
                                Log.v("Result : ","working");
                                Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
                                startActivity(intent);

                            }else if (type.equalsIgnoreCase("error")){
                                Log.v("Result : ","not working");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(Influencer_UserValidation.this).create();
                        alertDialog.setMessage("No data from server. Try again later.");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        });
                        alertDialog.show();
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
                                Intent intent = new Intent(Influencer_UserValidation.this, Influencer_Login.class);
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
                    params.put("mobile", mobile_no);
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
    }
}