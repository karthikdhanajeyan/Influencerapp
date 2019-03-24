package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class AppliedCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<AppliedCamp> appliedCampItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	ImageView cancel;

	AppliedCustomListAdapter(Activity activity, List<AppliedCamp> appliedCampItem) {
		this.activity = activity;
		this.appliedCampItem = appliedCampItem;
	}

	@Override
	public int getCount() {
		return appliedCampItem.size();
	}

	@Override
	public Object getItem(int location) {
		return appliedCampItem.get(location);
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

			convertView = inflater.inflate(R.layout.appliedcamplist, null);
		}

		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView campImg = convertView.findViewById(R.id.campImg);
		TextView cid = convertView.findViewById(R.id.cid);
		TextView campid = convertView.findViewById(R.id.campid);
		TextView campname = convertView.findViewById(R.id.campname);
		TextView campappliedstatus = convertView.findViewById(R.id.campappliedstatus);
		TextView campaignquote = convertView.findViewById(R.id.campaignquote);
		TextView campapplieddate = convertView.findViewById(R.id.campapplieddate);
		TextView campdeliverystatus = convertView.findViewById(R.id.campdeliverystatus);
		TextView camppaymentstatus = convertView.findViewById(R.id.camppaymentstatus);

//		// getting campaign data for the row
		AppliedCamp cc = appliedCampItem.get(position);
//		// thumbnail image
		// getting campaign data for the row
		campImg.setImageUrl(cc.getCampImg(), imageLoader);
//		//normal values
		cid.setText(cc.getCid());
		campid.setText(cc.getCampid());
		campname.setText(cc.getCampname());
		campappliedstatus.setText(cc.getCampappliedstatus());
		campaignquote.setText(cc.getCampaignquote());
		campapplieddate.setText(cc.getCampapplieddate());
		campdeliverystatus.setText(cc.getCampdeliverystatus());
		camppaymentstatus.setText(cc.getCamppaymentstatus());


		return convertView;
	}
}