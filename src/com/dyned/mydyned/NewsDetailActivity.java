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
import com.dyned.mydyned.model.News;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
author: jakalesmana
 */

public class NewsDetailActivity extends BaseActivity {
	
	private DisplayImageOptions optionsAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private News news;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		
		optionsAvatar = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_grey).showImageForEmptyUri(R.drawable.bg_grey)
				.cacheOnDisc().cacheInMemory().build();
		
		int width = (AppUtil.GetScreenWidth(this) / 2);
		
		news = (News) getIntent().getSerializableExtra("news");
		
		TextView txtTitle = (TextView)findViewById(R.id.txtNewsTitle);
		ImageView imgContent = (ImageView)findViewById(R.id.imgContent);
		TextView txtContent = (TextView)findViewById(R.id.txtContent);
		
		System.out.println("news title: " + news.getTitle());
		
		txtTitle.setText(news.getTitle());
		txtContent.setText(Html.fromHtml(news.getDescription()));
		
		imgContent.getLayoutParams().width = width;
		imgContent.getLayoutParams().height = width;
		imageLoader.displayImage(news.getImage(), imgContent, optionsAvatar, new AnimateFirstDisplayListener(news.getImage(), imgContent, false, 0, 0));
		
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
			v.startAnimation(AnimationUtils.loadAnimation(NewsDetailActivity.this, R.anim.image_click));
			v.postDelayed(new Runnable() {
				public void run() {	
					switch (index) {
					case SHARE:
						Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("text/plain");
						i.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());
						i.putExtra(Intent.EXTRA_TEXT, news.getUrl());
						startActivity(Intent.createChooser(i, "Share via:"));
						break;
					}
				}
			}, 200);
		}
	}
}
