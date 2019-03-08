package com.socialbeat.influencer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class AllCampDetailsPast extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button applynow,alreadyapplied,campaignclosed,submit;
    TextView campName,campShortNote,campCat,campLongNote,campGoal,campDos,campDont,campBacklink,
            campTag,campid,campApplyTill,campRewards,campRewardType,fixedamount,conditiontext,cancel,infotext,campEligibility,campDeliverables;
    ImageView campImg;
    EditText famtval;
    // Button applynow,alreadyapplied,campaignclosed,submit;
    boolean flg = true;
    String famt,cid,cdcampImg,cdcampName,cdcampShortNote,cdcampCat,cdcampLongNote,cdcampGoal,cdcampDos,cdcampDont,cdcampBacklink,
            cdcampTag,cdcampid,cdcampApplyTill,cdcampRewards,cdcampRewardType,cdfixedamount,TAG,campaignid,campaignname,cdcampEligibility,cdcampDeliverables;
    int first,second;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allcampdetails);
        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
//        if(cid.length()!=0){
//            setContentView(R.layout.allcampdetails);
//        }else{
//            // setContentView(R.layout.allcampdetails1);
//        }
        //bundle values from Campain Fragment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            cdcampImg = extras.getString("campImg");
            cdcampName = extras.getString("campName");
            cdcampShortNote = extras.getString("campShortNote");
            cdcampCat = extras.getString("campCat");
            cdcampLongNote = extras.getString("campLongNote");
            cdcampGoal = extras.getString("campGoal");
            cdcampDos = extras.getString("campDos");
            cdcampDont = extras.getString("campDont");
            cdcampBacklink = extras.getString("campBacklink");
            cdcampTag = extras.getString("campTag");
            cdcampid = extras.getString("campid");
            cdcampApplyTill = extras.getString("campApplyTill");
            cdcampRewards = extras.getString("campRewards");
            cdcampRewardType = extras.getString("campRewardType");
            cdfixedamount = extras.getString("fixedamount");
            cdcampEligibility = extras.getString("campEligibility");
            cdcampDeliverables = extras.getString("campDeliverables");
        }

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(cdcampName);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs1 = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs1.getString("valueofcid", "");

        // Displaying all values on the screen

        campName = (TextView) findViewById(R.id.campName);
        campShortNote = (TextView) findViewById(R.id.campShortNote);
        campCat = (TextView) findViewById(R.id.campCat);
        campLongNote = (TextView) findViewById(R.id.campLongNote);
        campGoal = (TextView) findViewById(R.id.campGoal);
        campDos = (TextView) findViewById(R.id.campDos);
        campDont = (TextView) findViewById(R.id.campDont);
        campBacklink = (TextView) findViewById(R.id.campBacklink);
        campTag = (TextView) findViewById(R.id.campTag);
        campid = (TextView) findViewById(R.id.campid);
        campApplyTill = (TextView) findViewById(R.id.campApplyTill);
        campRewards = (TextView) findViewById(R.id.campRewards);
        campRewardType = (TextView) findViewById(R.id.campRewardType);
        fixedamount = (TextView) findViewById(R.id.fixedamount);
        campEligibility = (TextView) findViewById(R.id.campEligibility);
        campDeliverables = (TextView) findViewById(R.id.campDeliverables);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        campImg = (ImageView) findViewById(R.id.campImg);
        campaignclosed = (Button) findViewById(R.id.campaignclosed);
        alreadyapplied = (Button) findViewById(R.id.alreadyapplied);
        applynow =(Button) findViewById(R.id.applynow);
        campImg.setImageResource(R.mipmap.influencerlistimg);
        new DownloadImageTask(campImg).execute(cdcampImg);
        campName.setText(cdcampName);
        Spanned sp = Html.fromHtml( cdcampShortNote );
        campShortNote.setText(sp);
        campShortNote.setMovementMethod(LinkMovementMethod.getInstance());
        campCat.setText(cdcampCat);

        Spanned sp1 = Html.fromHtml( cdcampLongNote );
        campLongNote.setText(sp1);
        campLongNote.setMovementMethod(LinkMovementMethod.getInstance());

        campGoal.setText(cdcampGoal);

        Spanned sp2 = Html.fromHtml( cdcampDos );
        campDos.setText(sp2);
        campDos.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sp3 = Html.fromHtml( cdcampDont );
        campDont.setText(sp3);
        campDont.setMovementMethod(LinkMovementMethod.getInstance());

        String link ="<a href="+cdcampBacklink+">"+ cdcampBacklink +" </a>" ;
        Spanned spd = Html.fromHtml( link );
        campBacklink.setText(spd);
        campBacklink.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sp4 = Html.fromHtml( cdcampTag );
        campTag.setText(sp4);
        campTag.setMovementMethod(LinkMovementMethod.getInstance());

        campid.setText(cdcampid);
        campApplyTill.setText(cdcampApplyTill);
        campRewards.setText(cdcampRewards);
        campRewardType.setText(cdcampRewardType);
        fixedamount.setText(cdfixedamount);

        Spanned sp5 = Html.fromHtml( cdcampEligibility );
        campEligibility.setText(sp5);
        campEligibility.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned sp6 = Html.fromHtml( cdcampDeliverables );
        campDeliverables.setText(sp6);
        campDeliverables.setMovementMethod(LinkMovementMethod.getInstance());

        System.out.println("cfixedamount value  :"+ cdfixedamount);
        if (cdfixedamount.length()>0) {
            fixedamount.setText("Maximum amount Rs : " + cdfixedamount);
        }else{
            fixedamount.setText("No Quote Amount Condition for this Campaign.");
        }

        campaignid =campid.getText().toString();
        campaignname =campName.getText().toString();

        campaignclosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllCampDetailsPast.this, "This Campaign is closed already.", Toast.LENGTH_LONG).show();
            }
        });
        alreadyapplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllCampDetailsPast.this, "You have already applied to this campaign", Toast.LENGTH_LONG).show();
            }
        });



    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                MyApplication.getInstance().trackException(e);
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        // super.onBackPressed();  // optional depending on your needs
        Intent intent  = new Intent(this, NewHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
        MyApplication.getInstance().trackScreenView("Pastcampaign Details Screen");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent  = new Intent(this, NewHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
