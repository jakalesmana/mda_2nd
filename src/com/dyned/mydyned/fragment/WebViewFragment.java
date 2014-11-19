package com.dyned.mydyned.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.R;

/**
 * author: jakalesmana
 */

public class WebViewFragment extends SherlockFragment {

	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_webviewer, container, false);

		String url = getArguments().getString("url_menu");

		WebView wvContent = (WebView) view.findViewById(R.id.wvContent);
		wvContent.getSettings().setJavaScriptEnabled(true);
		wvContent.loadUrl(url);
		wvContent.setWebViewClient(new MyWebViewClient());
		wvContent.setWebChromeClient(new WebChromeClient());
		// wvContent.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		return view;
	}

	private class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}
	}

//	private class AudioInterface {
//		Context mContext;
//
//		AudioInterface(Context c) {
//			mContext = c;
//		}
//
//		// Play an audio file from the webpage
//		@JavascriptInterface
//		public void playAudio(String aud) { // String aud - file name passed from the JavaScript function
//			final MediaPlayer mp;
//			try {
//				AssetFileDescriptor fileDescriptor = mContext.getAssets()
//						.openFd(aud);
//				mp = new MediaPlayer();
//				mp.setDataSource(fileDescriptor.getFileDescriptor(),
//						fileDescriptor.getStartOffset(),
//						fileDescriptor.getLength());
//				fileDescriptor.close();
//				mp.prepare();
//				mp.start();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
