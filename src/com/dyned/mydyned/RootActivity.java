package com.dyned.mydyned;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dyned.mydyned.fragment.BlankFragment;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.fragment.SupportFragment;
import com.dyned.mydyned.fragment.WebViewFragment;
import com.facebook.Session;

/**
author: jakalesmana
 */

public class RootActivity extends BaseFragmentActivity {
	
	private LinearLayout layoutHome, layoutSetting, layoutSupport, layoutServer; 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_root);
		
		View navigationView = ((ViewStub) findViewById(R.id.stub_navigation)).inflate();
		
		HomeFragment f = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, f).commit();
		
		FrameLayout layoutFragment = (FrameLayout)findViewById(R.id.layoutFragment);
		layoutHome = (LinearLayout)navigationView.findViewById(R.id.layoutHome);
		layoutSetting = (LinearLayout)navigationView.findViewById(R.id.layoutSetting);
		layoutSupport = (LinearLayout)navigationView.findViewById(R.id.layoutSupport);
		layoutServer = (LinearLayout)navigationView.findViewById(R.id.layoutServer);
		layoutHome.setOnClickListener(new ClickHandler());
		layoutSetting.setOnClickListener(new ClickHandler());
		layoutSupport.setOnClickListener(new ClickHandler());
		layoutServer.setOnClickListener(new ClickHandler());
		layoutHome.measure(layoutHome.getWidth(), layoutHome.getHeight());
		layoutFragment.setPadding(0, 0, 0, layoutHome.getMeasuredHeight());
		layoutHome.setBackgroundColor(0xff7a7a7a);
	}
	
	class ClickHandler implements OnClickListener {
		public void onClick(View v) {
			clearNavigationBackground();
			v.setBackgroundColor(0xff7a7a7a);
			if (layoutHome == v) {
				HomeFragment f = new HomeFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, f).commit();
			} else if(layoutServer == v){
				WebViewFragment f = new WebViewFragment();
				Bundle b = new Bundle();
				b.putString("url_menu", "http://www.dyned.com/cgi-bin/WebObjects/ServerStatus");
				f.setArguments(b);
				getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, f).commit();
			} else if(layoutSupport == v) {
				SupportFragment f = new SupportFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, f).commit();
			} else {
				BlankFragment f = new BlankFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, f).commit();
			}
		}
	}
	
	private void clearNavigationBackground(){
		layoutHome.setBackgroundColor(Color.TRANSPARENT);
		layoutSetting.setBackgroundColor(Color.TRANSPARENT);
		layoutSupport.setBackgroundColor(Color.TRANSPARENT);
		layoutServer.setBackgroundColor(Color.TRANSPARENT);
	}
	
	@Override
	public void onBackPressed() {
		logoutFacebook();
		super.onBackPressed();
	}
	
	private void logoutFacebook(){
		Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation(); //facebook logout
		}
	}
}
