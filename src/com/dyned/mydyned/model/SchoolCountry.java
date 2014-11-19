package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

/**
author: jakalesmana
 */

@SuppressWarnings("serial")
public class SchoolCountry implements Serializable {

	private String code;
	private String country;
	private boolean isEurope;
	private List<School> listSchool;
	
	public SchoolCountry() {
	}
	
	public boolean isEurope() {
		return isEurope;
	}
	
	public void setEurope(boolean isEurope) {
		this.isEurope = isEurope;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<School> getListSchool() {
		return listSchool;
	}

	public void setListSchool(List<School> city) {
		this.listSchool = city;
	}
	
	public static List<SchoolCountry> parseCountrySchool(Context c, String data){
		try {
			List<SchoolCountry> list = new ArrayList<SchoolCountry>();
			JSONObject obj = new JSONObject(data);
			JSONObject dataObj = obj.getJSONObject("data");
			JSONArray arr = dataObj.getJSONArray("all");
			for (int i = 0; i < arr.length(); i++) {
				SchoolCountry sc = new SchoolCountry();
				if (arr.getJSONObject(i).has("country_code")) {
					sc.setCode(arr.getJSONObject(i).getString("country_code"));
				} else {
					sc.setCode("");
				}
				sc.setEurope(false);
				sc.setCountry(arr.getJSONObject(i).getString("country"));
				sc.setListSchool(School.parseSchool(arr.getJSONObject(i).getString("schools"), sc.getCountry(), sc.getCode(), false));
				list.add(sc);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(c, "Error connecting. Please check your network connection.", Toast.LENGTH_SHORT).show();
			return new ArrayList<SchoolCountry>();
		}
	}
	
	public static List<SchoolCountry> parseEuroCountrySchool(Context c, String data){
		try {
			List<SchoolCountry> list = new ArrayList<SchoolCountry>();
			JSONObject obj = new JSONObject(data);
			JSONObject dataObj = obj.getJSONObject("data");
			JSONArray arr = dataObj.getJSONArray("europe");
			for (int i = 0; i < arr.length(); i++) {
				SchoolCountry sc = new SchoolCountry();
				if (arr.getJSONObject(i).has("country_code")) {
					sc.setCode(arr.getJSONObject(i).getString("country_code"));
				} else {
					sc.setCode("");
				}
				sc.setEurope(true);
				sc.setCountry(arr.getJSONObject(i).getString("country"));
				sc.setListSchool(School.parseSchool(arr.getJSONObject(i).getString("schools"), sc.getCountry(), sc.getCode(), true));
				list.add(sc);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(c, "Error connecting. Please check your network connection.", Toast.LENGTH_SHORT).show();
			return new ArrayList<SchoolCountry>();
		}
	}
}
