package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SMLazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>>data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public SMLazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.smproflie_list, null);


        TextView name = vi.findViewById(R.id.name);
        TextView key1 = vi.findViewById(R.id.key1);

        ImageView profile_image= vi.findViewById(R.id.profile_image); // thumb image
        ImageView sm_image= vi.findViewById(R.id.sm_image); // thumb image

        HashMap<String, String> pagedetails = new HashMap<String, String>();
        pagedetails = data.get(position);
        // Setting all values in listview
        name.setText(pagedetails.get(SMProfile.TAG_UNAME));
        key1.setText(pagedetails.get(SMProfile.TAG_FOLLOWERS));
        imageLoader.DisplayImage(pagedetails.get(SMProfile.TAG_PIMAGE), profile_image);

        if(pagedetails.get(SMProfile.TAG_SOCIALMEDIA).equalsIgnoreCase("Facebook")){
            sm_image.setImageResource(R.mipmap.facebook_clr_icon);
        }else if(pagedetails.get(SMProfile.TAG_SOCIALMEDIA).equalsIgnoreCase("Instagram")){
            sm_image.setImageResource(R.mipmap.instagram_clr_icon);
        } else if(pagedetails.get(SMProfile.TAG_SOCIALMEDIA).equalsIgnoreCase("GA")){
            sm_image.setImageResource(R.mipmap.blogger_clr_icon);
        }else if(pagedetails.get(SMProfile.TAG_SOCIALMEDIA).equalsIgnoreCase("Twitter")){
            sm_image.setImageResource(R.mipmap.twitter_clr_icon);
        }else if(pagedetails.get(SMProfile.TAG_SOCIALMEDIA).equalsIgnoreCase("Youtube")){
            sm_image.setImageResource(R.mipmap.twitter_clr_icon);
        }

        return vi;
    }
}