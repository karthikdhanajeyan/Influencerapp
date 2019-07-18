package com.socialbeat.influencer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by SocialBeat on 10-11-2017.
 */

public class SocialMediaAuthentication extends AppCompatActivity {

    Button connectfacebook,connecttwitter,connectga,connectinstagram,connectblog,connectyoutube;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socialmediaauthentication);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SocialMedia Authentication");

        connectfacebook = findViewById(R.id.connectfacebook);
        connecttwitter = findViewById(R.id.connecttwitter);
        connectga = findViewById(R.id.connectga);
 //       connectinstagram = findViewById(R.id.connectinstagram);
//        connectblog = findViewById(R.id.connectblog);
//        connectyoutube = findViewById(R.id.connectyoutube);

        connectfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SocialMediaAuthentication.this, FacebookAuthentication.class);
                startActivity(intent);
            }
        });
        connecttwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SocialMediaAuthentication.this, TwitterAuthentication.class);
                startActivity(intent);
            }
        });
        connectinstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SocialMediaAuthentication.this, InstagramAuthentication.class);
                startActivity(intent);
            }
        });
        connectga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(SocialMediaAuthentication.this, GAAuthentication.class);
//                startActivity(intent);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signin/oauth/oauthchooseaccount?client_id=136832612678-3mjjhnccd805if2dtds9h6770or9tgaj.apps.googleusercontent.com&as=US1pBrL1MshH8qz34Z987w&destination=https%3A%2F%2Fwww.influencer.in&approval_state=!ChRrY3pCNHZ1V0MteV9rcXgxWlRiLRIfY3llZ3pIanBGNTRkOEhuU1JuY2dubXAtdVQta3Z4WQ%E2%88%99AJDr988AAAAAXS70T-Jr7n08EBhFHmASNDXn_fiCJM3r&oauthriskyscope=1&xsrfsig=ChkAeAh8TxUCwHqio7KAjCJRKyYmi4X0gB_jEg5hcHByb3ZhbF9zdGF0ZRILZGVzdGluYXRpb24SBXNvYWN1Eg9vYXV0aHJpc2t5c2NvcGU&flowName=GeneralOAuthFlow"));
                startActivity(browserIntent);
            }
        });

//        connectblog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
//            }
//        });
//        connectyoutube.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
//            }
//        });
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
