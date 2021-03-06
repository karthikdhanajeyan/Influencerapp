package com.socialbeat.influencer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;

public class CampAdapter extends ArrayAdapter<CampValues> {
	ArrayList<CampValues> campList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public CampAdapter(Context context, int resource, ArrayList<CampValues> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		campList = objects;
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);

			holder.campImg = v.findViewById(R.id.campImg);
			holder.campName = v.findViewById(R.id.campName);
			holder.campShortNote = v.findViewById(R.id.campShortNote);
			holder.campCat = v.findViewById(R.id.campCat);
			holder.campLongNote = v.findViewById(R.id.campLongNote);
			holder.campGoal = v.findViewById(R.id.campGoal);
			holder.campDos = v.findViewById(R.id.campDos);
			holder.campDont = v.findViewById(R.id.campDont);
			holder.campBacklink = v.findViewById(R.id.campBacklink);
			holder.campTag = v.findViewById(R.id.campTag);
			holder.campid = v.findViewById(R.id.campid);
			holder.campApplyTill = v.findViewById(R.id.campApplyTill);
			holder.campRewards = v.findViewById(R.id.campRewards);
			holder.campRewardType = v.findViewById(R.id.campRewardType);
			holder.fixedamount = v.findViewById(R.id.fixedamount);
			holder.campEligibility = v.findViewById(R.id.campEligibility);
			holder.campDeliverables = v.findViewById(R.id.campDeliverables);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.campImg.setImageResource(R.mipmap.influencerlistimg);
		new DownloadImageTask(holder.campImg).execute(campList.get(position).getCampImg());
		holder.campName.setText(campList.get(position).getCampName());
		holder.campShortNote.setText(campList.get(position).getCampShortNote());
		holder.campCat.setText(campList.get(position).getCampCat());
		holder.campLongNote.setText(campList.get(position).getCampLongNote());
		holder.campGoal.setText(campList.get(position).getCampGoal());
		holder.campDos.setText(campList.get(position).getCampDos());
		holder.campDont.setText(campList.get(position).getCampDont());
		holder.campBacklink.setText(campList.get(position).getCampBacklink());
		holder.campTag.setText(campList.get(position).getCampTag());
		holder.campid.setText(campList.get(position).getCampid());
		holder.campApplyTill.setText(campList.get(position).getCampApplyTill());
		holder.campRewards.setText(campList.get(position).getCampRewards());
		holder.campRewardType.setText(campList.get(position).getCampRewardType());
		holder.fixedamount.setText(campList.get(position).getFixedamount());
		holder.campEligibility.setText(campList.get(position).getCampEligibility());
		holder.campDeliverables.setText(campList.get(position).getCampDeliverables());
		return v;
	}

	static class ViewHolder {
		public ImageView campImg;
		public TextView campName;
		public TextView campShortNote;
		public TextView campCat;
		public TextView campLongNote;
		public TextView campGoal;
		public TextView campDos;
		public TextView campDont;
		public TextView campBacklink;
		public TextView campTag;
		public TextView campid;
		public TextView campApplyTill;
		public TextView campRewards;
		public TextView campRewardType;
		public TextView fixedamount;
		public TextView campEligibility;
		public TextView campDeliverables;

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}