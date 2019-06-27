package com.socialbeat.influencer;

/**
 * Created by SocialBeat on 21-08-2017.
 */
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String cid,cname,email,cmobileno,ccity,cuserimage;
    ImageView user_icon;
    TextView user_name, user_email,user_mobile;
    String devicenamenew,deviceserialnew,devicemodelnew,deviceGCMServerkeynew,appversionnew;
    String username1, emailid1, playstoreversion, mobileno, city, blog, blogtraffic, twitterhandle, message, profileprogress, mozrank,twitter_followers, instagram, instagram_followers, overallreach, foi, userimage, overallscore, response;
    Boolean isInternetPresent = false;
    SharedPreferences.Editor editor,editor1,editor2,editor3,editor4;
    ConnectionDetector cd;
    ProgressDialog pdialog;
    public static final String LOGIN_NAME = "LoginFile";
    private static final String PREFS_NAME = "NewHomeActivity";
    protected DrawerLayout drawerLayout;
    Context context=NewHomeActivity.this;

    public static final String KEY_DEVICENAME = "device_name";
    public static final String KEY_DEVICEMODEL = "device_model";
    public static final String KEY_DEVICESERIAL = "device_serial";
    public static final String KEY_APPVERSION = "app_version";
    public static final String KEY_DEVICEGCMSERVERKEY = "deviceGCMServerkey";
    public static final String KEY_CID = "cid";

    private static String TAG = NewHomeActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
