package com.socialbeat.influencer;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

public class SMProfileCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<SMProfileDetails> smprofiledetails;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
//	private HashMap<String,String> myCampid = new HashMap<String,String>();
//	private HashMap<String,String> myBrief = new HashMap<String,String>();
//	private HashMap<String,String> myConversation = new HashMap<String,String>();
	String cbrief,ccampid,ccampname;
	ImageView cancel;
	TextView campbriefvalue;
	FragmentManager fragmentManager;

	SMProfileCustomListAdapter(Activity activity, List<SMProfileDetails> smprofiledetails) {
		this.activity = activity;
		this.smprofiledetails = smprofiledetails;
	}

	@Override
	public int getCount() {
		return smprofiledetails.size();
	}

	@Override
	public Object getItem(int location) {
		return smprofiledetails.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.smproflie_list, null);
		}

		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView profile_image = convertView.findViewById(R.id.profile_image);
//		NetworkImageView sm_image = convertView.findViewById(R.id.sm_image);

		TextView name = convertView.findViewById(R.id.name);
		TextView key1_text = convertView.findViewById(R.id.key1_text);
		TextView key1 = convertView.findViewById(R.id.key1);
		TextView key2_text = convertView.findViewById(R.id.key2_text);
		TextView key2 = convertView.findViewById(R.id.key2);
//		TextView key3_text = convertView.findViewById(R.id.key3_text);
//		TextView key3 = convertView.findViewById(R.id.key3);
		TextView socialmedia = convertView.findViewById(R.id.socialmedia);
		TextView link = convertView.findViewById(R.id.link);


		//		// getting campaign data for the row
		SMProfileDetails cc = smprofiledetails.get(position);
//		// thumbnail image
		// getting campaign data for the row
		profile_image.setImageUrl(cc.getProfile_image(), imageLoader);
		//sm_image.setImageUrl(cc.gets(), imageLoader);
//		//normal values
		name.setText(cc.getName());
		key1_text.setText(cc.getKey1_text());
		key1.setText(cc.getKey1());
		key2_text.setText(cc.getKey2_text());
		key2.setText(cc.getKey2());
//		key3_text.setText(cc.getKey3_text());
//		key3.setText(cc.getKey3());
		socialmedia.setText(cc.getSocialmedia());
		link.setText(cc.getLink());

		return convertView;
	}
}