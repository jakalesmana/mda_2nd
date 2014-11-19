package com.dyned.mydyned.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SocialMedia {

	private String title;
	private List<Social> listItem;
	
	public SocialMedia() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Social> getListItem() {
		return listItem;
	}

	public void setListItem(List<Social> listItem) {
		this.listItem = listItem;
	}
	
	public static List<SocialMedia> ParseSocialMediaList(String data){
		ArrayList<SocialMedia> listSocmed = new ArrayList<SocialMedia>();
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray arrJson = obj.getJSONArray("data");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject supportJsonObj = arrJson.getJSONObject(i);
				SocialMedia socmed = ParseSocialMedia(supportJsonObj.toString());
				listSocmed.add(socmed);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return new ArrayList<SocialMedia>();
		}
		
		return listSocmed;
	}

	private static SocialMedia ParseSocialMedia(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			String title = obj.getString("title");
			SocialMedia item = new SocialMedia();
			item.setTitle(title);
			item.setListItem(Social.ParseSocialList(obj.getString("list")));
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}

	
}
