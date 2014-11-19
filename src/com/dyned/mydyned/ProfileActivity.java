package com.dyned.mydyned;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.Country;
import com.dyned.mydyned.model.Language;
import com.dyned.mydyned.model.Profile;
import com.dyned.mydyned.tools.FileUploaderTask;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.ImagePickerUtil;
import com.dyned.mydyned.utils.ImageUtil;
import com.dyned.mydyned.utils.PreferencesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author: jakalesmana
 */

@SuppressLint("DefaultLocale")
public class ProfileActivity extends BaseActivity {

	private static final int DATE_PICKER_ID = 1111;
	private int year;
	private int month;
	private int day;
	
	private DisplayImageOptions optionsAvatar = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.def_profile)
			.showImageForEmptyUri(R.drawable.def_profile).cacheOnDisc()
			.cacheInMemory().build();

	private ImagePickerUtil imagePickerUtil;

	private Handler handler = new Handler();
	private ProgressDialog dialog;

	private List<Language> listLanguage;
	private List<Country> listCountry;

	private String selectedCountry, selectedLanguage;

	private TextView txtCountry;
	private TextView txtGender;
	private TextView txtLanguage;

	private Bitmap bitmap;

	private ImageView imgProfile;

	private TextView txtBirthdate;
//	private RadioGroup rbGender;
	private TextView txtCode;
	private Profile profile;
	private EditText txtCity;
	private EditText txtPhone;
	private EditText txtName;
	private EditText txtEmail;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		// User user = (User) getIntent().getSerializableExtra("user");
		// boolean socialMedia = getIntent().getBooleanExtra("social", false);

		imgProfile = (ImageView) findViewById(R.id.imgProfile);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtName = (EditText) findViewById(R.id.txtName);
		txtPhone = (EditText) findViewById(R.id.txtPhone);
		txtCity = (EditText) findViewById(R.id.txtCity);
		txtCountry = (TextView) findViewById(R.id.txtCountry);
		txtGender = (TextView) findViewById(R.id.txtGender);
		txtCode = (TextView) findViewById(R.id.txtCode);
		txtLanguage = (TextView) findViewById(R.id.txtLanguage);
		txtBirthdate = (TextView) findViewById(R.id.txtBirthdate);
