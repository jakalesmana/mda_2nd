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
public class Event implements Serializable{
	private String id;
	private String title;
	private String description;
	private String image;
	private String startDate;
	private String endDate;
	private String url;
	
	public Event() {
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
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public static ArrayList<Event> ParseEventList(String data){
		ArrayList<Event> listEvent = new ArrayList<Event>();
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray arrJson = obj.getJSONArray("data");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject mediaJsonObj = arrJson.getJSONObject(i);
				Event event = ParseEvent(mediaJsonObj.toString());
				listEvent.add(event);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return listEvent;
	}

	public static Event ParseEvent(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			String id = obj.getString("id");
			String title = obj.getString("title");
			String image = obj.getString("image");
			String startDate = obj.getString("start_date");
			String endDate = obj.getString("end_date");
			String desc = (obj.has("description") ? obj.getString("description") : "");
			String url = (obj.has("url") ? obj.getString("url") : "");
			Event item = new Event();
			item.setId(id);
			item.setImage(image);
			item.setDescription(desc);
			item.setTitle(title);
			item.setStartDate(startDate);
			item.setEndDate(endDate);
			item.setUrl(url);
			
			return item;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
