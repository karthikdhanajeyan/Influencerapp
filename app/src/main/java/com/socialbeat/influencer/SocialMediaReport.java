package com.socialbeat.influencer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import in.nashapp.androidsummernote.Summernote;

public class SocialMediaReport extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    LinearLayout facebook,twitter,instagram,blogger,youtube;
    String cid,campid,campname,Facebook,Instagram,GA,Twitter,Youtube;
    private static final String TAG = SocialMediaReport.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newreport);

//        ActionBar bar = getSupportActionBar();
//        assert bar != null;
//        bar.setDisplayHomeAsUpEnabled(true);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Socialmedia Reports");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        instagram = findViewById(R.id.instagram);
        blogger = findViewById(R.id.blogger);
        youtube = findViewById(R.id.youtube);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            campid = extras.getString("campid");
            campname = extras.getString("campname");
        }
        SharedPreferences prefernce1 = getSharedPreferences("ANALYTICS_NEW_REPORT", Context.MODE_PRIVATE);
        campid = prefernce1.getString("campid", "");
        campname = prefernce1.getString("campname", "");
        Log.v("SMReport CCampid : ", campid);
        Log.v("SMReport CCampnme: ", campname);

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        //cid="1";

        ScreenVisible();


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, DummyActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "facebook");
                Log.v("socialmedia : ","facebook");
                Log.v("FSMReport CCampid : ",campid);
                Log.v("FSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, DummyActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "twitter");
                Log.v("socialmedia : ","twitter");
                Log.v("TSMReport CCampid : ",campid);
                Log.v("TSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, DummyActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "instagram");
                Log.v("socialmedia : ","instagram");
                Log.v("ISMReport CCampid : ",campid);
                Log.v("ISMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        blogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, DummyActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "blogger");
                Log.v("socialmedia : ","blogger");
                Log.v("BSMReport CCampid : ",campid);
                Log.v("BSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, DummyActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "youtube");
                Log.v("socialmedia : ","youtube");
                Log.v("YSMReport CCampid : ",campid);
                Log.v("YSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });

    }

    private void ScreenVisible() {

        String SCREEN_URL = "http://stage.influencer.in/API/v6/api_v6.php/SMConnectionDetailsSample?cid="+cid;
        System.out.println("conversation url : "+SCREEN_URL);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, SCREEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with response string
                Log.d(TAG, response);
                //hidePDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (response != null) {

                        String responstatus = object.getString("success");
                        Log.d("response status : ", responstatus);
                        String responsemessage = object.getString("message");
                        Log.d("response message : ", responsemessage);

                        if (responstatus == "true") {

                            String data = object.getString("data");
                            JSONObject obj = new JSONObject(data);

                            Facebook = obj.getString("Facebook");
                            Instagram = obj.getString("Instagram");
                            GA = obj.getString("GA");
                            Twitter = obj.getString("Twitter");
                            Youtube = obj.getString("Youtube");

                            SharedPreferences preferences = getSharedPreferences("SM_SCREEN_VALUE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("valueoffacefook",Facebook);
                            editor.putString("valueofinstagram",Instagram);
                            editor.putString("valueofga",GA);
                            editor.putString("valueoftwitter",Twitter);
                            editor.putString("valueofyoutube",Youtube);
                            editor.apply();

                            Log.v(" Facebook value :", Facebook);
                            Log.v(" Instagram value :", Instagram);
                            Log.v(" GA value :", GA);
                            Log.v(" Twitter value :", Twitter);
                            Log.v(" Youtube value :", Youtube);

                        } else {
                            Log.d("success : ", "False");
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No Data", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Click Here", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(SocialMediaReport.this, SocialMediaReport.class);
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
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
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
}