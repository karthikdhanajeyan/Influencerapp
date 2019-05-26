package com.socialbeat.influencer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import java.util.HashMap;
import java.util.List;

public class ApproveCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ApprovedCamp> approvedCampItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	private HashMap<String,String> myCampid = new HashMap<String,String>();
	private HashMap<String,String> myBrief = new HashMap<String,String>();
	private HashMap<String,String> myConversation = new HashMap<String,String>();
	String cbrief,ccampid,ccampname;
	ImageView cancel;
	TextView campbriefvalue;
	FragmentManager fragmentManager;

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
		myCampid.put(cc.getCampid(),cc.getCampid());
		myBrief.put(cc.getCampid(),cc.getCampbrief());
		myConversation.put(cc.getCampid(),cc.getCampname());


		final String testid = cc.getCampid();

		camp_brief.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(activity,"linear layout clicked", Toast.LENGTH_LONG).show();
				myBrief.get(testid);
				System.out.println("test result : "+myBrief.get(testid));
				System.out.println("testid result : "+testid);
				final Dialog dialog = new Dialog(activity);
				dialog.setContentView(R.layout.briefview);
				dialog.setCancelable(false);
				cancel = dialog.findViewById(R.id.close_img);
				campbriefvalue = dialog.findViewById(R.id.campbrief);
				Log.d("value1 : ",myBrief.get(testid));
				Spanned sp1 = Html.fromHtml( myBrief.get(testid) );
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

				SharedPreferences preferences1 = activity.getSharedPreferences("COMPLETE_CAMP_CONTENT", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor1 = preferences1.edit();
				editor1.putString("campid",myCampid.get(testid));
				editor1.putString("campname",myConversation.get(testid));
				System.out.println("value of campid Complete Approved List: "+myCampid.get(testid));
				System.out.println("value of campname Complete Approved List: "+myConversation.get(testid));
				editor1.apply();

				Intent intent = new Intent(activity, Conversations.class);
				Bundle bund = new Bundle();
				//Inserts a String value into the mapping of this Bundle
				bund.putString("campid", myCampid.get(testid));
				bund.putString("campname", myConversation.get(testid));
				Log.v("Approve CCampid : ",myCampid.get(testid));
				Log.v("Approve CCampname : ",myConversation.get(testid));
				//Add the bundle to the intent.
				intent.putExtras(bund);
				activity.startActivity(intent);
			}
		});
		analtyics_value.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				SharedPreferences preferences1 = activity.getSharedPreferences("COMPLETE_CAMP_CONTENT", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor1 = preferences1.edit();
				editor1.putString("campid",myCampid.get(testid));
				editor1.putString("campname",myConversation.get(testid));
				System.out.println("value of campid Complete Approved List: "+myCampid.get(testid));
				System.out.println("value of campname Complete Approved List: "+myConversation.get(testid));
				editor1.apply();

				Fragment fragment = new AnalyticsReportFragment();
				Bundle bundle = new Bundle();
				bundle.putString("campid", myCampid.get(testid));
				bundle.putString("campname", myConversation.get(testid));
				Log.v("Approve CCampid : ",myCampid.get(testid));
				Log.v("Approve CCampname : ",myConversation.get(testid));
				FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.newframe, fragment );
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		return convertView;
	}
}