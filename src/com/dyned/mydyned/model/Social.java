package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
author: jakalesmana
 */

@SuppressWarnings("serial")
public class Social implements Serializable {
	
	public static int TYPE_TWITTER = 0;
	public static int TYPE_FACEBOOK = 1;
	public static int TYPE_WEIBO = 2;
	
	private int type;
	private String label;
	private String url;
	private String shortcut;
	
	public Social() {
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static List<Social> ParseSocialList(String data) {
		ArrayList<Social> listSocialItem = new ArrayList<Social>();
		try {
			JSONArray arrJson = new JSONArray(data);
			for (int i = 0; i < arrJson.length(); i++) {
				Social item = Social.ParseSocial(arrJson.get(i).toString());
				listSocialItem.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return listSocialItem;
	}
	
	public static Social ParseSocial(String data){
		try {
			JSONObject obj = new JSONObject(data);
			String title = obj.getString("title");
			String url = obj.getString("url");
			String shortcut = obj.getString("shortcut");
			Social item = new Social();
			item.setUrl(url);
			item.setLabel(title);
			item.setShortcut(shortcut);
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}
	
}
