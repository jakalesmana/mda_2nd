package com.dyned.mydyned;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.Country;
import com.dyned.mydyned.model.School;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.GMapV2Direction;
import com.dyned.mydyned.utils.LocationUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * author: jakalesmana
 */

public class MapActivity extends BaseFragmentActivity {
	private GoogleMap map;
	private LatLng target;
	private boolean isNearest;
	private List<Country> listCountry;
	private TextView txtCountry;
	private List<School> listAllSchool;
	
	private String selectedcountryCode;
	private List<School> schools;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//				this));

		txtCountry = (TextView)findViewById(R.id.txtCountry);
		txtCountry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showCountryPicker();
			}
		});
		
		listCountry = (List<Country>) getIntent().getSerializableExtra("countries");
		schools = (List<School>) getIntent().getSerializableExtra("schools");
		listAllSchool = (List<School>) getIntent().getSerializableExtra("all_school");
		isNearest = getIntent().getBooleanExtra("is_nearest", false);

		if (!isNearest) {
			txtCountry.setVisibility(View.GONE);
		} else {
			txtCountry.setVisibility(View.VISIBLE);
		}
		
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		if (map != null) {
			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				public void onInfoWindowClick(Marker marker) {
					loadDetail(marker.getPosition());
				}
			});
			
			for (int i = 0; i < schools.size(); i++) {
				School school = schools.get(i);
				LatLng position = new LatLng(school.getLatitude(),
						school.getLongitude());
				if (i == 0) {
					target = position;
				}
				map.addMarker(new MarkerOptions()
						.position(position)
						.title(school.getName() + ". City: " + school.getCity())
						.snippet(school.getAddress()));
			}

			if (isNearest) {
				if (LocationUtil.getMyLocation() != null) { // cant get my current location
					LatLng me = new LatLng(LocationUtil.getMyLocation()
							.getLatitude(), LocationUtil.getMyLocation()
							.getLongitude());
					map.addMarker(new MarkerOptions().position(me)
							.title("Your location")
							.icon(BitmapDescriptorFactory.defaultMarker(50))
							.snippet("Your location"));
					CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(me) // Sets the center of the map to position
					.zoom(9) // Sets the zoom
					.build(); // Creates a CameraPosition from the builder
					map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
				} else {
					//can't get nearest location due to cannot get current location
					viewInCountry(0, 9);
				}
			} else {
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(target) // Sets the center of the map to
										// position
						.zoom(10) // Sets the zoom
						.build(); // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
				
//				showDirection(schools.get(0).getCountryCode(), target);
			}
		}
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
						System.out.println("code clicked: " + sc.getCountryCode() + " : " + listCountry.get(item).getCountryCode());
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
					viewInCountry(item, 9);
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
	
	private void populateSchools(int zoomLevel) {
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
			Toast.makeText(this, "No DynEd School provided for this country.", Toast.LENGTH_SHORT).show();
		}
	}

	private void populateMap(ArrayList<School> schools, boolean isMyCountry, int zoomLevel) {
		if (map != null) {
			
			map.clear();
			
			for (int i = 0; i < schools.size(); i++) {
				School school = schools.get(i);
				LatLng position = new LatLng(school.getLatitude(),
						school.getLongitude());
				if (i == 0) {
					target = position;
				}
				map.addMarker(new MarkerOptions()
						.position(position)
						.title(school.getName() + ". City: " + school.getCity())
						.snippet(school.getAddress()));
			}

			if (isNearest || isMyCountry) {
				LatLng me = new LatLng(LocationUtil.getMyLocation()
						.getLatitude(), LocationUtil.getMyLocation()
						.getLongitude());
				map.addMarker(new MarkerOptions().position(me)
						.title("Your location")
						.icon(BitmapDescriptorFactory.defaultMarker(50))
						.snippet("Your location"));
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(me) // Sets the center of the map to position
						.zoom(zoomLevel) // Sets the zoom
						.build(); // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			} else {
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(target) // Sets the center of the map to
										// position
						.zoom(10) // Sets the zoom
						.build(); // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
		}
	}
	
	
	//TODO Newly created. Need to be tested.
	private void showDirection(String countryCode, LatLng dest){
		
		if (!LocationUtil.getCountryCode().equals(countryCode)) {
			return;
		}
		
		Location myLoc = LocationUtil.getMyLocation();
		if (myLoc != null) {
			new BuildDestinationTrackTask(myLoc, dest).execute();
		}

	}
	
	
	private void loadDetail(LatLng position) {
		final School finSc;
		School school = null;
		if (listAllSchool == null) {
			for (int i = 0; i < schools.size(); i++) {
				String lat1 = String.format(Locale.getDefault(), "%.4f", schools.get(i).getLatitude());
				String long1 = String.format(Locale.getDefault(), "%.4f", schools.get(i).getLongitude());
				String lat2 = String.format(Locale.getDefault(), "%.4f", position.latitude);
				String long2 = String.format(Locale.getDefault(), "%.4f", position.longitude);
				if (lat1.equals(lat2) && long1.equals(long2)) {
					school = schools.get(i);
				}
			}
		} else {
			for (int i = 0; i < listAllSchool.size(); i++) {
				String lat1 = String.format(Locale.getDefault(), "%.4f", listAllSchool.get(i).getLatitude());
				String long1 = String.format(Locale.getDefault(), "%.4f", listAllSchool.get(i).getLongitude());
				String lat2 = String.format(Locale.getDefault(), "%.4f", position.latitude);
				String long2 = String.format(Locale.getDefault(), "%.4f", position.longitude);
				if (lat1.equals(lat2) && long1.equals(long2)) {
					school = listAllSchool.get(i);
				}
			}
		}
		
		if (school == null) return;
		finSc = school;
		
		//start load detail
		PostInternetTask postInternetTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {					
					public void run() {
						dialog = ProgressDialog.show(MapActivity.this, "", "Loading..");
					}
				});	
			}
			
			public void onDone(String str) {
				System.out.println("response school detail: " + str);
				
				School sc = School.parseSchoolObject(str, finSc.getCountryName(), finSc.isEurope());
				showDetailDialog(sc);
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(MapActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		postInternetTask.addPair("id", "" + school.getId());
    	postInternetTask.execute(URLAddress.SCHOOLS_DETAIL_URL);
	}
	
	
	private void showDetailDialog(School school) {
		View v = LayoutInflater.from(this).inflate(R.layout.map_detail_dialog, null);
		TextView txtAddress = (TextView)v.findViewById(R.id.txtAddress);
		TextView txtWebsite = (TextView)v.findViewById(R.id.txtWebsite);
		TextView txtEmail = (TextView)v.findViewById(R.id.txtEmail);
		TextView txtPhone = (TextView)v.findViewById(R.id.txtPhone);
		TextView txtCP = (TextView)v.findViewById(R.id.txtCP);
		TextView txtDesc = (TextView)v.findViewById(R.id.txtDesc);
		  
		txtAddress.setText(school.getAddress());
		txtWebsite.setText(Html.fromHtml("Website: <u>" + school.getWebsite() + "</u>"));
		txtEmail.setText("Email: " + school.getEmail());
		txtPhone.setText(Html.fromHtml("Phone: <u>" + school.getTelp() + "</u>"));
		txtCP.setText("CP: " + school.getContactPerson());
		txtDesc.setText(Html.fromHtml(school.getDesc()));
		
		final String telp = school.getTelp();
		txtPhone.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (AppUtil.hasCallPhoneFeature(MapActivity.this)) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
		            callIntent.setData(Uri.parse("tel:"+ telp));
		            startActivity(callIntent);
				}
			}
		});
		
		String web = school.getWebsite();
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
		alertDialog.setTitle(school.getName());
		alertDialog.setView(v);
		
		final double lat = school.getLatitude();
		final double lang = school.getLongitude();
		
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Direction", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr=" + lat + "," + lang + ""));
				intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
				startActivity(intent);
			}
		});
		
		alertDialog.show();
	}


	private class BuildDestinationTrackTask extends AsyncTask<String, Void, PolylineOptions> {

	    private Location loc;
	    private LatLng dest;
	    
	    public BuildDestinationTrackTask(Location loc, LatLng dest) {
	    	this.loc = loc;
	    	this.dest = dest;
		}
	    
	    protected PolylineOptions doInBackground(String... dummy) {
	    	LatLng fromPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
			LatLng toPosition = dest;
	
			GMapV2Direction md = new GMapV2Direction();
	
			Document doc = md.getDocument(fromPosition, toPosition, GMapV2Direction.MODE_DRIVING);
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.BLUE);
	
			for(int i = 0 ; i < directionPoint.size() ; i++) {          
				rectLine.add(directionPoint.get(i));
			}
	
			return rectLine;
	    }

	    protected void onPostExecute(PolylineOptions line) {
	    	map.addPolyline(line);
	    	map.addMarker(new MarkerOptions()
			.position(new LatLng(loc.getLatitude(), loc.getLongitude()))
			.title("My Location")
			.snippet("My Current Location"));
	    }
	}
}
