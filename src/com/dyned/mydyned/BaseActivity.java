package com.dyned.mydyned;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

/**
author: jakalesmana
 */

public class BaseActivity extends SherlockActivity {

	protected ActionBar actionBar;
	private ImageView imgLogo;
	private TextView txtTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
	}
	
	private void setupActionBar(){
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.header, null);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setCustomView(v);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		FrameLayout back = (FrameLayout) v.findViewById(R.id.layoutBack);
		FrameLayout login = (FrameLayout) v.findViewById(R.id.layoutLogin);
		back.setOnClickListener(new OnClickHandler(OnClickHandler.BACK));
		login.setOnClickListener(new OnClickHandler(OnClickHandler.LOGIN));
		
		back.setVisibility(View.GONE);
		login.setVisibility(View.GONE);
		
		imgLogo = (ImageView) v.findViewById(R.id.imgLogo);
		txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setVisibility(View.GONE);
	}
	
	protected void setTitle(String title){
		txtTitle.setText(title);
		txtTitle.setVisibility(View.VISIBLE);
		imgLogo.setVisibility(View.GONE);
	}
	
	private class OnClickHandler implements OnClickListener {
		public static final int BACK = 0;
		public static final int LOGIN = 1;
		int index;
		public OnClickHandler(int index) {
			this.index = index;
		}
		public void onClick(View v) {
			v.startAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.image_click));
			v.postDelayed(new Runnable() {
				public void run() {	
					switch (index) {
					case BACK:
						break;
					case LOGIN:
						startActivity(new Intent(BaseActivity.this, SignInActivity.class));
						break;
					}
				}
			}, 200);
		}
	}
}