//		rbGender = (RadioGroup) findViewById(R.id.radioGroupSex);

		Button btnUpdate = (Button)findViewById(R.id.btnUpdate);
		Button btnSignout = (Button)findViewById(R.id.btnLogout);
		Button btnViewRecord = (Button)findViewById(R.id.btnViewRecord);
		// if (socialMedia) {
		// txtPassword.setVisibility(View.GONE);
		// }

		// if (user != null) {
		// txtEmail.setText(user.getEmail());
		// txtName.setText(user.getFirstName());
		// txtPhone.setText(user.getPhoneNumber());
		//
		// Bitmap bmp = BitmapFactory.decodeResource(getResources(),
		// R.drawable.def_profile);
		// imgProfile.getLayoutParams().width = bmp.getWidth();
		// imgProfile.getLayoutParams().height = bmp.getWidth();
		// ImageLoader imageLoader = ImageLoader.getInstance();
		// imageLoader.displayImage(user.getImageUrl(), imgProfile,
		// optionsAvatar, new AnimateFirstDisplayListener(user.getImageUrl(),
		// imgProfile, false, 0, 0));
		// }

		listCountry = (List<Country>) getIntent().getSerializableExtra("countries");
		listLanguage = (List<Language>) getIntent().getSerializableExtra("languages");
		profile = (Profile) getIntent().getSerializableExtra("profile");
		
		txtCountry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showCountryPicker();
			}
		});
		
		txtGender.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showGenderPicker();
			}
		});
		
		txtLanguage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showLanguagePicker();
			}
		});

		imgProfile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showImageSourceChooserDialog();
			}
		});

		txtBirthdate.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_PICKER_ID);
			}
		});
		
		if (profile != null) {
			txtName.setText(profile.getName());
			txtPassword.setText(profile.getPassword());
			txtEmail.setText(profile.getEmail());
			txtPhone.setText(profile.getMobileNo());
			txtCity.setText(profile.getCity());
			setCountry();
			setLanguage();
			setBirthdate();
			setGender();
			txtPassword.setVisibility(View.GONE);
			txtEmail.setEnabled(false);
			
			String dates[] = profile.getBirthdate().split("-");
			year = Integer.parseInt(dates[0]);
			month = Integer.parseInt(dates[1]) - 1;
			day = Integer.parseInt(dates[2]);
		} else {
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				submit();
			}
		});
		
		btnSignout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PreferencesUtil.getInstance(ProfileActivity.this).logout();
				finish();
			}
		});
		
		btnViewRecord.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, WebRecordsViewerActivity.class);
				i.putExtra("url_menu", URLAddress.RECORD_URL);
				startActivity(i);
				finish();
			}
		});
		
		loadImage();
	}
	
	private void loadImage(){
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.def_profile);
		 imgProfile.getLayoutParams().width = bmp.getWidth();
		 imgProfile.getLayoutParams().height = bmp.getWidth();
		 ImageLoader imageLoader = ImageLoader.getInstance();
		 
		 imageLoader.displayImage(profile.getAvatar(), imgProfile,
		 optionsAvatar, new AnimateFirstDisplayListener(profile.getAvatar(),imgProfile, false, 0, 0));
	}
	
	private void setGender() {
		if (profile.getGender().equals("m")) {
//			((RadioButton)rbGender.getChildAt(0)).setChecked(true);
//			((RadioButton)rbGender.getChildAt(1)).setChecked(false);
			txtGender.setText("Male");
		} else {
//			((RadioButton)rbGender.getChildAt(0)).setChecked(false);
//			((RadioButton)rbGender.getChildAt(1)).setChecked(true);
			txtGender.setText("Female");
		}
	}

	private void setBirthdate() {
		txtBirthdate.setText(profile.getBirthdate());
	}

	private void setLanguage() {
		for (int i = 0; i < listLanguage.size(); i++) {
			if (profile.getLanguage().equals(listLanguage.get(i).getCode())) {
				txtLanguage.setText(listLanguage.get(i).getName());
				selectedLanguage = profile.getLanguage();
			}
		}
	}

	private void setCountry() {		
		for (int i = 0; i < listCountry.size(); i++) {
			if (profile.getCountryIso().equals(listCountry.get(i).getCountryCode())) {
				txtCountry.setText(listCountry.get(i).getName());
				selectedCountry = profile.getCountryIso();
				txtCode.setText(listCountry.get(i).getDialCode());
			}
		}
	}

	@SuppressLint("DefaultLocale")
	private void submit(){
		if (selectedCountry == null) {
			Toast.makeText(this, "Please select your country", Toast.LENGTH_SHORT).show();
			return;
		}
		if (selectedLanguage == null) {
			Toast.makeText(this, "Please select your language", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtName.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Your name is empty.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtCity.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Your city is empty.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtGender.getText().toString().trim().contains("gender")) {
			Toast.makeText(this, "Please select your gender.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (txtPhone.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Your phone Number is empty.", Toast.LENGTH_SHORT).show();
			return;
		}
		  
		postProfile();
	}

	private void postProfile() {
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(ProfileActivity.this, "", "Updating..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response update: " + str);
				
				try {
					JSONObject obj = new JSONObject(str);
					if (obj.getBoolean("status")) {
						Profile me = Profile.parseProfile(str);
						PreferencesUtil pref = PreferencesUtil.getInstance(ProfileActivity.this);
						pref.setName(me.getName());
//						pref.setRoleKey(me.getRoleKey());
						pref.setRoleKey(PreferencesUtil.buildRoleKey(me.getRoleKey(), me.getEmail()));
						pref.setRoleTitle(PreferencesUtil.buildRoleTitle(me.getRoleTitle(), me.getEmail()));
						
						pref.setAvatar(me.getAvatar());
						
						if (bitmap == null) {
							dialog.dismiss();
							Toast.makeText(ProfileActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
							finish();
						} else {
							uploadAvatar();
						}
					} else {
						Toast.makeText(ProfileActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				} catch (JSONException e) {
					Toast.makeText(ProfileActivity.this, "Failed to update your profile. Try again later", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(ProfileActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
//		String radiovalue = ((RadioButton)this.findViewById(rbGender.getCheckedRadioButtonId())).getText().toString();
		String gendervalue = txtGender.getText().toString();  
		String gender;
		  if (gendervalue.toLowerCase().equals("male")) {
			  gender = "m";
		  } else {
			  gender = "f";
		  }
		  
		postInternetTask.addPair("app_key", PreferencesUtil.getInstance(this).getAppKey());
		postInternetTask.addPair("name", txtName.getText().toString().trim());
		postInternetTask.addPair("country_iso", selectedCountry);
		postInternetTask.addPair("city", txtCity.getText().toString().trim());
		postInternetTask.addPair("mobile_no", txtPhone.getText().toString().trim());
		postInternetTask.addPair("birthdate", txtBirthdate.getText().toString().trim());
		postInternetTask.addPair("gender", gender);
		postInternetTask.addPair("language", selectedLanguage);
    	postInternetTask.execute(URLAddress.UPDATE_PROFILE_URL);
	}
	
	private void uploadAvatar() {
		FileUploaderTask task = new FileUploaderTask(ProfileActivity.this, new InternetConnectionListener() {
			public void onStart() {
				
			}
			
			public void onDone(String str) {
				System.out.println("reponse add image: " + str);
				try {
					JSONObject obj = new JSONObject(str);
					if (obj.getBoolean("status")) {
						JSONObject j = obj.getJSONObject("data");
						PreferencesUtil.getInstance(ProfileActivity.this).setAvatar(j.getString("avatar"));
						dialog.dismiss();
						Toast.makeText(ProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						dialog.dismiss();
						Toast.makeText(ProfileActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (JSONException e) {
					dialog.dismiss();
					Toast.makeText(ProfileActivity.this, "Failed to update your avatar. Try again later", Toast.LENGTH_SHORT).show();
					finish();
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(ProfileActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		}, ImageUtil.ConvertToFile(ProfileActivity.this, bitmap, 100, "" + System.currentTimeMillis() + ".jpg"));
		task.addPair("app_key", profile.getAppKey());
		task.execute(URLAddress.UPDATE_PROFILE_AVATAR);
	}

	private void showCountryPicker() {
		String[] countries = new String[listCountry.size()];
		for (int i = 0; i < countries.length; i++) {
			countries[i] = listCountry.get(i).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, countries);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Country:");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				selectedCountry = listCountry.get(item).getCountryCode();
				txtCountry.setText(listCountry.get(item).getName());
				txtCode.setText(listCountry.get(item).getDialCode());
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	private void showGenderPicker() {
		final String[] genders = {"Male", "Female"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, genders);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Gender:");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
//				selectedCountry = listCountry.get(item).getCountryCode();
				txtGender.setText(genders[item]);
//				txtCode.setText(listCountry.get(item).getDialCode());
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void showLanguagePicker() {
		String[] languages = new String[listLanguage.size()];
		for (int i = 0; i < languages.length; i++) {
			languages[i] = listLanguage.get(i).getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, languages);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Language:");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				selectedLanguage = listLanguage.get(item).getCode();
				txtLanguage.setText(listLanguage.get(item).getName());
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void showImageSourceChooserDialog() {
		final String[] items = new String[] { "From Camera", "From SD Card" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int item) {
				imagePickerUtil = ImagePickerUtil.GetInstance(
						ProfileActivity.this, null);
				if (item == 0) {
					imagePickerUtil.startCameraPickerActivity();
				} else if (item == 1) {
					imagePickerUtil.startSDCardPickerActivity();
				}
			}
		});
		builder.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			return new DatePickerDialog(this, pickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			txtBirthdate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-")
					.append(day));
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		int imgSize = 300;

		bitmap = imagePickerUtil.getBitmapResult(requestCode, resultCode, data,
				imgSize);

		if (bitmap != null) {
			int width = imgProfile.getWidth();
			int height = imgProfile.getHeight();
			Bitmap resizedbBitmap = Bitmap.createScaledBitmap(bitmap, width,
					height, false);
			imgProfile.setImageBitmap(resizedbBitmap);
		}
	}
}
