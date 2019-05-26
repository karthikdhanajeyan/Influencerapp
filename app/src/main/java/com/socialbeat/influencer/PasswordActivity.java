package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordActivity extends AppCompatActivity {
    private static String TAG = PasswordActivity.class.getSimpleName();

    Button login;
    TextView forgotpassword,password,terms;
    EditText emails;
    boolean flg = true;
    String newpassword,email;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    final Context context = this;
    private CoordinatorLayout coordinatorLayout;

    public static final String LOGIN_NAME = "LoginFile";
    SharedPreferences.Editor editor;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private String cid,status,nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_screen);
        login = findViewById(R.id.login_button);
        forgotpassword = findViewById(R.id.forgotpassword);
        terms = findViewById(R.id.terms);
        password = findViewById(R.id.password);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font/headfont.ttf");
        password.setTypeface(myFont);
        Spanned sp = Html.fromHtml("Bterms.setText(sp);y Signing with us, you agree to the Influencer " +
                "<a href=\"http://www.influencer.in/terms-and-conditions/\"> Terms &amp; Conditions.</a>" );

        terms.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface myFont6 = Typeface.createFromAsset(getAssets(), "font/gothic.ttf");
        terms.setTypeface(myFont6);
        forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        // get the Intent that started this Activity
        Intent in = getIntent();
        // get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        // getting data from bundle
        email = b.getString("email");
        cid = b.getString("cid");


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in =new Intent(PasswordActivity.this,Forgetpassword.class);
//                startActivity(in);
            }
        });

        //onclick method for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg = true;
                //getting value from database
                newpassword = password.getText().toString();

                if ((TextUtils.isEmpty(newpassword))) {
                    flg = false;
                    password.setError("Password field is empty");
                    return;
                } else if (!isValidPassword(newpassword)) {
                    flg = false;
                    password.setError("Minimum required value is 6");
                    return;
                }

                if (flg) {
                    MyApplication.getInstance().trackEvent("User Login Button Clicked Event", "OnClick", "Track Login Event");
                    loginFunction();
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



    private void loginFunction() {
        pDialog = new ProgressDialog(PasswordActivity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        String LOGIN_URL = "https://www.influencer.in/API/v6/api_v6.php/verifyPassword?email="+email+"&password="+newpassword ;
        System.out.println("login url : "+LOGIN_URL);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response.toString());
                hidePDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (response != null) {

                        String responstatus = object.getString("success").toString();
                        Log.d("response status : ", responstatus);
                        String responsemessage = object.getString("message").toString();
                        Log.d("response message : ", responsemessage);

                        JSONObject resobj = object.getJSONObject("data");

                        cid = resobj.getString("cid");
                        status = resobj.getString("status");
                        nextPage = resobj.getString("nextPage");
//
                        Log.v(" Cid value :", cid);
                        Log.v("status value :", status);
                        Log.v("NextPage value :", nextPage);


                        SharedPreferences preferences = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("valueofcid",cid);
                        editor.apply();

                        if (nextPage.equalsIgnoreCase("profile")) {
                            Log.v("Result : ", "profile working");
                            //Toast.makeText(getApplicationContext(), nextPage, Toast.LENGTH_LONG).show();
                            Intent in =new Intent(PasswordActivity.this,MyProfileNew.class);
                            startActivity(in);

                        } else if (nextPage.equalsIgnoreCase("foi")) {
                            Log.v("Result : ", "FOI working");
                            //Toast.makeText(getApplicationContext(), nextPage, Toast.LENGTH_LONG).show();
//                            Intent in =new Intent(PasswordActivity.this,PasswordActivity.class);
//                            startActivity(in);

                        } else if (nextPage.equalsIgnoreCase("sm")) {
                            Log.v("Result : ", "Social Media working");
                            //Toast.makeText(getApplicationContext(), nextPage, Toast.LENGTH_LONG).show();
//                            Intent in =new Intent(PasswordActivity.this,ValidateActivity.class);
//                            startActivity(in);

                    } else if (nextPage.equalsIgnoreCase("homepage")) {
                            Log.v("Result : ", "homepage working");
                            //Toast.makeText(getApplicationContext(), nextPage, Toast.LENGTH_LONG).show();
                            Intent in =new Intent(PasswordActivity.this,NewHomeActivity.class);
                            startActivity(in);
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
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Login Screen");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    // validating password
    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }
//    // validating email id
//    private boolean isValidEmailid1(String emailid1) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(emailid1);
//        return matcher.matches();
//    }

}