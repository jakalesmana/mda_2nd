package com.dyned.mydyned;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.dyned.mydyned.utils.NotificationUtil;

public class NotifHandlerActivity extends SherlockActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationUtil.getInstance().clear();
		
		Intent marketIntent = new Intent(Intent.ACTION_VIEW);
		marketIntent.setData(Uri.parse("http://pistarlabs.net/projects/dyned/MyDynEd/"));
		startActivity(marketIntent);
		
		finish();
	}
}
