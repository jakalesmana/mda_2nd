package com.dyned.mydyned.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dyned.mydyned.NotifHandlerActivity;
import com.dyned.mydyned.utils.NotificationUtil;

public class GCMReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
	        handleRegistration(context, intent);
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	        handleMessage(context, intent);
	    }
	}

	private void handleRegistration(Context context, Intent intent) {
	    String registration = intent.getStringExtra("registration_id");
	    if (intent.getStringExtra("error") != null) {
	        // Registration failed, should try again later.
		    Log.d("C2DM", "registration failed");
		    String error = intent.getStringExtra("error");
		    if(error == "SERVICE_NOT_AVAILABLE"){
		    	Log.d("C2DM", "SERVICE_NOT_AVAILABLE");
		    }else if(error == "ACCOUNT_MISSING"){
		    	Log.d("C2DM", "ACCOUNT_MISSING");
		    }else if(error == "AUTHENTICATION_FAILED"){
		    	Log.d("C2DM", "AUTHENTICATION_FAILED");
		    }else if(error == "TOO_MANY_REGISTRATIONS"){
		    	Log.d("C2DM", "TOO_MANY_REGISTRATIONS");
		    }else if(error == "INVALID_SENDER"){
		    	Log.d("C2DM", "INVALID_SENDER");
		    }else if(error == "PHONE_REGISTRATION_ERROR"){
		    	Log.d("C2DM", "PHONE_REGISTRATION_ERROR");
		    } else {
		    	Log.d("C2DM", "C2DM: " + error);
		    }
	    } else if (intent.getStringExtra("unregistered") != null) {
	        // unregistration done, new messages from the authorized sender will be rejected
	    	Log.i("C2DM", "unregistered");
	    } else if (registration != null) {
	    	System.out.println("gcm id: " + registration);
	    	MDAGCMRegistrar.sendDeviceIdToServer(context, registration);
	    }
	}
	
	private void handleMessage(Context context, Intent intent)
	{
		System.out.println("data pushed");
		
		String type = intent.getExtras().getString("type");
		String data = intent.getExtras().getString("data");
		
		System.out.println("pushed: " + type);
		System.out.println("data: " + data);
		
		if (type.equals("update")) {
			NotificationUtil.getInstance().show(context, "New Update Available", "MyDynEd", data, NotifHandlerActivity.class);
		}
	}
}
