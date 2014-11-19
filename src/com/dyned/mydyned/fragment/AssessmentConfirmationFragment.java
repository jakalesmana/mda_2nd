package com.dyned.mydyned.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.AssessmentActivity;
import com.dyned.mydyned.AssessmentResultActivity;
import com.dyned.mydyned.R;
import com.dyned.mydyned.RegisterActivity;
import com.dyned.mydyned.SignInActivity;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.manager.AssessmentManager;
import com.dyned.mydyned.model.AssessmentResult;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.PreferencesUtil;

public class AssessmentConfirmationFragment extends SherlockFragment {
	private ProgressDialog dialog;
	private boolean isLogingIn;
	
	public static AssessmentConfirmationFragment newInstance() {
		AssessmentConfirmationFragment f = new AssessmentConfirmationFragment();
        return f;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.assessment_confirmation_fragment, container, false);
		TextView txtThanks = (TextView)v.findViewById(R.id.txtThanks);
		String thanks = "Thank you for taking the assessment.\n\nYou may re-check your answer before submiting.";
		txtThanks.setText(thanks);
		
		Button btnRecheck = (Button)v.findViewById(R.id.btnRecheck);
		Button btnSubmit = (Button)v.findViewById(R.id.btnSubmit);
		btnRecheck.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				((AssessmentActivity)getSherlockActivity()).recheck();
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if (PreferencesUtil.getInstance(getSherlockActivity()).isLoggedIn()) {
					//hit api result
					submitAssessment();
				} else {
					//show chooser dialog Sign in, sign up, later
					showChooser();
				}
			}
		});
		
		return v;
	}
	
	private void submitAssessment(){
		final Handler handler = new Handler();
		PostInternetTask task = new PostInternetTask(getSherlockActivity(), new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(getSherlockActivity(), "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				try {
					System.out.println("response submit assessment: " + str);
					
					AssessmentResult res = AssessmentResult.parseResult(str);
					Intent i = new Intent(getSherlockActivity(), AssessmentResultActivity.class);
					i.putExtra("result", res);
					startActivity(i);
					getSherlockActivity().finish();
					dialog.dismiss();
				} catch (Exception e) {
					dialog.dismiss();
					Toast.makeText(getSherlockActivity(), "Error loading Assessment, try again later.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		
		task.addPair("app_key", PreferencesUtil.getInstance(getSherlockActivity()).getAppKey());
		task.addPair("answers", AssessmentManager.getInstance().getQuestionAnswerString());
		task.execute(URLAddress.ASSESSMENT_SUBMIT_URL);
	}
	
	private void showChooser() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setTitle("View Assessment Result");
		builder.setMessage("Please Sign In or Sign Up to view assessment result.")
		.setPositiveButton("Sign In", dialogClickListener)
		.setNeutralButton("Sign Up", dialogClickListener) 
		.setNegativeButton("Later", dialogClickListener).show();
	}
	
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE: // Sign In
	        	Intent i = new Intent(getSherlockActivity(), SignInActivity.class);
	        	isLogingIn = true;
	        	startActivity(i);
	            break;
	        case DialogInterface.BUTTON_NEUTRAL: // Sign Up
	        	Intent it = new Intent(getSherlockActivity(), RegisterActivity.class);
	        	startActivity(it);
	        	isLogingIn = true;
	            break;
	        case DialogInterface.BUTTON_NEGATIVE: // Later
	            break;
	        }
	    }
	};
	
	public void onResume() {
		if (isLogingIn && PreferencesUtil.getInstance(getSherlockActivity()).isLoggedIn()) {
			submitAssessment();
		}
		isLogingIn = false;
		super.onResume();
	};
}
