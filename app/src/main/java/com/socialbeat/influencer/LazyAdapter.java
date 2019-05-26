package com.socialbeat.influencer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>>data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.facebookpostlist, null);


        TextView post_id = vi.findViewById(R.id.post_id); // artist name
        Button post_select = vi.findViewById(R.id.post_select); // duration
        ImageView post_icon= vi.findViewById(R.id.post_icon); // thumb image

        HashMap<String, String> postvalue = new HashMap<String, String>();
        postvalue = data.get(position);

        // Setting all values in listview
        post_id.setText(postvalue.get(FacebookPostActivity.KEY_ID));
        imageLoader.DisplayImage(postvalue.get(FacebookPostActivity.KEY_FULLPICTURE), post_icon);
        final HashMap<String, String> finalPostvalue = postvalue;
        post_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Post ID  : ", finalPostvalue.get(FacebookPostActivity.KEY_ID));
                Intent intent = new Intent(activity, FacebookPostSumbitActivity.class);
                Bundle bund = new Bundle();
                bund.putString("postid", finalPostvalue.get(FacebookPostActivity.KEY_ID));
                intent.putExtras(bund);
                activity.startActivity(intent);
            }
        });

        return vi;
    }
}