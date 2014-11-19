package com.dyned.mydyned.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaMenu {

	private int id;
	private String name;
	private String code;
	private String icon;
	private int docType;
	private String share;
	private boolean selected;
	
	public MediaMenu() {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getDocumentType() {
		return docType;
	}

	public void setDocumentType(int fileType) {
		this.docType = fileType;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public static List<MediaMenu> parseMediaMenuList(String json){
		ArrayList<MediaMenu> listMedia = new ArrayList<MediaMenu>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray arrJson = obj.getJSONArray("data");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject mediaJsonObj = arrJson.getJSONObject(i);
				MediaMenu media = ParseMediaMenu(mediaJsonObj.toString());
				listMedia.add(media);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return listMedia;
	}

	private static MediaMenu ParseMediaMenu(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			int id = obj.getInt("id");
			String name = obj.getString("name");
			String code = obj.getString("code");
			String icon = obj.getString("icon");
			int mediaType = obj.getInt("media_type_id");
			String share = obj.getString("share");
			
			MediaMenu item = new MediaMenu();
			item.setId(id);
			item.setName(name);
			item.setCode(code);
			item.setIcon(icon);
			item.setDocumentType(mediaType);
			item.setShare(share);
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}
}
