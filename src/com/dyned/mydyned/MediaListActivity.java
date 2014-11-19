package com.dyned.mydyned;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.composite.MediaGridAdapter;
import com.dyned.mydyned.composite.MediaGridAdapter.ViewHolder;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.MediaItem;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.dyned.mydyned.utils.Setting;

/**
author: jakalesmana
 */

public class MediaListActivity extends SherlockFragment {
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private GridView gvMedia;
	private ProgressBar progress;
	private MediaActivity parentActivity;
	
	private MediaGridAdapter adapter;
	private View view;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentActivity = (MediaActivity) getSherlockActivity();
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	    	view = inflater.inflate(R.layout.activity_media, container, false);
			initView(view);
	    } catch (InflateException e) {
	    }
	    
	    return view;
	}
	
	private void initView(View view){
		gvMedia = (GridView)view.findViewById(R.id.gvMedia);
		progress = (ProgressBar)view.findViewById(R.id.progressBarMedia);
		TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
		
		String title = getArguments().getString("title");
		txtTitle.setText(title);
						
		gvMedia.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				ViewHolder holder = ((ViewHolder)view.getTag());
				retrieveMediaDetail(holder.id);
			}
		});
		
		gvMedia.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
				ViewHolder holder = ((ViewHolder)view.getTag());
				showChooser(holder.id);
				return false;
			}
		});
		progress.setVisibility(View.INVISIBLE);
	}
	
	private void showChooser( final String id) {
		final String[] items = new String[] { "Share", "Open Media" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentActivity, android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);

		builder.setTitle("Select Action");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					retrieveMediaDetailForShare(id);
				} else if (item == 1) {
					retrieveMediaDetail(id);
				} 
			}
		});
		builder.show();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		retrieveMediaList(getArguments().getString("type"));
	}
	
	private void retrieveMediaList(String type){
		InternetTask postTask = new InternetTask(parentActivity, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
						progress.setVisibility(View.VISIBLE);
					}
				});
			}
			
			public void onDone(String str) {
				ArrayList<MediaItem> listMedia = MediaItem.ParseMediaList(str);
				
				if (listMedia != null) {
					adapter = new MediaGridAdapter(parentActivity, listMedia);
					gvMedia.setAdapter(adapter);
				} 
				
				progress.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onConnectionError(String message) {
				if(dialog != null) dialog.dismiss();
				Toast.makeText(parentActivity, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
//		postTask.addPair("code", type);
//		postTask.addPair("lang", Setting.getInstance(parentActivity).getLangString());
		postTask.execute(URLAddress.MEDIA_URL + "?code=" + type + "&lang=" + Setting.getInstance(parentActivity).getLangString());
	}
	
	private void retrieveMediaDetail(String id){
		InternetTask postTask = new InternetTask(parentActivity, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(parentActivity, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response: " + str);
				MediaItem media = MediaItem.ParseMediaDetail(str);

				if (media.getDocumentType() == MediaItem.FILE_VIDEO || media.getDocumentType() == MediaItem.FILE_AUDIO) {
					Intent i = new Intent(parentActivity, VideoPlayerActivity.class);
					i.putExtra("url", media.getUrl());
					startActivity(i);
				} else if(media.getDocumentType() == MediaItem.FILE_PDF){
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(media.getUrl()));
					startActivity(browserIntent);
				}
				
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(parentActivity, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
//		postTask.addPair("type", type);
//		postTask.addPair("id", id);
		postTask.execute(URLAddress.MEDIA_DETAIL_URL + "?id=" + id);
	}
	
	private void retrieveMediaDetailForShare(String id){
		InternetTask postTask = new InternetTask(parentActivity, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(parentActivity, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response: " + str);
				try {
					JSONObject obj = new JSONObject(str);
					String data = obj.getString("data");
					MediaItem media = MediaItem.ParseMedia(data);
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("text/plain");
					i.putExtra(Intent.EXTRA_SUBJECT, media.getTitle());
					i.putExtra(Intent.EXTRA_TEXT, media.getDescription() + "\n\n" + media.getUrl());
					startActivity(Intent.createChooser(i, "Share via:"));
					
					dialog.dismiss();
				} catch (JSONException e) {
					e.printStackTrace();
					dialog.dismiss();
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(parentActivity, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
//		postTask.addPair("type", type);
//		postTask.addPair("id", id);
		postTask.execute(URLAddress.MEDIA_DETAIL_URL + "?id=" + id);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View newView = inflater.inflate(R.layout.activity_media, null);
	    ViewGroup rootView = (ViewGroup) view;
	    rootView.removeAllViews();
	    rootView.addView(newView);
	    initView(rootView);
	    
	    if (adapter != null) {
	    	gvMedia.setAdapter(adapter);
		}
	}
}
