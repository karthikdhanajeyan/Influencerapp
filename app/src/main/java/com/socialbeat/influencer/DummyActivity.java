package com.socialbeat.influencer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Objects;

public class DummyActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    String cid,campid,campname,socialmedia,facebookvalue,instagramvalue,gavalue,twittervalue,youtubevalue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummyactivity);

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            campid = extras.getString("campid");
            campname = extras.getString("campname");
            socialmedia = extras.getString("socialmedia");
        }else {
        Log.v("Error : ","No Value in Bundle");
        }

        SharedPreferences prfsnew = getSharedPreferences("SM_SCREEN_VALUE", Context.MODE_PRIVATE);
        facebookvalue = prfsnew.getString("valueoffacefook", "");
        instagramvalue = prfsnew.getString("valueofinstagram", "");
        gavalue = prfsnew.getString("valueofga", "");
        twittervalue = prfsnew.getString("valueoftwitter", "");
        youtubevalue = prfsnew.getString("valueofyoutube", "");

        if (socialmedia.equalsIgnoreCase("facebook")){

            if(facebookvalue.equalsIgnoreCase("true")){
                Intent intent = new Intent(DummyActivity.this, FacebookPostActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "facebook");
                intent.putExtras(bund);
                startActivity(intent);
            }else if(facebookvalue.equalsIgnoreCase("false")){
                Intent intent = new Intent(DummyActivity.this, SMDataReportActivity.class);
                Bundle bund = new Bundle();
                bund.putString("campid", campid);
                bund.putString("campname", campname);
                bund.putString("socialmedia", "facebook");
                intent.putExtras(bund);
                startActivity(intent);
            }
        }
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