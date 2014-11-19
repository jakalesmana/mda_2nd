package com.dyned.mydyned.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.AssessmentActivity;
import com.dyned.mydyned.R;
import com.dyned.mydyned.component.AudioPlayer;
import com.dyned.mydyned.component.CustomButton;
import com.dyned.mydyned.component.CustomButton.ButtonHandler;
import com.dyned.mydyned.manager.AssessmentManager;
import com.dyned.mydyned.model.Answer;
import com.dyned.mydyned.model.Assessment;

public class AssessmentFragment extends SherlockFragment {
	
	private Assessment assessment;
	private List<CustomButton> btns = new ArrayList<CustomButton>();
	private int currentPos;
	private int totalAssessment;
	private AudioPlayer ap;
	
	public static AssessmentFragment newInstance(Assessment assessment, int pos, int total) {
		AssessmentFragment f = new AssessmentFragment();
		f.setAssessment(assessment, pos, total);
        return f;
    }
	
	public void setAssessment(Assessment assessment, int pos, int total){
		this.assessment = assessment;
		this.currentPos = pos;
		this.totalAssessment = total;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.assessment_pager_item_fragment, container, false);
		LinearLayout layout = (LinearLayout)v.findViewById(R.id.layoutAnswers);
		
		ImageView btnPrev = (ImageView)v.findViewById(R.id.imgPrev);
		ImageView btnNext = (ImageView)v.findViewById(R.id.imgNext);
		
		btnPrev.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				((AssessmentActivity)getSherlockActivity()).onPrev();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				((AssessmentActivity)getSherlockActivity()).onNext();
			}
		});
		
		if (!assessment.getAudio().equals("")) {
			ap = new AudioPlayer(getSherlockActivity());
			((FrameLayout)v.findViewById(R.id.layoutAudio)).addView(ap);
		} 
		
		for (int i = 0; i < assessment.getListAnswer().size(); i++) {
			CustomButton btn = new CustomButton(getSherlockActivity(), assessment.getListAnswer().get(i), onClick);
			btns.add(btn);
			layout.addView(btn);
		}
		
		TextView txtPart = (TextView)v.findViewById(R.id.txtPart);
		TextView txtHint = (TextView)v.findViewById(R.id.txtHint);
		TextView txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);
		TextView txtTotal = (TextView)v.findViewById(R.id.txtTotal);
		
		txtPart.setText(assessment.getPart());
		txtTotal.setText("Question " + (currentPos + 1) + "/" + (totalAssessment - 1));
		
		if (assessment.getInstruction().isEmpty()) {
			txtHint.setVisibility(View.GONE);
		} else {
			txtHint.setText(assessment.getInstruction());
		}
		
		txtQuestion.setText(assessment.getQuestion());
		return v;
	}
	
	private ButtonHandler onClick = new ButtonHandler() {
		public void onClickButton(CustomButton btn, Answer answer) {
			for (int j = 0; j < btns.size(); j++) {
				if (answer.getId() == btns.get(j).getAnswer().getId()) {
					btn.setSelected(true);
					AssessmentManager.getInstance().addAnswer(assessment.getId(), answer.getId());
				} else {
					btns.get(j).setSelected(false);
				}
			}
			
			((AssessmentActivity)getSherlockActivity()).onNext();
		}
	};
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (!isVisibleToUser) {
			if (ap != null) {
				ap.forceStop();
			}
		} else {
			if (ap != null) {
				ap.init();
			}
		}
	};
	
	public void onPause() {
		if (ap != null) {
			ap.forceStop();
		}
		super.onPause();
	};
}
