package com.dyned.mydyned;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.Profile;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.PreferencesUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

/**
author: jakalesmana
 */

public class SignInActivity extends BaseActivity {
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        public void call(final Session session, final SessionState state, final Exception exception) {
//        	onSessionStateChange(session, state, exception);
        }
    };
	private EditText txtEmail, txtPassword;
	private TextView txtForgotPassword;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);
        
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doValidation();
			}
		});
        
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				loadCountryAndLanguage();
				Intent i = new Intent(SignInActivity.this, RegisterActivity.class);
				startActivity(i);
			}
		});
        
        txtForgotPassword.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SignInActivity.this, WebViewerActivity.class);
				i.putExtra("url_menu", URLAddress.FORGOT_PASSWORD_URL);
				startActivity(i);
			}
		});
        
        //facebook
		LoginButton authButton = (LoginButton) findViewById(R.id.fbAuthButton);
		authButton.setReadPermissions(Arrays.asList("email"));
		
		//twitter
		
        Button btnTwitter = (Button)findViewById(R.id.btnTwitter);
        btnTwitter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				startActivity(new Intent(SignInActivity.this, TwitterSignInActivity.class));
//				finish();
			}
		});
	}
	
	
	
	private void doValidation() {
		if (txtEmail.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Please fill your email.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtPassword.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Please fill your password.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		postAccount();
	}

	private void postAccount() {
		final String email = txtEmail.getText().toString().trim();
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(SignInActivity.this, "", "Signing in..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response login: " + str);
				try {
					JSONObject obj = new JSONObject(str);
					boolean success = obj.getBoolean("status");
					if (success) {
						Profile me = Profile.parseProfile(str);
						PreferencesUtil pref = PreferencesUtil.getInstance(SignInActivity.this);
						pref.setLoggedIn(true);
						pref.setAppKey(me.getAppKey());
						pref.setName(me.getName());
						pref.setRoleKey(PreferencesUtil.buildRoleKey(me.getRoleKey(), email));
						System.out.println("set pref role title: " + me.getRoleTitle());
						pref.setRoleTitle(PreferencesUtil.buildRoleTitle(me.getRoleTitle(), email));
						pref.setAvatar(me.getAvatar());
						finish();
					} else {
						Toast toast = Toast.makeText(SignInActivity.this, obj.getString("error"), Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} catch (JSONException e) {
					Toast toast = Toast.makeText(SignInActivity.this, "Failed to sign in.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(SignInActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		postInternetTask.addPair("email", email);
    	postInternetTask.addPair("password", txtPassword.getText().toString().trim());
    	postInternetTask.execute(URLAddress.LOGIN_URL);
	}
	
	

	//handle facebook login callback
//	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
//    	if (state.isOpened()) {
//    		Log.i("LOGIN FB", "Logged in...");
//    		handler.post(new Runnable() {
//				public void run() {
//					dialog = ProgressDialog.show(SignInActivity.this, "Facebook", "Signing In..");
//				}
//			});
//    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback(){
//				public void onCompleted(GraphUser user, Response response) {
//					if (user != null ) {
////						String fb_access_token = session.getAccessToken();
////						userPref.setFacebookAccessToken(fb_access_token);
////						userPref.setUserFBId(user.getId());
////						userPref.setFacebook(true);
//						
//						String id = user.getId();
//						String name = user.getName();
//						String email = (String) user.getProperty("email");
//						String firstname = user.getFirstName();
//						String lastname = user.getLastName();
//
//						System.out.println(id);
//						System.out.println(name);
//						System.out.println(email);
//						System.out.println(firstname);
//						System.out.println(lastname);
//						
//						User dynedUser = new User();
//						dynedUser.setFacebookId(id);
//						dynedUser.setEmail(email);
//						dynedUser.setFirstName(firstname);
//						dynedUser.setLastName(lastname);
//						dynedUser.setImageUrl("http://graph.facebook.com/" + id + "/picture?width=200&height=200");
//						
//						dialog.dismiss();
//						
//						// go to register
//						Intent i = new Intent(SignInActivity.this, RegisterActivity.class);
//						i.putExtra("user", dynedUser);
//						i.putExtra("social", true);
//						startActivity(i);
//						finish();
//						
//						
//						
////						userPref.setEmail(email);
////						userPref.setFirstName(firstname);
////						userPref.setLastName(lastname);
////						
////						//user check, first user or already member
////						voutWorker.userMemberCheck(new VoutWorker.StatusMessageListener() {							
////							public void onSuccess() {
////								isAlreadyRegistered = true;
////								dofacebookAccess(id, name, email, firstname, lastname);
////							}
////							
////							@Override
////							public void onError(String message) {
////								if (message.equals("0")) {
////									isAlreadyRegistered = false;
////									dofacebookAccess(id, name, email, firstname, lastname);
////								} else {
////									Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
////									dialog.dismiss();
////									Session.getActiveSession().closeAndClearTokenInformation();
////								}
////							}
////						});
//					}
//				}}
//    		);
//        } else if (state.isClosed()) {
//        	Log.i("LOGIN FB", "Logged out...");
//        }
//    }
	
	@Override
	protected void onResume() {
		super.onResume();
        uiHelper.onResume();
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
