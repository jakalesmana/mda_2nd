package com.dyned.mydyned;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.utils.PreferencesUtil;

/**
author: jakalesmana
 */

public class WebRecordsViewerActivity extends BaseActivity {
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	private PreferencesUtil pref;
	private final int SIGN_IN_STATUS = 0;
	private boolean firstShow = true;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webviewer);
		
		setTitle(HomeFragment.RECORDS);
		
		pref = PreferencesUtil.getInstance(this);
		
		if (pref.isLoggedIn()) {
			String url = getIntent().getStringExtra("url_menu");
			
			System.out.println("load url web: " + url + "?app_key=" + pref.getAppKey());
			loadUrl(url + "?app_key=" + pref.getAppKey() + "&mode=webview&layout=mobile");
		} else {
			Intent i = new Intent(this, SignInActivity.class);
			startActivityForResult(i, SIGN_IN_STATUS);
		}
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
	    
	    @Override
	    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
	    	handler.proceed();
	    }
	    
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	super.onPageStarted(view, url, favicon);
	    	if (firstShow) {
//	    		firstShow = false;
//	    		handler.post(new Runnable() {
//					public void run() {
//				    	dialog = ProgressDialog.show(WebRecordsViewerActivity.this, "", "Loading..");				
//					}
//				});
			}
	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    	super.onPageFinished(view, url);
	    	if(dialog != null) dialog.dismiss();
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (pref.isLoggedIn()) {
			String url = getIntent().getStringExtra("url_menu");
			loadUrl(url + "?app_key=" + pref.getAppKey() + "&mode=webview&layout=mobile");
		} else {
			finish();
		}
	}
}
