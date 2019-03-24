package com.socialbeat.influencer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class Socialmediahandle extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_dummy);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SocialMedia Handle");

        add_button  = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Socialmediahandle.this, Socialmediadetails.class);
                startActivity(intent);

            }
        });

    }
}