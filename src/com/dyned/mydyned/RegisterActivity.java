package com.dyned.mydyned;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;

public class RegisterActivity extends BaseActivity {

	private EditText txtEmail, txtPassword, txtName, txtConfirm;

	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		txtEmail = (EditText)findViewById(R.id.txtEmail);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtConfirm = (EditText)findViewById(R.id.txtConfirmPassword);
		txtName = (EditText)findViewById(R.id.txtName);
		Button btnSubmit = (Button)findViewById(R.id.btnFinish);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	private void submit() {
		if (txtEmail.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Please fill your email.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtPassword.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Please fill your password.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!txtPassword.getText().toString().trim().equals(txtConfirm.getText().toString().trim())) {
			Toast.makeText(this, "Your password do not match.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtName.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Please fill your name.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		postdata();
	}

	private void postdata() {
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(RegisterActivity.this, "", "Updating..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response register: " + str);
				dialog.dismiss();
				
				try {
					JSONObject obj = new JSONObject(str);
					boolean status = obj.getBoolean("status");
					if (status) {
						Toast toast = Toast.makeText(RegisterActivity.this, obj.getString("message"), Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						finish();
					} else {
						Toast toast = Toast.makeText(RegisterActivity.this, obj.getString("error"), Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast toast = Toast.makeText(RegisterActivity.this, message + ", try again later.", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
		
		postInternetTask.addPair("password", txtPassword.getText().toString().trim());
		postInternetTask.addPair("name", txtName.getText().toString().trim());
		postInternetTask.addPair("email", txtEmail.getText().toString().trim());
    	postInternetTask.execute(URLAddress.REGISTER_URL);
	}
}
