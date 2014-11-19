package com.dyned.mydyned.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class AssessmentManager {

	private static AssessmentManager instance;
	private static List<BasicNameValuePair> questionAnswers;
	
	private AssessmentManager(){
	}
	
	public static AssessmentManager getInstance(){
		if (instance == null) {
			instance = new AssessmentManager();
			questionAnswers = new ArrayList<BasicNameValuePair>();
		}
		return instance;
	}

	public List<BasicNameValuePair> getQuestionAnswers() {
		return questionAnswers;
	}
	
	public String getQuestionAnswerString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < questionAnswers.size(); i++) {
			sb.append(questionAnswers.get(i).getName() + "=" + questionAnswers.get(i).getValue());
			if (i < questionAnswers.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public void addAnswer(int qId, int aId) {
		boolean exist = false;
		for (int i = 0; i < questionAnswers.size(); i++) {
			if (questionAnswers.get(i).getName().equals("" + qId)) {
				exist = true;
				questionAnswers.remove(i);
				questionAnswers.add(i, new BasicNameValuePair("" + qId, "" + aId));
			}
		}
		
		if (!exist) {
			questionAnswers.add(new BasicNameValuePair("" + qId, "" + aId));
		}
	}
	
	public void clear(){
		questionAnswers.clear();
	}
	
}
