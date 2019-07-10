package com.socialbeat.influencer;

import android.content.Intent;
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

    Button connectfacebook,connecttwitter,connectinstagram,connectblog,connectyoutube;

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
//        connectinstagram.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getApplicationContext(),"Please check our website to connect your Social Media Authentication",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(SocialMediaAuthentication.this, InstagramAuthentication.class);
//                startActivity(intent);
//            }
//        });
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
