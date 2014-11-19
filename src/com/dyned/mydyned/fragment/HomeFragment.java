package com.dyned.mydyned.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.AssessmentActivity;
import com.dyned.mydyned.EventActivity;
import com.dyned.mydyned.MediaActivity;
import com.dyned.mydyned.NewsActivity;
import com.dyned.mydyned.R;
import com.dyned.mydyned.SchoolListActivity;
import com.dyned.mydyned.SocialFlipListActivity;
import com.dyned.mydyned.WebAssessmentViewerActivity;
import com.dyned.mydyned.WebRecordsViewerActivity;
import com.dyned.mydyned.WebViewerActivity;
import com.dyned.mydyned.component.AppPickerDialog;
import com.dyned.mydyned.composite.HomeGridAdapter;
import com.dyned.mydyned.composite.HomeGridAdapter.ViewHolder;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.Assessment;
import com.dyned.mydyned.model.Event;
import com.dyned.mydyned.model.HomeItem;
import com.dyned.mydyned.model.News;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;
import com.dyned.mydyned.utils.LocationUtil;
import com.dyned.mydyned.utils.PreferencesUtil;

/**
author: jakalesmana
 */

public class HomeFragment extends SherlockFragment {
		
	public static final String SOCIAL_MEDIA = "Social Media";
	public static final String LAUNCH_DYNED = "Launch DynEd";
	public static final String EVENT = "Events";
	public static final String SHARE = "Share";
	public static final String FIND_SCHOOL = "Find A School";
	public static final String ASESSMENT = "Assessment";
	public static final String NEWS = "News";
	public static final String ANALYTIC = "Analytics";
	public static final String RECORDS = "MyRecords";
	public static final String MEDIA = "Media";
	
