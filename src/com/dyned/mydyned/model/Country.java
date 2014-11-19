package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Country implements Serializable {
	
	private String dialCode;
	private String name;
	private String countryCode;
	private boolean isEurope;
	
	public Country() {
	}

	public boolean isEurope() {
		return isEurope;
	}

	public void setEurope(boolean isEurope) {
		this.isEurope = isEurope;
	}

	public String getDialCode() {
		return dialCode;
	}

	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public static List<Country> parseCountries(String data){
		try {
			List<Country> list = new ArrayList<Country>();
			JSONObject obj = new JSONObject(data);
			JSONArray arr = obj.getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				Country c = new Country();
				c.setCountryCode(arr.getJSONObject(i).getString("country_code"));
				c.setDialCode(arr.getJSONObject(i).getString("dial_code"));
				c.setName(arr.getJSONObject(i).getString("name"));
				c.setEurope(arr.getJSONObject(i).getBoolean("is_europe"));
				list.add(c);
			}
			return list;
		} catch (JSONException e) {
			return new ArrayList<Country>();
		}
	}
}
