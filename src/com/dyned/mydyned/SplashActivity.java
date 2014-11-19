package com.dyned.mydyned;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.gcm.MDAGCMRegistrar;
import com.dyned.mydyned.manager.AppManager;
import com.dyned.mydyned.model.App;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.LocationUtil;
import com.dyned.mydyned.utils.PreferencesUtil;
import com.facebook.Session;

public class SplashActivity extends SherlockActivity {
	
	private boolean timerDone;
	private boolean versionDone;
	private boolean appDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
				
		if (PreferencesUtil.getInstance(this).getGCMID().equals("")) {
			MDAGCMRegistrar.registerGCM(this);
		}
		
		logoutFacebook();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {	
			public void run() {
				if (versionDone && appDone) {
					readyGoToHome();
				} else {
					timerDone = true;
				}
			}
		}, 3000);
		
		requestVersion();
		requestDynEdApps();
	}
	
	private void requestDynEdApps(){
		InternetTask getTask = new InternetTask(this, new InternetConnectionListener() {
			public void onStart() {
			}
			
			public void onDone(String str) {
				System.out.println("response dyned apps: " + str);
				parseApp(str);
			}

			@Override
			public void onConnectionError(String message) {
				if (timerDone && versionDone) {
					readyGoToHome();
				} else {
					appDone = true;
				}
			}
		});
		getTask.execute(URLAddress.DYNED_APP_URL);
	}
	
	private void parseApp(String str) {
		try {
			List<App> apps = new ArrayList<App>();
			JSONObject obj = new JSONObject(str);
			JSONArray jsonArr = obj.getJSONArray("data");
			for (int i = 0; i < jsonArr.length(); i++) {
				apps.add(new App(jsonArr.getJSONObject(i).getString("package"), 
						jsonArr.getJSONObject(i).getString("name"), 
						jsonArr.getJSONObject(i).getString("images"),
						jsonArr.getJSONObject(i).getString("link_store"),
						jsonArr.getJSONObject(i).getString("china_link_store"),
						jsonArr.getJSONObject(i).getString("direct_link")));
			}
			
			AppManager.getInstance().setApps(apps);
			
			if (timerDone && versionDone) {
				readyGoToHome();
			} else {
				appDone = true;
			}
		} catch (JSONException e) {
			if (timerDone && versionDone) {
				readyGoToHome();
			} else {
				appDone = true;
			}
		}
	}
	
	private void requestVersion() {
		InternetTask getTask = new InternetTask(this, new InternetConnectionListener() {
			
			public void onStart() {
			}
			
			public void onDone(String str) {
				System.out.println("response version: " + str);
				parseVersion(str);
			}

			@Override
			public void onConnectionError(String message) {
				if (timerDone && appDone) {
					readyGoToHome();
				} else {
					versionDone = true;
				}
			}
		});
		getTask.execute(URLAddress.VERSION_CHECK);
	}

	private void logoutFacebook(){
		Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation(); //facebook logout
		}
	}
	
	private void parseVersion(String data) {
		try {
			System.out.println("data version: " + data);
			JSONObject obj = new JSONObject(data);
			JSONObject objV = obj.getJSONObject("data");
//			String version = objV.getString("version");
			String linkStore = objV.getString("link_store");
			String chinaLinkStore = objV.getString("china_link_store");
			String build = objV.getString("build");
			String desc = objV.getString("description");
			
			PreferencesUtil.getInstance(this).setAppDescription(desc);
			PreferencesUtil.getInstance(this).setLinkStore(linkStore);
			PreferencesUtil.getInstance(this).setChinaLinkStore(chinaLinkStore);
			checkVersion(build, linkStore);
		} catch (JSONException e) {
			if (timerDone && appDone) {
				readyGoToHome();
			} else {
				versionDone = true;
			}
		}
	}
	
	private void checkVersion(String build, String linkStore) {
		int num = Integer.parseInt(build.replace(".", ""));
		if (num > AppUtil.getVersionCode(this)) {
			showNewVersionDialog(linkStore);
		} 
		else {
			if (timerDone && appDone) {
				readyGoToHome();
			} else {
				versionDone = true;
			}
		}
	}

	private void showNewVersionDialog(final String linkStore) {
		new AlertDialog.Builder(this)
	    .setTitle("New Version Available")
	    .setMessage("Please update for the new version MyDynEd App.")
	    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkStore));
	        	startActivity(browserIntent);
	        	finish();
	        }
	     })
	    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if (timerDone && appDone) {
					readyGoToHome();
				} else {
					versionDone = true;
				}
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_info)
	     .show();
	}

	private void readyGoToHome(){
		//but first, retrieve location
		retrieveLocationByIp(AppUtil.getLocalIpv4Address());
	}
	
	private void goToHome(){
		startActivity(new Intent(SplashActivity.this, HomeFragmentActivity.class));
		finish();
	}

	private void retrieveLocationByIp(String localIpv4Address) {
		InternetTask internetTask = new InternetTask(this, new InternetConnectionListener() {
			public void onStart() {
			}
			
			public void onDone(String str) {
				System.out.println("response geocode: " + str);
				try {
					JSONObject obj = new JSONObject(str);
					String code = obj.getString("country_code");
					LocationUtil.setCountryCodeByIP(code);
					System.out.println("dyned code by ip: " + code);
					goToHome();
				} catch (JSONException e) {
					goToHome();
				}
			}

			@Override
			public void onConnectionError(String message) {
				goToHome();
			}
		});
		internetTask.execute(URLAddress.GEO_IP + localIpv4Address);
	}

}
