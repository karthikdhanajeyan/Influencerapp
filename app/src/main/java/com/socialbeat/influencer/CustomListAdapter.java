package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<CampValues> campValuesItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

	CustomListAdapter(Activity activity, List<CampValues> campValuesItem) {
		this.activity = activity;
		this.campValuesItem = campValuesItem;
	}

	@Override
	public int getCount() {
		return campValuesItem.size();
	}

	@Override
	public Object getItem(int location) {
		return campValuesItem.get(location);
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

			convertView = inflater.inflate(R.layout.influencer_livecamp_list, null);
		}

		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView campImg = convertView.findViewById(R.id.campImg);
		TextView campName = convertView.findViewById(R.id.campName);
		TextView campShortNote = convertView.findViewById(R.id.campShortNote);
		TextView campCat = convertView.findViewById(R.id.campCat);
		TextView campLongNote = convertView.findViewById(R.id.campLongNote);
		TextView campGoal = convertView.findViewById(R.id.campGoal);
		TextView campDos = convertView.findViewById(R.id.campDos);
		TextView campDont = convertView.findViewById(R.id.campDont);
		TextView campBacklink = convertView.findViewById(R.id.campBacklink);
		TextView campTag = convertView.findViewById(R.id.campTag);
		TextView campid = convertView.findViewById(R.id.campid);
		TextView campApplyTill = convertView.findViewById(R.id.campApplyTill);
		TextView campRewards = convertView.findViewById(R.id.campRewards);
		TextView campRewardType = convertView.findViewById(R.id.campRewardType);
		TextView fixedamount = convertView.findViewById(R.id.fixedamount);
		TextView campEligibility = convertView.findViewById(R.id.campEligibility);
		TextView campDeliverables = convertView.findViewById(R.id.campDeliverables);

		// getting campaign data for the row
		CampValues cv = campValuesItem.get(position);
		// thumbnail image
		campImg.setImageUrl(cv.getCampImg(), imageLoader);
		//normal values
		campName.setText(cv.getCampName());
		campShortNote.setText(cv.getCampShortNote());
		campCat.setText(cv.getCampCat());
		campLongNote.setText(cv.getCampLongNote());
		campGoal.setText(cv.getCampGoal());
		campDos.setText(cv.getCampDos());
		campDont.setText(cv.getCampDont());
		campBacklink.setText(cv.getCampBacklink());
		campTag.setText(cv.getCampTag());
		campid.setText(cv.getCampid());
		campApplyTill.setText(cv.getCampApplyTill());
		campRewards.setText(cv.getCampRewards());
		campRewardType.setText(cv.getCampRewardType());
		fixedamount.setText(cv.getFixedamount());
		campEligibility.setText(cv.getCampEligibility());
		campDeliverables.setText(cv.getCampDeliverables());
		return convertView;
	}

}