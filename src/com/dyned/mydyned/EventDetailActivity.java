package com.dyned.mydyned;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.dyned.mydyned.model.Event;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
author: jakalesmana
 */

public class EventDetailActivity extends SherlockActivity {
	
	private DisplayImageOptions optionsAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Event event;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		
		optionsAvatar = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_grey).showImageForEmptyUri(R.drawable.bg_grey)
				.cacheOnDisc().cacheInMemory().build();
		
		int width = (AppUtil.GetScreenWidth(this) / 2);
		
		event = (Event) getIntent().getSerializableExtra("event");
		
		System.out.println("event detail: " + event.getTitle());
		
		TextView txtTitle = (TextView)findViewById(R.id.txtNewsTitle);
		ImageView imgContent = (ImageView)findViewById(R.id.imgContent);
		TextView txtContent = (TextView)findViewById(R.id.txtContent);
		
		txtTitle.setText(event.getTitle());
		txtContent.setText(Html.fromHtml("Start: " + event.getStartDate() + "<br/>End: " + event.getEndDate() + "\n\n" + event.getDescription()));
		
		imgContent.getLayoutParams().width = width;
		imgContent.getLayoutParams().height = width;
		imageLoader.displayImage(event.getImage(), imgContent, optionsAvatar, new AnimateFirstDisplayListener(event.getImage(), imgContent, false, 0, 0));
		setupActionBar();
	}
	
	private void setupActionBar(){
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.header_share, null);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(v);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		FrameLayout share = (FrameLayout) v.findViewById(R.id.layoutShare);
		share.setOnClickListener(new OnClickHandler(OnClickHandler.SHARE));
	}
	
	private class OnClickHandler implements OnClickListener {
		public static final int SHARE = 0;
		int index;
		public OnClickHandler(int index) {
			this.index = index;
		}
		public void onClick(View v) {
			v.startAnimation(AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.image_click));
			v.postDelayed(new Runnable() {
				public void run() {	
					switch (index) {
					case SHARE:
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("text/plain");
						i.putExtra(Intent.EXTRA_SUBJECT, event.getTitle());
						i.putExtra(Intent.EXTRA_TEXT, event.getUrl());
						startActivity(Intent.createChooser(i, "Share via:"));
						break;
					}
				}
			}, 200);
		}
	}
}
