package com.dyned.mydyned.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dyned.mydyned.model.SerializedNameValuePair;

public class Setting {

	private static Setting instance;
	private static SharedPreferences appPreference;
	
	private final String MAP = "MyDynEdSetting_map";
	private final String LANGUAGE = "MyDynEdSetting_Language";
	
	private Setting() {
	}
	
	public static Setting getInstance(Context ctx){
		if(instance == null){
			instance = new  Setting();
			appPreference =  PreferenceManager.getDefaultSharedPreferences(ctx);
		}
		return instance;
	}
	
	public void setLang(List<SerializedNameValuePair> list){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getName() + ",");
		}
		SharedPreferences.Editor editor = appPreference.edit();
		editor.putString(LANGUAGE, sb.toString());
        editor.commit();
	}
	
	public String getLangString(){
		return appPreference.getString(LANGUAGE, "");
	}
	
	public List<SerializedNameValuePair> getLangList(){
		String langs =  getLangString();
		String[] langsArr = langs.split(",");
		List<SerializedNameValuePair> list = new ArrayList<SerializedNameValuePair>();
		for (int i = 0; i < langsArr.length; i++) {
			if (langsArr[i].compareTo("") != 0) {
				list.add(new SerializedNameValuePair(langsArr[i], ""));
			}
		}
		return list;
	}
	
	public void setDefaultMap(String map) {
		SharedPreferences.Editor editor = appPreference.edit();
		editor.putString(MAP, map);
		editor.commit();
	}

	public String getMap() {
		return appPreference.getString(MAP, "");
	}
	
}
