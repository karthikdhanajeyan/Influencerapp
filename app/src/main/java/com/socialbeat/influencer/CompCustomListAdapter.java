package com.socialbeat.influencer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

public class CompCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<CompletedCamp> completedCampItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	private HashMap<String,String> myBrief = new HashMap<String,String>();
	private HashMap<String,String> paymentStatus = new HashMap<String,String>();
	String cbrief,cpaystatus,ctransstatus,ctransid,camount,cdate;
    ImageView cancel;
    TextView campbriefvalue,paymentstatus1,transactionstatus1,transactionid1,transactionamount1,transactiondate1;

	CompCustomListAdapter(Activity activity, List<CompletedCamp> completedCampItem) {
		this.activity = activity;
		this.completedCampItem = completedCampItem;
	}

	@Override
	public int getCount() {
		return completedCampItem.size();
	}

	@Override
	public Object getItem(int location) {
		return completedCampItem.get(location);
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

			convertView = inflater.inflate(R.layout.completedcamplist, null);
		}
//
		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView campImg = convertView.findViewById(R.id.campImg);

		TextView campid = convertView.findViewById(R.id.campid);
		TextView campname = convertView.findViewById(R.id.campname);
		TextView status = convertView.findViewById(R.id.campstatus);
		TextView completeddate = convertView.findViewById(R.id.completeddate);
		TextView campbrief = convertView.findViewById(R.id.campbrief);
		TextView paymentstatus = convertView.findViewById(R.id.paymentstatus);
		TextView transactionstatus = convertView.findViewById(R.id.transactionstatus);
		TextView transactionid = convertView.findViewById(R.id.transactionid);
		TextView amount = convertView.findViewById(R.id.amount);
		TextView date = convertView.findViewById(R.id.date);
		LinearLayout camp_brief = convertView.findViewById(R.id.camp_brief);
		LinearLayout payment_details = convertView.findViewById(R.id.payment_fulldetails);
//		// getting campaign data for the row
		CompletedCamp cc = completedCampItem.get(position);
//		// thumbnail image
		campImg.setImageUrl(cc.getCampImg(), imageLoader);
//		//normal values
		campid.setText(cc.getCampid());
		campname.setText(cc.getCampname());
		status.setText(cc.getStatus());
		completeddate.setText(cc.getcompleteddate());
		campbrief.setText(cc.getCampbrief());
		paymentstatus.setText(cc.getPaymentstatus());
		transactionstatus.setText(cc.getTransactionstatus());
		transactionid.setText(cc.getTransactionid());
		amount.setText(cc.getAmount());
		date.setText(cc.getDate());
		myBrief.put(cc.getCampid(),cc.getCampbrief());


		cbrief = cc.getCampbrief();
		cpaystatus = cc.getPaymentstatus();
		ctransstatus = cc.getTransactionstatus();
		ctransid = cc.getTransactionid();
		camount = cc.getAmount();
		cdate = cc.getDate();

		final String testid = cc.getCampid();

		Log.d("cbrief : ",cbrief);
		Log.d("cpaystatus : ",cpaystatus);
		Log.d("ctransstatus : ",ctransstatus);
		Log.d("ctransid : ",ctransid);
		Log.d("camount : ",camount);
		Log.d("cdate : ",cdate);



		camp_brief.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
                //Toast.makeText(activity,"linear layout clicked", Toast.LENGTH_LONG).show();
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

		payment_details.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(activity,"linear layout clicked", Toast.LENGTH_LONG).show();
				final Dialog dialog = new Dialog(activity);
				dialog.setContentView(R.layout.paymentview);
				dialog.setCancelable(false);
				cancel = dialog.findViewById(R.id.close_img);
				paymentstatus1 = dialog.findViewById(R.id.paymentstatus);
				transactionstatus1 = dialog.findViewById(R.id.transactionstatus);
				transactionid1 = dialog.findViewById(R.id.transactionid);
				transactionamount1 = dialog.findViewById(R.id.transactionamount);
				transactiondate1 = dialog.findViewById(R.id.transactiondate);

				Spanned sp2 = Html.fromHtml(cpaystatus);
				paymentstatus1.setText(sp2);
				paymentstatus1.setMovementMethod(LinkMovementMethod.getInstance());

				Spanned sp3 = Html.fromHtml(ctransstatus);
				transactionstatus1.setText(sp3);
				transactionstatus1.setMovementMethod(LinkMovementMethod.getInstance());

				Spanned sp4 = Html.fromHtml(ctransid);
				transactionid1.setText(sp4);
				transactionid1.setMovementMethod(LinkMovementMethod.getInstance());

				Spanned sp5 = Html.fromHtml(camount);
				transactionamount1.setText(sp5);
				transactionamount1.setMovementMethod(LinkMovementMethod.getInstance());

				Spanned sp6 = Html.fromHtml(cdate);
				transactiondate1.setText(sp6);
				transactiondate1.setMovementMethod(LinkMovementMethod.getInstance());

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
	}
}