package com.socialbeat.influencer;

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


        TextView pgname = vi.findViewById(R.id.pgname);
        TextView pgid = vi.findViewById(R.id.pgid);
        TextView pgtoken = vi.findViewById(R.id.pgtoken);
        TextView pgfancount = vi.findViewById(R.id.pgfancount);
        Button select = vi.findViewById(R.id.select);
        ImageView profile_image= vi.findViewById(R.id.profile_image); // thumb image

        HashMap<String, String> pagevalue = new HashMap<String, String>();
        pagevalue = data.get(position);

        // Setting all values in listview
        pgname.setText(pagevalue.get(FacebookAuthentication.TAG_PNAME));
        pgfancount.setText(pagevalue.get(FacebookAuthentication.TAG_PFANCOUNT));
        imageLoader.DisplayImage(pagevalue.get(FacebookAuthentication.TAG_URL), profile_image);
        final HashMap<String, String> finalpagevalue = pagevalue;
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Post ID  : ", finalpagevalue.get(FacebookAuthentication.TAG_PNAME));
                Toast.makeText(activity,finalpagevalue.get(FacebookAuthentication.TAG_PNAME )+"  "+" page Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, NewHomeActivity.class);
//                Bundle bund = new Bundle();
//                bund.putString("fbPageId", finalpagevalue.get(FacebookAuthentication.TAG_PID));
//                bund.putString("fbPageToken", finalpagevalue.get(FacebookAuthentication.TAG_PTOKEN));
//                intent.putExtras(bund);
                activity.startActivity(intent);
            }
        });

        return vi;
    }
}