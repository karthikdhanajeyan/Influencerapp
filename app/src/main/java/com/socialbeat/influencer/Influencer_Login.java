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
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Influencer_Login extends AppCompatActivity {
    private static String TAG = Influencer_Login.class.getSimpleName();

    Button next;
    TextView register,terms;
    EditText emails,passwords;
    boolean flg = true;
    String email,password,token;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    final Context context = this;
    private CoordinatorLayout coordinatorLayout;

    SharedPreferences.Editor editor;
    private String cid,status,nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.influencer_login);
        next = findViewById(R.id.next_button);
        register = findViewById(R.id.register);
        terms = findViewById(R.id.terms);
        emails = findViewById(R.id.emailid);
        passwords = findViewById(R.id.password);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        emails.setTypeface(myFont);
        Spanned sp = Html.fromHtml("Bterms.setText(sp);y Signing with us, you agree to the Influencer " +
                "<a href=\"http://www.influencer.in/terms-and-conditions/\"> Terms &amp; Conditions.</a>" );

        terms.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface myFont6 = Typeface.createFromAsset(getAssets(), "font/gothic.ttf");
        terms.setTypeface(myFont6);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(Influencer_Login.this, Influencer_Registeration.class);
                startActivity(in);
            }
        });

        //onclick method for login button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg = true;
                //getting value from database
                email = emails.getText().toString();

                if ((TextUtils.isEmpty(email))) {
                    flg = false;
                    emails.setError("Email id field is empty");
                    return;
                } else if (!isValidEmailid1(email)) {
                    flg = false;
                    emails.setError("Enter Valid Email id");
                    return;
                }
                password = passwords.getText().toString();
                if ((TextUtils.isEmpty(password))) {
                    flg = false;
                    passwords.setError("Password field is empty");
                    return;
                } else if (!isValidPassword(password)) {
                    flg = false;
                    passwords.setError("Minimum required value is 6");
                    return;
                }

                if (flg) {
                    LoginFunction();
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
            }
        });
    }

    private void LoginFunction() {
        if (isInternetPresent) {
            pDialog = new ProgressDialog(Influencer_Login.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            String check_password = getResources().getString(R.string.base_url_v6) + getResources().getString(R.string.verifypassword_url);
            StringRequest checkPassword = new StringRequest(Request.Method.POST,check_password , new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    hidePDialog();
                    if (response != null && !response.isEmpty()){
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
                                Log.v("Token value :",token);
                            }

                            if (responstatus.equalsIgnoreCase("true")) {

                                JSONObject resobj = responseObj.getJSONObject("data");
                                cid = resobj.getString("cid");
                                status = resobj.getString("status");
                                nextPage = resobj.getString("nextPage");

                                Log.v(" Cid value :", cid);
                                Log.v("status value :", status);
                                Log.v("NextPage value :", nextPage);

                                SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("valueofcid",cid);
                                editor.apply();


                                if (status.equalsIgnoreCase("social media connection incomplete")) {
                                    if (nextPage.equalsIgnoreCase("sm")) {
                                        Log.v("Result : ", "SM working");
                                        Intent in =new Intent(Influencer_Login.this,SocialMediaAuthentication.class);
                                        startActivity(in);
                                    }
                                } else if (status.equalsIgnoreCase("Field of interest incomplete")) {
                                    if (nextPage.equalsIgnoreCase("foi")) {
                                        Log.v("Result : ", "FOI working");
                                        Intent in =new Intent(Influencer_Login.this, Influencer_FOI.class);
                                        startActivity(in);
                                    }
                                } else if (status.equalsIgnoreCase("profile completed")) {
                                    if (nextPage.equalsIgnoreCase("homepage")) {
                                        Log.v("Result : ", "Homepage working");
                                        Intent in =new Intent(Influencer_Login.this,NewHomeActivity.class);
                                        startActivity(in);
                                    }
                                } else if (status.equalsIgnoreCase("profile incomplete")) {
                                    if (nextPage.equalsIgnoreCase("profile")) {
                                        Log.v("Result : ", "profile working");
                                        Intent in =new Intent(Influencer_Login.this, Influencer_MyProfile.class);
                                        startActivity(in);
                                    }
                                } else if (status.equalsIgnoreCase("mobile and mail not validated")) {
                                    if (nextPage.equalsIgnoreCase("verify_account")) {
                                        Log.v("Result : ", "Validation Activity");
                                        Intent in = new Intent(Influencer_Login.this, Influencer_UserValidation.class);
                                        startActivity(in);
                                    }
                                } else if (status.equalsIgnoreCase("mobile not validated")) {
                                    if (nextPage.equalsIgnoreCase("verify_account")) {
                                        Log.v("Result : ", "Validation Activity");
                                        Intent in = new Intent(Influencer_Login.this, Influencer_UserValidation.class);
                                        startActivity(in);
                                    }
                                } else if (status.equalsIgnoreCase("mail not validated")) {
                                    if (nextPage.equalsIgnoreCase("verify_account")) {
                                        Log.v("Result : ", "Validation Activity");
                                        Intent in = new Intent(Influencer_Login.this, Influencer_UserValidation.class);
                                        startActivity(in);
                                    }
                                }
                            }else
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Login.this).create();
                                alertDialog.setMessage("Invalid Password");
                                alertDialog.setCancelable(false);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        emails.setText("");
                                        passwords.setText("");

                                    }
                                });
                                alertDialog.show();
                            }
                        } catch(JSONException e){
                            Log.e(TAG, "Error Value : " + e.getMessage());

                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No data from server. Please try again later.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Influencer_Login.this, Influencer_Home.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.setActionTextColor(Color.RED);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();
                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(Influencer_Login.this).create();
                        alertDialog.setMessage("No data from server. Try again later.");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Influencer_Login.this, Influencer_Home.class);
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
                                Intent intent = new Intent(Influencer_Login.this, Influencer_Login.class);
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
                    params.put("email", email);
                    params.put("password",password);

                    return params;
                }
            };

            int socketTimeout = 60000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            checkPassword.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(checkPassword);
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
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    // validating email id
    private boolean isValidEmailid1(String emailid1) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailid1);
        return matcher.matches();
    }
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }

}