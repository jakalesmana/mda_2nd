package com.dyned.mydyned;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dyned.mydyned.component.MyExpandableListAdapter;
import com.dyned.mydyned.component.PinnedHeaderExpListView;
import com.dyned.mydyned.composite.SchoolListAdapter;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.model.Country;
import com.dyned.mydyned.model.School;
import com.dyned.mydyned.model.SchoolCountry;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.LocationUtil;
import com.dyned.mydyned.utils.Setting;

/**
author: jakalesmana
 */

public class SchoolListActivity extends BaseActivity {

	private ArrayList<Country> listCountry;

	private Handler handler = new Handler();
	
	private ArrayList<School> listSchool;
	private StickyListHeadersListView lvSchool;
	private PinnedHeaderExpListView lvSchoolExp;
	private ProgressDialog dialog;

	private Button btnNearest;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_school);
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this, logJson));
		
		setTitle(HomeFragment.FIND_SCHOOL);
		
		listSchool = new ArrayList<School>();
		
		btnNearest = (Button)findViewById(R.id.btnNearest);
		btnNearest.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				loadCountryAndLanguage();
			}
		});
		
		lvSchool = (StickyListHeadersListView)findViewById(R.id.lvSchool);
		lvSchoolExp = (PinnedHeaderExpListView)findViewById(R.id.lvSchoolExp);
		
		lvSchool.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				if(parent.getAdapter().getItem(pos) instanceof School){
					if (((School)parent.getAdapter().getItem(pos)).isEurope()) {
						Toast.makeText(SchoolListActivity.this, "show website", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				if (AppUtil.IsGooglePlayServiceInstalled(SchoolListActivity.this)) {
					if (parent.getAdapter().getItem(pos) instanceof School) {
						School school = (School)parent.getAdapter().getItem(pos);
						Intent i = new Intent(SchoolListActivity.this, MapActivity.class);
						ArrayList<School> schools = new ArrayList<School>();
						schools.add(school);
						i.putExtra("schools", schools);
						startActivity(i);
					}
				} else {
					showGooglePlayServiceDialog();
				}
			}
		});
		
		lvSchoolExp.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
				MyExpandableListAdapter myAdapter = ((MyExpandableListAdapter)parent.getExpandableListAdapter());
				if(myAdapter.getChild(groupPosition, childPosition) instanceof School){
					if (((School)myAdapter.getChild(groupPosition, childPosition)).isEurope()) {
						String url = ((School)myAdapter.getChild(groupPosition, childPosition)).getUrl();
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(browserIntent);
						return false;
					}
				}
				
				if (!AppUtil.hasGPSFeature(SchoolListActivity.this) || !AppUtil.hasLocationFeature(SchoolListActivity.this)) {
					Toast.makeText(SchoolListActivity.this, "To use this feature, you will require device with GPS supported.", Toast.LENGTH_SHORT).show();
					return false;
				}
				
				MyExpandableListAdapter adapter = ((MyExpandableListAdapter)parent.getExpandableListAdapter());
				if (adapter.getChild(groupPosition, childPosition) instanceof School) {
					School school = (School)adapter.getChild(groupPosition, childPosition);
					loadSchoolDetail(school);
				}
				
				return false;
			}			
		});
		
		loadSchools();
	}
	
	private void openSchoolInMap(School school){
		String mapview = Setting.getInstance(SchoolListActivity.this).getMap();
		if (mapview.equals("")) {
			showMapChooser(school);
		} else if (mapview.equals("google")) {
			showSingleLocation("google", school);
		} else if (mapview.equals("baidu")) {
			showSingleLocation(mapview, school);
		}
	}
	
	
	private void loadSchoolDetail(final School school) {
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(SchoolListActivity.this, "", "Loading..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response school detail: " + str);
				
				School sc = School.parseSchoolObject(str, school.getCountryName(), school.isEurope());
				System.out.println("detail: " + sc.getContactPerson());
		        openSchoolInMap(sc);
				
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(SchoolListActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		postInternetTask.addPair("id", "" + school.getId());
    	postInternetTask.execute(URLAddress.SCHOOLS_DETAIL_URL);
	}
	
	private void showMapChooser(final School school) {
		final View v = LayoutInflater.from(this).inflate(R.layout.map_dialog_chooser, null);
		
		final RadioGroup rbMap = (RadioGroup)v.findViewById(R.id.radioGroupMap);
		final CheckBox cbDefault = (CheckBox)v.findViewById(R.id.cbDefault);
		
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle("Show map using:");
		alertDialog.setView(v);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Show Map", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String radiovalue = ((RadioButton)v.findViewById(rbMap.getCheckedRadioButtonId())).getText().toString().toLowerCase(Locale.getDefault());
				if (cbDefault.isChecked()) {
					if (radiovalue.contains("google")) { //google
						Setting.getInstance(SchoolListActivity.this).setDefaultMap("google");
					} else if(radiovalue.contains("baidu")) {//baidu
						Setting.getInstance(SchoolListActivity.this).setDefaultMap("baidu");
					}
				}
				
				if (school == null) { //more locations
					showMoreLocations((radiovalue.contains("google")) ? "google" : "baidu");
				} else {// one location
					showSingleLocation((radiovalue.contains("baidu")) ? "baidu" : "google", school);
				}
				
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	private void showSingleLocation(String mapview, School school){
		if (mapview.equals("google")) {
			if (AppUtil.IsGooglePlayServiceInstalled(SchoolListActivity.this)) {
				Intent i = new Intent(SchoolListActivity.this, MapActivity.class);
				ArrayList<School> schools = new ArrayList<School>();
				schools.add(school);
				i.putExtra("schools", schools);
				startActivity(i);
			} else {
				showGooglePlayServiceDialog();
			}
		} else if(mapview.equals("baidu")) {			
			Intent i = new Intent(SchoolListActivity.this, BaiduMapViewerActivity.class);
			ArrayList<School> schools = new ArrayList<School>();
			schools.add(school);
			i.putExtra("schools", schools);
			startActivity(i);
			
		}
	}

	private void showMoreLocations(String mapview){
		if (mapview.equals("google")) {
			String myCountryCode = LocationUtil.getCountryCode();
			ArrayList<School> nearests = new ArrayList<School>();
			for (int i = 0; i < listSchool.size(); i++) {
				School sc = listSchool.get(i);
				if (myCountryCode.equals(sc.getCountryCode())) {
					nearests.add(sc);
				}
			}
			Intent i = new Intent(SchoolListActivity.this, MapActivity.class);
			i.putExtra("all_school", listSchool);
			i.putExtra("schools", nearests);
			i.putExtra("is_nearest", true);
			i.putExtra("countries", listCountry);
			startActivity(i);
		} else if (mapview.equals("baidu")) {
			String myCountryCode = "IDN";
			ArrayList<School> nearests = new ArrayList<School>();
			for (int i = 0; i < listSchool.size(); i++) {
				School sc = listSchool.get(i);
				if (myCountryCode.equals(sc.getCountryCode())) {
					nearests.add(sc);
				}
			}
			Intent i = new Intent(SchoolListActivity.this, BaiduMapViewerActivity.class);
			School.SchoolObjectBridge.setAllSchools(listSchool);
			School.SchoolObjectBridge.setCurrentSchools(nearests);
			
//			i.putExtra("all_school", listSchool);
			i.putExtra("countries", listCountry);
//			i.putExtra("schools", nearests);
			i.putExtra("countryCode", myCountryCode);
			i.putExtra("is_nearest", true);
			startActivity(i);			
		}
	}
	
	private void loadCountryAndLanguage() {
		InternetTask getTask = new InternetTask(this, new InternetConnectionListener() {

			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(SchoolListActivity.this, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response load country: " + str);
				listCountry = (ArrayList<Country>) Country.parseCountries(str);
				dialog.dismiss();
				doLoadNearest();
			}
			
			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(SchoolListActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		getTask.execute(URLAddress.COUNTRY_URL);
	}
	
	private void doLoadNearest(){
		String mapview = Setting.getInstance(SchoolListActivity.this).getMap();
		if (mapview.equals("")) {
			showMapChooser(null);
		} else if(mapview.equals("google")) {
			if (AppUtil.IsGooglePlayServiceInstalled(SchoolListActivity.this)) {
				showMoreLocations("google");
			} else {
				showGooglePlayServiceDialog();
			}
		} else if(mapview.equals("baidu")) {
			showMoreLocations(mapview);
		}
		
	}

	private void loadSchools() {
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(SchoolListActivity.this, "", "Loading..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response school: " + str);
				
				//add general school
				List<SchoolCountry> listCountry = SchoolCountry.parseCountrySchool(SchoolListActivity.this, str);
				for (int i = 0; i < listCountry.size(); i++) {
					List<School> list = listCountry.get(i).getListSchool();
					System.out.println("list school general count: " + list.size());
					listSchool.addAll(list);
				}
				
				//add europe
				List<SchoolCountry> listEuropeCountry = SchoolCountry.parseEuroCountrySchool(SchoolListActivity.this, str);
				for (int i = 0; i < listEuropeCountry.size(); i++) {
					List<School> list = listEuropeCountry.get(i).getListSchool();
					System.out.println("list school europe count: " + list.size());
					listSchool.addAll(list);
				}
				
				lvSchool.setAdapter(new SchoolListAdapter(SchoolListActivity.this, listSchool));
				
				//======================
				List<SchoolCountry> all = new ArrayList<SchoolCountry>();
				all.addAll(listCountry);
				all.addAll(listEuropeCountry);
				MyExpandableListAdapter adapter = new MyExpandableListAdapter(SchoolListActivity.this, all);
				lvSchoolExp.setAdapter(adapter);

				lvSchoolExp.setGroupIndicator(null);
		        View view = LayoutInflater.from(SchoolListActivity.this).inflate(R.layout.list_header_section, null, false);
		        View h = view.findViewById(R.id.txtTitle);
		        lvSchoolExp.setPinnedHeaderView(h);
		        lvSchoolExp.setOnScrollListener((OnScrollListener) adapter);
		        lvSchoolExp.setDividerHeight(1);
		        //=======================
		        
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(SchoolListActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
    	postInternetTask.execute(URLAddress.SCHOOLS_URL);
	}
	
	private void showGooglePlayServiceDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setTitle("Google Play Services")
				.setMessage("The map requires Google Play Services to be installed.")
				.setCancelable(true)
				.setPositiveButton("Download", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.dismiss();
						// Try the new HTTP method (I assume that is the official way now given that google uses it).
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
							intent.setPackage("com.android.vending");
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							// Ok that didn't work, try the market method.
							try
							{
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
								intent.setPackage("com.android.vending");
								startActivity(intent);
							}
							catch (ActivityNotFoundException f)
							{
								// Ok, weird. Maybe they don't have any market app. Just show the website.
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.gms"));
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
								startActivity(intent);
							}
						}
						
						//download gms from dyned web
//						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pistarlabs.net/projects/dyned/MyDynEd/android/com.google.android.gms.apk"));
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//						startActivity(intent);
						
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				})
				.create()
				.show();
	}
}
