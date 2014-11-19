package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
author: jakalesmana
 */

@SuppressWarnings("serial")
public class MediaItem implements Serializable{
	public static final String TYPE_VIDEO = "V";
	public static final String TYPE_PODCAST = "PA";
	public static final String TYPE_STUDY_GUIDE = "SG";
	public static final String TYPE_TEACHER_GUIDE = "TG";
	public static final String TYPE_BROCHURE = "BR";
	public static final String TYPE_SOV = "SOV";
	
	public static final int FILE_PDF = 1;
	public static final int FILE_AUDIO = 5;
	public static final int FILE_VIDEO = 4;
	
	private String id;
	private String title;
	private String imageRes;
	private String type;
	private String thumbnail;
	private int color;
	private int documentType;
	private String url;
	private String description;
	
	public MediaItem(String id, String title, String type, String imageRes, String thumbnail, int color, String url, int documentType) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.imageRes = imageRes;
		this.color = color;
		this.url = url;
		this.thumbnail = thumbnail;
		this.documentType = documentType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getDocumentType() {
		return documentType;
	}

	public void setDocumentType(int documentType) {
		this.documentType = documentType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageRes() {
		return imageRes;
	}

	public void setImageRes(String imageRes) {
		this.imageRes = imageRes;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static ArrayList<MediaItem> ParseMediaList(String data){
		ArrayList<MediaItem> listMedia = new ArrayList<MediaItem>();
		try {
			JSONObject obj = new JSONObject(data);
			JSONArray arrJson = obj.getJSONArray("data");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject mediaJsonObj = arrJson.getJSONObject(i);
				MediaItem media = ParseMedia(mediaJsonObj.toString());
				listMedia.add(media);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return listMedia;
	}
	
	public static MediaItem ParseMedia(String data){
		try {
			JSONObject obj = new JSONObject(data);
			String id = obj.getString("id");
			String title = obj.getString("title");
			String type = obj.getString("media_category_code");
			String thumbnail = (obj.has("thumbnail") ? obj.getString("thumbnail") : "");
			int fileTypeId = (obj.has("media_type_id") ? obj.getInt("media_type_id") : 0);
			String desc = (obj.has("description") ? obj.getString("description") : "");
			String path = (obj.has("path") ? obj.getString("path") : "");
			String imgres;
			if (fileTypeId == MediaItem.FILE_VIDEO) {
				imgres = "ic_vids";
			} else if(fileTypeId == MediaItem.FILE_PDF) {
				imgres = "ic_pdf";
			} else if(fileTypeId == MediaItem.FILE_AUDIO) {
				imgres = "ic_audio";
			} else {
				imgres = "ic_media";
			}
			MediaItem item = new MediaItem(id, title, type, imgres, thumbnail, 0xFF66656b, path, fileTypeId);
			item.setDescription(desc);
			
			return item;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static MediaItem ParseMediaDetail(String data){
		try {
			JSONObject object = new JSONObject(data);
			JSONObject obj = object.getJSONObject("data");
			return ParseMedia(obj.toString());
		} catch (JSONException e) {
			return null;
		}
	}
}
