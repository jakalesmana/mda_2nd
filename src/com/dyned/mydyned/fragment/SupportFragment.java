package com.dyned.mydyned.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.HomeFragmentActivity;
import com.dyned.mydyned.R;
import com.dyned.mydyned.WebViewerActivity;
import com.dyned.mydyned.component.SupportMenuItem;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.SupportItem;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;

/**
author: jakalesmana
 */

public class SupportFragment extends SherlockFragment {
	
	private HomeFragmentActivity mParent;
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private LinearLayout layoutSupport;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mParent = (HomeFragmentActivity) getSherlockActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_support, container, false);
//		RelativeLayout layoutFaq = (RelativeLayout)view.findViewById(R.id.layoutFaq);
//		RelativeLayout layoutContact = (RelativeLayout)view.findViewById(R.id.layoutContact);
//		layoutFaq.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent i = new Intent(getSherlockActivity(), WebViewerActivity.class);
//				i.putExtra("url_menu", "http://m.dyned.com/us/support/");
//				startActivity(i);
//			}
//		});
//		layoutContact.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent i = new Intent(getSherlockActivity(), WebViewerActivity.class);
//				i.putExtra("url_menu", "http://m.dyned.com/us/contact");
//				startActivity(i);
//			}
//		});
		
		layoutSupport = (LinearLayout) view.findViewById(R.id.layoutSupport);
		retrieveData();
		
		return view;
	}

	private void retrieveData() {
		InternetTask getTask = new InternetTask(mParent, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(mParent, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response: " + str);
				List<SupportItem> listSupport = SupportItem.ParseSupportList(str);
				
				for (int i = 0; i < listSupport.size(); i++) {
					SupportMenuItem viewItem = new SupportMenuItem(mParent, listSupport.get(i), (i % 2 == 0) ? R.drawable.bg_bluesky_selectable:R.drawable.bg_pink_selectable);
					viewItem.setOnClickHandler(new OnClickListener() {
						public void onClick(View v) {
							SupportItem item = (SupportItem) v.getTag();
							Intent i = new Intent(mParent, WebViewerActivity.class);
							i.putExtra("url_menu", item.getUrl());
							startActivity(i);
						}
					});
					layoutSupport.addView(viewItem);
				}
				
				dialog.dismiss();
			}
			
			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(mParent, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		getTask.execute(URLAddress.SUPPORT_URL);
	}
}
