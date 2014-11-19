package com.dyned.mydyned.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


public class Server {

	private String status;
	private String name;
	private String message;
	
	public Server() {
		status = "";
		name = "";
		message = "";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static List<Server> parseServer(String data){
		try {
			List<Server> res = new ArrayList<Server>();
			JSONArray obj = new JSONArray(data);
			for (int i = 0; i < obj.length(); i++) {
				String status = obj.getJSONObject(i).getString("status");
				String name = obj.getJSONObject(i).getString("name");
				String message = obj.getJSONObject(i).getString("message");
				Server s = new Server();
				s.setStatus(status);
				s.setName(name);
				s.setMessage(message);
				res.add(s);
			}
			return res;
		} catch (JSONException e) {
			return new ArrayList<Server>();
		}
	}
}
