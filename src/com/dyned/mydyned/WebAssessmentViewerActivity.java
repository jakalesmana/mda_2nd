package com.dyned.mydyned;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.utils.PreferencesUtil;

/**
author: jakalesmana
 */

public class WebAssessmentViewerActivity extends BaseActivity {
	
	private PreferencesUtil pref;
//	private final int SIGN_IN_STATUS = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webviewer);
		setTitle(HomeFragment.ASESSMENT);
		
		pref = PreferencesUtil.getInstance(this);
		
//		if (pref.isLoggedIn()) {
			String url = getIntent().getStringExtra("url_menu");
			loadUrl(url + "?app_key=" + pref.getAppKey());
//		} else {
//			Intent i = new Intent(this, SignInActivity.class);
//			startActivityForResult(i, SIGN_IN_STATUS);
//		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void loadUrl(String url) {
		WebView wvContent = (WebView)findViewById(R.id.wvContent);
//		wvContent.getSettings().setSupportZoom(true);
		wvContent.getSettings().setDefaultZoom(ZoomDensity.FAR);
		wvContent.getSettings().setJavaScriptEnabled(true);
		wvContent.loadUrl(url);
		wvContent.setWebViewClient(new MyWebViewClient());
	}

	private class MyWebViewClient extends WebViewClient {
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	       return false;
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (pref.isLoggedIn()) {
			String url = getIntent().getStringExtra("url_menu");
			loadUrl(url + "?app_key=" + pref.getAppKey());
		} else {
			finish();
		}
	}
}
