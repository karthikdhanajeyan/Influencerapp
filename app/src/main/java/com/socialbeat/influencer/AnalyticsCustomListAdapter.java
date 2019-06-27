package com.socialbeat.influencer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class AnalyticsCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<AnalyticsReport> analyticsItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	ImageView cancel;
	String urlvalue,ccampid,ccampname,ccontentid,creachvalue,cengagevalue,creachstatus,cengagestatus;

	TextView reachvalue,engagevalue;
	//ImageView reachstatus,engagestatus;

	AnalyticsCustomListAdapter(Activity activity, List<AnalyticsReport> analyticsItem) {
		this.activity = activity;
		this.analyticsItem = analyticsItem;
	}

	@Override
	public int getCount() {
		return analyticsItem.size();
	}

	@Override
	public Object getItem(int location) {
		return analyticsItem.get(location);
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

			convertView = inflater.inflate(R.layout.analyticsreportlist, null);
		}

		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();

		final NetworkImageView reachstatus = convertView.findViewById(R.id.reachstatus);
		final NetworkImageView engagestatus = convertView.findViewById(R.id.engagestatus);

		TextView campid = convertView.findViewById(R.id.campid);
		TextView campname = convertView.findViewById(R.id.campname);
		TextView contentid = convertView.findViewById(R.id.contentid);
		TextView social_media = convertView.findViewById(R.id.social_media);
		TextView posted_date = convertView.findViewById(R.id.posted_date);
		TextView posted_link = convertView.findViewById(R.id.posted_link);
		TextView from_date = convertView.findViewById(R.id.from_date);
		TextView to_date = convertView.findViewById(R.id.to_date);
		TextView reach = convertView.findViewById(R.id.reach);
		TextView status = convertView.findViewById(R.id.status);
		TextView reach_attach = convertView.findViewById(R.id.reach_attach);
		TextView engagement = convertView.findViewById(R.id.engagement);
		TextView engage_attach = convertView.findViewById(R.id.engage_attach);
		LinearLayout linearn = convertView.findViewById(R.id.linearn);

		Button reach_value = convertView.findViewById(R.id.reach_value);
		Button action_view = convertView.findViewById(R.id.action_view);

		// getting campaign data for the row
		AnalyticsReport cc = analyticsItem.get(position);
		campid.setText(cc.getCampid());
		campname.setText(cc.getCampname());
		contentid.setText(cc.getContentid());
		social_media.setText(cc.getSocial_media());
		posted_date.setText(cc.getPosted_date());
		posted_link.setText(cc.getPosted_link());
		from_date.setText(cc.getFrom_date());
		to_date.setText(cc.getTo_date());
		status.setText(cc.getStatus());
		reach.setText(cc.getReach());
		reach_attach.setText(cc.getReach_attach());
		engagement.setText(cc.getEngagement());
		engage_attach.setText(cc.getEngage_attach());

		ccampid = cc.getCampid();
		ccampname = cc.getCampname();
		ccontentid = cc.getContentid();
		creachvalue = cc.getReach();
		cengagevalue = cc.getEngagement();
		creachstatus = cc.getReach_attach();
		cengagestatus = cc.getEngage_attach();

		Log.v("newreachvalue :",creachvalue);
		Log.v("newengagevalue :",cengagevalue);

		SharedPreferences preferences1 = activity.getSharedPreferences("ANALYTICS_NEW_REPORT", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor1 = preferences1.edit();
		editor1.putString("campid",ccampid);
		editor1.putString("campname",ccampname);
		editor1.putString("contentid",ccontentid);
		System.out.println("value of campid Analytics: "+ccampid);
		System.out.println("value of campname Analytics: "+ccampname);
		System.out.println("value of contentid Analytics: "+ccontentid);
		editor1.apply();

		action_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(activity,"Content clicked", Toast.LENGTH_LONG).show();
				SharedPreferences preferences1 = activity.getSharedPreferences("ANALYTICS_REPORT", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor1 = preferences1.edit();
				editor1.putString("campid",ccampid);
				editor1.putString("campname",ccampname);
				editor1.putString("contentid",ccontentid);
				System.out.println("value of campid Analytics: "+ccampid);
				System.out.println("value of campname Analytics: "+ccampname);
				System.out.println("value of contentid Analytics: "+ccontentid);
				editor1.apply();

				Intent intent = new Intent(activity, SMReportsUpdate.class);
				Bundle bund = new Bundle();
				//Inserts a String value into the mapping of this Bundle
				bund.putString("campid", ccampid);
				bund.putString("campname", ccampname);
				bund.putString("contentid", ccontentid);
				Log.v("Analytics CCampid : ",ccampid);
				Log.v("Analytics CCampname : ",ccampname);
				Log.v("Analytics ccontentid : ",ccontentid);
				//Add the bundle to the intent.
				intent.putExtras(bund);
				activity.startActivity(intent);
			}
		});

		urlvalue = cc.getPosted_link();
//		linearn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (urlvalue != null) {
//					Intent i = new Intent(Intent.ACTION_VIEW);
//					i.setData(Uri.parse(urlvalue));
//					activity.startActivity(i);
//				} else {
//					Toast.makeText(activity, "URL Link is Empty", Toast.LENGTH_SHORT).show();
//					Log.v("Result : ", "URL Link is Empty");
//				}
//			}
//		});

		reach_value.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(activity,"linear layout clicked", Toast.LENGTH_LONG).show();
				final Dialog dialog = new Dialog(activity);
				dialog.setContentView(R.layout.reachview);
				dialog.setCancelable(false);
				cancel = dialog.findViewById(R.id.close_img);
				reachvalue = dialog.findViewById(R.id.reachvalue);
				engagevalue = dialog.findViewById(R.id.engagevalue);

//				reachstatus.setImageUrl(creachstatus, imageLoader);
//				engagestatus.setImageUrl(cengagestatus, imageLoader);
				Log.v("reachvalue :",creachvalue);
				Log.v("engagevalue :",cengagevalue);

				if (creachvalue != null ) {
					Log.v("reachOutput",creachvalue);
					reachvalue.setText(creachvalue);
				}else if ((creachvalue == null)){
					Log.v("reachOutput","-nil-");
					reachvalue.setText("-NIL-");
				}

				if (cengagevalue != null) {
					Log.v("engageOutput",cengagevalue);
					engagevalue.setText(cengagevalue);
				}else if ((cengagevalue == null)){
					Log.v("engageOutput","-nil-");
					engagevalue.setText("-NIL-");
				}

				dialog.show();
				cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});

		return convertView;

		//Toast.makeText(activity,"Content clicked", Toast.LENGTH_LONG).show();

	}
}