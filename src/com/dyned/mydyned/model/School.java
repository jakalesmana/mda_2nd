package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
author: jakalesmana
 */

@SuppressWarnings("serial")
public class School implements Serializable {

	private int id;
	private String name;
	private String city;
	private String address;
	private double latitude;
	private double longitude;
	private double baidu_lat;
	private double baidu_long;
	private String countryCode;
	private String countryName;
	private String focus;
	private String desc;
	private String code;
	private String website;
	private String telp;
	private String email;
	private String contactPerson;
	private String url;
	private boolean isEurope;
	
	public School() {
	}

	public boolean isEurope() {
		return isEurope;
	}
	
	public void setEurope(boolean isEurope) {
		this.isEurope = isEurope;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		if (city == null) {
			city = "";
		}
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getBaiduLat() {
		return baidu_lat;
	}

	public void setBaiduLat(double baidu_lat) {
		this.baidu_lat = baidu_lat;
	}

	public double getBaiduLong() {
		return baidu_long;
	}

	public void setBaiduLong(double baidu_long) {
		this.baidu_long = baidu_long;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getFocus() {
		return focus;
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTelp() {
		return telp;
	}

	public void setTelp(String telp) {
		this.telp = telp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	public static School parseSchoolObject(String data, String country, boolean isEurope){
		try {
			JSONObject obj = new JSONObject(data);
			JSONObject object = obj.getJSONObject("data");
			
			School sc = new School();
			if(object.has("id")) sc.setId(object.getInt("id"));
			sc.setName(object.getString("name"));
			if(object.has("lat")) sc.setLatitude(object.getDouble("lat"));
			if(object.has("long")) sc.setLongitude(object.getDouble("long"));
			if(object.has("baidu_lat")) sc.setBaiduLat(object.getDouble("baidu_lat"));
			if(object.has("baidu_long")) sc.setBaiduLong(object.getDouble("baidu_long"));
			if(object.has("city")) sc.setCity(object.getString("city"));
			if(object.has("country")) sc.setCountryCode(object.getString("country"));
			sc.setCountryName(country);
			if(object.has("focus")) sc.setFocus(object.getString("focus"));
			if(object.has("desc")) sc.setDesc(object.getString("desc"));
			if(object.has("code")) sc.setCode(object.getString("code"));
			if(object.has("website")) sc.setWebsite(object.getString("website"));
			if(object.has("telp")) sc.setTelp(object.getString("telp"));
			if(object.has("email")) sc.setEmail(object.getString("email"));
			if(object.has("address")) sc.setAddress(object.getString("address"));
			if(object.has("contact_person")) sc.setContactPerson(object.getString("contact_person"));
			if(object.has("url")) sc.setUrl(object.getString("url"));
			sc.setEurope(isEurope);
			
			return sc;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<School> parseSchool(String data, String countryName, String countryCode, boolean isEurope) {
		try {
			List<School> list = new ArrayList<School>();
			JSONArray arr = new JSONArray(data);
			for (int i = 0; i < arr.length(); i++) {
				School sc = new School();
				if(arr.getJSONObject(i).has("id")) sc.setId(arr.getJSONObject(i).getInt("id"));
				sc.setName(arr.getJSONObject(i).getString("name"));
				if(arr.getJSONObject(i).has("lat")) sc.setLatitude(arr.getJSONObject(i).getDouble("lat"));
				if(arr.getJSONObject(i).has("long")) sc.setLongitude(arr.getJSONObject(i).getDouble("long"));
				if(arr.getJSONObject(i).has("baidu_lat")) sc.setBaiduLat(arr.getJSONObject(i).getDouble("baidu_lat"));
				if(arr.getJSONObject(i).has("baidu_long")) sc.setBaiduLong(arr.getJSONObject(i).getDouble("baidu_long"));
				if(arr.getJSONObject(i).has("city")) sc.setCity(arr.getJSONObject(i).getString("city"));
				
				if(arr.getJSONObject(i).has("country")) {
					sc.setCountryCode(arr.getJSONObject(i).getString("country"));
				} else {
					sc.setCountryCode(countryCode);
				}
				
				sc.setCountryName(countryName);
				if(arr.getJSONObject(i).has("focus")) sc.setFocus(arr.getJSONObject(i).getString("focus"));
				if(arr.getJSONObject(i).has("desc")) sc.setDesc(arr.getJSONObject(i).getString("desc"));
				if(arr.getJSONObject(i).has("code")) sc.setCode(arr.getJSONObject(i).getString("code"));
				if(arr.getJSONObject(i).has("website")) sc.setWebsite(arr.getJSONObject(i).getString("website"));
				if(arr.getJSONObject(i).has("telp")) sc.setTelp(arr.getJSONObject(i).getString("telp"));
				if(arr.getJSONObject(i).has("email")) sc.setEmail(arr.getJSONObject(i).getString("email"));
				if(arr.getJSONObject(i).has("address")) sc.setAddress(arr.getJSONObject(i).getString("address"));
				if(arr.getJSONObject(i).has("contact_person")) sc.setContactPerson(arr.getJSONObject(i).getString("contact_person"));
				if(arr.getJSONObject(i).has("url")) sc.setUrl(arr.getJSONObject(i).getString("url"));
				sc.setEurope(isEurope);
				list.add(sc);
			}
			return list;
		} catch (JSONException e) {
			System.out.println("error parse list schools of country");
			e.printStackTrace();
			return null;
		}
	}
	
	public static class SchoolObjectBridge {
		private static List<School> mCurList;
		private static List<School> mAllList;
		
		public static void setCurrentSchools(List<School> list){
			mCurList = list;
		}
		public static List<School> getCurrentSchools(){
//			List<School> res = mCurList;
//			mCurList = null;
			return mCurList;
		}
		
		public static void setAllSchools(List<School> list){
			mAllList = list;
		}
		public static List<School> getAllSchools(){
//			List<School> res = mAllList;
//			mAllList = null;
			return mAllList;
		}
	}
	
}
