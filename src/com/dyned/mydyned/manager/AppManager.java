package com.dyned.mydyned.manager;

import java.util.ArrayList;
import java.util.List;

import com.dyned.mydyned.model.App;

public class AppManager {

	private static AppManager instance;
	private List<App> apps;
	
	private AppManager(){
	}
	
	public static AppManager getInstance(){
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}
	
	public void setApps(List<App> list){
		apps = list;
	}
	
	public List<App> getApps(){
		if (apps == null) {
			apps = new ArrayList<App>();
			apps.add(new App("com.dyned.engine", 
					"DynEd Pro", 
					"http://cmsmda.dyned.com/uploads/images/icon/DynEdPro.png", 
					"https://play.google.com/store/apps/details?id=com.dyned.engine",
					"http://www.anzhi.com/soft_1241068.html", ""));
		}
		return apps;
	}
}
