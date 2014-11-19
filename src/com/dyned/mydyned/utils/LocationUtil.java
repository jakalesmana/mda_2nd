package com.dyned.mydyned.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.baidu.location.BDLocation;

public class LocationUtil {
	private Timer timer1;
	private LocationManager lm;
	private LocationResult locationResult;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private static boolean google_connect = false;
	private static String myGCode;
	private static String myBDCode;
	private static Location myGLoc;
	private static BDLocation myBDLoc;
	private static String codeByIP;

	public static void setPingGoogleSuccess(boolean success){
		google_connect = success;
	}
	
	public static boolean isPingGoogleSucceded(){
		return google_connect;
	}
	
	public static void setCountryCodeByIP(String code){
		codeByIP = code;
	}
	
	public static String getCountryCodeByIP(){
		return codeByIP;
	}
	
	public static void setCountryCode(String code){
		myGCode = code;
	}
	
	public static void setBDCountryCode(String code){
		myBDCode = code;
	}
	
	public static void setMyLocation(Location loc){
		myGLoc = loc;
	}
	
	public static void setMyLocation(BDLocation loc){
		myBDLoc = loc;
	}
	
	public static Location getMyLocation(){
		return myGLoc;
	}
	
	public static BDLocation getMyBDLocation(){
		return myBDLoc;
	}
	public static String getCountryCode(){
		if (myGCode == null) {
			return "";
		}
		return myGCode;
	}
	
	public static String getBDCountryCode(){
		if (myBDCode == null) {
			return "";
		}
		return myBDCode;
	}
	
	public void fetchLocation(Context context, LocationResult result) {
		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled)
			return;
		
		if (gps_enabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		if (network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		timer1 = new Timer();
		timer1.schedule(new GetLastLocation(), 20000);
	}
	
	public static String parseArea(Context context, Location location){
		try {
			Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
			List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (list != null & list.size() > 0) {
	            Address address = list.get(0);
//	            String result = address.getAddressLine(1).split(" ")[0];
	            String result = address.getLocality();
	            return result;
	        }
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
		return null;
	} 
	
	public static String getCountryCode(Context context, Location location){
		try {
			Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
			List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (list != null & list.size() > 0) {
	            Address address = list.get(0);
	            String result = address.getCountryCode();
	            if (result == null) {
					return "";
				}
	            return result;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (Exception e) {
			return "";
		}
		return "";
	}
	
	public static String getBDCountryCode(BDLocation location){
		if (location == null) {
			return "";
		}
		String city = location.getCity();
		if (city == null) {
			return "";
		}
		if (!city.equals("")) {
			return "CN";
		}
		return "";
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			locationResult.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					locationResult.gotLocation(gps_loc);
				else
					locationResult.gotLocation(net_loc);
				return;
			}

			if (gps_loc != null) {
				locationResult.gotLocation(gps_loc);
				return;
			}
			if (net_loc != null) {
				locationResult.gotLocation(net_loc);
				return;
			}
			locationResult.gotLocation(null);
		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
}