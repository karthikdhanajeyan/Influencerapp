package com.socialbeat.influencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class ViewConversationCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ViewConversation> viewconversationItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	//String urlvalue,con_auth,ifluence;
	ImageView cancel;

	ViewConversationCustomListAdapter(Activity activity, List<ViewConversation> viewconversationItem) {
		this.activity = activity;
		this.viewconversationItem = viewconversationItem;
	}

	@Override
	public int getCount() {
		return viewconversationItem.size();
	}

	@Override
	public Object getItem(int location) {
		return viewconversationItem.get(location);
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

			convertView = inflater.inflate(R.layout.viewconversationlist, null);
		}
		if (imageLoader == null)
		imageLoader = MyApplication.getInstance().getImageLoader();
		NetworkImageView conversation_thumbnail_url = convertView.findViewById(R.id.conversation_thumbnail_url);

		TextView id_post_conversation = convertView.findViewById(R.id.id_post_conversation);
		TextView message = convertView.findViewById(R.id.message);
		TextView conversation_author = convertView.findViewById(R.id.conversation_author);
		TextView added_date = convertView.findViewById(R.id.added_date);
		TextView id_post_attach = convertView.findViewById(R.id.id_post_attach);
//		TextView file_name = convertView.findViewById(R.id.file_name);
//		TextView file_location = convertView.findViewById(R.id.file_location);
		TextView approved_status = convertView.findViewById(R.id.approved_status);
//		ImageView chatusericon = convertView.findViewById(R.id.chatusericon);
		ImageView fileone = convertView.findViewById(R.id.fileone);
		ImageView filetwo = convertView.findViewById(R.id.filetwo);
		ImageView filethree = convertView.findViewById(R.id.filethree);
		ImageView filefour = convertView.findViewById(R.id.filefour);


		// getting campaign data for the row
		ViewConversation cc = viewconversationItem.get(position);
		// getting campaign data for the row

//		Spanned sp1 = Html.fromHtml( cc.getId_post_conversation() );
//		id_post_conversation.setText(sp1);
//		id_post_conversation.setMovementMethod(LinkMovementMethod.getInstance());
		id_post_conversation.setText(cc.getId_post_conversation());

		conversation_thumbnail_url.setImageUrl(cc.getConversation_thumbnail_url(), imageLoader);

		Spanned sp2 = Html.fromHtml( cc.getMessage() );
		message.setText(sp2);
		message.setMovementMethod(LinkMovementMethod.getInstance());



//		Spanned sp3 = Html.fromHtml(cc.getConversation_author());
//		conversation_author.setText(sp3);
//		conversation_author.setMovementMethod(LinkMovementMethod.getInstance());
		conversation_author.setText(cc.getConversation_author());
//		con_auth =conversation_author.toString();
//		Log.v("Admin Value : ",con_auth);
//		if(con_auth.equals("influencer")){
//			Log.v("influencer","working");
//		chatusericon.setImageResource(R.mipmap.chatusericon);
//		}else if(con_auth.equals("admin")){
//			Log.v("admin","working");
//		chatusericon.setImageResource(R.mipmap.user_color_icon);
//		}
//		Spanned sp4 = Html.fromHtml(cc.getAdded_date());
//		added_date.setText(sp4);
//		added_date.setMovementMethod(LinkMovementMethod.getInstance());
		added_date.setText(cc.getAdded_date());

//		Spanned sp5 = Html.fromHtml(cc.getId_post_attach());
//		id_post_attach.setText(sp5);
//		id_post_attach.setMovementMethod(LinkMovementMethod.getInstance());
		id_post_attach.setText(cc.getId_post_attach());

//		Spanned sp6 = Html.fromHtml(cc.getFile_name());
//		file_name.setText(sp6);
//		file_name.setMovementMethod(LinkMovementMethod.getInstance());
//		file_name.setText(cc.getFile_name());
//
//		Spanned sp7 = Html.fromHtml(cc.getFile_location());
//		file_location.setText(sp7);
//		file_location.setMovementMethod(LinkMovementMethod.getInstance());
		//file_location.setText(cc.getFile_location());
//		 urlvalue = cc.getFile_location();
//		 file_location.setOnClickListener(new View.OnClickListener() {
//			 @Override
//			 public void onClick(View v) {
//				 if (urlvalue != null) {
//					 Intent i = new Intent(Intent.ACTION_VIEW);
//					 i.setData(Uri.parse(urlvalue));
//					 activity.startActivity(i);
//				 } else {
//					 Toast.makeText(activity, "URL Link is Empty", Toast.LENGTH_SHORT).show();
//					 Log.v("Result : ", "URL Link is Empty");
//				 }
//			 }
//		 });

//
////		Spanned sp8 = Html.fromHtml(cc.getApproved_status());
////		approved_status.setText(sp8);
////		approved_status.setMovementMethod(LinkMovementMethod.getInstance());
		approved_status.setText(cc.getApproved_status());

		return convertView;
	}
}