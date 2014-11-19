package com.dyned.mydyned.component;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.dyned.mydyned.R;
import com.dyned.mydyned.manager.AssessmentManager;
import com.dyned.mydyned.model.Answer;

public class CustomButton extends FrameLayout {

	private LayoutInflater inflater;
	private Answer answer;
	private Button btn;
	private boolean selected;
	private ButtonHandler mHandler;
	
	public interface ButtonHandler {
		void onClickButton(CustomButton btn, Answer answer);
	}
	
	public CustomButton(Context c, final Answer answer, ButtonHandler handler) {
		super(c);
		setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		inflater = LayoutInflater.from(c);
		this.mHandler = handler;
		this.answer = answer;
		FrameLayout layout = (FrameLayout)inflater.inflate(R.layout.custom_button, null);
		btn = (Button)layout.findViewById(R.id.btnCustom);
		btn.setText(answer.getAnswer());
		btn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				selected = !selected;
				mHandler.onClickButton(CustomButton.this, answer);
			}
		});
		
		addView(layout);
		
		List<BasicNameValuePair> res = AssessmentManager.getInstance().getQuestionAnswers();
		for (int i = 0; i < res.size(); i++) {
			if (res.get(i).getValue().equals("" + answer.getId())) {
				setSelected(true);
				break;
			}
		}
	}
	
	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Answer getAnswer(){
		return answer;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
		if (selected) {
			btn.setBackgroundResource(R.drawable.bg_greensky_selectable);
		} else {
			btn.setBackgroundResource(R.drawable.bg_btn_blue_noborder);
		}
	}
	
	public boolean isSelected(){
		return selected;
	}
}
