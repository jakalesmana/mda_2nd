package com.dyned.mydyned.model;

import android.graphics.drawable.Drawable;


public class App {

	private String packageName;
	private String appName;
	private String appIcon;
	private String link;
	private String chinaLink;
	private String directLink;
	private Drawable ic;
	
	public App(String pckg, String name, String icon, String market, String chinaMarket, String directLink) {
		packageName = pckg;
		appName = name;
		appIcon = icon;
		link = market;
		chinaLink = chinaMarket;
		this.directLink = directLink;
	}
	
	public App(String pckg, String name, Drawable icon) {
		packageName = pckg;
		appName = name;
		ic = icon;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
	
	public String getMarketUrl() {
		return link;
	}

	public void setMarketUrl(String market) {
		this.link = market;
	}
	
	public void setDirectLink(String link) {
		this.directLink = link;
	}
	
	public String getChinaMarketUrl() {
		return chinaLink;
	}
	
	public String getDirectLink(){
		if (directLink == null) {
			return "";
		}
		return directLink;
	}

	public void setChinaMarketUrl(String market) {
		chinaLink = market;
	}
	
	public Drawable getDrawableIcon() {
		return ic;
	}

	public void setAppIcon(Drawable appIcon) {
		this.ic = appIcon;
	}
	
	
}
