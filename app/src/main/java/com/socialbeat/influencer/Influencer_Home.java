package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Influencer_Home extends AppCompatActivity {

    String REG_ID,TAG;
    public static final String LOGIN_NAME="LoginFile";
    SharedPreferences.Editor editor;
    String open="haslogin";
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button register,login;
    SharedPreferences pref;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.influencer_home);

        MessageDigest md = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        SharedPreferences prefernce=getSharedPreferences(LOGIN_NAME,MODE_PRIVATE);
        editor =  prefernce.edit();

        register = findViewById(R.id.signup);
        login = findViewById(R.id.signin);
        cd = new ConnectionDetector(getApplicationContext());
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        pref = getPreferences(0);
        /**
         * Check Internet status button click event
         * */
        isInternetPresent = cd.isConnectingToInternet();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    editor.putBoolean(open, true);
                    editor.commit();
                    Intent in = new Intent(Influencer_Home.this, Influencer_Registeration.class);
                    startActivity(in);
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    editor.putBoolean(open, true);
                    editor.commit();
                    Intent in = new Intent(Influencer_Home.this, Influencer_Login.class);
                    startActivity(in);
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

        boolean haslogin=prefernce.getBoolean(open,false);
        if(haslogin){
            Intent i = new Intent(Influencer_Home.this,NewHomeActivity.class);
            startActivity(i);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
