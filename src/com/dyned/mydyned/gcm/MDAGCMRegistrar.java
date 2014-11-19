package com.dyned.mydyned.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dyned.mydyned.constant.Constant;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.PreferencesUtil;

public class MDAGCMRegistrar {	
    public static void registerGCM(Context context) {
    	System.out.println("register for gcm id");
    	
//    	new Thread(new Runnable() {			
//			public void run() {
//				GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
//		    	try {
//					String registrationId = gcm.register(ApplicationProperties.GCM_PROJECT_ID);
//					System.out.println("registrationId: " + registrationId);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
    	
//    	GCMRegistrar.checkDevice(context);
//    	GCMRegistrar.checkManifest(context);
    	
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", Constant.GCM_PROJECT_ID);
		context.startService(registrationIntent);
    }
    
    public static void sendDeviceIdToServer(final Context context, final String deviceId){
    	PostInternetTask internetTask = new PostInternetTask(context, new InternetConnectionListener() {			
			public void onStart() {
			}
			
			public void onDone(String str) {
				System.out.println("device id registrar: " + str);
				try {
					JSONObject obj = new JSONObject(str);
					if(obj.getBoolean("status")){
						PreferencesUtil.getInstance(context).setGCMID(deviceId);
					}
				} catch (JSONException e) {
				}
			}

			@Override
			public void onConnectionError(String message) {
			}
		});
    	internetTask.addPair("platform", "android");
    	internetTask.addPair("token", deviceId);
    	internetTask.execute(URLAddress.SET_DEVICE_ID);
    }
}