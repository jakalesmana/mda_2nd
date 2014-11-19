package com.dyned.mydyned.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Profile implements Serializable {
	
	private int id;
	private String appKey;
	private String name;
	private String email;
	private String password;
	private String status;
	private String roleKey;
	private String roleTitle;
	private String countryIso;
	private String city;
	private String mobileNo;
	private String facebook;
	private String twitter;
	private String birthdate;
	private String gender;
	private String language;
	private String avatar;
	
	public Profile() {
	}
	
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String role) {
		this.roleKey = role;
	}
	
	public String getRoleTitle() {
		return roleTitle;
	}

	public void setRoleTitle(String role) {
		this.roleTitle = role;
	}

	public String getCountryIso() {
		return countryIso;
	}

	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public static Profile parseProfile(String data){
		try {
			JSONObject obj = new JSONObject(data);
			JSONObject objData = obj.getJSONObject("data");
			String appkey = "";
			if (objData.has("app_key")) {
				appkey = objData.getString("app_key");
			}
			JSONObject objProfile = objData.getJSONObject("profile");
			Profile p = new Profile();
			p.setAppKey(appkey);
			if (objProfile.has("id")) {
				p.setId(objProfile.getInt("id"));
			}
			
			p.setName(objProfile.getString("name"));
			p.setEmail(objProfile.getString("email"));
			if (objProfile.has("password")) {
				p.setPassword(objProfile.getString("password"));
			}
			
			p.setStatus(objProfile.getString("status"));
			if (objProfile.has("role")) {
				p.setRoleKey(objProfile.getString("role"));
			}
			
			if (objData.has("role_title")) {
				p.setRoleTitle(objData.getString("role_title"));
			}
			
			p.setCountryIso(objProfile.getString("country_iso"));
			p.setCity(objProfile.getString("city"));
			p.setMobileNo(objProfile.getString("mobile_no"));
			p.setFacebook(objProfile.getString("facebook"));
			p.setTwitter(objProfile.getString("twitter"));
			p.setBirthdate(objProfile.getString("birthdate"));
			p.setGender(objProfile.getString("gender"));
			p.setLanguage(objProfile.getString("language"));
			p.setAvatar(objProfile.getString("avatar"));
			return p;
		} catch (JSONException e) {
			return null;
		}
	}
}
