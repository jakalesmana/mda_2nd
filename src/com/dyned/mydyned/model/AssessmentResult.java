package com.dyned.mydyned.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class AssessmentResult implements Serializable {
	private String code;
	private String message;
	private String link;
	private String description;
	
	public AssessmentResult() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static AssessmentResult parseResult(String json){
		try {
			AssessmentResult res = new AssessmentResult();
			JSONObject obj = new JSONObject(json);
			res.setCode(obj.getString("code"));
			res.setDescription(obj.getString("desc"));
			res.setLink(obj.getString("android"));
			res.setMessage(obj.getString("message"));
			return res;
		} catch (JSONException e) {
			return null;
		}
	}
}
