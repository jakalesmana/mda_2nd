package com.dyned.mydyned;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dyned.mydyned.model.AssessmentResult;

public class AssessmentResultActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Assessment Result");
		setContentView(R.layout.activity_assessment_result);
		
		final AssessmentResult res = (AssessmentResult) getIntent().getSerializableExtra("result");
		
		TextView txtMessage = (TextView)findViewById(R.id.txtMessage);
		TextView txtCode = (TextView)findViewById(R.id.txtCode);
		Button btnDownload = (Button)findViewById(R.id.btnDownload);
		txtMessage.setText(res.getMessage());
		txtCode.setText(res.getCode());
		
		btnDownload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(res.getLink()));
				startActivity(browserIntent);
			}
		});
	}
}
