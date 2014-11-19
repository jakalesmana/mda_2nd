package com.dyned.mydyned;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.dyned.mydyned.composite.EventAdapter;
import com.dyned.mydyned.composite.EventAdapter.ViewHolder;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.model.Event;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;

/**
author: jakalesmana
 */

public class EventActivity extends BaseActivity {

	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private EventAdapter adapter;
	private GridView gvEvent;
	private ArrayList<Event> listEvents;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
		
	@SuppressWarnings("unchecked")
	private void initView() {
		setContentView(R.layout.activity_event);
		setTitle(HomeFragment.EVENT);
		listEvents = (ArrayList<Event>)getIntent().getSerializableExtra("list");
		gvEvent = (GridView)findViewById(R.id.gvEvent);
		adapter = new EventAdapter(this, listEvents);
		gvEvent.setAdapter(adapter);
		
		gvEvent.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				ViewHolder holder = ((ViewHolder)view.getTag());
				retrieveEventDetail(holder.id);
			}
		});
	}

	private void retrieveEventDetail(String id) {
		PostInternetTask getTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(EventActivity.this, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				try {
					System.out.println("response: " + str);
					JSONObject obj = new JSONObject(str);
					Event event = Event.ParseEvent(obj.getString("data"));
					System.out.println("event: " + event.getTitle());
					Intent i = new Intent(EventActivity.this, EventDetailActivity.class);
					i.putExtra("event", event);
					startActivity(i);
					dialog.dismiss();
				} catch (Exception e) {
				}				
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(EventActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		getTask.addPair("id", id);
		getTask.execute(URLAddress.EVENT_DETAIL_URL);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initView();
	}
}
