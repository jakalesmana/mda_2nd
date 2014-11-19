package com.dyned.mydyned.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupportItem {

	private int id;
	private String title;
	private String imageUrl;
	private String url;
	
	public SupportItem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static List<SupportItem> ParseSupportList(String data){
		ArrayList<SupportItem> listMedia = new ArrayList<SupportItem>();
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray arrJson = obj.getJSONArray("data");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject supportJsonObj = arrJson.getJSONObject(i);
				SupportItem support = ParseSupportItem(supportJsonObj.toString());
				listMedia.add(support);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return listMedia;
	}
	
	public static SupportItem ParseSupportItem(String data){
		try {
			JSONObject obj = new JSONObject(data);
			int id = obj.getInt("id");
			String title = obj.getString("title");
			String image = (obj.has("image") ? obj.getString("image") : "");
			String url = (obj.has("url") ? obj.getString("url") : "");

			SupportItem item = new SupportItem();
			item.setId(id);
			item.setImageUrl(image);
			item.setTitle(title);
			item.setUrl(url);
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}
}
