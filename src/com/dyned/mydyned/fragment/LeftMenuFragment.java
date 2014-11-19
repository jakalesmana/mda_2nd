package com.dyned.mydyned.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.HomeFragmentActivity;
import com.dyned.mydyned.ProfileActivity;
import com.dyned.mydyned.R;
import com.dyned.mydyned.SignInActivity;
import com.dyned.mydyned.component.AppMenuItem;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.manager.AppManager;
import com.dyned.mydyned.model.App;
import com.dyned.mydyned.model.Country;
import com.dyned.mydyned.model.Language;
import com.dyned.mydyned.model.Profile;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.PreferencesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LeftMenuFragment extends SherlockFragment {

	private LinearLayout layoutMenu, layoutProfile, layoutSignIn, layoutHome, layoutSetting, layoutSupport, layoutServer, layoutDyned;
	private View menuView;

	private ArrayList<Country> listCountry;
	private ArrayList<Language> listLanguage;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	private DisplayImageOptions optionsAvatar = new DisplayImageOptions.Builder()
	.showStubImage(R.drawable.ic_user)
	.showImageForEmptyUri(R.drawable.ic_user).cacheOnDisc()
	.cacheInMemory().build();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		menuView = inflater.inflate(R.layout.left_menu_fragment, container, false);
		layoutMenu = (LinearLayout)menuView.findViewById(R.id.layoutMenu);
		layoutSignIn = (LinearLayout)menuView.findViewById(R.id.layoutLogin);
		layoutProfile = (LinearLayout)menuView.findViewById(R.id.layoutProfile);
		layoutHome = (LinearLayout)menuView.findViewById(R.id.layoutHome);
		layoutSetting = (LinearLayout)menuView.findViewById(R.id.layoutSetting);
		layoutSupport = (LinearLayout)menuView.findViewById(R.id.layoutSupport);
		layoutServer = (LinearLayout)menuView.findViewById(R.id.layoutServer);
		layoutDyned = (LinearLayout)menuView.findViewById(R.id.layoutDynEdPro);
		layoutSignIn.setOnClickListener(new ClickHandler());
		layoutHome.setOnClickListener(new ClickHandler());
		layoutSetting.setOnClickListener(new ClickHandler());
		layoutSupport.setOnClickListener(new ClickHandler());
		layoutServer.setOnClickListener(new ClickHandler());
		layoutDyned.setOnClickListener(new ClickHandler());
		layoutProfile.setOnClickListener(new ClickHandler());
		
		List<App> listApp = AppManager.getInstance().getApps();
		for (int i = 0; i < listApp.size(); i++) {
			App app = listApp.get(i);
			AppMenuItem item = new AppMenuItem(getSherlockActivity(), app);
			layoutMenu.addView(item);
		}
		
		return menuView;
	}
	
//	private List<App> getAppList(Context ctx){
//		List<App> apps = new ArrayList<App>();
		
//		PackageManager pm = ctx.getPackageManager();
//		for(ApplicationInfo app : pm.getInstalledApplications(0)) {
//		    if (app.packageName.contains("qs.dn.ns")) {
//				try {
//					apps.add(new App(app.packageName, pm.getApplicationLabel(app).toString(), pm.getApplicationIcon(app.packageName)));
//				} catch (NameNotFoundException e) {
//					apps.add(new App(app.packageName, pm.getApplicationLabel(app).toString(), "ic_ge"));
//				}
//			}
//		}
//		
//		Collections.sort(apps, new Comparator<App>() {
//	        public int compare(App app1, App app2) {
//	            return app1.getAppName().compareToIgnoreCase(app2.getAppName());
//	        }
//	    });
		
//		apps.add(new App("qs.dn.ns", "General English 1", "ic_ge"));
//		apps.add(new App("qs.dn.ns2", "General English 2", "ic_ge"));
//		apps.add(new App("qs.dn.ns3", "General English 3", "ic_ge"));
//		apps.add(new App("qs.dn.ns4", "General English 4", "ic_ge"));
//		apps.add(new App("qs.dn.ns5", "General English 5", "ic_ge"));
//		apps.add(new App("qs.dn.ns6", "General English 6", "ic_ge"));
		
