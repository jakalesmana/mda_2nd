//package com.dyned.mydyned.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//@SuppressWarnings("serial")
//public class EuropeSchool extends School {
//
//	private String url;
//	private String website;
//	
//	public EuropeSchool() {
//		url = "";
//		website = "";
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public String getWebsite() {
//		return website;
//	}
//
//	public void setWebsite(String website) {
//		this.website = website;
//	}
//	
//	public static List<EuropeSchool> parseEuropeSchool(String data, String country) {
//		try {
//			List<EuropeSchool> list = new ArrayList<EuropeSchool>();
//			JSONArray arr = new JSONArray(data);
//			for (int i = 0; i < arr.length(); i++) {
//				EuropeSchool es = new EuropeSchool();
//				es.setName(arr.getJSONObject(i).getString("name"));
//				es.setUrl(arr.getJSONObject(i).getString("url"));
//				es.setWebsite(arr.getJSONObject(i).getString("website"));
//				es.setCountryName(country);
//				list.add(es);
//			}
//			return list;
//		} catch (JSONException e) {
//			System.out.println("error parse list schools of country");
//			e.printStackTrace();
//			return new ArrayList<EuropeSchool>();
//		}
//	}
//	
//}