	private ProgressDialog dialog;
	private HomeGridAdapter adapter;
	private GridView gvHome;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	    	view = inflater.inflate(R.layout.activity_home, container, false);
			initView(view);
	    } catch (InflateException e) {
	    }
	    
	    return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initView(view);
	}
	
	@SuppressLint("DefaultLocale")
	private void initView(View view) {
		gvHome = (GridView) view.findViewById(R.id.gvHome);
		adapter = new HomeGridAdapter(getSherlockActivity(), buildItemHome());
		gvHome.setAdapter(adapter);
		
		gvHome.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				final ViewHolder holder = ((ViewHolder)view.getTag());
				RelativeLayout layout = holder.layout;
				layout.startAnimation(AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.image_click));
				new Handler().postDelayed(new Runnable() {
					
					public void run() {
						if (holder.name.getText().equals(RECORDS)) {
							if (PreferencesUtil.getInstance(getSherlockActivity()).isLoggedIn()) {
								Intent i = new Intent(getSherlockActivity(), WebRecordsViewerActivity.class);
								i.putExtra("url_menu", holder.url);
								startActivity(i);
							} else {
								showLoginConfirmDialog("View DynEd Study Records", "Please sign in to view MyRecords", holder.url, RECORDS);
							}
							return;
						}
						
						if (holder.name.getText().equals(ASESSMENT)) {
//							if (PreferencesUtil.getInstance(getSherlockActivity()).isLoggedIn()) {
//								Intent i = new Intent(getSherlockActivity(), WebAssessmentViewerActivity.class);
//								i.putExtra("url_menu", holder.url);
//								startActivity(i);
//							} else {
//								showLoginConfirmDialog("View DynEd Assessment", "Please sign in to view Assessment", holder.url, ASESSMENT);
//							}
							loadAssessment();
							return;
						}
						
						if (holder.url != null && !holder.url.equals("")) {
							Intent i = new Intent(getSherlockActivity(), WebViewerActivity.class);
							i.putExtra("url_menu", holder.url);
							startActivity(i);
						} else {
							if (holder.name.getText().equals(SOCIAL_MEDIA)) {
								Intent i = new Intent(getSherlockActivity(), SocialFlipListActivity.class);
								startActivity(i);
							} else if(holder.name.getText().equals(FIND_SCHOOL)) {
								Intent i = new Intent(getSherlockActivity(), SchoolListActivity.class);
								startActivity(i);
							} else if(holder.name.getText().equals(MEDIA)) {
								Intent i = new Intent(getSherlockActivity(), MediaActivity.class);
								startActivity(i);
							} else if(holder.name.getText().equals(NEWS)) {
								retrieveNewsList();
							} else if(holder.name.getText().equals(EVENT)) {
								retrieveEventList();
							} else if(holder.name.getText().equals(SHARE)) {
//								String share = "I love MyDynEd App! Thought I’d share this great App with you, it’s free and it has helped me find the smart way to learn English with DynEd!" +
//										"\n\n<Link to Google Play>";
								PreferencesUtil pref = PreferencesUtil.getInstance(getSherlockActivity()); 
								String linkStore = "";
								String globalCode = LocationUtil.getCountryCode();
								String baiduCode = LocationUtil.getBDCountryCode();
								String codeByIP = LocationUtil.getCountryCodeByIP();
								
								if (globalCode.toLowerCase().equals("cn") || codeByIP.toLowerCase().equals("cn") ||
										baiduCode.toLowerCase().equals("cn") || !LocationUtil.isPingGoogleSucceded()) {
									linkStore = pref.getChinaLinkStore();
								} else {
									linkStore = pref.getLinkStore();
								}
								
								String share = pref.getAppDesc() + " - " + linkStore;
								Intent i = new Intent(Intent.ACTION_SEND);
								i.setType("text/plain");
								i.putExtra(Intent.EXTRA_SUBJECT, "I love MyDynEd App!");
								i.putExtra(Intent.EXTRA_TEXT, share);
								startActivity(Intent.createChooser(i, "Share via:"));
							} else if(holder.name.getText().equals(LAUNCH_DYNED)) {
								AppPickerDialog dialog = new AppPickerDialog(getSherlockActivity(), true);
								dialog.show();
							}
						}
					}
					
				}, 500);
			}
		});
	}
	
	private void showLoginConfirmDialog(String title, String message, final String url, final String menu) {
		new AlertDialog.Builder(getSherlockActivity())
	    .setTitle(title)
	    .setMessage(message)
	    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if (menu.equals(ASESSMENT)) {
	        		Intent i = new Intent(getSherlockActivity(), WebAssessmentViewerActivity.class);
					i.putExtra("url_menu", url);
					startActivity(i);
				} else if (menu.equals(RECORDS)) {
					Intent i = new Intent(getSherlockActivity(), WebRecordsViewerActivity.class);
					i.putExtra("url_menu", url);
					startActivity(i);
				}
	        }
	     })
	    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        }
	     })
	    .show();
	}
	
	private List<HomeItem> buildItemHome(){
		List<HomeItem> list = new ArrayList<HomeItem>();
		list.add(new HomeItem(ASESSMENT, "ic_passport", 0xFFcd4900, "http://mdc.pistarlabs.net/0.1.0/index.php/widget/assessment"));
		list.add(new HomeItem(EVENT, "ic_event", 0xFFa9aaac, ""));
		list.add(new HomeItem(FIND_SCHOOL, "ic_findschool", 0xFF343074,""));
		list.add(new HomeItem(SOCIAL_MEDIA, "ic_social", 0xFFd4d4d4, ""));
		list.add(new HomeItem(LAUNCH_DYNED, "ic_launch_dyned", 0xFF66656b, ""));
		list.add(new HomeItem(NEWS, "ic_news", 0xFFcd4900, ""));
		if (PreferencesUtil.getInstance(getSherlockActivity()).getRoleKey().toLowerCase(Locale.getDefault())
				.contains("dyned_staff") || 
				PreferencesUtil.getInstance(getSherlockActivity()).getRoleKey().toLowerCase(Locale.getDefault())
				.contains("dyned_partner")) {
			list.add(new HomeItem(ANALYTIC, "ic_analytic", 0xFF0072bb, "http://www.dyned.com/dynedanalytics"));
		}
		
		list.add(new HomeItem(MEDIA, "ic_media", 0xFF66656b, ""));
		list.add(new HomeItem(RECORDS, "ic_live", 0xFFf04743, URLAddress.RECORD_URL));
		list.add(new HomeItem(SHARE, "ic_apps", 0xFF008641, ""));
		
		return list;
	}	
	
	private void loadAssessment() {
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
					System.out.println("response: " + str);
					
					ArrayList<Assessment> list = (ArrayList<Assessment>) Assessment.parseListAssessment(str);					
					Intent i = new Intent(getSherlockActivity(), AssessmentActivity.class);
					i.putExtra("assessments", list);
					startActivity(i);
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
		task.execute(URLAddress.ASSESSMENT_URL);
	}
	
	private void retrieveNewsList() {
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
				System.out.println("response: " + str);
				ArrayList<News> list = News.ParseNewsList(str);
				Intent i = new Intent(getSherlockActivity(), NewsActivity.class);
				i.putExtra("list", list);
				startActivity(i);
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		task.execute(URLAddress.NEWS_URL);
	}
	
	private void retrieveEventList() {
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
					System.out.println("response: " + str);
					ArrayList<Event> list = Event.ParseEventList(str);
					Intent i = new Intent(getSherlockActivity(), EventActivity.class);
					i.putExtra("list", list);
					startActivity(i);
					dialog.dismiss();
				} catch (Exception e) {
					
				}
				
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		task.execute(URLAddress.EVENT_URL);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		LayoutInflater inflater = (LayoutInflater) getSherlockActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View newView = inflater.inflate(R.layout.activity_home, null);
	    ViewGroup rootView = (ViewGroup) view;
	    rootView.removeAllViews();
	    rootView.addView(newView);
	    initView(rootView);
	    
	}
}