//		return apps;
//	}
	
	class ClickHandler implements OnClickListener {
		public void onClick(View v) {
			Fragment targetFragment = null;
			if(v == layoutProfile) {
				loadCountryAndLanguage();
				return;
			} else if(v == layoutSignIn) {
				startActivity(new Intent(getSherlockActivity(), SignInActivity.class));
				return;
			}else if (v == layoutDyned) {
//				if(check()) {
//					PackageManager pack = getSherlockActivity().getPackageManager();
//					Intent app = pack.getLaunchIntentForPackage("com.dyned.engine");
//					startActivity(app);
//				} else {
//					Intent marketIntent = new Intent(Intent.ACTION_VIEW);
//					marketIntent.setData(Uri.parse("market://details?id=com.dyned.engine"));
//					startActivity(marketIntent);
//				}
                
				return;
			} else if(v == layoutHome) {
				targetFragment = new HomeFragment();
			} else if(v == layoutSetting) {
				targetFragment = new SettingFragment();
			} else if(v == layoutSupport){
				targetFragment = new SupportFragment();
			} else if(v == layoutServer){
//				targetFragment = new WebViewFragment();
//				Bundle b = new Bundle();
//				b.putString("url_menu", "http://m.dyned.com/serverstatus");
//				targetFragment.setArguments(b);
				
				targetFragment = new ServerFragment();
			}
			((HomeFragmentActivity)getSherlockActivity()).switchContent(targetFragment);
		}
	}
	
	public boolean check()
	{
	    try{
	        getSherlockActivity().getPackageManager().getApplicationInfo("com.dyned.engine", 0 );
	        return true;
	    } catch( PackageManager.NameNotFoundException e ){
	        return false;
	    }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		PreferencesUtil pref = PreferencesUtil.getInstance(getSherlockActivity());
		if (pref.isLoggedIn()) {
			ImageView imgAvatar = (ImageView)menuView.findViewById(R.id.imgAvatar);
			TextView txtName = (TextView)menuView.findViewById(R.id.txtName);
			txtName.setText(pref.getname());
			TextView txtRole = (TextView)menuView.findViewById(R.id.txtRole);
//			txtRole.setText(pref.getRoleKey());
			txtRole.setText(pref.getRoleTitle());
			
			loadImage(pref, imgAvatar);
			
			layoutProfile.setVisibility(View.VISIBLE);
			layoutSignIn.setVisibility(View.GONE);
		} else {
			layoutProfile.setVisibility(View.GONE);
			layoutSignIn.setVisibility(View.VISIBLE);
		}
	}

	private void loadImage(PreferencesUtil pref, ImageView imgAvatar) {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
		imgAvatar.getLayoutParams().width = bmp.getWidth();
		imgAvatar.getLayoutParams().height = bmp.getWidth();
		 ImageLoader imageLoader = ImageLoader.getInstance();
		 
		 imageLoader.displayImage(pref.getAvatar(), imgAvatar,
		 optionsAvatar, new AnimateFirstDisplayListener(pref.getAvatar(),imgAvatar, false, 0, 0));
	}
	
	
	
	private void loadCountryAndLanguage() {
		InternetTask getTask = new InternetTask(getSherlockActivity(), new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(getSherlockActivity(), "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response load country: " + str);
				listCountry = (ArrayList<Country>) Country.parseCountries(str);
				loadLanguage();
			}
			
			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		getTask.execute(URLAddress.COUNTRY_URL);
	}
	
	private void loadLanguage() {
		InternetTask getTask = new InternetTask(getSherlockActivity(), new InternetConnectionListener() {
			public void onStart() {
			 
			}
			
			public void onDone(String str) {
				System.out.println("response load language: " + str);
				listLanguage = (ArrayList<Language>) Language.parseLanguages(str);
				loadProfile();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		getTask.execute(URLAddress.LANGUAGE_URL);
	}
	
	private void loadProfile() {
		PostInternetTask postTask = new PostInternetTask(getSherlockActivity(), new InternetConnectionListener() {
			public void onStart() {
			}
			
			public void onDone(String str) {
				System.out.println("response load profile: " + str);
				Profile p = Profile.parseProfile(str);
				dialog.dismiss();
				
				if (p!=null) {
					Intent i = new Intent(getSherlockActivity(), ProfileActivity.class);
					i.putExtra("countries", listCountry);
					i.putExtra("languages", listLanguage);
					i.putExtra("profile", p);
					startActivity(i);
				} else {
					Toast.makeText(getSherlockActivity(), "Can not profile" + ", try again later.", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		postTask.addPair("app_key", PreferencesUtil.getInstance(getSherlockActivity()).getAppKey());
		postTask.execute(URLAddress.PROFILE_URL);
	}
}
