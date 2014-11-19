package com.dyned.mydyned;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.Country;
import com.dyned.mydyned.model.School;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.LocationUtil;

public class BaiduMapViewerActivity extends BaseActivity {

	private List<School> schools;
	private List<School> listAllSchool;
	private List<Country> listCountry;
	private boolean isNearest, firstShow;
	private TextView txtCountry;
	private FrameLayout mMapLayout;
	private String selectedcountryCode;
	
	private MapView mMapView;
	private BaiduMap mMap;
	private School schoolForDetail;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_baidu_map);
		
		firstShow = true;
		mMapLayout = (FrameLayout)findViewById(R.id.mapLayout);
		schools = (List<School>) getIntent().getSerializableExtra("schools");
		listAllSchool = (List<School>) getIntent().getSerializableExtra("all_school");
		
		if (schools == null && listAllSchool == null) {
			schools = School.SchoolObjectBridge.getCurrentSchools();
			listAllSchool = School.SchoolObjectBridge.getAllSchools();
			
			if(!doValidation(schools)){
				finish();
			}
		}
		
		listCountry = (List<Country>) getIntent().getSerializableExtra("countries");
		isNearest = getIntent().getBooleanExtra("is_nearest", false);
		selectedcountryCode = getIntent().getStringExtra("countryCode");
				
		txtCountry = (TextView)findViewById(R.id.txtCountry);
		txtCountry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showCountryPicker();
			}
		});
		
		if (!isNearest) {
			txtCountry.setVisibility(View.GONE);
		} else {
			txtCountry.setVisibility(View.VISIBLE);
		}
		 
		if (schools!= null) {
			if (!isNearest) {
				double latitude = (schools.get(0).getBaiduLat());
				double longitude = (schools.get(0).getBaiduLong());
				mMapView = new MapView(this, new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(new LatLng(latitude, longitude)).build()));
				mMapLayout.addView(mMapView);
				mMap = mMapView.getMap();
				mMap.setOnMarkerClickListener(markerClickHandler);
				
				Bundle info = new Bundle();
				info.putSerializable("school", schools.get(0));
				OverlayOptions op = new MarkerOptions()
				.position(new LatLng(latitude, longitude))
				.title(schools.get(0).getName())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
				.extraInfo(info);
				mMap.addOverlay(op);
			} else {
				initMap();
				populateSchools(12);
			}
		} else {
			initMap();
			populateDefault();
		}
		
		if (firstShow && isNearest) {
			startTimerForDefault();
			firstShow = false;
		}
	}
	
	private boolean doValidation(List<School> schools) {
		for (int i = 0; i < schools.size(); i++) {
			if (((int)schools.get(i).getBaiduLat()) == 0 || ((int)schools.get(i).getBaiduLong()) == 0) {
				Toast.makeText(this, "Invalid Baidu location.", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	private void startTimerForDefault() {
		final Dialog d = ProgressDialog.show(this, "", "Loading..");
		d.setCancelable(false);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				populateDefault();
				d.dismiss();
			}
		}, 5000);
	}

	private void initMap(){
		mMapView = new MapView(this, new BaiduMapOptions().mapStatus(new MapStatus.Builder().zoom(12).build()));
		mMapLayout.addView(mMapView);
		mMap = mMapView.getMap();
		mMap.setOnMarkerClickListener(markerClickHandler);
		
	}
	
	private void populateDefault(){
		selectedcountryCode = "CN";
		txtCountry.setText("China");
		isNearest = false;
		populateSchools(12);
	}
	
	private void populateSchools(int zoomLevel) {
		System.out.println("populateSchool(): " + listAllSchool.size());
		ArrayList<School> locs = new ArrayList<School>();
		for (int i = 0; i < listAllSchool.size(); i++) {
			School sc = listAllSchool.get(i);
			if (selectedcountryCode.equals(sc.getCountryCode())) {
				locs.add(sc);
			}
		}
		boolean myCountry = false;
		if (selectedcountryCode.equals(LocationUtil.getCountryCode())) {
			myCountry = true;
		}
		
		if (locs.size() > 0) {
			populateMap(locs, myCountry, zoomLevel);
		} else {
			if (!firstShow) {
				Toast.makeText(this, "No DynEd School provided for this country.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@SuppressLint("NewApi")
	private void populateMap(ArrayList<School> locs, boolean myCountry, int zoomLevel) {
		
		mMap.clear();
		for (int i = 0; i < locs.size(); i++) {
			School sch = locs.get(i);
			Bundle info = new Bundle();
			info.putSerializable("school", sch);
			info.putSerializable("me", false);
			OverlayOptions op = new MarkerOptions()
			.position(new LatLng(sch.getBaiduLat(), sch.getBaiduLong()))
			.title(sch.getName())
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
			.extraInfo(info);
			mMap.addOverlay(op);
			
			if (i == 0) {
				if (!selectedcountryCode.equals("CN")) {
					LatLng ll = new LatLng(sch.getBaiduLat(), sch.getBaiduLong());
					mMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
				}
			}
		}
		
		if (selectedcountryCode.equals("CN") && LocationUtil.getMyBDLocation() != null) {
//			if (LocationUtil.getBDCountryCode().equals("CN")) {
				Bundle info = new Bundle();
				info.putSerializable("me", true);
				OverlayOptions op = new MarkerOptions()
				.position(new LatLng(LocationUtil.getMyBDLocation().getLatitude(), LocationUtil.getMyBDLocation().getLongitude()))
				.title("My location")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_g))
				.extraInfo(info);
				mMap.addOverlay(op);
				
				LatLng ll = new LatLng(LocationUtil.getMyBDLocation().getLatitude(), LocationUtil.getMyBDLocation().getLongitude());
				mMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
//			}
		}
	}
	
	private OnMarkerClickListener markerClickHandler = new OnMarkerClickListener() {		
		public boolean onMarkerClick(Marker marker) {
			if (marker.getExtraInfo().getBoolean("me")) {
				Toast.makeText(BaiduMapViewerActivity.this, "My location", Toast.LENGTH_SHORT).show();
			} else {
				School school = (School) marker.getExtraInfo().getSerializable("school");
				loadDetail(new LatLng(school.getBaiduLat(), school.getBaiduLong()));
			}
			return false;
		}
	};
	
	private void loadDetail(LatLng position) {
		schoolForDetail = null;
		if (listAllSchool == null) {
			for (int i = 0; i < schools.size(); i++) {
				String lat1 = String.format(Locale.getDefault(), "%.4f", schools.get(i).getBaiduLat());
				String long1 = String.format(Locale.getDefault(), "%.4f", schools.get(i).getBaiduLong());
				String lat2 = String.format(Locale.getDefault(), "%.4f", position.latitude);
				String long2 = String.format(Locale.getDefault(), "%.4f", position.longitude);
				if (lat1.equals(lat2) && long1.equals(long2)) {
					schoolForDetail = schools.get(i);
				}
			}
		} else {
			for (int i = 0; i < listAllSchool.size(); i++) {
				String lat1 = String.format(Locale.getDefault(), "%.4f", listAllSchool.get(i).getBaiduLat());
				String long1 = String.format(Locale.getDefault(), "%.4f", listAllSchool.get(i).getBaiduLong());
				String lat2 = String.format(Locale.getDefault(), "%.4f", position.latitude);
				String long2 = String.format(Locale.getDefault(), "%.4f", position.longitude);
				if (lat1.equals(lat2) && long1.equals(long2)) {
					schoolForDetail = listAllSchool.get(i);
				}
			}
		}
		
		if (schoolForDetail == null) return;
		
		//start load detail
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(BaiduMapViewerActivity.this, "", "Loading..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response school detail: " + str);
				
				School sc = School.parseSchoolObject(str, schoolForDetail.getCountryName(), schoolForDetail.isEurope());
				showDetailDialog(sc);
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(BaiduMapViewerActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		postInternetTask.addPair("id", "" + schoolForDetail.getId());
    	postInternetTask.execute(URLAddress.SCHOOLS_DETAIL_URL);
		
	}
	
	private void showDetailDialog(final School schoolForDetail2) {
		View v = LayoutInflater.from(this).inflate(R.layout.map_detail_dialog, null);
		TextView txtAddress = (TextView)v.findViewById(R.id.txtAddress);
		TextView txtWebsite = (TextView)v.findViewById(R.id.txtWebsite);
		TextView txtEmail = (TextView)v.findViewById(R.id.txtEmail);
		TextView txtPhone = (TextView)v.findViewById(R.id.txtPhone);
		TextView txtCP = (TextView)v.findViewById(R.id.txtCP);
		TextView txtDesc = (TextView)v.findViewById(R.id.txtDesc);
		  
		txtAddress.setText(schoolForDetail2.getAddress());
		txtWebsite.setText(Html.fromHtml("Website: <u>" + schoolForDetail2.getWebsite() + "</u>"));
		txtEmail.setText("Email: " + schoolForDetail2.getEmail());
		txtPhone.setText(Html.fromHtml("Phone: <u>" + schoolForDetail2.getTelp() + "</u>"));
		txtCP.setText("CP: " + schoolForDetail2.getContactPerson());
		System.out.println("xxx school CP: " + schoolForDetail2.getContactPerson());
		System.out.println("xxx school description: " + schoolForDetail2.getDesc());
		txtDesc.setText(Html.fromHtml(schoolForDetail2.getDesc()));
		
		final String telp = schoolForDetail2.getTelp();
		txtPhone.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
	            Intent callIntent = new Intent(Intent.ACTION_CALL);
	            callIntent.setData(Uri.parse("tel:"+ telp));
	            startActivity(callIntent);
			}
		});
		
		String web = schoolForDetail2.getWebsite();
		if (!web.startsWith("http://") && !web.startsWith("https://"))
			web = "http://" + web;
		final String url = web;
		txtWebsite.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
			}
		});
		
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setTitle(schoolForDetail2.getName());
		alertDialog.setView(v);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "导航", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startNavi(schoolForDetail2);
			}
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "返回", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
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
				if (listCountry.get(item).isEurope()) {
					for (int i = 0; i < listAllSchool.size(); i++) {
						School sc = listAllSchool.get(i);
						if (listCountry.get(item).getCountryCode().equals(sc.getCountryCode())) {
							//open url in browser;
							String url = sc.getUrl();
							System.out.println("url clicked: " + url);
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							startActivity(browserIntent);
							return;
						}
					}
				} else {
					viewInCountry(item, 12);
				}
				
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	private void viewInCountry(int item, int zoomLevel) {
		selectedcountryCode = listCountry.get(item).getCountryCode();
		txtCountry.setText(listCountry.get(item).getName());
		isNearest = false;
		populateSchools(zoomLevel);
	}
	
	public void startNavi(School endPoint) {
		BDLocation myLoc = LocationUtil.getMyBDLocation();
		if (myLoc == null) {
			Toast.makeText(this, "Baidu Map cannot retrieve your current location.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		LatLng pt1 = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "My location";
		para.endPoint = new LatLng(endPoint.getBaiduLat(), endPoint.getBaiduLong());
		para.endName = endPoint.getName();

		try {
			BaiduMapNavigation.openBaiduMapNavi(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Install Baidu Map app to complete your action?");
			builder.setTitle("");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					BaiduMapNavigation.getLatestBaiduMapApp(BaiduMapViewerActivity.this);
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.create().show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mMapView != null) mMapView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mMapView != null) mMapView.onPause();
	}
}
