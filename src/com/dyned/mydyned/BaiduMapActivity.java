//package com.dyned.mydyned;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.MKOfflineMap;
//import com.baidu.mapapi.map.MKOfflineMapListener;
//import com.baidu.mapapi.map.MapController;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.OverlayItem;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.dyned.mydyned.app.DynEdApplication;
//import com.dyned.mydyned.model.Country;
//import com.dyned.mydyned.model.School;
//import com.dyned.mydyned.utils.LocationUtil;
//import com.google.android.gms.maps.model.LatLng;
//
//public class BaiduMapActivity extends BaseActivity {
//
//	private BMapManager mBMapMan;
//	private List<School> schools;
//	private List<School> listAllSchool;
//	private TextView txtCountry;
//	private List<Country> listCountry;
//	private String selectedcountryCode;
//	private boolean isNearest;
//	private MapView mMapView;
//	private MKOfflineMap mOffline;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		
//		schools = (List<School>) getIntent().getSerializableExtra("schools");
//		listAllSchool = (List<School>) getIntent().getSerializableExtra("all_school");
//		listCountry = (List<Country>) getIntent().getSerializableExtra("countries");
//		isNearest = getIntent().getBooleanExtra("is_nearest", false);
//		
////		mBMapMan = new BMapManager(getApplication());
////		mBMapMan.init("7ACEFF85C0E8824D3EDE8EBE3C9EBE32838422A7", null);
//		
//		mBMapMan = ((DynEdApplication)this.getApplication()).mBMapMan;
//		if (mBMapMan == null) {
//			mBMapMan = new BMapManager(this);
//			mBMapMan.init("7ACEFF85C0E8824D3EDE8EBE3C9EBE32838422A7", null);
//		}
//		
//		setContentView(R.layout.activity_baidu_map);
////		getSupportActionBar().hide();
//
//		txtCountry = (TextView)findViewById(R.id.txtCountry);
//		txtCountry.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				showCountryPicker();
//			}
//		});
//		
//		if (!isNearest) {
//			txtCountry.setVisibility(View.GONE);
//		} else {
//			txtCountry.setVisibility(View.VISIBLE);
//		}
//		
//		
//		mMapView = (MapView) findViewById(R.id.map);
//		mMapView.setBuiltInZoomControls(true);
//		mOffline = new MKOfflineMap();
////		mMapView.showScaleControl(true);
//		
//		final MapController mMapController = mMapView.getController();
//		mOffline.init(mMapController, new MKOfflineMapListener() {			
//			public void onGetOfflineMapState(int arg0, int arg1) {
//			}
//		});
//		
//		mMapView.getOverlays().clear();  
//		if (schools!= null) {
//			DynedLocationItem itemOverlay =  new DynedLocationItem(getResources().getDrawable(R.drawable.map_marker), mMapView, schools.get(0));
//			mMapView.getOverlays().add(itemOverlay);
//			 
//			GeoPoint point = new GeoPoint(( int ) ( schools.get(0).getLatitude() * 1E6),  (int) ( schools.get(0).getLongitude() * 1E6));
//			OverlayItem item = new OverlayItem(point, schools.get(0).getName(), schools.get(0).getCity());
//			itemOverlay.addItem (item);  
////			mMapController.animateTo(point);
//			mMapController.setCenter(point);
//			mMapController.setZoom(12);
//			mMapView.setTraffic(false);
//		} else {
////			for (int i = 0; i < listAllSchool.size(); i++) {
////				DynedLocationItem itemOverlay =  new DynedLocationItem(getResources().getDrawable(R.drawable.map_marker), mMapView, listAllSchool.get(i));
////				mMapView.getOverlays().add(itemOverlay);
////				
////				GeoPoint point = new GeoPoint(( int ) ( listAllSchool.get(i).getLatitude() * 1E6),  (int) ( listAllSchool.get(i).getLongitude() * 1E6));
////				OverlayItem item = new OverlayItem(point, listAllSchool.get(i).getName(), listAllSchool.get(i).getCity());
////				itemOverlay.addItem (item);  
////			}
//			
//			selectedcountryCode = "CN";
//			txtCountry.setText("China");
//			isNearest = false;
//			populateSchools(3);
//		}
//		
////		mMapView.refresh();
//		mMapView.onResume();
////		mMapView.refresh();
//		
//		new Handler().postDelayed(new Runnable() {			
//			public void run() {
//				mMapView.onResume();
//			}
//		}, 20000);
//	}
//
//	
//	private void showCountryPicker() {
//		String[] countries = new String[listCountry.size()];
//		for (int i = 0; i < countries.length; i++) {
//			countries[i] = listCountry.get(i).getName();
//		}
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, countries);
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//		builder.setTitle("Country:");
//		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int item) {
//				viewInCountry(item, 3);
//				dialog.dismiss();
//			}
//			
//		});
//		builder.show();
//	}
//	
//	private void viewInCountry(int item, int zoomLevel) {
//				
//		selectedcountryCode = listCountry.get(item).getCountryCode();
//		txtCountry.setText(listCountry.get(item).getName());
//		isNearest = false;
//		populateSchools(zoomLevel);
//	}
//	
//	private void populateSchools(int zoomLevel) {
//		ArrayList<School> locs = new ArrayList<School>();
//		for (int i = 0; i < listAllSchool.size(); i++) {
//			School sc = listAllSchool.get(i);
//			if (selectedcountryCode.equals(sc.getCountryCode())) {
//				locs.add(sc);
//			}
//		}
//		boolean myCountry = false;
//		if (selectedcountryCode.equals(LocationUtil.getCountryCode())) {
//			myCountry = true;
//		}
//		
//		if (locs.size() > 0) {
//			populateMap(locs, myCountry, zoomLevel);
//		} else {
//			Toast.makeText(this, "No DynEd School provided for this country.", Toast.LENGTH_SHORT).show();
//		}
//	}
//	
//	private void populateMap(ArrayList<School> locs, boolean myCountry, int zoomLevel) {
//		mMapView.getOverlays().clear();
//		for (int i = 0; i < locs.size(); i++) {
//			DynedLocationItem itemOverlay =  new DynedLocationItem(getResources().getDrawable(R.drawable.map_marker), mMapView, locs.get(i));
//			mMapView.getOverlays().add(itemOverlay);
//			
//			GeoPoint point = new GeoPoint(( int ) ( locs.get(i).getLatitude() * 1E6),  (int) ( locs.get(i).getLongitude() * 1E6));
//			OverlayItem item = new OverlayItem(point, locs.get(i).getName(), locs.get(i).getCity());
//			itemOverlay.addItem (item);  
//		}
//		mMapView.refresh();
//	}
//
//	@Override
//	protected void onDestroy() {
//		if (mBMapMan != null) {
//			mBMapMan.destroy();
//			mBMapMan = null;
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onPause() {
//		if (mBMapMan != null) {
////			mBMapMan.stop();
//		}
//		mMapView.onPause();
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		if (mBMapMan != null) {
////			mBMapMan.start();
//		}
//		mMapView.onResume();
//		super.onResume();
//	}
//	
//	@Override
//	public void onBackPressed() {
////		if (mBMapMan != null) {
////			mBMapMan.stop();
////			mBMapMan.destroy();
////			mBMapMan = null;
////		}
//		System.gc();
//		System.gc();
//		super.onBackPressed();
//	}
//	
//	
//	class DynedLocationItem  extends  ItemizedOverlay <OverlayItem> {  
//
//		private School school;
//		
//		public DynedLocationItem(Drawable Mark, MapView mapView, School school) {  
//	            super(Mark, mapView);
//	            this.school = school;
//	    }
//	    
//	    protected  boolean  onTap (int  index) {  
//	        showDetail(new LatLng(school.getLatitude(), school.getLongitude()));
//	        return true ;  
//	    }  
//	    
//	    public boolean onTap (GeoPoint pt, MapView mapView) {  
//            super.onTap(pt, mapView);  
//            return false ;  
//	    }  
//	        // Since 2.1.1, use the add / remove management overlay, without rewriting the following interfaces  
//	        /* 
//	        @ Override 
//	        protected OverlayItem createItem (int i) { 
//	                return mGeoList.get (i); 
//	        } 
//	        
//	        @ Override 
//	        public int size () { 
//	                return mGeoList.size (); 
//	        } 
//	        */  
//	}          
//	
//	
//	
//	private void showDetail(LatLng position) {
//		School school = null;
//		if (listAllSchool == null) {
//			for (int i = 0; i < schools.size(); i++) {
//				String lat1 = String.format(Locale.getDefault(), "%.4f", schools.get(i).getLatitude());
//				String long1 = String.format(Locale.getDefault(), "%.4f", schools.get(i).getLongitude());
//				String lat2 = String.format(Locale.getDefault(), "%.4f", position.latitude);
//				String long2 = String.format(Locale.getDefault(), "%.4f", position.longitude);
//				if (lat1.equals(lat2) && long1.equals(long2)) {
//					school = schools.get(i);
//				}
//			}
//		} else {
//			for (int i = 0; i < listAllSchool.size(); i++) {
//				String lat1 = String.format(Locale.getDefault(), "%.4f", listAllSchool.get(i).getLatitude());
//				String long1 = String.format(Locale.getDefault(), "%.4f", listAllSchool.get(i).getLongitude());
//				String lat2 = String.format(Locale.getDefault(), "%.4f", position.latitude);
//				String long2 = String.format(Locale.getDefault(), "%.4f", position.longitude);
//				if (lat1.equals(lat2) && long1.equals(long2)) {
//					school = listAllSchool.get(i);
//				}
//			}
//		}
//		
//		if (school == null) return;
//		  
//		View v = LayoutInflater.from(this).inflate(R.layout.map_detail_dialog, null);
//		TextView txtAddress = (TextView)v.findViewById(R.id.txtAddress);
//		TextView txtWebsite = (TextView)v.findViewById(R.id.txtWebsite);
//		TextView txtEmail = (TextView)v.findViewById(R.id.txtEmail);
//		TextView txtPhone = (TextView)v.findViewById(R.id.txtPhone);
//		TextView txtCP = (TextView)v.findViewById(R.id.txtCP);
//		TextView txtDesc = (TextView)v.findViewById(R.id.txtDesc);
//		  
//		txtAddress.setText(school.getAddress());
//		txtWebsite.setText(Html.fromHtml("Website: <u>" + school.getWebsite() + "</u>"));
//		txtEmail.setText("Email: " + school.getEmail());
//		txtPhone.setText(Html.fromHtml("Phone: <u>" + school.getTelp() + "</u>"));
//		txtCP.setText("CP: " + school.getContactPerson());
//		txtDesc.setText(Html.fromHtml(school.getDesc()));
//		
//		final String telp = school.getTelp();
//		txtPhone.setOnClickListener(new OnClickListener() {
//			public void onClick(View view) {
//	            Intent callIntent = new Intent(Intent.ACTION_CALL);
//	            callIntent.setData(Uri.parse("tel:"+ telp));
//	            startActivity(callIntent);
//			}
//		});
//		
//		String web = school.getWebsite();
//		if (!web.startsWith("http://") && !web.startsWith("https://"))
//			web = "http://" + web;
//		final String url = web;
//		txtWebsite.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//				startActivity(browserIntent);
//			}
//		});
//		
//		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//		alertDialog.setCanceledOnTouchOutside(false);
//		alertDialog.setTitle(school.getName());
//		alertDialog.setView(v);
//		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				alertDialog.dismiss();
//			}
//		});
//		alertDialog.show();
//	}
//	
//	@Override
//    protected void onSaveInstanceState(Bundle outState) {
//    	super.onSaveInstanceState(outState);
//    	mMapView.onSaveInstanceState(outState);
//    	
//    }
//    
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//    	super.onRestoreInstanceState(savedInstanceState);
//    	mMapView.onRestoreInstanceState(savedInstanceState);
//    }
//}
