package com.dyned.mydyned;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dyned.mydyned.utils.AppUtil;

public class WebViewerActivity extends BaseActivity {
	
	private final String WECHAT = "weixin";
	private final String WECHAT_PKG = "com.tencent.mm";
	
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webviewer);
		
		String url = getIntent().getStringExtra("url_menu");
		
		if(checkUrl(url)) return;
		
		WebView wvContent = (WebView)findViewById(R.id.wvContent);		
//		wvContent.getSettings().setSupportZoom(true);
		wvContent.getSettings().setDefaultZoom(ZoomDensity.FAR);
		wvContent.getSettings().setJavaScriptEnabled(true);
		wvContent.loadUrl(url);
		wvContent.setWebViewClient(new MyWebViewClient());
	}
	
	private boolean checkUrl(String url) {
		if (url.contains(WECHAT)) {
			if (AppUtil.IsAppInstalled(WebViewerActivity.this, WECHAT_PKG)) {
    			Intent in = getPackageManager().getLaunchIntentForPackage(WECHAT_PKG);
    			if (in != null) {
    				startActivity(in);
    				finish();
				} else {
					installWeixin();
				}
			} else {
				installWeixin();
			}
			return true;
		}
		return false;
	}

	private void installWeixin(){
		Toast.makeText(this, "WeChat is not installed. Please install WeChat first.", Toast.LENGTH_SHORT).show();
		String weixinUrl = "http://weixin.qq.com/cgi-bin/readtemplate?uin=&stype=&promote=&fr=www.baidu.com&lang=zh_CN&ADTAG=&check=false&t=weixin_download_method&sys=android&loc=weixin,android,web,0";
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(weixinUrl));
		startActivity(browserIntent);
		finish();
	}
	
	private class MyWebViewClient extends WebViewClient {
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	System.out.println("redirect webview: " + url);
	    	if (Uri.parse(url).getScheme().equals("market")) {
	    		Uri uri = Uri.parse(url);
	    		if (AppUtil.IsAppInstalled(WebViewerActivity.this, uri.getQueryParameter("id"))) {
	    			Intent in = getPackageManager().getLaunchIntentForPackage(uri.getQueryParameter("id"));
	    			if (in != null) {
	    				startActivity(in);
	    				finish();
	    				return false;
					} else {
						return openMarket(view, uri);
					}
					
				} else {
					return openMarket(view, uri);
				}
			} else {
				view.loadUrl(url);
				return false;
			}
	    }
	}
	
	private boolean openMarket(WebView view, Uri uri){
		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
    		i.setData(uri);
    		startActivity(i);
    		finish();
    		return true;
		} catch (ActivityNotFoundException e) {
			view.loadUrl("http://play.google.com/store/apps/" + uri.getHost());
			return false;
		}
	}
	
	@Override
	protected void onStop() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		super.onStop();
	}

}
