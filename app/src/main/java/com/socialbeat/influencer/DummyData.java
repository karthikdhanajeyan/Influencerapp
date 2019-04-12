package com.socialbeat.influencer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import in.nashapp.androidsummernote.Summernote;

public class DummyData extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    final Context context = this;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Summernote summernote;
    Button showtext,show_file;
    TextView contentstatus,contentshared,view_conversation;
    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyticsreport);

//        ActionBar bar = getSupportActionBar();
//        assert bar != null;
//        bar.setDisplayHomeAsUpEnabled(true);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Analtyics");

//        Fragment frag = new AnalyticsReportFragment();
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.frame,frag,"Test Fragment");
//        transaction.commit();

    }


}