//
//            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
//
//            ShortcutInfo webShortcut = new ShortcutInfo.Builder(this, "shortcut_web")
//                    .setShortLabel("Influencer Blog")
//                    .setLongLabel("Open Influencer Blog")
//                    .setIcon(Icon.createWithResource(this, R.mipmap.blogger))
//                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.influencer.in/blog/")))
//                    .build();
//
//            assert shortcutManager != null;
//            shortcutManager.setDynamicShortcuts(Collections.singletonList(webShortcut));
//
//            ShortcutInfo dynamicShortcut1 = new ShortcutInfo.Builder(this, "shortcut_dynamic")
//                    .setShortLabel("My Campaigns")
//                    .setLongLabel("My Campaigns")
//                    .setIcon(Icon.createWithResource(this, R.mipmap.bulb))
//                    .setIntents(
//                            new Intent[]{
//                                    new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MyCampaignsFragment.class),
//                                    //new Intent(String.valueOf(MyProfileDummy.class))
//                            })
//                    .build();
//            shortcutManager.setDynamicShortcuts(Arrays.asList(webShortcut, dynamicShortcut1));
//        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        SharedPreferences prfs = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        cid = prfs.getString("valueofcid", "");
        cname = prfs.getString("username1", "");
        email = prfs.getString("emailid1", "");
        cmobileno = prfs.getString("mobileno1", "");
        ccity = prfs.getString("city1", "");
        cuserimage = prfs.getString("userimage1", "");
        playstoreversion = prfs.getString("playstoreversion1", "");

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean firstStart = settings.getBoolean("firstStart", true);
        //profileFunction();
        if (firstStart) {
            //display your Message here
            Log.v("DeviceStatus : ","First Time");
            DeviceDetails();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        if (cid.length() != 0) {
            if (isInternetPresent) {
                profileFunction();
               // Toast.makeText(getApplicationContext(), "Internet Connection is Working!", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(getApplicationContext(), "User Could not login properly,Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewHomeActivity.this, Influencer_Login.class);
            startActivity(intent);
        }
        @SuppressLint("CutPasteId") android.support.design.widget.NavigationView navigationView1 = findViewById(R.id.nav_view);
        View header = navigationView1.getHeaderView(0);
        user_name = header.findViewById(R.id.header_user_name);
        user_email = header.findViewById(R.id.header_user_email);
        user_mobile = header.findViewById(R.id.header_user_mobile);
        user_icon = header.findViewById(R.id.header_user_icon);

        Glide.with(getApplicationContext()).load(cuserimage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(user_icon);
        user_name.setText(cname);
        user_email.setText(email);
        user_mobile.setText(cmobileno);
    }

    @SuppressLint("HardwareIds")
    private void DeviceDetails() {
        devicenamenew = Build.BRAND;
        devicemodelnew = Build.MODEL;
        deviceserialnew = Build.SERIAL;
        Log.v("DeviceDetails : ","Working");
        if(deviceserialnew==null || deviceserialnew.length()==0) deviceserialnew = ""+System.currentTimeMillis();
        PackageInfo pInfonew = null;
        try {
            pInfonew = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pInfonew != null;
        appversionnew = pInfonew.versionName;
        deviceGCMServerkeynew =  PreferenceManager.getPushCatID(NewHomeActivity.this);

        System.out.println("Customer ID :  "+cid);
        System.out.println("Device Name :  "+devicenamenew);
        System.out.println("Device Model :  "+devicemodelnew);
        System.out.println("Device Serial :  "+deviceserialnew);
        System.out.println("App Version : "+appversionnew);
        System.out.println("Device Service Key :  "+deviceGCMServerkeynew);

        String DEVICE_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.device_details_url);
        Log.v("URL",DEVICE_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DEVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                String message;
                try {
                    org.json.JSONObject json = new org.json.JSONObject(success);
                    success = json.getString("success");
                    message = json.getString("message");
                    System.out.println("success "+success);
                    System.out.println("message "+message);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewHomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_CID, cid);
                params.put(KEY_DEVICENAME, devicenamenew);
                params.put(KEY_DEVICEMODEL, devicemodelnew);
                params.put(KEY_DEVICESERIAL, deviceserialnew);
                params.put(KEY_APPVERSION, appversionnew);
                params.put(KEY_DEVICEGCMSERVERKEY, deviceGCMServerkeynew);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void profileFunction() {
        pdialog = new ProgressDialog(NewHomeActivity.this);
        pdialog.setMessage("Loading...");
        pdialog.setCancelable(false);
        pdialog.show();
        String PROFILE_URL = getResources().getString(R.string.base_url)+getResources().getString(R.string.profile_url);
        Log.v("PROFILE_URL : ",PROFILE_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String success) {
                try {
                    JSONObject json = new JSONObject(success);
                    response = json.getString("success");
                    message = json.getString("message");
                    profileprogress = json.getString("profileProgress");
                    cid = json.getString("cid");
                    username1 = json.getString("name");
                    emailid1 = json.getString("emailid");
                    mobileno = json.getString("mobileno");
                    twitterhandle = json.getString("twitter");
                    twitter_followers = json.getString("twitterFollowers");
                    blog = json.getString("bloglink");
                    blogtraffic = json.getString("blogTraffic");
                    instagram = json.getString("instagram");
                    instagram_followers = json.getString("instaFollowers");
                    foi = json.getString("foi");
                    city = json.getString("city");
                    mozrank = json.getString("mozrank");
                    overallreach = json.getString("overallreach");
                    overallscore = json.getString("overallscore");
                    userimage = json.getString("userimage");

                    Glide.with(getApplicationContext()).load(userimage)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(user_icon);
                    user_name.setText(username1);
                    user_email.setText(emailid1);
                    user_mobile.setText(mobileno);

                } catch (JSONException e) {
                    e.printStackTrace();
                 }
                pdialog.dismiss();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewHomeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_CID, cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Influencer_Livecamp_List(), "Live Campaigns");
        adapter.addFragment(new Influencer_Pastcamp_List(), "Past Campaigns");
        // set the default tab to the second tab
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
      //  noinspection SimplifiableIfStatement
//        if (id == R.id.notification) {
//            Intent intent = new Intent(NewHomeActivity.this, NotificationManager.class);
//            startActivity(intent);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(NewHomeActivity.this, NewHomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mycampaigns) {
            //Toast.makeText(NewHomeActivity.this, "MY CAMPAIGNS", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewHomeActivity.this, MyCampaigns.class);
            startActivity(intent);
        }  else if (id == R.id.nav_ourblog) {
            Intent intent = new Intent(NewHomeActivity.this, OurBlogPage.class);
            startActivity(intent);
         } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(NewHomeActivity.this, Influencer_UserSettings.class);
            startActivity(intent);
        } else if (id == R.id.nav_contactus) {
            Intent intent = new Intent(NewHomeActivity.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
           logout();
       }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout() {
        SharedPreferences prefernce = getSharedPreferences("CID_VALUE", Context.MODE_PRIVATE);
        editor = prefernce.edit();
        editor.clear();
        editor.apply();
        SharedPreferences prefernce1 = getSharedPreferences(LOGIN_NAME, MODE_PRIVATE);
        editor1 = prefernce1.edit();
        editor1.clear();
        editor1.apply();
        SharedPreferences prefernce2 = getSharedPreferences("USER_DEVICE_VALUE", Context.MODE_PRIVATE);
        editor2 = prefernce2.edit();
        editor2.clear();
        editor2.apply();
        SharedPreferences prefernce3 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor3 = prefernce3.edit();
        editor3.clear();
        editor3.apply();
        SharedPreferences prefernce4 = getSharedPreferences("USER_ADD_VALUE", MODE_PRIVATE);
        editor4 = prefernce4.edit();
        editor4.clear();
        editor4.apply();

        Intent intent = new Intent(NewHomeActivity.this, Influencer_Home.class);
        startActivity(intent);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
