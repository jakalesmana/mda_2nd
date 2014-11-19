package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Answer implements Serializable {
	private int id;
	private String answer;
	
	public Answer(int id, String answer) {
		this.id = id;
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public static List<Answer> parseListAnswer(String json){
		try {
			List<Answer> list = new ArrayList<Answer>();
			JSONArray arr = new JSONArray(json);
			for (int i = 0; i < arr.length(); i++) {
				list.add(parseAnswer(arr.getJSONObject(i).toString()));
			}
			return list;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static Answer parseAnswer(String json){
		try {
			JSONObject obj = new JSONObject(json);
			Answer answer = new Answer(obj.getInt("id"), obj.getString("answer"));
			return answer;
		} catch (JSONException e) {
			return null;
		}
	}
}
