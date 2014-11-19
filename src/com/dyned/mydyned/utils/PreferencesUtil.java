package com.dyned.mydyned.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	private static PreferencesUtil instance = null;
	private static SharedPreferences myPref;
	
	private final String APP_KEY = "app_key";
	private final String GCM_ID = "gcm_id";
	private final String LOGGEN_IN = "logged_in";
	private final String NAME = "name";
	private final String ROLE = "role";
	private final String ROLE_TITLE = "role_title";
	private final String AVATAR = "avatar";
	
	private final String APP_DESC = "app_desc";
	private final String LINK_STORE = "link_store";
	private final String CHINA_LINK_STORE = "china_link_store";
	
	private final String MEDIA_HINT = "media_hint";

	private PreferencesUtil() {
	}

	public static PreferencesUtil getInstance(Context context) {
		if (instance == null) {
			instance = new PreferencesUtil();
			myPref = context.getSharedPreferences("MyDynEd", 0);
		}
		return instance;
	}
	
	public void setAppDescription(String desc){
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(APP_DESC, desc);
		editor.commit();
	}
	
	public void setLinkStore(String link){
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(LINK_STORE, link);
		editor.commit();
	}
	
	public void setChinaLinkStore(String link){
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(CHINA_LINK_STORE, link);
		editor.commit();
	}
	
	public String getAppDesc() {
		return myPref.getString(APP_DESC, "");
	}
	
	public String getLinkStore() {
		return myPref.getString(LINK_STORE, "");
	}
	
	public String getChinaLinkStore() {
		return myPref.getString(CHINA_LINK_STORE, "");
	}
	
	public void setLoggedIn(boolean isLoggedIn) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putBoolean(LOGGEN_IN, isLoggedIn);
		editor.commit();
	}

	public boolean isLoggedIn() {
		return myPref.getBoolean(LOGGEN_IN, false);
	}
	
	public void setName(String name) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(NAME, name);
		editor.commit();
	}

	public String getname() {
		return myPref.getString(NAME, "");
	}
	
	public void setRoleKey(String role) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(ROLE, role);
		editor.commit();
	}

	public String getRoleKey() {
		String roleKey = myPref.getString(ROLE, "general_user");
		if (roleKey.isEmpty()) {
			return "general_user";
		} else {
			return roleKey;
		}
	}
	
	public void setRoleTitle(String role) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(ROLE_TITLE, role);
		editor.commit();
	}

	public String getRoleTitle() {
		String title =  myPref.getString(ROLE_TITLE, "General User");
		if (title.isEmpty()) {
			return "General User";
		} else {
			return title;
		}
	}
	
	public void setAvatar(String image_url) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(AVATAR, image_url);
		editor.commit();
	}

	public String getAvatar() {
		return myPref.getString(AVATAR, "");
	}
	
	public void setMediaHint(boolean hint) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putBoolean(MEDIA_HINT, hint);
		editor.commit();
	}

	public boolean getMediaHint() {
		return myPref.getBoolean(MEDIA_HINT, false);
	}
	
	public void setAppKey(String appkey) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(APP_KEY, appkey);
		editor.commit();
	}
	
	public void setGCMID(String id) {
		SharedPreferences.Editor editor = myPref.edit();
		editor.putString(GCM_ID, id);
		editor.commit();
	}

	public String getAppKey() {
		return myPref.getString(APP_KEY, "");
	}
	
	public String getGCMID() {
		return myPref.getString(GCM_ID, "");
	}

	public void logout() {
		setLoggedIn(false);
		setAppKey("");
		setAvatar("");
		setName("");
		setRoleKey("");
		setRoleTitle("");
	}
	
	
	
	//DEVELOPMENT ONLY, ONCE API READY< STRIP THIS!!!!!
		public static String buildRoleKey(String defaultRoleKey, String email){
			if (email.endsWith("@dyned.com") || email.endsWith("@dyned.com.cn") || email.endsWith("@dyned.com.tr") ||
					email.endsWith("@dynedeurope.com") || email.endsWith("@dynedlatam.com")) {
				return "dyned_staff";
			} else {
				return defaultRoleKey;
			}
		}
		
		//DEVELOPMENT ONLY, ONCE API READY< STRIP THIS!!!!!
		public static String buildRoleTitle(String defaultRoleTitle, String email){
			if (email.endsWith("@dyned.com") || email.endsWith("@dyned.com.cn") || email.endsWith("@dyned.com.tr") ||
					email.endsWith("@dynedeurope.com") || email.endsWith("@dynedlatam.com")) {
				return "DynEd Staff";
			} else {
				return defaultRoleTitle;
			}
		}
}
