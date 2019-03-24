package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.util.List;

public class ApproveCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ApprovedCamp> approvedCampItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	String cbrief;
	ImageView cancel;
	TextView campbriefvalue;

	ApproveCustomListAdapter(Activity activity, List<ApprovedCamp> approvedCampItem) {
		this.activity = activity;
		this.approvedCampItem = approvedCampItem;
	}

	@Override
	public int getCount() {
		return approvedCampItem.size();
	}

	@Override
	public Object getItem(int location) {
		return approvedCampItem.get(location);
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

			convertView = inflater.inflate(R.layout.approvedcamplist, null);
		}

		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView campImg = convertView.findViewById(R.id.campImg);

		TextView campid = convertView.findViewById(R.id.campid);
		TextView campname = convertView.findViewById(R.id.campname);
		TextView status = convertView.findViewById(R.id.campstatus);
		TextView approveddate = convertView.findViewById(R.id.approveddate);
		TextView campbrief = convertView.findViewById(R.id.campbrief);
		LinearLayout camp_brief = convertView.findViewById(R.id.camp_brief);
		Button content_value = convertView.findViewById(R.id.content_value);
		Button analtyics_value = convertView.findViewById(R.id.analtyics_value);

//		// getting campaign data for the row
		ApprovedCamp cc = approvedCampItem.get(position);
//		// thumbnail image
		// getting campaign data for the row
		campImg.setImageUrl(cc.getCampImg(), imageLoader);
//		//campImg.setImageUrl(cc.getCampImg(), imageLoader);
//		//normal values
		campid.setText(cc.getCampid());
		campname.setText(cc.getCampname());
		status.setText(cc.getStatus());
		approveddate.setText(cc.getApproveddate());
		campbrief.setText(cc.getCampbrief());

		cbrief = cc.getCampbrief();

		camp_brief.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(activity,"linear layout clicked", Toast.LENGTH_LONG).show();
				final Dialog dialog = new Dialog(activity);
				dialog.setContentView(R.layout.briefview);
				dialog.setCancelable(false);
				cancel = dialog.findViewById(R.id.close_img);
				campbriefvalue = dialog.findViewById(R.id.campbrief);
				Log.d("value1 : ",cbrief);
				Spanned sp1 = Html.fromHtml( cbrief );
				campbriefvalue.setText(sp1);
				campbriefvalue.setMovementMethod(LinkMovementMethod.getInstance());

				dialog.show();
				cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});
		content_value.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(activity,"Content clicked", Toast.LENGTH_LONG).show();

				Intent intent = new Intent(activity, Conversations.class);
				activity.startActivity(intent);

//				AppliedCampaignFragment fragment = new AppliedCampaignFragment();
//				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//				fragmentTransaction.replace(R.id.frame, fragment);
//				fragmentTransaction.addToBackStack(null);
//				fragmentTransaction.commit();
			}
		});
		analtyics_value.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(activity,"Analtyics Report clicked", Toast.LENGTH_LONG).show();
			}
		});

		return convertView;
	}
}