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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class CompleteCampAdapter extends ArrayAdapter<CompletedCamp> {
	ArrayList<CompletedCamp> completedCampList;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public CompleteCampAdapter(Context context, int resource, ArrayList<CompletedCamp> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		completedCampList = objects;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);

			holder.campid = v.findViewById(R.id.campid);
			//holder.campImg = v.findViewById(R.id.campImg);
			holder.campname = v.findViewById(R.id.campname);
			holder.status = v.findViewById(R.id.status);
			holder.completeddate = v.findViewById(R.id.completeddate);
			holder.campbrief = v.findViewById(R.id.campbrief);
			holder.paymentstatus = v.findViewById(R.id.paymentstatus);
			holder.transactionstatus = v.findViewById(R.id.transactionstatus);
			holder.transactionid = v.findViewById(R.id.transactionid);
			holder.amount = v.findViewById(R.id.amount);
			holder.date = v.findViewById(R.id.date);

			holder.camp_brief = v.findViewById(R.id.camp_brief);
			//holder.payment_details = v.findViewById(R.id.payment_details);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		//holder.campImg.setImageResource(R.mipmap.influencerlistimg);
		//new DownloadImageTask(holder.campImg).execute(completedCampList.get(position).getCampImg());
		holder.campid.setText(completedCampList.get(position).getCampid());
		holder.campname.setText(completedCampList.get(position).getCampname());
		holder.status.setText(completedCampList.get(position).getStatus());
		holder.completeddate.setText(completedCampList.get(position).getcompleteddate());
		holder.campbrief.setText(completedCampList.get(position).getCampbrief());
		holder.paymentstatus.setText(completedCampList.get(position).getPaymentstatus());
		holder.transactionstatus.setText(completedCampList.get(position).getTransactionstatus());
		holder.transactionid.setText(completedCampList.get(position).getTransactionid());
		holder.amount.setText(completedCampList.get(position).getAmount());
		holder.date.setText(completedCampList.get(position).getDate());

		holder.camp_brief.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Output is : Campaign Brief Clicked");
			}
		});
//		holder.payment_details.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				System.out.println("Output is : Payment Details Clicked");
//			}
//		});

		return v;
	}

	static class ViewHolder {

		//public ImageView campImg;
		public TextView campid;
		public TextView campname;
		public TextView status;
		public TextView completeddate;
		public TextView campbrief;
		public TextView paymentstatus;
		public TextView transactionstatus;
		public TextView transactionid;
		public TextView amount;
		public TextView date;
		public LinearLayout camp_brief;
		public LinearLayout payment_details;


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