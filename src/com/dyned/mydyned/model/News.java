package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
author: jakalesmana
 */

@SuppressWarnings("serial")
public class News implements Serializable{
	private String id;
	private String title;
	private String description;
	private String image;
	private String url;
	
	public News() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public static ArrayList<News> ParseNewsList(String data){
		ArrayList<News> listNews = new ArrayList<News>();
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray arrJson = obj.getJSONArray("data");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject mediaJsonObj = arrJson.getJSONObject(i);
				News news = ParseNews(mediaJsonObj.toString());
				listNews.add(news);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return listNews;
	}

	public static News ParseNews(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			String id = obj.getString("id");
			String title = obj.getString("title");
			String image = obj.getString("image");
			String desc = (obj.has("description") ? obj.getString("description") : "");
			News item = new News();
			item.setId(id);
			item.setImage(image);
			item.setDescription(desc);
			item.setTitle(title);
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static News ParseNewsDetail(String data) {
		try {
			JSONObject jsonObj = new JSONObject(data);
			JSONObject obj = jsonObj.getJSONObject("data");
			String id = obj.getString("id");
			String title = obj.getString("title");
			String image = obj.getString("image");
			String desc = (obj.has("content") ? obj.getString("content") : "");
			String url = (obj.has("url") ? obj.getString("url") : "");
			News item = new News();
			item.setId(id);
			item.setImage(image);
			item.setDescription(desc);
			item.setTitle(title);
			item.setUrl(url);
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}
}
