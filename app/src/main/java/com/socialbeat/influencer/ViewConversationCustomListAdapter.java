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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

public class ViewConversationCustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ViewConversation> viewconversationItem;
	private ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
	private HashMap<String,String> myFilelink = new HashMap<String,String>();
	ImageView cancel;
	private String file_path;

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
		NetworkImageView fileone = convertView.findViewById(R.id.fileone);

		TextView id_post_conversation = convertView.findViewById(R.id.id_post_conversation);
		TextView message = convertView.findViewById(R.id.message);
		TextView conversation_author = convertView.findViewById(R.id.conversation_author);
		TextView added_date = convertView.findViewById(R.id.added_date);
		TextView id_post_attach = convertView.findViewById(R.id.id_post_attach);
		TextView approved_status = convertView.findViewById(R.id.approved_status);

		ViewConversation cc = viewconversationItem.get(position);
		id_post_conversation.setText(cc.getId_post_conversation());

		conversation_thumbnail_url.setImageUrl(cc.getConversation_thumbnail_url(), imageLoader);
		Spanned sp2 = Html.fromHtml( cc.getMessage() );
		message.setText(sp2);
		message.setMovementMethod(LinkMovementMethod.getInstance());

		conversation_author.setText(cc.getConversation_author());

		fileone.setImageUrl(cc.getThumbnail_url(), imageLoader);

		added_date.setText(cc.getAdded_date());

		id_post_attach.setText(cc.getId_post_attach());

		approved_status.setText(cc.getApproved_status());

		file_path = cc.getFile_location();

		String filename = cc.getFile_name();

		myFilelink.put(cc.getId_post_conversation(),cc.getFile_location());
		final String conversationid = cc.getId_post_conversation();


//
		Log.v("post id : ",cc.getId_post_attach());

		if (fileone!=null){
			fileone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = file_path;
					Log.v("Url File value : ",myFilelink.get(conversationid));
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(myFilelink.get(conversationid)));
					activity.startActivity(i);
				}
			});
		}
//		for(int i = 0 ; i < fileone.size() ; i++ ){
//			params.put("files[" + i + "]", MultipartBody.create(mediaType, new File("First file path")));
//		}

		return convertView;
	}
}