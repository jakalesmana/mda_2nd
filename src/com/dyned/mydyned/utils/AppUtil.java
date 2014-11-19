package com.dyned.mydyned.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AppUtil {

	private static List<Activity> activities = new ArrayList<Activity>();
	
	public static int GetScreenWidth(Activity act) {
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	public static int GetScreenHeight(Activity act) {
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	public static boolean IsSDCARDMounted(){
		String status = Environment.getExternalStorageState();
	    if (status.equals(Environment.MEDIA_MOUNTED)) return true;
	    return false;
	}
	
	public static void HideKeyboard(IBinder windowToken, Context context){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(windowToken, 0);
	}
	
	public static void ShowKeyboard(View view, Context context){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);		
		imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}
	
	public static boolean IsNetworkConnected(Context context){
		ConnectivityManager conMgr =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		  if (i == null)
		    return false;
		  if (!i.isConnected())
		    return false;
		  if (!i.isAvailable())
		    return false;
		  
		  return true;
	}
	
	public static String getLocalIpv4Address(){
	    try {
	        String ipv4;
	        List<NetworkInterface>  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
	        if(nilist.size() > 0){
	            for (NetworkInterface ni: nilist){
	                List<InetAddress>  ialist = Collections.list(ni.getInetAddresses());
	                if(ialist.size()>0){
	                    for (InetAddress address: ialist){
	                        if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress())){ 
	                            return ipv4;
	                        }
	                    }
	                }
	            }
	        }

	    } catch (SocketException ex) {
	    	return "";
	    }
	    return "";
	}
	
	public static boolean IsGooglePlayServiceInstalled(Context context) {
		try {
			context.getPackageManager().getApplicationInfo("com.google.android.gms", 0);
			return true;
		} catch(PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	
	public static boolean IsAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch(PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	
	public static void AddActivityHistory(Activity activity){
		activities.add(activity);
	}
	
	public static void ClearActivityHistory(){
		for (int i = 0; i < activities.size(); i++) {
			if (activities.get(i) != null) {
				activities.get(i).finish();
			}
		}
	}
	
	public static String getVersionName(Context ctx) {
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static int getVersionCode(Context ctx) {
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	
	public static String getApplicationName(Context context){
		Resources appR = context.getResources(); 
		String txt = (String) appR.getText(appR.getIdentifier("app_name",  "string", context.getPackageName()));
		return txt;
    }
	
	public static String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return capitalize(model);
		  } else {
		    return capitalize(manufacturer) + " " + model;
		  }
		}

	private static String capitalize(String s) {
	  if (s == null || s.length() == 0) {
	    return "";
	  }
	  char first = s.charAt(0);
	  if (Character.isUpperCase(first)) {
	    return s;
	  } else {
	    return Character.toUpperCase(first) + s.substring(1);
	  }
	} 
	
	public static String getOsversion(){
		return Build.VERSION.RELEASE;
	}
	
	public static boolean hasCallPhoneFeature(Context c){
		PackageManager pm = c.getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
		    return true;
		}
		return false;
	}
	
	public static boolean hasLocationFeature(Context c){
		PackageManager pm = c.getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
		    return true;
		}
		return false;
	}
	
	public static boolean hasGPSFeature(Context c){
		PackageManager pm = c.getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
		    return true;
		}
		return false;
	}
	
}
