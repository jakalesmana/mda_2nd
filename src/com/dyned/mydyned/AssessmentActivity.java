package com.dyned.mydyned;

import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dyned.mydyned.component.NonSwipeableViewPager;
import com.dyned.mydyned.composite.AssessmentPagerAdapter;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.manager.AssessmentManager;
import com.dyned.mydyned.model.Assessment;
import com.dyned.mydyned.utils.PreferencesUtil;

public class AssessmentActivity extends BaseFragmentActivity {
	
	private NonSwipeableViewPager vp;
	private List<Assessment> listAssessment;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assessment);
		setTitle(HomeFragment.ASESSMENT);
		
		listAssessment = (List<Assessment>) getIntent().getSerializableExtra("assessments");
				
		vp = (NonSwipeableViewPager) findViewById(R.id.pagerAssessment);
		vp.setAdapter(new AssessmentPagerAdapter(getSupportFragmentManager(), listAssessment));
		
		Button btnPrev = (Button)findViewById(R.id.btnPrev);
		Button btnNext = (Button)findViewById(R.id.btnNext);
		
		btnPrev.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onPrev();
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				onNext();
			}
		});
		
		showAssessmentDialog();
	}
	
	private void showAssessmentDialog() {
		new AlertDialog.Builder(this)
	    .setTitle("")
	    .setIcon(android.R.drawable.ic_dialog_info)
	    .setMessage("This quick English Assessment Test is designed to help you decide which DynEd Plus app is the right one for your level.")
	    .setNeutralButton("OK", null)
	    .show();
		PreferencesUtil.getInstance(this).setMediaHint(true);
	}
	
	@Override
	public void onBackPressed() {
		AssessmentManager.getInstance().clear();
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		AssessmentManager.getInstance().clear();
		super.onDestroy();
	}
	
	public void onNext(){
		if (vp.getCurrentItem() < listAssessment.size()) {
			vp.setCurrentItem(vp.getCurrentItem() + 1);
		} else {
			//submit answer
//			List<BasicNameValuePair> res = AssessmentManager.getInstance().getQuestionAnswers();
//			for (int i = 0; i < res.size(); i++) {
//				System.out.println("" + res.get(i).getName() + "=" + res.get(i).getValue());
//			}
			
			//start assessment submit option activity
		}
	}
	
	public void onPrev(){
		vp.setCurrentItem(vp.getCurrentItem() - 1);
	}
	
	public void recheck(){
		vp.setCurrentItem(0);
	}
}
