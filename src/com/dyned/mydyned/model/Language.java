package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Language implements Serializable {
	
	private String code;
	private String name;
	
	public Language() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static List<Language> parseLanguages(String data){
		try {
			List<Language> list = new ArrayList<Language>();
			JSONObject obj = new JSONObject(data);
			JSONArray arr = obj.getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				Language c = new Language();
				c.setCode(arr.getJSONObject(i).getString("code"));
				c.setName(arr.getJSONObject(i).getString("name"));
				list.add(c);
			}
			return list;
		} catch (JSONException e) {
			return null;
		}
	}
}
