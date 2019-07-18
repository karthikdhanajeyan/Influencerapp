package com.socialbeat.influencer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class GAAuthentication extends AppCompatActivity {

    private static final String TAG = Influencer_Livecamp_List.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    TextView campheading;
    Context context;
    WebView browser;
    private ProgressDialog pDialog;
    @Nullable
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Google Analytics ");

        cd = new ConnectionDetector(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            browser = (WebView)findViewById(R.id.webview);
            browser.setWebViewClient(new MyBrowser());
            browser.loadUrl("https://accounts.google.com/signin/oauth/oauthchooseaccount?client_id=136832612678-3mjjhnccd805if2dtds9h6770or9tgaj.apps.googleusercontent.com&as=US1pBrL1MshH8qz34Z987w&destination=https%3A%2F%2Fwww.influencer.in&approval_state=!ChRrY3pCNHZ1V0MteV9rcXgxWlRiLRIfY3llZ3pIanBGNTRkOEhuU1JuY2dubXAtdVQta3Z4WQ%E2%88%99AJDr988AAAAAXS70T-Jr7n08EBhFHmASNDXn_fiCJM3r&oauthriskyscope=1&xsrfsig=ChkAeAh8TxUCwHqio7KAjCJRKyYmi4X0gB_jEg5hcHByb3ZhbF9zdGF0ZRILZGVzdGluYXRpb24SBXNvYWN1Eg9vYXV0aHJpc2t5c2NvcGU&flowName=GeneralOAuthFlow");

        }
        else {
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
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        //   hidePDialog();
    }

    private class MyBrowser extends WebViewClient  //If you click on any link inside the webpage of the WebView , that page will not be loaded inside your WebView. In order to do that you need to extend your class from WebViewClient by using the below function.
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        // code here to show dialog
         super.onBackPressed();  // optional depending on your needs
//        Intent intent  = new Intent(this, NewHomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Tracking the screen view
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                Intent intent  = new Intent(this, NewHomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                super.onResume();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}