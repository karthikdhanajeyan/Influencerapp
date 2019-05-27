package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class FBLazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>>data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public FBLazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.fbpagelist, null);


        TextView userid = vi.findViewById(R.id.userid);
        TextView name = vi.findViewById(R.id.name);
        TextView email = vi.findViewById(R.id.email);
        TextView imageURL = vi.findViewById(R.id.imageURL);
        TextView usertoken = vi.findViewById(R.id.profile_access_token);

        TextView pgname = vi.findViewById(R.id.pgname);
        TextView pgid = vi.findViewById(R.id.pgid);
        TextView pgtoken = vi.findViewById(R.id.pgtoken);
        TextView pgfancount = vi.findViewById(R.id.pgfancount);
        TextView link = vi.findViewById(R.id.link);
        Button select = vi.findViewById(R.id.select);

        //TextView about = vi.findViewById(R.id.about);
        TextView newlikecount = vi.findViewById(R.id.new_like_count);
        //TextView ratingcount = vi.findViewById(R.id.rating_count);
        TextView talkingaboutcount = vi.findViewById(R.id.talking_about_count);

        ImageView profile_image= vi.findViewById(R.id.profile_image); // thumb image

        HashMap<String, String> pagevalue = new HashMap<String, String>();
        pagevalue = data.get(position);
        // Setting all values in listview
        userid.setText(pagevalue.get(FacebookAuthentication.TAG_FID));
        name.setText(pagevalue.get(FacebookAuthentication.TAG_FNAME));
        email.setText(pagevalue.get(FacebookAuthentication.TAG_FEMAIL));
        imageURL.setText(pagevalue.get(FacebookAuthentication.TAG_FIMAGE));
        usertoken.setText(pagevalue.get(FacebookAuthentication.TAG_FUATOKEN));

        pgname.setText(pagevalue.get(FacebookAuthentication.TAG_PNAME));
        pgid.setText(pagevalue.get(FacebookAuthentication.TAG_PID));
        pgtoken.setText(pagevalue.get(FacebookAuthentication.TAG_FPATOKEN));
        pgfancount.setText(pagevalue.get(FacebookAuthentication.TAG_PFCOUNT));
        link.setText(pagevalue.get(FacebookAuthentication.TAG_PLINK));

        newlikecount.setText(pagevalue.get(FacebookAuthentication.TAG_PNLC));
        talkingaboutcount.setText(pagevalue.get(FacebookAuthentication.TAG_PTAC));

        imageLoader.DisplayImage(pagevalue.get(FacebookAuthentication.TAG_FPIMAGE), profile_image);
        final HashMap<String, String> finalpagevalue = pagevalue;
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Post ID  : ", finalpagevalue.get(FacebookAuthentication.TAG_PNAME));
                //Toast.makeText(activity,finalpagevalue.get(FacebookAuthentication.TAG_PNAME )+"  "+" page Selected", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = activity.getSharedPreferences("FB_DETAILS_LIST", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fid",finalpagevalue.get(FacebookAuthentication.TAG_FID));
                editor.putString("fname",finalpagevalue.get(FacebookAuthentication.TAG_FNAME));
                editor.putString("femail",finalpagevalue.get(FacebookAuthentication.TAG_FEMAIL));
                editor.putString("fimage",finalpagevalue.get(FacebookAuthentication.TAG_FIMAGE));
                editor.putString("fuatoken",finalpagevalue.get(FacebookAuthentication.TAG_FUATOKEN));

                editor.putString("pname",finalpagevalue.get(FacebookAuthentication.TAG_PNAME));
                editor.putString("pid",finalpagevalue.get(FacebookAuthentication.TAG_PID));
                editor.putString("fpatoken",finalpagevalue.get(FacebookAuthentication.TAG_FPATOKEN));
                editor.putString("pfcount",finalpagevalue.get(FacebookAuthentication.TAG_PFCOUNT));
                editor.putString("plink",finalpagevalue.get(FacebookAuthentication.TAG_PLINK));

                editor.putString("pnlc",finalpagevalue.get(FacebookAuthentication.TAG_PNLC));
                editor.putString("ptac",finalpagevalue.get(FacebookAuthentication.TAG_PTAC));
                editor.putString("fpimage",finalpagevalue.get(FacebookAuthentication.TAG_FPIMAGE));

                editor.apply();

                Intent intent = new Intent(activity, FacebookPageSumbitActivity.class);
                activity.startActivity(intent);
            }
        });

        return vi;
    }
}