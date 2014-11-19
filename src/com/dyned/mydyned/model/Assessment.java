package com.dyned.mydyned.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Assessment implements Serializable {
	private int id;
	private List<Answer> listAnswer;
	private String audio;
	private String instruction;
	private String question;
	private String part;
	
	public Assessment() {
	}
	
	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Answer> getListAnswer() {
		return listAnswer;
	}

	public void setListAnswer(List<Answer> listAnswer) {
		this.listAnswer = listAnswer;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public static List<Assessment> parseListAssessment(String json){
		try {
			List<Assessment> list = new ArrayList<Assessment>();
			JSONObject obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("assessment");
			for (int i = 0; i < arr.length(); i++) {
				list.add(parseAssessment(arr.getJSONObject(i).toString()));
			}
			System.out.println("listassessments: " + list.size());
			return list;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static Assessment parseAssessment(String json){
		try {
			JSONObject obj = new JSONObject(json);
			Assessment asm = new Assessment();
			asm.setId(obj.getInt("id"));
			asm.setAudio(obj.getString("audio"));
			asm.setInstruction(obj.getString("instruction"));
			asm.setQuestion(obj.getString("question"));
			asm.setPart(obj.getString("part"));
			asm.setListAnswer(Answer.parseListAnswer(obj.getString("answers")));
			
			return asm;
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("returning null");
			return null;
		}		
	}
}
