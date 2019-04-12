package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
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
    String campid,campname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newreport);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Socialmedia Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);
        instagram = findViewById(R.id.instagram);
        blogger = findViewById(R.id.blogger);
        youtube = findViewById(R.id.youtube);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//
//            campid = extras.getString("campid");
//            campname = extras.getString("campname");
//            Log.v("SM CCampid : ",campid);
//            Log.v("SM CCampnme: ",campname);
//        }else {
//
//        }
        SharedPreferences prefernce1 = getSharedPreferences("ANALYTICS_NEW_REPORT", Context.MODE_PRIVATE);
        campid = prefernce1.getString("campid", "");
        campname = prefernce1.getString("campname", "");
        Log.v("SMReport CCampid : ", campid);
        Log.v("SMReport CCampnme: ", campname);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, FacbookReportActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                Log.v("FSMReport CCampid : ",campid);
                Log.v("FSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, TwitterReportActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                Log.v("TSMReport CCampid : ",campid);
                Log.v("TSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, InstagramReportActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                Log.v("ISMReport CCampid : ",campid);
                Log.v("ISMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        blogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, BloggerReportActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                Log.v("BSMReport CCampid : ",campid);
                Log.v("BSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialMediaReport.this, YoutubeReportActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                Log.v("YSMReport CCampid : ",campid);
                Log.v("YSMReport CCampname : ",campname);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });

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