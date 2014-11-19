package com.dyned.mydyned;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.fragment.LeftMenuFragment;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.LocationUtil;
import com.dyned.mydyned.utils.LocationUtil.LocationResult;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
author: jakalesmana
 */

public class HomeFragmentActivity extends SlidingFragmentActivity {
	
	protected ActionBar actionBar;
	private SlidingMenu sm;
	private Fragment contentFragment;
	private LocationClient mLocClient;
	private boolean isRetrieved = false;
	private BDLocHandler locHandler = new BDLocHandler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_slider);
		
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this, ""));
		
		if (savedInstanceState != null)
			contentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "contentFragment");
		if (contentFragment == null)
			contentFragment = new HomeFragment();
		
		setupHeader();
		switchContent(contentFragment);
		setupSlidingMenu();
		setupLeftMenu();
		
		retrieveLocation();
		retrieveBaiduLocation();
		pingGoogle();
	}
	
	private void pingGoogle() {
		InternetTask internetTask = new InternetTask(this, new InternetConnectionListener() {
			public void onStart() {}
			
			public void onDone(String str) {
				System.out.println("ping google 200");
				LocationUtil.setPingGoogleSuccess(true);
			}

			@Override
			public void onConnectionError(String message) {
				System.out.println("ping google NOT 200");
				LocationUtil.setPingGoogleSuccess(false);
			}
		});
		internetTask.execute("http://www.google.com");
	}

	private void retrieveLocation() {
    	LocationResult locationResult = new LocationResult(){
            @Override
            public void gotLocation(Location location){
            	if (location != null) {
                	LocationUtil.setMyLocation(location);
                	LocationUtil.setCountryCode(LocationUtil.getCountryCode(HomeFragmentActivity.this, location));
				}
            }
        };
        LocationUtil myLocation = new LocationUtil();
        myLocation.fetchLocation(this, locationResult);
	}
	
	private void retrieveBaiduLocation(){
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(locHandler);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private void setupHeader() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.header, null);
		
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setCustomView(v);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
//		ImageView imgLogo = (ImageView) v.findViewById(R.id.imgLogo);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setVisibility(View.GONE);
		
		FrameLayout back = (FrameLayout) v.findViewById(R.id.layoutBack);
		FrameLayout login = (FrameLayout) v.findViewById(R.id.layoutLogin);
		login.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				showMenu();
			}
		});
	}
	
	private void setupSlidingMenu() {
		int offset = AppUtil.GetScreenWidth(this) / 6;
		sm = getSlidingMenu();
		sm.setShadowWidth(offset / 4);
		sm.setBehindOffset(offset);
		sm.setFadeDegree(0.35f);
		sm.setMode(SlidingMenu.LEFT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindScrollScale(0.0f);
	}
	
	private void setupLeftMenu(){
		setBehindContentView(R.layout.activity_left_menu_fragment);
		LeftMenuFragment lmf = new LeftMenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layoutLeftMenuFragment, lmf).commit();
	}
	
	public void switchContent(Fragment fragment) {
		contentFragment = fragment;
		getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment).commit();
		getSlidingMenu().showContent();
	}
	
	private class BDLocHandler implements BDLocationListener {		
		public void onReceivePoi(BDLocation loc) {}
		
		public void onReceiveLocation(BDLocation loc) {
			if (!isRetrieved) {
				LocationUtil.setMyLocation(loc);
	        	LocationUtil.setBDCountryCode(LocationUtil.getBDCountryCode(loc));
				if(mLocClient != null) mLocClient.stop();
				isRetrieved = true;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		if(mLocClient.isStarted()) mLocClient.stop();
		super.onDestroy();
	}
}
