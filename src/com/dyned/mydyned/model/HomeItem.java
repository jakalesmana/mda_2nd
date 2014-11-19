package com.dyned.mydyned.model;

/**
author: jakalesmana
 */

public class HomeItem {
	private String title;
	private String imageRes;
	private int color;
	private String url;
	
	public HomeItem(String title, String imageRes, int color, String url) {
		this.title = title;
		this.imageRes = imageRes;
		this.color = color;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageRes() {
		return imageRes;
	}

	public void setImageRes(String imageRes) {
		this.imageRes = imageRes;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
